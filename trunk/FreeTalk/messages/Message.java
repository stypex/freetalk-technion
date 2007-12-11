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
	public String toString() {
		String sep = System.getProperty("line.separator");
		String s = "Message " + getClass() + sep +
			"From: " + from + " To: " + to + " Connection id: " + cId + sep;
		return s;
	}
	
	
}
