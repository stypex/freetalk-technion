/**
 * 
 */
package client.func;

import interfaces.IncomingInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import messages.ProbeAckMessage;
import messages.ProbeMessage;
import util.Consts.ResponseCode;
import client.ClientMain;
import client.Globals;
import client.data.ClientsList;
import client.gui.Main;

/**
 * @author lenka
 *
 */
public class SimpleFunctions {


	public static boolean loggedIn = false;

	public static void replyProbe(IncomingInterface in, ProbeMessage pm) {

		if (!pm.getTo().equals(Globals.getClientName()))
			return;

		ProbeAckMessage pam = new ProbeAckMessage(Globals.getClientName(),
				"Server", pm.getCId(), ResponseCode.OK);
		try {
			in.createMatching().send(pam);
			ClientMain.lastProbed = System.currentTimeMillis();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addClients(Set<String> clients) {

		Main m = ClientMain.getMainWindow();

		for (String c : clients) {
			ClientsList.getInstance().put(c, new ArrayList<TalkThread>());
			m.addClient(c);
		}

		/* shows a newly logged in client but not those that are online when
		 * current client logs in
		 */
		if ( ! loggedIn )
			loggedIn = true;
		else if (clients.size() > 0)
			m.showClientOnlineMessage((String)(clients.toArray())[0]);
	}
}
