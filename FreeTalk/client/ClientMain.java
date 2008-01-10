package client;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import client.data.ClientsList;
import client.data.ConferenceCallsHash;
import client.func.TalkThread;
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

	public static Boolean serverAlive = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		UIManager.put("swing.boldMetal", Boolean.FALSE);

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

	public static void setServerOut() {

		synchronized (serverAlive) {
			if (serverAlive) {
				ClientMain.serverAlive = false;

				for (TalkThread tt : ConferenceCallsHash.getInstance().values()) {
					tt.setServerOut();
				}

				Main m = getMainWindow();
				m.setTitle(m.getTitle() + " - Disconnected");
			}
		}

	}
}
