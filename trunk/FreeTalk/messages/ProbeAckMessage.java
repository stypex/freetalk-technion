/**
 * 
 */
package messages;

import util.Consts.ResponseCode;

/**
 * @author lenka
 *
 */
public class ProbeAckMessage extends Message {

	private static final long serialVersionUID = -5192328680389688274L;


	ResponseCode rCode;
	
	
	public ProbeAckMessage(String from, String to, ConnectionId id, ResponseCode code) {
		super(from, to, id);
		rCode = code;
	}

	public ResponseCode getRCode() {
		return rCode;
	}
	
	public String toString(){
		return super.toString() + 
			   Func.toStringRow("Response code", rCode);
	}
	
	@Override
	public ProbeAckMessage clone() {
		ProbeAckMessage m = new ProbeAckMessage(from, to, cId, rCode);
		m.setUdpData(getUdpData());
		
		return m;
	}
}
