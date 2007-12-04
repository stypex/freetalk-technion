/**
 * 
 */
package client;

import javax.swing.SwingUtilities;

import client.gui.Login;

/**
 * @author lenka
 *
 */
public class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Globals.load();
		
		//		Start the GUI
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
