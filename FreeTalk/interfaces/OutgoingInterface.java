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
public abstract class OutgoingInterface {

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
}
