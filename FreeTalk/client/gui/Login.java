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
import client.data.ClientsList;
import client.func.Loginner;
import client.func.SimpleFunctions;
import client.func.TalkThread;
import client.listeners.TCPListener5000;
import client.listeners.TCPListener80;
import client.listeners.UDPListener;

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


	/**
	 * Builds the Login GUI menu
	 */
	public Login(){

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

		Loginner loginner = new Loginner(this);
		if (loginner.doLogin(t.getText())) {

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					
					new Main(ClientsList.getInstance().keySet().toArray(new String[0])).setVisible(true);
				}
			}); 
			dispose(); 
		}
	}

	
}
