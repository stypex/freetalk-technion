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

		@Override
		public String toString() {
			if (equals(CLIENT_NAME_EXISTS))
				return "A client with this username already exists";
			
			return super.toString();
		}
		
		
	}
	
	ConnectionId cId;
	ErrorType eType;
	
	
	public ErrorMessage(String from, String to, ConnectionId id, ErrorType type) {
		super(from, to);
		cId = id;
		eType = type;
	}


	public ErrorType getEType() {
		return eType;
	}
	
	
}
