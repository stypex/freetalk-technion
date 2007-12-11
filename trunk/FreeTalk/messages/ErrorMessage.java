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
		CLIENT_NAME_EXISTS,
		CLIENT_DOES_NOT_EXIST;

		/*@Override
		public String toString() {
			if (equals(CLIENT_NAME_EXISTS))
				return "A client with this username already exists";
			if (equals(CLIENT_DOES_NOT_EXIST))
				return "The target client does not exist";
			
			
			return super.toString();
		}*/
		
		
	}
	
	ErrorType eType;
	
	
	public ErrorMessage(String from, String to, ConnectionId id, ErrorType type) {
		super(from, to, id);
		eType = type;
	}


	public ErrorType getEType() {
		return eType;
	}
	
	public String toString(){
		return super.toString() + 
			   Func.toStringRow("Error type", eType);
	}
}
