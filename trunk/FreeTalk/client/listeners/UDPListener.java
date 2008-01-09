/**
 * 
 */
package client.listeners;

import interfaces.OutgoingInterface;
import interfaces.UDPIncomingInterface;
import interfaces.UDPOutgoingInterface;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import messages.ClientExitMessage;
import messages.Message;
import messages.UDPAckMessage;
import util.Consts;
import util.Log;
import util.ThreadsHash;
import client.Globals;

/**
 * @author lenka
 *
 */
public class UDPListener extends ClientListener {

	DatagramSocket ds;
	
	// In case we receive several identical messages with diff.
	// serial number, we want to discard duplicates
	HashMap<String,HashSet<String>> receivedMessages;

	public UDPListener() {
		super();
		receivedMessages = new HashMap<String,HashSet<String>>();
		
		boolean success = false;
		int port = Globals.getUDPPort();
		try {
			while (!success) {
				try {
					ds = new DatagramSocket(port);
					success = true;
				} catch (BindException e) {
					port++;
				}
			}
			
			if (port != Globals.getUDPPort())
				Globals.setUDPPort(port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run()  {
		if (!Consts.doUDP5000)
			return;
		
		byte[] b = new byte[65535];
		ByteArrayInputStream bIn = new ByteArrayInputStream(b);
		DatagramPacket dp = new DatagramPacket(b, b.length);
		ObjectInputStream oIn;
		
		while (isStopped == false) {
			try {
				ds.receive(dp); // blocks
				
				// DeSerialization...
				oIn = new ObjectInputStream(bIn);
				Object o = oIn.readObject();
				dp.setLength(b.length); // must reset length field!
				bIn.reset(); 
				
				final Message m = (Message)o;
				
				// We will discard duplicate messages
				if(receivedMessages.containsKey(m.getUdpData().getId().getClientName())){
					if (receivedMessages.get(m.getUdpData().getId().getClientName()).contains(m.getUdpData().getId().getId()))
						continue;
				}
				else
					receivedMessages.put(m.getUdpData().getId().getClientName(), new HashSet<String>());
				
				receivedMessages.get(m.getUdpData().getId().getClientName()).add(m.getUdpData().getId().getId());
				
				Log.getInstance().addDatedText("Receiving Message in listener on UDP port: " + Globals.getUDPPort(), true);
				Log.getInstance().addMessage(m);
				
				// Send through the incoming interface
				boolean passed = ThreadsHash.getInstance().passMessage(m);
				
				// Nothing more to do with UDPAcks
				if (m instanceof UDPAckMessage) {
					continue;				
				}
				
				final UDPIncomingInterface in = new UDPIncomingInterface(m.getCId(), 
						dp.getAddress(), m.getUdpData().getLocalPort(), Globals.getUDPPort());
				
				// Send UDP ack
				OutgoingInterface out = in.createMatching();
				
				((UDPOutgoingInterface)out).sendUDPAck(m);
				
				out.close();
						
				if (!passed) {
					
					if (m instanceof ClientExitMessage){
						ClientExitMessage cem = (ClientExitMessage)m;
						receivedMessages.remove(cem.getClient());
					}
					
					// Must be in a different thread because some of
					// the handles send and receive more messages so this
					// loop has to go on and can't get stuck on handles
					new Thread(new Runnable() {

						public void run() {
							// General handling
							receiveMessage(m, in);
						}
						
					}).start();
					
				}
				else
					in.close();
				
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
