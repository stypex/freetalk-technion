/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class ErrorMessage extends Message {

	private static final long serialVersionUID = 8480774248609116937L;


	public enum ErrorType {
		CLIENT_NAME_EXISTS;
	}
	
	ConnectionId cId;
	ErrorType eType;
	
	
	public ErrorMessage(String from, String to, ConnectionId id, ErrorType type) {
		super(from, to);
		cId = id;
		eType = type;
	}
	
	
}
