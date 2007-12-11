/**
 * 
 */
package server.data;

import java.util.concurrent.ConcurrentHashMap;

import server.handler.HandlerThread;


/**
 * For each client contains information about that client. Like
 * What's it's port status, connection mode and list of threads
 * handling that client currently
 * @author lenka
 *
 */
public class ClientsHash extends ConcurrentHashMap<String, ClientData> {

	private static final long serialVersionUID = 3222918723101144580L;

	private static ClientsHash singleton;
	
	/**
	 * A singleton initializer
	 * @return
	 */
	public static ClientsHash getInstance() {
		if (singleton == null) {
			singleton = new ClientsHash();
		}
			
		return singleton;
	}
	
	private ClientsHash() {
		super();
	}
	
	/**
	 * Registeres that the thread handles that client
	 * @param client
	 * @param thread
	 */
	public void registerThread(String client, HandlerThread thread) {
		if (client == null)
			return;
		
		ClientData cd = get(client);
		if (cd != null)
			cd.addThread(thread);
	}
	
	/**
	 * Register that the thread no longer handles that client
	 * @param client
	 * @param thread
	 */
	public void unRegisterThread(String client, HandlerThread thread) {
		if (client == null)
			return;
		
		ClientData cd = get(client);
		if (cd != null)
			cd.removeThread(thread);
	}
}
