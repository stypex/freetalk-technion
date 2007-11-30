/**
 * 
 */
package server.data;

import interfaces.IncomingInterface;
import interfaces.UDPIncomingInterface;

import java.util.concurrent.ConcurrentHashMap;

import messages.ConnectionId;

/**
 * @author lenka
 *
 */
public class ThreadsHash extends ConcurrentHashMap<ConnectionId, IncomingInterface> {

	private static final long serialVersionUID = 3222918723101144580L;

	private static ThreadsHash singleton;
	
	/**
	 * A singleton initializer
	 * @return
	 */
	public static ThreadsHash getInstance() {
		if (singleton == null) {
			singleton = new ThreadsHash();
		}
			
		return singleton;
	}
	
	protected ThreadsHash() {
		super();
	}
//	
//	public static class Data {
//		public IncomingInterface inInterface;
//		public HandlerThread thread; 
//		
//		public Data(IncomingInterface inInterface, HandlerThread thread) {
//			super();
//			this.inInterface = inInterface;
//			this.thread = thread;
//		}		
//	}

	public void register(ConnectionId cId, UDPIncomingInterface in) {
		put(cId, in);	
	}
}
