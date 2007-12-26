/**
 * 
 */
package server.handler;

import interfaces.OutgoingInterface;

import java.io.IOException;

import messages.ClientExitMessage;
import messages.ConnectionId;
import server.data.ClientData;
import server.data.ClientsHash;

/**
 * Removes the client, sends CLIENT_EXIT messages.
 * Should be used after receiving a CLIENT_EXIT message or
 * if a probe failed
 * @author lenka
 *
 */
public class ClientRemover {

	String client;
	ConnectionId cId;
	
	public ClientRemover(String client, ConnectionId cId) {
		this.client = client;
		this.cId = cId;
	}

	/**
	 * Haldles client exit
	 */
	public void execute() {
		try {
			ClientData cd = ClientsHash.getInstance().get(client);
			if (cd == null)
				return;
			
			synchronized (cd) {
				// Go over all the other clients and send them 
				// the CLIENT_EXIT message
				for (String c : ClientsHash.getInstance().keySet()) {

					if (c.equals(client))
						continue;

					ClientData cd1 = ClientsHash.getInstance().get(c);

					synchronized (cd1) {
						OutgoingInterface oi = cd1.createOutInterface(cId, true);
						ClientExitMessage newTm = new ClientExitMessage("Server", cd.getName(), cId, client);
						oi.send(newTm);

						if (cd1.getTcp80() == null)
							oi.close();


					}
				}
			}
			
			// Close all the threads that are handling this client
			for (HandlerThread ht : cd.getThreads())
				ht.doStop();
			
			// Remove the client itself
			ClientsHash.getInstance().remove(client);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
