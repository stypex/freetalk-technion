/**
 * 
 */
package messages;

import java.net.InetAddress;

/**
 * @author lenka
 *
 */
public class InitCallMessage extends Message {

	
	private static final long serialVersionUID = 5101088486523014098L;
	
	String dest;
	InetAddress destIp;
	int destPort;
	
	
	public InitCallMessage(String from, String to, ConnectionId cId, String dest, InetAddress destIp, int destPort) {
		super(from, to, cId);
		this.dest = dest;
		this.destIp = destIp;
		this.destPort = destPort;
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
			   Func.toStringRow("Dest. IP", destIp.getHostAddress()) +
			   Func.toStringRow("Dest. port", destPort);
	}
}
