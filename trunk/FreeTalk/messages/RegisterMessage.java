/**
 * 
 */
package messages;

import java.net.InetAddress;

import util.Consts.Protocol;

/**
 * @author lenka
 *
 */
public class RegisterMessage extends Message {

	private static final long serialVersionUID = 2949605236630981919L;

	InetAddress clientIp;
	ConnectionId cId;
	int port1;
	int port2;
	Protocol connType1;
	Protocol connType2;
	
	public RegisterMessage(String from, String to, InetAddress clientIp, ConnectionId id, int port1, int port2, Protocol connType1, Protocol connType2) {
		super(from, to);
		this.clientIp = clientIp;
		cId = id;
		this.port1 = port1;
		this.port2 = port2;
		this.connType1 = connType1;
		this.connType2 = connType2;
	}

	public ConnectionId getCId() {
		return cId;
	}

	public InetAddress getClientIp() {
		return clientIp;
	}

	public Protocol getConnType1() {
		return connType1;
	}

	public Protocol getConnType2() {
		return connType2;
	}

	public int getPort1() {
		return port1;
	}

	public int getPort2() {
		return port2;
	}
}
