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
public abstract class OutgoingInterface {

	int localPort;
	int remotePort;
	InetAddress remoteIp;
	
	public OutgoingInterface(int localPort, int remotePort, InetAddress remoteIp) {
		super();
		this.localPort = localPort;
		this.remotePort = remotePort;
		this.remoteIp = remoteIp;
	}

	/**
	 * Send a message 
	 * @param message
	 * @return 
	 */
	public abstract void send(Message message) throws IOException;

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
	 * Creates an incoming interface of the same kind (UDP for
	 * UDP, TCP for TCP) which connects with the same target. In
	 * case of TCP they will share the same socket
	 * @return
	 */
	public abstract IncomingInterface createMatching();

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public InetAddress getRemoteIp() {
		return remoteIp;
	}

	public int getRemotePort() {
		return remotePort;
	}
}
