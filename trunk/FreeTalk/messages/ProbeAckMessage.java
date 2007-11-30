/**
 * 
 */
package messages;

import util.Consts.Protocol;
import util.Consts.ResponseCode;

/**
 * @author lenka
 *
 */
public class ProbeAckMessage extends Message {

	private static final long serialVersionUID = -5192328680389688274L;


	ConnectionId cId;
	int port;
	Protocol protocol; 
	ResponseCode rCode;
	
	
	public ProbeAckMessage(String from, String to, ConnectionId id, int port, Protocol protocol, ResponseCode code) {
		super(from, to);
		cId = id;
		this.port = port;
		this.protocol = protocol;
		rCode = code;
	}


	public ConnectionId getCId() {
		return cId;
	}


	public int getPort() {
		return port;
	}


	public Protocol getProtocol() {
		return protocol;
	}


	public ResponseCode getRCode() {
		return rCode;
	}
	
	
}
