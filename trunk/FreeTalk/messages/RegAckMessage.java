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

	ResponseCode port1open;
	ResponseCode port2open;
	ConnectionMethod connectionMethod;
	
	public RegAckMessage(String from, String to, ConnectionId cId, ResponseCode port1open, ResponseCode port2open, ConnectionMethod connectionMethod) {
		super(from, to, cId);
		this.port1open = port1open;
		this.port2open = port2open;
		this.connectionMethod = connectionMethod;
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
	
	public String toString(){
		return super.toString() + 
		 	   Func.toStringRow("Port 1 open", port1open) +
		 	   Func.toStringRow("Port 2 open", port2open) +
			   Func.toStringRow("Conn. method", connectionMethod);
	}

}
