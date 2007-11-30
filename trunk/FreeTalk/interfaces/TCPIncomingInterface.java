/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import messages.Message;

/**
 * @author lenka
 *
 */
public class TCPIncomingInterface extends IncomingInterface {

	Socket socket;
	
	public TCPIncomingInterface(InetAddress ip, int port) {
		// TODO Auto-generated constructor stub
	}
	
	public TCPIncomingInterface(Socket socket) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#getSocket()
	 */
	@Override
	public Socket getSocket() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#receive(long)
	 */
	@Override
	public Message receive(long timeout) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
