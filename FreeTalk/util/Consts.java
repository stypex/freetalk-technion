/**
 * 
 */
package util;

/**
 * @author lenka
 *
 */
public class Consts {

	public enum ResponseCode {
		OK,
		BAD;
	}
	
	public enum Protocol {
		 TCP,
		 UDP;
	}
	
	public enum ConnectionMethod {
		TCPDirect,
		UDPDirect,
		Indirect;
	}
}


