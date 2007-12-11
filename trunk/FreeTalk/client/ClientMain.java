package client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

import javax.swing.SwingUtilities;

import util.Consts;

import messages.CallMeMessage;
import messages.ClientCheckMessage;
import messages.ClientExitMessage;
import messages.ClientsAddedMessage;
import messages.ConAckMessage;
import messages.ConnectMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.InitCallMessage;
import messages.ProbeAckMessage;
import messages.ProbeMessage;
import messages.RegAckMessage;
import messages.RegisterMessage;
import messages.TerminationMessage;

import client.data.ClientsList;
import client.gui.Login;
import client.gui.Main;
import client.listeners.TCPListener5000;
import client.listeners.TCPListener80;
import client.listeners.UDPListener;

/**
 * @author lenka
 *
 */
public class ClientMain {


	public static TCPListener5000 tcp;
	public static TCPListener80 tcp80;
	public static UDPListener udp;
	
	private static Main mainWindow;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Globals.load();
		
		// Start the listeners
		tcp = new TCPListener5000();
		udp = new UDPListener();

		tcp.start();
		udp.start();
		
		//	Start the GUI
		mainWindow = new Main(ClientsList.getInstance().keySet().toArray(new String[0]));
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Login(mainWindow).setVisible(true);
			}
		});  
	}

}
