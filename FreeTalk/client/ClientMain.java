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
		//		Start the GUI
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Login().setVisible(true);
			}
		});  
	}

}
