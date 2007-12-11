/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import messages.Message;

/**
 * @author lenka
 *
 */
public abstract class IncomingInterface {

	int localPort;
	int remotePort;
	InetAddress remoteIp;
	
	


	public IncomingInterface(int localPort, int remotePort, InetAddress remoteIp) {
		super();
		this.localPort = localPort;
		this.remotePort = remotePort;
		this.remoteIp = remoteIp;
	}


	/**
	 * Receive a message. Blocking.
	 * @param timeout how long to wait for a message before
	 * returning. 0 - indefinitely.
	 * @return a message if got any, null if got nothing.
	 * @throws IOException
	 */
	public abstract Message receive(int timeout) throws IOException;

	
	/**
	 * Close any sockets the interface has open
	 */
	public abstract void close();


	/**
	 * @return if this is a TCP interface - will return the Socket
	 * Otherwise, will return null
	 */
	public abstract Socket getSocket();
	
	/**
	 * Creates an outgoing interface of the same kind (UDP for
	 * UDP, TCP for TCP) which connects with the same target. In
	 * case of TCP they will share the same socket
	 * @return
	 */
	public abstract OutgoingInterface createMatching();
	

	public int getLocalPort() {
		return localPort;
	}


	public InetAddress getRemoteIp() {
		return remoteIp;
	}


	public int getRemotePort() {
		return remotePort;
	}
}
