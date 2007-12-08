/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import messages.ConnectionId;
import messages.Message;
import server.data.ThreadsHash;

/**
 * @author lenka
 *
 */
public class UDPIncomingInterface extends IncomingInterface {


	ConnectionId cId;
	
	public UDPIncomingInterface(ConnectionId cId, InetAddress ip, int remotePort, int localPort) {
		super(localPort, remotePort, ip);
		
		this.cId = cId;
		// TODO the rest of the constructor
		ThreadsHash.getInstance().register(cId, this);
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#close()
	 */
	@Override
	public void close() {
		// TODO the rest of the method

		ThreadsHash.getInstance().remove(cId);
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#getSocket()
	 */
	@Override
	public Socket getSocket() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#receive(long)
	 */
	@Override
	public Message receive(int timeout) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Will be called by other classes and threads to insert a
	 * new message into the queue.
	 * @param m
	 */
	public void accept(Message m) {
		//		 TODO Auto-generated method stub
	}

	@Override
	public OutgoingInterface createMatching() {
		return new UDPOutgoingInterface(remoteIp, localPort, remotePort, cId);
	}
}
