/**
 * 
 */
package messages;

/**
 * @author lenka
 *
 */
public class UDPAckMessage extends Message {

	
	private static final long serialVersionUID = -7916547134546129362L;


	/**
	 * @param from
	 * @param to
	 * @param cId
	 */
	public UDPAckMessage(String from, String to, ConnectionId cId, int udpSn) {
		super(from, to, cId);
		setUdpSn(udpSn);
	}


	public void setUdpSn(int udpSn) {
		this.udpSn = udpSn;
	}
}
