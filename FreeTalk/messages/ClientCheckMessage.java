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
	/**
	 * @param from
	 * @param to
	 * @param cId
	 */
	public ClientCheckMessage(String from, String to, ConnectionId cId, String target) {
		super(from, to, cId);

		this.target = target;
	}
	public String getTarget() {
		return target;
	}
	
	public String toString(){
		return super.toString() + Func.toStringRow("Target",target);
	}
}
