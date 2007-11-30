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
	
	
	public Message(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
}
