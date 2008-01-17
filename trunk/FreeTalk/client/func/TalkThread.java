/**
 * 
 */
package client.func;

import interfaces.IncomingInterface;
import interfaces.OutgoingInterface;
import interfaces.TCPIncomingInterface;
import interfaces.TCPOutgoingInterface;
import interfaces.UDPOutgoingInterface;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import messages.AddClientMessage;
import messages.ClientCheckMessage;
import messages.ConAckMessage;
import messages.ConnectMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.InitCallMessage;
import messages.JoinTalkMessage;
import messages.Message;
import messages.TerminationMessage;
import messages.TextMessage;
import util.Consts;
import util.Consts.ConnectionMethod;
import client.ClientMain;
import client.Globals;
import client.data.ClientsList;
import client.data.ConferenceCallsHash;
import client.gui.Chat;
import client.listeners.StoppableThread;

/**
 * @author lenka
 *
 */
public class TalkThread extends StoppableThread {

	public Chat c;

	String dest;

	ConnectionId ccid;

	ConcurrentHashMap<String, IncomingInterface> ins;
	ConcurrentHashMap<String, OutgoingInterface> outs;
	ConcurrentHashMap<String, ConnectionId> cons;

	private boolean isConnected = false;

	// For sending messages directly to this thread
	Message dirM;
	Integer dirLock;

	//for avoiding the CPU eating loop in receiveMessages() in case the chat is empty
	private Object emptyChat;

	private boolean show;

