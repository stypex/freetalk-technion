/**
 * 
 */
package server.handler;

import interfaces.IncomingInterface;
import interfaces.OutgoingInterface;
import messages.Message;
import messages.RegisterMessage;
import server.data.ClientsHash;

/**
 * @author lenka
 *
 */
public class HandlerThread extends Thread {

	IncomingInterface in;
	OutgoingInterface out;
	String client;
	
	public static HandlerThread createHandler(Message m, IncomingInterface inInter) {
		
		HandlerThread ht = null;
		
		if (m instanceof RegisterMessage) {
			RegisterMessage rm = (RegisterMessage) m;
			
			ht = new RegisterHandler(rm, inInter);
		}
		
		return ht; 
	}

	public HandlerThread(String client) {
		super();
		this.client = client;
	}

	public IncomingInterface getIn() {
		return in;
	}

	public void setIn(IncomingInterface inInter) {
		this.in = inInter;
	}

	public OutgoingInterface getOut() {
		return out;
	}

	public void setOut(OutgoingInterface outInter) {
		this.out = outInter;
	}

	@Override
	public void run() {
		super.run();
		
		ClientsHash.getInstance().registerThread(client, this);
		
	}
	
	
}
