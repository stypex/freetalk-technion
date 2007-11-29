/**
 * 
 */
package messages;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lenka
 *
 */
public class ConnectionId implements Serializable {

	private static final long serialVersionUID = 6155638045818581691L;

	private static int connectionIdSN = 0;
	
	String side1;
	String side2;
	Date date;
	
	String text;
	
	public ConnectionId(String text) {
		super();
		this.text = text;
	}

	public ConnectionId(String side1, String side2, Date date) {
		super();
		this.side1 = side1;
		this.side2 = side2;
		this.date = date;
		
		text = side1 + ":" + side2 + ":" + date.toString() + ":" + connectionIdSN;
		connectionIdSN++;
	}

	@Override
	public String toString() {
		return text;
	}

	@Override
	public boolean equals(Object arg0) {
		return toString().equals(arg0.toString());
	}
	
	
}
