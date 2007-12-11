/**
 * 
 */
package messages;

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
	
	public static class ConnMethod {
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
}
