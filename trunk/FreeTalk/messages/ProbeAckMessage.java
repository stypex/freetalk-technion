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


	ConnectionId cId;
	ResponseCode rCode;
	
	
	public ProbeAckMessage(String from, String to, ConnectionId id, ResponseCode code) {
		super(from, to);
		cId = id;
		rCode = code;
	}


	public ConnectionId getCId() {
		return cId;
	}


	public ResponseCode getRCode() {
		return rCode;
	}
	
	
}
