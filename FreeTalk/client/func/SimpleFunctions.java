/**
 * 
 */
package client.func;

import interfaces.OutgoingInterface;

import java.io.IOException;

import messages.ProbeAckMessage;
import messages.ProbeMessage;
import util.Consts.ResponseCode;
import client.Globals;

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
}
