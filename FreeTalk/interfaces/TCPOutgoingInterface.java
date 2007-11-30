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
public class TCPOutgoingInterface extends OutgoingInterface {

	Socket socket;
	
	public TCPOutgoingInterface(InetAddress ip, int port) {
		// TODO Auto-generated constructor stub
	}

	public TCPOutgoingInterface(Socket socket) {
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see interfaces.OutgoingInterface#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see interfaces.OutgoingInterface#send(messages.Message)
	 */
	@Override
	public void send(Message message) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Socket getSocket() {
		// TODO Auto-generated method stub
		return null;
	}

}
