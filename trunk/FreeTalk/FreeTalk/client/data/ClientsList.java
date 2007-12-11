/**
 * 
 */
package client.data;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import client.func.TalkThread;

/**
 * Contains the list of all existing clients in the net.
 * For each client contains a list of all chats talking to this
 * client right now.
 * @author lenka
 *
 */
public class ClientsList extends ConcurrentHashMap<String, List<TalkThread>> {

	private static final long serialVersionUID = 3222918723101144580L;

	private static ClientsList singleton;
	
	/**
	 * A singleton initializer
	 * @return
	 */
	public static ClientsList getInstance() {
		if (singleton == null) {
			singleton = new ClientsList();
		}
			
		return singleton;
	}
	
	private ClientsList() {
		super();
	}
}
