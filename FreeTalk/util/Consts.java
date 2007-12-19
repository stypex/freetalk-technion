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
	
	public static final int PROBE_WAIT = 10000;
}


