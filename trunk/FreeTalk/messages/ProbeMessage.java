/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class ProbeMessage extends Message implements IndependantMessage {

	private static final long serialVersionUID = -746642510904499733L;

	public ProbeMessage(String from, String to, ConnectionId cId) {
		super(from, to, cId);
	}
	
	public String toString(){
		return super.toString();
	}
	
	@Override
	public ProbeMessage clone() {
		ProbeMessage m = new ProbeMessage(from, to, cId);
		m.setUdpData(getUdpData());
		
		return m;
	}
}
