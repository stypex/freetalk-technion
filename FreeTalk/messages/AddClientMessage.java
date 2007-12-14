/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class AddClientMessage extends Message {

	
	private static final long serialVersionUID = -4417847982231445832L;
	
	String client;

	public AddClientMessage(String from, String to, ConnectionId cId, String client) {
		super(from, to, cId);
		this.client = client;
	}
	
	
	public String toString(){
		return super.toString() + Func.toStringRow("Client", client);
	}


	public String getClient() {
		return client;
	}
}
