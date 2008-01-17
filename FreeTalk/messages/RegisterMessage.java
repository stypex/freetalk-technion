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
	int port1;
	int port2;
	Protocol connType1;
	Protocol connType2;
	
	public RegisterMessage(String from, String to, ConnectionId id, InetAddress clientIp,  int port1, int port2, Protocol connType1, Protocol connType2) {
		super(from, to, id);
		this.clientIp = clientIp;
		this.port1 = port1;
		this.port2 = port2;
		this.connType1 = connType1;
		this.connType2 = connType2;
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
	
	public String toString(){
		return super.toString() + 
			   Func.toStringRow("Client IP", clientIp.getHostAddress()) +
			   Func.toStringRow("Port1 num", port1) +
			   Func.toStringRow("Conn. type1", connType1) +
			   Func.toStringRow("Port2 num", port2) +
			   Func.toStringRow("Conn. type2", connType2);
	}
	
	@Override
	public RegisterMessage clone() {
		RegisterMessage m = new RegisterMessage(from, to, cId, clientIp, port1, port2, connType1, connType2);
		m.setUdpData(getUdpData());
		
		return m;
	}
}
