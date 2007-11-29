/**
 * 
 */
package server.data;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import server.handler.HandlerThread;

/**
 * @author lenka
 *
 */
public class ClientsHash extends ConcurrentHashMap<String, ClientsHash.ClientData> {

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
	
	public static class ClientData {
		
		public InetAddress ip;
		public int port1;
		public int port2;
		public boolean port1open; 
		public boolean port2open; 
		
		public Socket tcp80;	
		public List<HandlerThread> threads;

		
		public ClientData(InetAddress ip, int port1, int port2, boolean port1open, boolean port2open, Socket tcp80, List<HandlerThread> threads) {
			super();
			this.ip = ip;
			this.port1 = port1;
			this.port2 = port2;
			this.port1open = port1open;
			this.port2open = port2open;
			this.tcp80 = tcp80;
			this.threads = threads;
		}
		
		
	}
}
