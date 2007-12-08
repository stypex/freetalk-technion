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

	public void execute() {
		try {
			for (String c : ClientsHash.getInstance().keySet()) {
				
				if (c.equals(client))
					continue;
				
				ClientData cd = ClientsHash.getInstance().get(c);
				
				synchronized (cd) {
					OutgoingInterface oi = cd.createOutInterface(cId, true);
					ClientExitMessage newTm = new ClientExitMessage("Server", cd.getName(), cId, client);
					oi.send(newTm);
					
					if (cd.getTcp80() == null)
						oi.close();
					
					for (HandlerThread ht : cd.getThreads())
						ht.doStop();
				}
			}
			
			ClientsHash.getInstance().remove(client);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
