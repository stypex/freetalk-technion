/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import messages.Message;
import util.Log;

/**
 * @author lenka
 *
 */
public class TCPIncomingInterface extends IncomingInterface {

	Socket socket;
	
	public TCPIncomingInterface(Socket socket) {
		super(socket.getLocalPort(), socket.getPort(), socket.getInetAddress());
		this.socket = socket;
		try {
			if (!socket.isBound())
				socket.bind(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		if (socket.isClosed())
			return null;
		
		socket.setSoTimeout(timeout);
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		
		try {
			Object o = in.readObject();
			
			synchronized (Log.getInstance()) {
				Log.getInstance().addDatedText("Receiving Message on TCP port: " + socket.getLocalPort(), true);
				
				if (o instanceof Message) {
					Message m = (Message) o;
					Log.getInstance().addMessage(m);
					return m;
				}
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
