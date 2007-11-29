/**
 * 
 */
package client.data;

import interfaces.IncomingInterface;

import java.util.concurrent.ConcurrentHashMap;

import messages.ConnectionId;
import client.talk.TalkThread;

/**
 * @author lenka
 *
 */
public class ThreadsHashClient extends ConcurrentHashMap<ConnectionId, ThreadsHashClient.Data> {

	private static final long serialVersionUID = 3222918723101144580L;

	private static ThreadsHashClient singleton;
	
	/**
	 * A singleton initializer
	 * @return
	 */
	public static ThreadsHashClient getInstance() {
		if (singleton == null) {
			singleton = new ThreadsHashClient();
		}
			
		return singleton;
	}
	
	private ThreadsHashClient() {
		super();
	}
	
	public static class Data {
		public IncomingInterface inInterface;
		public TalkThread thread; 
		public ConnectionId conferenceCallId;
		
		public Data(IncomingInterface inInterface, TalkThread thread, ConnectionId conferenceCallId) {
			super();
			this.inInterface = inInterface;
			this.thread = thread;
			this.conferenceCallId = conferenceCallId;
		}		
	}
}
