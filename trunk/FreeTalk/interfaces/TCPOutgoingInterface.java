/**
 * 
 */
package interfaces;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import util.Log;

import messages.Message;

/**
 * @author lenka
 *
 */
public class TCPOutgoingInterface extends OutgoingInterface {

	Socket socket;
	
	public TCPOutgoingInterface(InetAddress ip, int remotePort) throws IOException {
		super(0, remotePort, ip);
		
		socket = new Socket(ip, remotePort);
		setLocalPort(socket.getLocalPort());
	}
	
	public TCPOutgoingInterface(Socket socket) {
		super(socket.getLocalPort(), socket.getPort(), socket.getInetAddress());
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
		
		Log.getInstance().addDatedText("Sending Message to TCP port: " + socket.getPort(), true);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(message);
		Log.getInstance().addMessage(message);
	}

	@Override
	public Socket getSocket() {
		return socket;
	}

	@Override
	public IncomingInterface createMatching() {
		return new TCPIncomingInterface(socket);
	}

}
