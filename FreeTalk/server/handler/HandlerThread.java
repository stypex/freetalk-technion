/**
 * 
 */
package server.handler;

import interfaces.IncomingInterface;
import interfaces.OutgoingInterface;

import java.util.LinkedList;
import java.util.List;

import messages.ClientCheckMessage;
import messages.ClientExitMessage;
import messages.ConnectMessage;
import messages.Message;
import messages.RegisterMessage;
import server.data.ClientsHash;
import client.listeners.StoppableThread;

/**
 * @author lenka
 *
 */
public class HandlerThread extends StoppableThread {

	IncomingInterface in;
	OutgoingInterface out;
	String client;
	
	private List<String> clients;
	
	
	public HandlerThread(String client, IncomingInterface in, OutgoingInterface out) {
		super();

		this.in = in;
		this.out = out;
		this.client = client;
		clients = new LinkedList<String>();
	}

	public static HandlerThread createHandler(Message m, IncomingInterface inInter) {
		
		
		HandlerThread ht = null;
		
		if (m instanceof RegisterMessage) {
			RegisterMessage rm = (RegisterMessage) m;
			
			ht = new RegisterHandler(rm, inInter);
		}
		else if (m instanceof ConnectMessage) {
			ConnectMessage cm = (ConnectMessage) m;
			
			ht = new ConnectionHandler(cm, inInter);
		}
		else if (m instanceof ClientCheckMessage) {
			ClientCheckMessage ccm = (ClientCheckMessage) m;
			
			ht = new ClientCheckHandler(ccm, inInter);
		}
		else if (m instanceof ClientExitMessage) {
			ClientExitMessage cem = (ClientExitMessage) m;
			
			ht = new ClientTerminationHandler(cem, inInter);
		}
		
		return ht; 
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
		
		registerForClient(client);		
	}
	
	protected void registerForClient(String client) {
		ClientsHash.getInstance().registerThread(client, this);
		clients.add(client);
	}
	
	protected void unregisterForAllClients() {
		
		for (String client : clients) {
			ClientsHash.getInstance().unRegisterThread(client, this);
		}
	}
}
