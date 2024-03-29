/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class ClientExitMessage extends Message implements IndependantMessage {

	
	private static final long serialVersionUID = -3834728946589296791L;
	String client;

	public ClientExitMessage(String from, String to, ConnectionId cId, String client) {
		super(from, to, cId);
		this.client = client;
	}

	public String getClient() {
		return client;
	}
	
	public String toString(){
		return super.toString() + Func.toStringRow("Client", client);
	}

	
	@Override
	public ClientExitMessage clone() {
		ClientExitMessage m = new ClientExitMessage(from, to, cId, client);
		m.setUdpData(getUdpData());
		
		return m;
	}
}
