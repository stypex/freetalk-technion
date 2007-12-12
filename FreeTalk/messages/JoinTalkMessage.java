/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class JoinTalkMessage extends Message {

	
	private static final long serialVersionUID = 2345725010429846150L;
	ConnectionId ccid;

	public JoinTalkMessage(String from, String to, ConnectionId cId, ConnectionId ccid) {
		super(from, to, cId);
		this.ccid = ccid;
	}

	public ConnectionId getCcid() {
		return ccid;
	}
	
	
	public String toString(){
		return super.toString() + 
			   Func.toStringRow("Conference call id", ccid);
	}
}
