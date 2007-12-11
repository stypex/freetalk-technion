/**
 * 
 */
package messages;

import java.util.HashSet;
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
	
	
}
