/**
 * 
 */
package server.data;

import interfaces.IncomingInterface;

import java.util.concurrent.ConcurrentHashMap;

import server.handler.HandlerThread;

import messages.ConnectionId;

/**
 * @author lenka
 *
 */
public class ThreadsHashServer extends ConcurrentHashMap<ConnectionId, ThreadsHashServer.Data> {

	private static final long serialVersionUID = 3222918723101144580L;

	private static ThreadsHashServer singleton;
	
	/**
	 * A singleton initializer
	 * @return
	 */
	public static ThreadsHashServer getInstance() {
		if (singleton == null) {
			singleton = new ThreadsHashServer();
		}
			
		return singleton;
	}
	
	private ThreadsHashServer() {
		super();
	}
	
	public static class Data {
		public IncomingInterface inInterface;
		public HandlerThread thread; 
		
		public Data(IncomingInterface inInterface, HandlerThread thread) {
			super();
			this.inInterface = inInterface;
			this.thread = thread;
		}		
	}
}
