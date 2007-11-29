/**
 * 
 */
package interfaces;

import java.io.IOException;

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
}
