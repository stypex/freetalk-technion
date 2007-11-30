/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class ProbeMessage extends Message {

	private static final long serialVersionUID = -746642510904499733L;

	ConnectionId cId;
	
	public ProbeMessage(String from, String to, ConnectionId cId) {
		super(from, to);
		
		this.cId = cId;
	}

	public ConnectionId getCId() {
		return cId;
	}

}
