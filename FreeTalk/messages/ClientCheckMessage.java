/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class ClientCheckMessage extends Message {

	
	private static final long serialVersionUID = 6211361921831194114L;
	
	String target;
	ConnectionId cCid;
	long timestamp;
	
	/**
	 * @param from
	 * @param to
	 * @param cId
	 */
	public ClientCheckMessage(String from, String to, 
			ConnectionId cId, String target, ConnectionId cCid) {
		super(from, to, cId);

		this.target = target;
		this.cCid = cCid;
		
		timestamp = System.currentTimeMillis();
	}
	public String getTarget() {
		return target;
	}
	
	public String toString(){
		return super.toString() + 
			Func.toStringRow("Target",target) + 
			Func.toStringRow("CCid",cCid);
	}
	
	public ConnectionId getCCid() {
		return cCid;
	}
	public long getTimestamp() {
		return timestamp;
	}
}
