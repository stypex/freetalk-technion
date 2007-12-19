package client;

import javax.swing.SwingUtilities;

import client.data.ClientsList;
import client.func.ProbeMonitor;
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
	
	public static long lastProbed;
	
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
		
		lastProbed = System.currentTimeMillis();
		
		//	Start the GUI
		mainWindow = new Main(ClientsList.getInstance().keySet().toArray(new String[0]));
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Login(mainWindow).setVisible(true);
			}
		});  
		
	}

	public static Main getMainWindow() {
		return mainWindow;
	}

}
