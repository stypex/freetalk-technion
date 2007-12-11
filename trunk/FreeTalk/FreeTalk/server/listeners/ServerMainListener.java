/**
 * 
 */
package server.listeners;

import interfaces.TCPIncomingInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import messages.CallMeMessage;
import messages.Message;

import server.data.ClientData;
import server.data.ClientsHash;
import server.handler.HandlerThread;
import server.handler.ProbeThread;



/**
 * @author lenka
 *
 */
public class ServerMainListener {

	ServerSocket ss;
	
	
	
	public ServerMainListener() {
		super();
		try {
			ss = new ServerSocket(80);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		ServerMainListener tcp = new ServerMainListener();
		UDPListener80 udp = new UDPListener80();
		ProbeThread pt = new ProbeThread();
		
		udp.start();
		tcp.run();
		pt.start();
	}

	private void run()  {
		while (true) {
			try {
				Socket s = ss.accept();
				TCPIncomingInterface in = new TCPIncomingInterface(s);
				Message m = in.receive(0);
				
				if (m instanceof CallMeMessage) {
					CallMeMessage cmm = (CallMeMessage) m;
					ClientData cd = ClientsHash.getInstance().get(cmm.getFrom());
					synchronized (cd) {
						cd.callMeSocket = s; 
					}
					continue;				
				}
				HandlerThread ht = HandlerThread.createHandler(m, in);
				ht.start();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
