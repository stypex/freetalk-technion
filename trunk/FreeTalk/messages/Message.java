/**
 * 
 */
package messages;

import java.io.Serializable;

import client.Globals;

/**
 * @author lenka
 *
 */
public abstract class Message implements Serializable {

	String from;
	String to;
	ConnectionId cId;
	int udpSn;
	int localPort;
	String id;
	
	static int serial = 0;
	
	public Message(String from, String to, ConnectionId cId) {
		super();
		this.from = from;
		this.to = to;
		this.cId = cId;
		udpSn = 0;
		localPort = 0;
		setId();
	}
	
	private void setId() {
		
		String name = Globals.getClientName() != null ? 
				Globals.getClientName() : "Server";
				
		id = name + ":" + serial++;
		
	}

	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}

	public ConnectionId getCId() {
		return cId;
	}

	@Override
	public String toString(){
		String sep = System.getProperty("line.separator");
		//next 2 rows get the Class name without the package name before it
		String p = this.getClass().getPackage().getName();
		return this.getClass().getName().substring(p.length()+1) + sep +
			   Func.toStringRow("From", from) +
		       Func.toStringRow("To", to) +
		       Func.toStringRow("Connection ID", cId) +
		       Func.toStringRow("UDP SN", udpSn) +
		       Func.toStringRow("Id", id) +
		       Func.toStringRow("Local port", localPort);
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setCId(ConnectionId id) {
		cId = id;
	}

	public int getUdpSn() {
		return udpSn;
	}

	public void incUdpSn() {
		this.udpSn++;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getId() {
		return id;
	}

}
