/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class TextMessage extends Message {

	
	private static final long serialVersionUID = 8142398971023695248L;
	
	String text;

	public TextMessage(String from, String to, ConnectionId cId, String text) {
		super(from, to, cId);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return super.toString() + 
		   Func.toStringRow("Text", text);
	}
	
	@Override
	public TextMessage clone() {
		TextMessage m = new TextMessage(from, to, cId, text);
		m.setUdpData(getUdpData());
		
		return m;
	}
}
