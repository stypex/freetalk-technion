/**
 * 
 */
package messages;

import java.io.Serializable;

/**
 * @author lenka
 *
 */
public abstract class Message implements Serializable {

	String from;
	String to;
	ConnectionId cId;
	
	
	public Message(String from, String to, ConnectionId cId) {
		super();
		this.from = from;
		this.to = to;
		this.cId = cId;
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
			   Func.toStringRow("From",from) +
		       Func.toStringRow("To",to) +
		       Func.toStringRow("Connection ID",cId);
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setCId(ConnectionId id) {
		cId = id;
	}
	
	
}
