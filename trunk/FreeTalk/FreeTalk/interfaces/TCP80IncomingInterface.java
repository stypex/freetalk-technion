package interfaces;

import messages.ConnectionId;
import client.Globals;

/**
 * This class is a tcp80 incoming interface. Meaning - it has
 * no socket. It listens on a queue. That's why it extends 
 * UDPIncomingInterface. For obvious reasons - only for client use
 * @author lenka
 *
 */
public class TCP80IncomingInterface extends UDPIncomingInterface {

	public TCP80IncomingInterface(ConnectionId cId) {
		
		super(cId, Globals.getServerIP(), 80, 80);
		// TODO Auto-generated constructor stub
	}

}
