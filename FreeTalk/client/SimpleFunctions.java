/**
 * 
 */
package client;

import java.io.IOException;

import util.Consts.ResponseCode;
import messages.ProbeAckMessage;
import messages.ProbeMessage;
import interfaces.OutgoingInterface;

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
