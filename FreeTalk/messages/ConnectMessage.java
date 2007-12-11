/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class ConnectMessage extends Message {

	private static final long serialVersionUID = 4398985968053517703L;
	
	String connTo;

	public ConnectMessage(String from, String to, ConnectionId cId, String connTo) {
		super(from, to, cId);
		this.connTo = connTo;
	}

	public String getConnTo() {
		return connTo;
	}
	
	public String toString(){
		return super.toString() + 
			   Func.toStringRow("Conn. to", connTo);
	}
}
