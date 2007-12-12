/**
 * 
 */
package client.func;

import interfaces.OutgoingInterface;

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

	public static void replyProbe(OutgoingInterface out, ProbeMessage pm) {
		ProbeAckMessage pam = new ProbeAckMessage(Globals.getClientName(),
				"Server", pm.getCId(), ResponseCode.OK);
		try {
			out.send(pam);
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
	}
}
