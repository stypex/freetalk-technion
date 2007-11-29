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
	
	public String getFrom() {
		return from;
	}
	public String getTo() {
		return to;
	}
	
	
}
