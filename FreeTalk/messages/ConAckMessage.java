/**
 * 
 */
package messages;

import java.io.Serializable;
import java.net.InetAddress;

import util.Consts.ConnectionMethod;

/**
 * @author lenka
 *
 */
public class ConAckMessage extends Message {

	private static final long serialVersionUID = -8875172908430662958L;
	
	InetAddress destAddr;
	ConnMethod connMethod;
	
	public ConAckMessage(String from, String to, ConnectionId cId, InetAddress destAddr, ConnMethod connMethod) {
		super(from, to, cId);
		this.destAddr = destAddr;
		this.connMethod = connMethod;
	}
	
	public static class ConnMethod implements Serializable {
		
		private static final long serialVersionUID = -7767133257107765949L;
		ConnectionMethod cm;
		int port;
		
		public ConnMethod(ConnectionMethod cm, int port) {
			super();
			this.cm = cm;
			this.port = port;
		}
	
		public ConnectionMethod getCm() {
			return cm;
		}
	
		public int getPort() {
			return port;
		}
		
	}
	
	public String toString(){
		return super.toString() + 
			   Func.toStringRow("Dest. IP", destAddr.getHostAddress()) +
			   Func.toStringRow("Port", connMethod.port) +
			   Func.toStringRow("Conn. method", connMethod.cm);
	}

	public ConnMethod getConnMethod() {
		return connMethod;
	}

	public InetAddress getDestAddr() {
		return destAddr;
	}
	
	@Override
	public ConAckMessage clone() {
		ConAckMessage m = new ConAckMessage(from, to, cId, destAddr, connMethod);
		m.setUdpData(getUdpData());
		
		return m;
	}
}
