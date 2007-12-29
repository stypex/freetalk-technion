/**
 * 
 */
package util;

import interfaces.UDPIncomingInterface;

import java.util.concurrent.ConcurrentHashMap;

import client.func.Exiter;

import messages.ConnectionId;
import messages.Message;

/**
 * @author lenka
 *
 */
public class ThreadsHash extends ConcurrentHashMap<ConnectionId, UDPIncomingInterface> {

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
			
		if (!containsKey(cId))
			put(cId, in);
//		else
//			System.out.println("Can't register interface for cid: " + cId);
	}

	
	/**
	 * Call this to pass a message to the right UDP incoming
	 * @param m
	 * @return true if found the right interface.
	 */
	public boolean passMessage(Message m) {
		
		UDPIncomingInterface in = get(m.getCId());
		
		if (in == null) {
			return false;
		}
		
		in.accept(m);
		return true;
	}
}
