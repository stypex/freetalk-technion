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
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import messages.AddClientMessage;
import messages.ConAckMessage;
import messages.ConnectMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.InitCallMessage;
import messages.JoinTalkMessage;
import messages.Message;
import messages.TerminationMessage;
import messages.TextMessage;
import util.Consts.ConnectionMethod;
import client.ClientMain;
import client.Globals;
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
	Message origM;
	
	ConnectionId ccid;
	
	ConcurrentHashMap<String, IncomingInterface> ins;
	ConcurrentHashMap<String, OutgoingInterface> outs;
	ConcurrentHashMap<String, ConnectionId> cons;
	
	
	// For sending messages directly to this thread
	Message dirM;
	Integer dirLock;
	
	
	/**
	 * @param dest - Name of the client in the destination computer with
	 * whom the chat is initiated.
	 * @param m - a JointTalk or InitCall message that caused the
	 * start of the thread
	 */
	public TalkThread(final String dest,  Message m){
		
		this.dest = dest;
		this.origM = m;
		
		dirLock = new Integer(0);
		
		ins = new ConcurrentHashMap<String, IncomingInterface>();
		outs = new ConcurrentHashMap<String, OutgoingInterface>();
		cons = new ConcurrentHashMap<String, ConnectionId>();
		
		if (m instanceof InitCallMessage) {
			InitCallMessage icm = (InitCallMessage) m;
			this.dest = icm.getDest();
		}
		
		// Getting the conference call id
		if (m != null) {
			if (m instanceof JoinTalkMessage) {
				JoinTalkMessage jtm = (JoinTalkMessage) m;
				ccid = jtm.getCcid();
			}
			else
				ccid = m.getCId();
		}
		else
			ccid = new ConnectionId(Globals.getClientName(), dest);
		
		ConferenceCallsHash.getInstance().put(ccid, this);
		cons.put(dest, ccid);
		
		c = new Chat(dest, this);
		
		
		/*Attach the event of closing a chat window with the actions
		 * taken when it happens*/
		c.addWindowListener(new WindowListener(){
			
			public void windowClosed(WindowEvent arg0) {
				TerminationMessage tm = new TerminationMessage(Globals.getClientName(),
						"", null);
				sendToAll(tm);
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
		if (origM == null || origM instanceof InitCallMessage) {
			doConnect(dest, ccid);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				c.setVisible(true);
			}
		});
        
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
			
			for (String client : ins.keySet()) {
				try {
					m = ins.get(client).receive(1000);
				} catch (SocketTimeoutException e) {
					// Do nothing. This is ok.
				} catch (IOException e) {
					// Do nothing. This is ok.
				}
				
				if (m != null) {
					handleMessage(m, ins.get(client));
					m = null;
				}
			}
		}
	}

	/**
	 * Called when the thread is exitting
	 */
	private void cleanUp() {
		
		ConferenceCallsHash.getInstance().remove(ccid);
		
		for (IncomingInterface in : ins.values())
			in.close();
		
		for (OutgoingInterface out : outs.values())
			out.close();
	}

	/**
	 * Handles the following messages: TextMessage, JoinTalk, Terminate,
	 * AddClient, ClientExit
	 * @param m
	 * @return false if the thread should exit
	 */
	public void handleMessage(Message m, IncomingInterface in) {
		
		try {
			if (m instanceof JoinTalkMessage) {			
				ins.put(m.getFrom(), in);
				outs.put(m.getFrom(), in.createMatching());
				cons.put(m.getFrom(), m.getCId());
				
				synchronized (dirLock) {
					dirM = m;
					dirLock.notify();
				}
				c.moveFromComboToList(m.getFrom());
				return;
			}
			
			if (m instanceof TextMessage) {
				TextMessage tm = (TextMessage) m;
				c.putTextInChatWindow(tm.getText(), tm.getFrom());
				return;
			}
			if (m instanceof TerminationMessage) {
				
				for (String client : cons.keySet()) {
					
					if (cons.get(client).equals(m.getCId())) {
						ins.get(client).close();
						ins.remove(client);
						outs.get(client).close();
						outs.remove(client);
						cons.remove(client);
						c.removeClientFromSession(client);
					}
				}
				return;
			}
			
			if (m instanceof AddClientMessage) {
				AddClientMessage acm = (AddClientMessage) m;
				
				String client = acm.getClient();
				ConnectionId cid = new ConnectionId(Globals.getClientName(), 
						client);
				cons.put(client, cid);
				doConnect(client, cid);
				c.moveFromComboToList(client);
				return;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Perform the connection algorithm with another client
	 * @param dest2
	 * @param cid
	 */
	private void doConnect(String dest2, ConnectionId cid) {
		
		try {
			
			TCPOutgoingInterface out = new TCPOutgoingInterface(Globals.getServerIP(), 80);
			
			ConnectMessage cm = new ConnectMessage(Globals.getClientName(), "Server", cid, dest2);
			out.send(cm);
			
			TCPIncomingInterface in = new TCPIncomingInterface(out.getSocket());
			
			Message mes = in.receive(0);
			
			if (mes instanceof ErrorMessage) {
				ErrorMessage err = (ErrorMessage) mes;
				JOptionPane.showMessageDialog(c, err.getEType().toString(), "Connection Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (!(mes instanceof ConAckMessage)) {
				JOptionPane.showMessageDialog(c, "Received wrong message.", "Connection Error", JOptionPane.ERROR_MESSAGE);
				return;
			}			
			
			ConAckMessage cam = (ConAckMessage) mes;
			if (!registerNewPartner(cam.getConnMethod().getCm(), 
					cam.getDestAddr(), cam.getConnMethod().getPort(), 
					in, out, cid, dest2))
				return; // TCP reverse. No need for Join Talk
			
			JoinTalkMessage jtm = new JoinTalkMessage(Globals.getClientName(), dest2, cid, ccid);
			
			sendToOne(dest2, jtm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Send the message to all cleints in the conference call
	 * @param m
	 */
	private void sendToAll(Message m) {
		
		for (String client : outs.keySet()) {
			m.setTo(client);
			m.setCId(cons.get(client));			
			
			sendToOne(client, m);
		}
	}

	private void sendToOne(String client, Message m) {
		try {
			outs.get(client).send(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		}
		
		outs.put(name, out);
		ins.put(name, out.createMatching());
		cons.put(name, cid);
		
		return true;
	}

	/**
	 * Sends a message to all the participants of the chat.
	 * @param msg - Message to send.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	public void send(String msg){	
		TextMessage tm = new TextMessage(Globals.getClientName(), "", null, msg);
		sendToAll(tm);
	}

	public void addClientToGUI(String client) {
		c.addClient(client);
	}

	public void removeClientFromGUI(String client) {
		c.removeClient(client);
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
		
		doConnect(client, cid);
	}
	
	
}
