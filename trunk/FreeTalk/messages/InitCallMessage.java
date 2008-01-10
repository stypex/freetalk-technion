/**
 * 
 */
package messages;

import java.net.InetAddress;

/**
 * @author lenka
 *
 */
public class InitCallMessage extends Message implements IndependantMessage {

	
	private static final long serialVersionUID = 5101088486523014098L;
	
	String dest;
	InetAddress destIp;
	int destPort;

	ConnectionId cCid;
	
	public InitCallMessage(String from, String to, ConnectionId cId, 
			String dest, InetAddress destIp, int destPort, 
			ConnectionId cCid) {
		super(from, to, cId);
		this.dest = dest;
		this.destIp = destIp;
		this.destPort = destPort;
		this.cCid = cCid;
	}


	public String getDest() {
		return dest;
	}


	public InetAddress getDestIp() {
		return destIp;
	}


	public int getDestPort() {
		return destPort;
	}
	
	public String toString(){
		return super.toString() + 
			   Func.toStringRow("Destination", dest) +
			   Func.toStringRow("Dest. IP", destIp.getHostAddress()) +
			   Func.toStringRow("Dest. port", destPort) +
			   Func.toStringRow("CCid", cCid);
	}


	public ConnectionId getCCid() {
		return cCid;
	}
}
