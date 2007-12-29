/**
 * 
 */
package server.listeners;

import interfaces.UDPOutgoingInterface;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

import messages.Message;
import messages.UDPAckMessage;
import util.Consts;
import util.Log;
import util.ThreadsHash;


/**
 * Listens on UDP 80. If it's not an ack, sends a UDP ack.
 * Passes the message to the UDP interface which registered
 * for the cid.
 * @author lenka
 *
 */
public class UDPListener80 extends Thread {

	DatagramSocket ds;
	
	// In case we receive several identical messages with diff.
	// serial number, we want to discard duplicates
	Set<String> receivedMessages;

	public UDPListener80() {
		super();
		receivedMessages = new HashSet<String>();
		
		try {
			ds = new DatagramSocket(Consts.SERVER_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run()  {
		
		byte[] b = new byte[65535];
		ByteArrayInputStream bIn = new ByteArrayInputStream(b);
		DatagramPacket dp = new DatagramPacket(b, b.length);
		ObjectInputStream oIn;
		
		while (true) {
			try {
				ds.receive(dp); // blocks
				
				// DeSerialization...
				oIn = new ObjectInputStream(bIn);
				Object o = oIn.readObject();
				dp.setLength(b.length); // must reset length field!
				bIn.reset(); 
				
				Message m = (Message)o;
				
				// We will discard duplicate messages
				if (receivedMessages.contains(m.getId()))
					continue;
				else
					receivedMessages.add(m.getId());
				
				Log.getInstance().addDatedText("Receiving Message in listener on UDP port: " + Consts.SERVER_PORT, true);
				Log.getInstance().addMessage(m);
				
				// Send through the incoming interface
				if (!ThreadsHash.getInstance().passMessage(m)) {
					System.err.println("UDPListener80: Can't find the right interface");
				}
				
				// Nothing more to do with UDPAcks
				if (m instanceof UDPAckMessage)
					continue;
				
				// Send UDP ack
				UDPOutgoingInterface out = new UDPOutgoingInterface(dp.getAddress(), 
						Consts.SERVER_PORT, m.getLocalPort(), m.getCId());
			
				out.sendUDPAck(m);	
				out.close();
						
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
