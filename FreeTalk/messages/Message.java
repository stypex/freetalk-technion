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
public abstract class Message implements Serializable, Cloneable {

	String from;
	String to;
	ConnectionId cId;
	UdpData udpData;
	
	
	static int serial = 0;
	
	public Message(String from, String to, ConnectionId cId) {
		super();
		this.from = from;
		this.to = to;
		this.cId = cId;
		udpData = null;
	}
	
	public UdpData getUdpData() {
		return udpData;
	}
	
	public void addUdpData(){
		udpData = new UdpData();
	}
	
	
	
	public void removeUdpData(){
		udpData = null;
	}
	
	/*private void setId() {
		
		String name = Globals.getClientName() != null ? 
				Globals.getClientName() : "Server";
				
		id = new Id(name, util.Func.getDateTime() + ":" + serial++);
		
	}*/

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
		String ret = this.getClass().getName().substring(p.length()+1) + sep +
		   					Func.toStringRow("From", from) +
		   					Func.toStringRow("To", to) +
		   					Func.toStringRow("Connection ID", cId); 
		if ( udpData != null ){ 
			ret += Func.toStringRow("UDP SN", udpData.getUdpSn()) +
		       	   Func.toStringRow("Id", udpData.getId()) +
		       	   Func.toStringRow("Local port", udpData.getLocalPort());
		}
		return ret;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setCId(ConnectionId id) {
		cId = id;
	}

	
	
	/*public int getUdpSn() {
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

	public Id getId() {
		return id;
	}*/

	@Override
	public abstract Message clone();



	public static class Id implements Serializable{

		private static final long serialVersionUID = 94629156586731718L;
		
		String clientName;
		String id;
		
		public Id(String c, String i){
			clientName = c;
			id = i;
		}
		
		public String toString(){
			return clientName + ":" + id;
		}

		public String getClientName() {
			return clientName;
		}

		public String getId() {
			return id;
		}
	}
	
	public static class UdpData implements Serializable {

		private static final long serialVersionUID = -4749526196526347862L;
		
		private int udpSn;
		private int localPort;
		private Id id;
		
		public UdpData(){
			udpSn = 0;
			localPort = 0;
			String name = Globals.getClientName() != null ? 
					Globals.getClientName() : "Server";
					
			id = new Id(name, util.Func.getDateTime() + ":" + serial++);
		}
		
		public UdpData(int udpSn1, int localPort1, String clientName, String id1){
			udpSn = udpSn1;
			localPort = localPort1;
			id = new Id(clientName, id1);
		}

		public int getUdpSn() {
			return udpSn;
		}

		public int getLocalPort() {
			return localPort;
		}

		public Id getId() {
			return id;
		}
		
		public void incUdpSn() {
			this.udpSn++;
		}

		public void setLocalPort(int localPort) {
			this.localPort = localPort;
		}

		public void setUdpSn(int udpSn) {
			this.udpSn = udpSn;
		}
	}

	public void setUdpData(UdpData udpData) {
		this.udpData = udpData;
	}
}
