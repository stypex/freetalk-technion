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
	
	
}
