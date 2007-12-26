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
		Indirect,
		TCPReverse,
		None;
	}
	
	public static final int PROBE_WAIT = 60000;
	
	public static final int SERVER_PORT = 81;
	
	// For debug only
	public static final boolean doTCP5000 = true;
}


