/**
 * 
 */
package messages;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lenka
 *
 */
public class ClientsAddedMessage extends Message {

	
	private static final long serialVersionUID = 7178517212623327148L;
	HashSet<String> clients;
	
	public ClientsAddedMessage(String from, String to, ConnectionId id, HashSet<String> clients) {
		super(from, to, id);
		this.clients = clients;
	}

	public Set<String> getClients() {
		return clients;
	}
	
	public String toString(){
		String s = super.toString();
		
		Iterator<String> i = clients.iterator();
		int j = 0;
		
		while (i.hasNext())
			s += Func.toStringRow("Client[" + (j++) + "]",i.next());
		
		return s;
	}
}