	/**
	 * @param dest - Name of the client in the destination computer with
	 * whom the chat is initiated.
	 * @param ccid - a Conference Call id for this thread. If
	 * there's none, send null
	 */
	public TalkThread(final String dest,  ConnectionId ccid, boolean show){

		emptyChat = new Object();

		this.dest = dest;
		this.show = show;

		dirLock = new Integer(0);

		ins = new ConcurrentHashMap<String, IncomingInterface>();
		outs = new ConcurrentHashMap<String, OutgoingInterface>();
		cons = new ConcurrentHashMap<String, ConnectionId>();


		// Getting the conference call id
		if (ccid != null) 
			this.ccid = ccid;		
		else
			this.ccid = new ConnectionId(Globals.getClientName(), dest);

		ConferenceCallsHash.getInstance().put(this.ccid, this);
		cons.put(dest, this.ccid);

		c = new Chat(dest, this);


		/*Attach the event of closing a chat window with the actions
		 * taken when it happens*/
		c.addWindowListener(new WindowListener(){

			public void windowClosed(WindowEvent arg0) {
				for (String client : cons.keySet()) {
					TerminationMessage tm = 
						new TerminationMessage(Globals.getClientName(),
								client, cons.get(client));
					synchronized (cons.get(client)) {
						try {
							sendToOne(client, tm);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						disconnectClient(client);
					}

				}
				doStop();
			}

			//unnecessary functions that have to be implemented
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});

		ClientMain.getMainWindow().registerTalkThread(this);
	}

	public void run() {
		if (!isConnected) {
			doConnect(dest, ccid, true);
		}

		if (show) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					c.setVisible(true);
				}
			});
		}

		receiveMessages();

		cleanUp();
	}

	/**
	 * The main loop of the TalkThread. Awaits messages from
	 * other clients and handles them
	 */
	private void receiveMessages() {
		Message m = null;

		while (!isStopped) {

			if ( cons.size() == 0 ){
				try {
					synchronized (emptyChat) {
						if (!isStopped)
							emptyChat.wait();
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			for (String client : ins.keySet()) {
				try {
					synchronized (cons.get(client)) {
						if (cons.get(client) != null)
							m = ins.get(client).receive(1000);
					}
				} catch (SocketTimeoutException e) {
					// Do nothing. This is ok.
				} catch (IOException e) {
					reconnect(client);
				} catch(NullPointerException e){
					//Do nothing. Happens when a client exits the system.
				}


				if (m != null) {
					handleMessage(m, ins.get(client));
					m = null;
				}
				yield();
			}


		}
	}

	private void reconnect(String client) {
		ConnectionId cid = 
			new ConnectionId(Globals.getClientName(),
					client);
		doConnect(client, cid, false);
	}

	/**
	 * Called when the thread is exitting.
	 */
	private void cleanUp() {

		ConferenceCallsHash.getInstance().remove(ccid);

		for (String client : cons.keySet()) {
			disconnectClient(client);
		}
	}

	/**
	 * Destroy all the data structures handling that client
	 * @param client
	 */
	private void disconnectClient(String client) {	


		try {
			synchronized (cons.get(client)) {
				cons.remove(client);
				ins.get(client).close();
				ins.remove(client);
				outs.get(client).close();
				outs.remove(client);
			}
		} catch (NullPointerException e) {
			// Do nothing
		}
	}

	/**
	 * Handles the following messages: TextMessage, JoinTalk, Terminate,
	 * AddClient, ClientExit, InitCall
	 * @param m
	 * @return false if the thread should exit
	 */
	public void handleMessage(Message m, IncomingInterface in) {

		try {
			if (m instanceof JoinTalkMessage) {	
				cons.put(m.getFrom(), m.getCId());

				synchronized (cons.get(m.getFrom())) {
					ins.put(m.getFrom(), in);
					outs.put(m.getFrom(), in.createMatching());	
				}

				isConnected = true;

				synchronized (dirLock) {
					dirM = m;
					dirLock.notify();
				}
				
				synchronized (emptyChat) {
					emptyChat.notify();
				}
				
				c.moveFromComboToList(m.getFrom());			
				return;
			}

			if (m instanceof InitCallMessage) {
				// Here we don't need to do connect because
				// the message already contains all we need
				try {
					// Update interfaces
					InitCallMessage icm = (InitCallMessage)m;					
					Socket s = new Socket(icm.getDestIp(), 
							icm.getDestPort());

					TCPIncomingInterface tIn = 
						new TCPIncomingInterface(s);
					cons.put(icm.getDest(), m.getCId());

					synchronized (cons.get(icm.getDest())) {
						ins.put(icm.getDest(), tIn);
						outs.put(icm.getDest(), tIn.createMatching());
					}

					// Update the conference call id
//					if (!isConnected)
//					ccid = icm.getCCid();

					// Send JOIN_TALK
					JoinTalkMessage jtm = 
						new JoinTalkMessage(Globals.getClientName(), 
								icm.getDest(), icm.getCId(), ccid);

					sendToOne(icm.getDest(), jtm);
					c.moveFromComboToList(icm.getDest());
					isConnected = true;
					synchronized (emptyChat) {
						emptyChat.notify();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}

			if (m instanceof TextMessage) {
				if ( !c.isVisible() )
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							c.setVisible(true);
						}
					});

				TextMessage tm = (TextMessage) m;
				c.putTextInChatWindow(tm.getText(), tm.getFrom());
				return;
			}
			if (m instanceof TerminationMessage) {

				for (String client : cons.keySet()) {

					if (cons.get(client).equals(m.getCId())) {
						removeClientFromSession(client);
					}
				}
				return;
			}

			if (m instanceof AddClientMessage) {
				AddClientMessage acm = (AddClientMessage) m;

				connectToClient(acm.getClient());
				return;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * All that needs to be done to add a new client to chat.
	 * Called after receiving AddClientMessage. Includes GUI
	 * update.
	 * @param client
	 */
	private void connectToClient(String client) {
		ConnectionId cid = new ConnectionId(Globals.getClientName(), 
				client);
		cons.put(client, cid);
		if (doConnect(client, cid, true))
			c.moveFromComboToList(client);
	}

	/**
	 * Perform the connection algorithm with another client
	 * @param dest2
	 * @param cid
	 * @param b 
	 */
	private boolean doConnect(String dest2, ConnectionId cid, 
			boolean sendConnect) {

		Message mes = null;
		TCPOutgoingInterface out = null;
		TCPIncomingInterface in = null;

		try {
			if (!ClientsList.getInstance().containsKey(dest2)) {
				removeClientFromSession(dest2);
				return false;
			}

			out = new TCPOutgoingInterface(Globals.getServerIP(), Consts.SERVER_PORT);

			Message m;
			if (sendConnect)
				m = new ConnectMessage(Globals.getClientName(), 
						"Server", cid, dest2, ccid);
			else
				m = new ClientCheckMessage(Globals.getClientName(),
						"Server", cid, dest2, ccid);

			out.send(m);

			in = new TCPIncomingInterface(out.getSocket());

			mes = in.receive(0);
		} catch (IOException e) {
			removeClientFromSession(dest2);
			ClientMain.setServerOut();
			return false;
		}

		try {
			if (mes instanceof ErrorMessage) {
				if (sendConnect) {
					ErrorMessage err = (ErrorMessage) mes;
					JOptionPane.showMessageDialog(c, err.getEType().toString(), "Connection Error", JOptionPane.ERROR_MESSAGE);
				}
				removeClientFromSession(dest2);
				return false;
			}

			if (!(mes instanceof ConAckMessage)) {
				JOptionPane.showMessageDialog(c, "Received wrong message.", "Connection Error", JOptionPane.ERROR_MESSAGE);
				removeClientFromSession(dest2);
				return false;
			}			

			ConAckMessage cam = (ConAckMessage) mes;
			if (!registerNewPartner(cam.getConnMethod().getCm(), 
					cam.getDestAddr(), cam.getConnMethod().getPort(), 
					in, out, cid, dest2))
				return true; // TCP reverse. No need for Join Talk

			JoinTalkMessage jtm = new JoinTalkMessage(Globals.getClientName(), dest2, cid, ccid);

			sendToOne(dest2, jtm);
			isConnected = true;
			return true;
		} catch (IOException e) {
			removeClientFromSession(dest2);
			JOptionPane.showMessageDialog(c, "Failed connecting to target.", "Connection Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	/**
	 * Send the message to all clients in the conference call
	 * @param m
	 * @throws IOException 
	 */
	private boolean sendToAll(Message m) {

		for (String client : outs.keySet()) {
			m.setTo(client);
			m.setCId(cons.get(client));			

			try {
				sendToOne(client, m);
			} catch (NoConnectionException e) {	
				reconnect(e.getClient());

				if (!cons.containsKey(e.getClient())) {
					JOptionPane.showMessageDialog(c, "Error sending message.", "Connection Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}

				try {
					sendToOne(e.getClient(), m);
				} catch (NoConnectionException e1) {
					JOptionPane.showMessageDialog(c, "Error sending message.", "Connection Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}

		}
		return true;
	}

	private void sendToOne(String client, Message m) throws NoConnectionException {
		try {
			synchronized (cons.get(client)) {
				outs.get(client).send(m);			
			}
		} catch (IOException e) {
			throw new NoConnectionException(client);
		} catch (NullPointerException e) {
			throw new NoConnectionException(client);
		}

	}

	/**
	 * Take the parameters and create all the needed interfaces
	 * @param cm
	 * @param destIp
	 * @param port
	 * @param in
	 * @param out
	 * @param cid
	 * @throws IOException
	 */
	private boolean registerNewPartner(ConnectionMethod cm, InetAddress destIp, int port, IncomingInterface in,
			OutgoingInterface out, ConnectionId cid, String name) throws IOException {

		if (cm.equals(ConnectionMethod.TCPDirect)) {
			out = 
				new TCPOutgoingInterface(destIp, port);		
		} else if (cm.equals(ConnectionMethod.UDPDirect)) {
			out = 
				new UDPOutgoingInterface(destIp, Globals.getUDPPort(), port, cid);		
		} else if (cm.equals(ConnectionMethod.TCPReverse)) {
			synchronized (dirLock) {
				if (dirM == null) {
					try {
						dirLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return false;
			}
		} else if (cm.equals(ConnectionMethod.None)) {
			removeClientFromSession(name);
			c.setStatusBarText("Client " + name + " is temporarily unavailable");

//			NagThread nt = new NagThread(this, name);
//			nt.start();
			return false;
		}

		cons.put(name, cid);

		synchronized (cons.get(name)) {
			outs.put(name, out);
			ins.put(name, out.createMatching());			
		}

		synchronized (emptyChat) {
			emptyChat.notify();
		}
		
		c.setStatusBarText("Connection established with client " + name);
		return true;
	}

	private void removeClientFromSession(String name) {
		disconnectClient(name);
		c.removeClientFromSession(name);
	}

	/**
	 * Sends a message to all the participants of the chat.
	 * @param msg - Message to send.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	public boolean send(String msg){	
		TextMessage tm = new TextMessage(Globals.getClientName(), "", null, msg);
		
		return sendToAll(tm);
		
	}

	public void addClientToGUI(String client) {
		c.addClient(client);
	}

	public void removeClient(String client) {
		c.removeClient(client);
		disconnectClient(client);
	}

	public void addclientToSession(String client) {

		if (cons.containsKey(client)) {
			JOptionPane.showMessageDialog(c, "The client is already a member of the chat.", 
					"Client Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		AddClientMessage acm = new AddClientMessage(Globals.getClientName(), "", null, client);

		
		sendToAll(acm);
		
		ConnectionId cid = new ConnectionId(Globals.getClientName(), client);
		cons.put(client, cid);

		synchronized (emptyChat) {
			emptyChat.notify();
		}

		doConnect(client, cid, true);
	}


//	public static class NagThread extends Thread {
//
//		String client;
//		TalkThread tt;
//
//		public NagThread(TalkThread tt, String client) {
//			super();
//			this.client = client;
//			this.tt = tt;
//		}
//
//		@Override
//		public void run() {
//			super.run();
//
//			try {
//				Thread.sleep(Consts.PROBE_WAIT);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			if (!tt.isStopped) {
//				ConnectionId cid = new ConnectionId(Globals.getClientName(),
//						client);
//				tt.doConnect(client, cid, false);
//			}
//		}
//
//
//	}

	public void doStop(){
		super.doStop();
		synchronized(emptyChat){
			emptyChat.notify();
		}
	}

	private static class NoConnectionException extends IOException {

		private static final long serialVersionUID = 2972775692949539479L;
		String client;

		public NoConnectionException(String client) {
			super();
			this.client = client;
		}

		public String getClient() {
			return client;
		}


	}

	public void setServerOut() {
		c.setServerOut();
	}

	public void setServerOk() {
		c.setServerOk();
	}
}
