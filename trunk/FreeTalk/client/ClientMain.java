/**
 * 
 */
package client;

import javax.swing.SwingUtilities;

import client.gui.Login;
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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Login().setVisible(true);
			}
		});  
		
//		new Login().setVisible(true);
//		
//		while (true) {
//			System.out.println("Hello World");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

}
