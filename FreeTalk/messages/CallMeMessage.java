/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class CallMeMessage extends Message {

	private static final long serialVersionUID = -4751764701185950557L;

	/**
	 * @param from
	 * @param to
	 * @param cId
	 */
	public CallMeMessage(String from, String to, ConnectionId cId) {
		super(from, to, cId);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
		return super.toString();
	}
	
	@Override
	public CallMeMessage clone() {
		CallMeMessage m = new CallMeMessage(from, to, cId);
		m.setUdpData(getUdpData());
		
		return m;
	}
}
