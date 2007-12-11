/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import util.Log;

import messages.Message;

/**
 * @author lenka
 *
 */
public class TCPIncomingInterface extends IncomingInterface {

	Socket socket;
	
	public TCPIncomingInterface(Socket socket) {
		super(socket.getLocalPort(), socket.getPort(), socket.getInetAddress());
		this.socket = socket;
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#close()
	 */
	@Override
	public void close() {
		if (!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#getSocket()
	 */
	@Override
	public Socket getSocket() {
		return socket;
	}

	/* (non-Javadoc)
	 * @see interfaces.IncomingInterface#receive(long)
	 */
	@Override
	public Message receive(int timeout) throws IOException {

		
		socket.setSoTimeout(timeout);
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		
		try {
			Log.getInstance().addText("Receiving Message on TCP port: " + socket.getLocalPort(), true);
			Object o = in.readObject();
			
			if (o instanceof Message) {
				Message m = (Message) o;
				Log.getInstance().addMessage(m);
				return m;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public OutgoingInterface createMatching() {
		return new TCPOutgoingInterface(socket);
	}
}
