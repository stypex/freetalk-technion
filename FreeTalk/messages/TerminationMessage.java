/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class TerminationMessage extends Message {

	
	private static final long serialVersionUID = 5837247985825925433L;

	public TerminationMessage(String from, String to, ConnectionId cId) {
		super(from, to, cId);
	}
	
	public String toString(){
		return super.toString();
	}
}
