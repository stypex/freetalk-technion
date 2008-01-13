/**
 * 
 */
package client.func;

import javax.swing.SwingUtilities;

import util.Consts;
import client.ClientMain;
import client.gui.Relogin;

/**
 * This class will check how often the client is probed.
 * If it isn't, it'll try to initiate contact with the server
 * @author lenka
 *
 */
public class ProbeMonitor extends Thread {

	public ProbeMonitor() {
		super();

	}

	@Override
	public void run() {
		super.run();

		while (true) {
			try {
				Thread.sleep(Consts.PROBE_WAIT / 2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			long now = System.currentTimeMillis();
			if (now - ClientMain.lastProbed > Consts.PROBE_WAIT * 2) {

				// Do the login again with the same user name
//				SwingUtilities.invokeLater(new Runnable() {
//				public void run() {
				final Relogin r = new Relogin();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						r.setVisible(true);
					}
				});  
				r.login();


				/*Login l =
							new Login(ClientMain.getMainWindow());
						l.setVisible(true);
						l.setUserName(Globals.getClientName());
						l.pressOk();*/
//				}
//				});
			}
		}
	}


}
