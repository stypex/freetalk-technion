/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import messages.ConnectionId;
import messages.Message;
import util.ThreadsHash;

/**
 * @author lenka
 *
 */
public class UDPIncomingInterface extends IncomingInterface {


	ConnectionId cId;
	
	LinkedBlockingQueue<Message> q;
	
	public UDPIncomingInterface(ConnectionId cId, InetAddress ip, int remotePort, int localPort) {
		super(localPort, remotePort, ip);
		
		this.cId = cId;
		
		q = new LinkedBlockingQueue<Message>();
		
		ThreadsHash.getInstance().register(cId, this);
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#close()
	 */
	@Override
	public void close() {

		ThreadsHash h = ThreadsHash.getInstance();
		if (h.containsKey(cId) && ThreadsHash.getInstance().get(cId).equals(this))
			ThreadsHash.getInstance().remove(cId);
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#getSocket()
	 */
	@Override
	public Socket getSocket() {
		return null;
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#receive(long)
	 */
	@Override
	public Message receive(int timeout) throws IOException {
		
		Message m = null;
		try {
			if (timeout == 0) {
				m = q.take();
			}
			else {
				m = q.poll(timeout, TimeUnit.MILLISECONDS);
			}
				
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (m == null)
			throw new SocketTimeoutException();
		
//		Log.getInstance().addDatedText("Receiving Message on UDP port: " + localPort, true);
//		Log.getInstance().addMessage(m);
		
		return m;
	}

	
	/**
	 * Will be called by other classes and threads to insert a
	 * new message into the queue.
	 * @param origM
	 */
	public void accept(Message m) {
		q.add(m);
	}

	@Override
	public OutgoingInterface createMatching() {
		return new UDPOutgoingInterface(remoteIp, localPort, remotePort, cId);
	}
}
