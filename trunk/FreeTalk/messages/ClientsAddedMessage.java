/**
 * 
 */
package messages;

import java.util.Set;

/**
 * @author lenka
 *
 */
public class ClientsAddedMessage extends Message {

	
	private static final long serialVersionUID = 7178517212623327148L;
	ConnectionId cId;
	Set<String> clients;
	
	public ClientsAddedMessage(String from, String to, ConnectionId id, Set<String> clients) {
		super(from, to);
		cId = id;
		this.clients = clients;
	}
	
	
}
