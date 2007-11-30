/**
 * 
 */
package messages;

import util.Consts.ConnectionMethod;
import util.Consts.ResponseCode;

/**
 * @author lenka
 *
 */
public class RegAckMessage extends Message {

	private static final long serialVersionUID = -6802917940410965042L;

	ConnectionId cId;
	ResponseCode port1open;
	ResponseCode port2open;
	ConnectionMethod connectionMethod;
	
	public RegAckMessage(String from, String to, ConnectionId cId, ResponseCode port1open, ResponseCode port2open, ConnectionMethod connectionMethod) {
		super(from, to);
		this.cId = cId;
		this.port1open = port1open;
		this.port2open = port2open;
		this.connectionMethod = connectionMethod;
	}

	public ConnectionId getCId() {
		return cId;
	}

	public ConnectionMethod getConnectionMethod() {
		return connectionMethod;
	}

	public ResponseCode getPort1open() {
		return port1open;
	}

	public ResponseCode getPort2open() {
		return port2open;
	}
	
	

}