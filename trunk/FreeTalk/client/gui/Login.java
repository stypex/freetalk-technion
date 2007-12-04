package client.gui;

import interfaces.TCPIncomingInterface;
import interfaces.TCPOutgoingInterface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import messages.ClientsAddedMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.Message;
import messages.ProbeMessage;
import messages.RegAckMessage;
import messages.RegisterMessage;
import util.Consts.ConnectionMethod;
import util.Consts.Protocol;
import util.Consts.ResponseCode;
import client.Globals;
import client.SimpleFunctions;
import client.data.ClientsList;
import client.listeners.TCPListener5000;
import client.listeners.TCPListener80;
import client.listeners.UDPListener;
import client.talk.TalkThread;

/**
 * @author Arthur
 * Represents the Login window.
 */
public class Login extends JFrame{
	// Field added automatically to avoid warning
	private static final long serialVersionUID = 1L;

	private JTextField t;
	private JLabel l;
	private JButton b;

	TCPListener5000 tcp;
	TCPListener80 tcp80;
	UDPListener udp;

	/**
	 * Builds the Login GUI menu
	 */
	public Login(){

		// Start the listeners
		tcp = new TCPListener5000();
		udp = new UDPListener();

		tcp.start();
		udp.start();

		//Initialize all window components
		setTitle("Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t = new JTextField();
		l = new JLabel("Enter your nickname");
		b = new JButton("Login");
		b.setEnabled(false);
		getRootPane().setDefaultButton(b);

		//Attach login() function to "Login" button
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				login();
			}
		});


		//Makes the "Login" button unpressable if the username begins with whitespaces
		t.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent arg0) {
				checkUserName();
			}

			public void insertUpdate(DocumentEvent arg0) {
				checkUserName();
			}

			public void removeUpdate(DocumentEvent arg0) {
				checkUserName();
			}
		});


		//Set the layout of the window
		GroupLayout loginLayout = new GroupLayout(getContentPane());
		getContentPane().setLayout(loginLayout);
		loginLayout.setHorizontalGroup(
				loginLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(loginLayout.createSequentialGroup()
						.addGap(10)
						.addComponent(l, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addGap(10))
						.addGroup(loginLayout.createSequentialGroup()
								.addGap(10)
								.addComponent(t, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addGap(10))
								.addGroup(loginLayout.createSequentialGroup()
										.addGap(10)
										.addComponent(b, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
										.addGap(10))    
		);
		loginLayout.setVerticalGroup(
				loginLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(loginLayout.createSequentialGroup()
						.addGap(10)
						.addComponent(l, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addGap(10)
						.addComponent(t, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addGap(10)
						.addComponent(b, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addGap(10))       
		);

		pack();

		//set the window in the middle of the screen regardless of screen resolution
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( ( d.width - getSize().width ) / 2, ( d.height - getSize().height ) / 2);
	}

	/**
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 * Allows Names user NickNames that don't start with whitespaces
	 * but may have whitespaces in them
	 */ 
	private void checkUserName() {
		if (t.getText().matches("((\\S)+(\\s)*)+"))
			b.setEnabled(true);
		else
			b.setEnabled(false);
	}

	/**
	 * Reads the username and performs the login
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	private void login() {

		if (doLogin(t.getText())) {

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					
					new Main(ClientsList.getInstance().keySet().toArray(new String[0])).setVisible(true);
				}
			}); 
			dispose(); 
		}
	}

	private boolean doLogin(String username) {

		try {
			TCPOutgoingInterface out = new TCPOutgoingInterface(Globals.getServerIP(), 80);
			TCPIncomingInterface in = new TCPIncomingInterface(out.getSocket());

			ConnectionId cId = new ConnectionId(username, "Server");
			RegisterMessage rm = new RegisterMessage(username, "Server", InetAddress.getLocalHost(),
					cId, Globals.getUDPPort(), Globals.getTCPPort(), Protocol.UDP, Protocol.TCP);

			boolean isConnected = false;

			// Initial connection
			while (!isConnected) {
				try {
					out.send(rm);
					isConnected = true;
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "No connection to the server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}

			Message reply = in.receive(0);

			// Error message
			if (reply instanceof ErrorMessage) {
				ErrorMessage err = (ErrorMessage) reply;

				JOptionPane.showMessageDialog(this, err.getEType().toString(), "Registration Error", JOptionPane.ERROR_MESSAGE);

				return false;
			} else if (reply instanceof ProbeMessage) {
				ProbeMessage pm = (ProbeMessage)reply;
				SimpleFunctions.replyProbe(out, pm);
			}

			reply = in.receive(0);
			
			// RegAck handle
			if (!(reply instanceof RegAckMessage)) {
				JOptionPane.showMessageDialog(this, "Error connecting to server", "Registration Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			RegAckMessage ram = (RegAckMessage) reply;

			if (ram.getPort1open() == ResponseCode.BAD)
				udp.doStop();

			if (ram.getPort2open() == ResponseCode.BAD)
				tcp.doStop();	

			// Clients list
			reply = in.receive(0);
			
			if (!(reply instanceof ClientsAddedMessage)) {

				JOptionPane.showMessageDialog(this, "Error connecting to server", "Registration Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			
			ClientsAddedMessage cam = (ClientsAddedMessage) reply;
			
			for (String client : cam.getClients())
				ClientsList.getInstance().put(client, new LinkedList<TalkThread>());

			// TCP80 Thread handle
			if (ram.getConnectionMethod() == ConnectionMethod.Indirect) {
				tcp80 = new TCPListener80(out.getSocket());
			}
			else {
				out.close();
				in.close();
			}

			Globals.setClientName(username);
			
			return true;
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(this, "Error connecting to server", "Registration Error", JOptionPane.ERROR_MESSAGE);
		return false;
	}
}
