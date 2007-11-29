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
public abstract class IncomingInterface {

	/**
	 * Receive a message. Blocking.
	 * @param timeout how long to wait for a message before
	 * returning
	 * @return a message if got any, null if got nothing.
	 * @throws IOException
	 */
	public abstract Message receive(long timeout) throws IOException;
}
