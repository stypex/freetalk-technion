/**
 * 
 */
package interfaces;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import messages.ConnectionId;
import messages.Message;
import messages.UDPAckMessage;
import util.Log;

/**
 * @author lenka
 *
 */
public class UDPOutgoingInterface extends OutgoingInterface {

	/**
	 * For UDP backoff acks
	 */
	UDPIncomingInterface ackInt;
	
	ConnectionId cId;
	
	DatagramSocket socket;
	
	public UDPOutgoingInterface(InetAddress ip, int localPort, int remotePort, ConnectionId cId) {
		super(localPort, remotePort, ip);
		this.cId = cId;
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// With a special connection id
		ackInt = new UDPIncomingInterface(cId.getAck(), ip, remotePort, localPort);
	}

	/* (non-Javadoc)
	 * @see interfaces.OutgoingInterface#close()
	 */
	@Override
	public void close() {
		ackInt.close();

		socket.close();
	}

	/* (non-Javadoc)
	 * @see interfaces.OutgoingInterface#send(messages.Message)
	 */
	@Override
	public void send(Message message) throws IOException {

		Message ack;
		
		message.addUdpData();
		
		// UDP exponential backoff (1 sec - 2 sec - 4 sec - 8 sec - 16 sec)
		for (int msecs = 1000; msecs <= 4000; msecs*=2) {
			
			sendMessage(message);

			try {
				do {
					ack = ackInt.receive(msecs); // Get ack
				} while (ack.getUdpData().getUdpSn() > message.getUdpData().getUdpSn()); // The ack must belong to this message
			} catch (SocketTimeoutException e) { 
				message.getUdpData().incUdpSn(); // No ack. Try again
				continue;
			}
			return; // All fine
		}

		// Can't connect
		throw new IOException("Failed connecting to target");
	}

	@Override
	public Socket getSocket() {
		return null;
	}


	@Override
	public IncomingInterface createMatching() {
		return new UDPIncomingInterface(cId, remoteIp, remotePort, localPort);
	}

	public void sendUDPAck(Message m) {
		ConnectionId cid = m.getCId().getAck();
		
		Message ack = new UDPAckMessage(m.getTo(), 
				m.getFrom(), cid, m.getUdpData().getUdpSn());
		
		try {
			sendMessage(ack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sends one message to the target. No exponential backoff
	 * @param m
	 * @throws IOException
	 */
	private void sendMessage(Message m) throws IOException {
		
		// So they'll know how to answer
		m.getUdpData().setLocalPort(localPort);
		
		//	Serialize...
		ByteArrayOutputStream b_out = new ByteArrayOutputStream();
		ObjectOutputStream o_out = new ObjectOutputStream(b_out);
		o_out.writeObject(m);
		byte[] b = b_out.toByteArray();

		// Send message
		DatagramPacket dp = new DatagramPacket(b, b.length, remoteIp, remotePort); 
		socket.send(dp);
		
		synchronized (Log.getInstance()) {
			Log.getInstance().addDatedText("Sending Message to UDP port: " + remotePort, true);
			Log.getInstance().addMessage(m);
		}
	}
}
