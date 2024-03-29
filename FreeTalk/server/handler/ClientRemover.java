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
import server.listeners.ServerMainListener;

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

	public ClientRemover(String client) {
		this.client = client;
		this.cId = new ConnectionId("Server", client);
	}

	/**
	 * Haldles client exit
	 */
	public void execute() {
		synchronized(ServerMainListener.udp.receivedMessages){
			ServerMainListener.udp.receivedMessages.remove(client);
		}
		
		ClientData cd = ClientsHash.getInstance().get(client);
		if (cd == null)
			return;


		// Go over all the other clients and send them 
		// the CLIENT_EXIT message
		for (String c : ClientsHash.getInstance().keySet()) {

			try {
				if (c.equals(client))
					continue;

				ClientData cd1 = ClientsHash.getInstance().get(c);

				if (cd1 == null)
					continue;
				
				synchronized (cd1) {
					OutgoingInterface oi = cd1.createOutInterface(cId, true);
					
					if (oi == null)
						continue;
					
					ClientExitMessage newTm = new ClientExitMessage("Server", cd1.getName(), cId, client);
					oi.send(newTm);

					if (cd1.getTcp80() == null)
						oi.close();


				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		synchronized (cd) {
			// Close all the threads that are handling this client
			for (HandlerThread ht : cd.getThreads())
				ht.doStop();

			// Remove the client itself
			ClientsHash.getInstance().remove(client);
		}
	}

}
