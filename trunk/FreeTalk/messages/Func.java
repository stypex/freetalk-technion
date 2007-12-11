package messages;

public class Func {
	/**
	 * returns a string that represents 1 field in a toString() 
	 * representation of an object.
	 * @param fieldName - Name of field
	 * @param field - The field itself to be printed
	 * @return a sting in the format <fieldName>: <field>\n
	 * @author Arthur Kiyanovsky
	 * Dec 11, 2007
	 */
	public static String toStringRow(String fieldName, Object field){
		String sep = System.getProperty("line.separator");
		return String.format("%1$-13s : %2$1s", fieldName, field) + sep;
	}
}
