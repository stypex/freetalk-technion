/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.net.Socket;

import messages.Message;

/**
 * @author lenka
 *
 */
public abstract class IncomingInterface {

	public IncomingInterface() {
		super();
	}


	/**
	 * Receive a message. Blocking.
	 * @param timeout how long to wait for a message before
	 * returning
	 * @return a message if got any, null if got nothing.
	 * @throws IOException
	 */
	public abstract Message receive(long timeout) throws IOException;

	
	/**
	 * Close any sockets the interface has open
	 */
	public abstract void close();


	/**
	 * @return if this is a TCP interface - will return the Socket
	 * Otherwise, will return null
	 */
	public abstract Socket getSocket();
	// Ilya - trial update
}
