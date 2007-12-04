/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import messages.Message;

/**
 * @author lenka
 *
 */
public class TCPOutgoingInterface extends OutgoingInterface {

	Socket socket;
	
	public TCPOutgoingInterface(InetAddress ip, int port) throws IOException {
		
		socket = new Socket(ip, port);
	}
	
	public TCPOutgoingInterface(Socket socket) {
		this.socket = socket;
	}
	
	/* (non-Javadoc)
	 * @see interfaces.OutgoingInterface#close()
	 */
	@Override
	public void close() {
		if (!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/* (non-Javadoc)
	 * @see interfaces.OutgoingInterface#send(messages.Message)
	 */
	@Override
	public void send(Message message) throws IOException {
		
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(message);
	}

	@Override
	public Socket getSocket() {
		return socket;
	}

}
