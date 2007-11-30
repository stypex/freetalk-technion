/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import messages.ConnectionId;
import messages.Message;

/**
 * @author lenka
 *
 */
public class UDPOutgoingInterface extends OutgoingInterface {

	/**
	 * For UDP backoff acks
	 */
	UDPIncomingInterface ackInt;
	
	ConnectionId cId;
	
	public UDPOutgoingInterface(InetAddress ip, int port1, ConnectionId cId) {
		// TODO Auto-generated constructor stub
		this.cId = cId;
		
		// With a special connection id
		ackInt = new UDPIncomingInterface(cId.getAck());
	}

	/* (non-Javadoc)
	 * @see interfaces.OutgoingInterface#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see interfaces.OutgoingInterface#send(messages.Message)
	 */
	@Override
	public void send(Message message) throws IOException {
		// TODO Auto-generated method stub

		// To get the UDP backoff ack, use the ackInt data member (ackInt.receive(...))
	}

	@Override
	public Socket getSocket() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This one will be called by other threads, to send a UDP
	 * exponential backoff ack to a message this interface sent.
	 * @param ack
	 */
	public void acceptAck(Message ack) {
		ackInt.accept(ack);
	}

}
