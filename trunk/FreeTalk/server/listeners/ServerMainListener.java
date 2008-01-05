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

import server.Log;
import server.data.ClientData;
import server.data.ClientsHash;
import server.handler.HandlerThread;
import server.handler.ProbeThread;
import util.Consts;



/**
 * @author lenka
 *
 */
public class ServerMainListener {

	ServerSocket ss;
	public static UDPListener80 udp;
	
	
	public ServerMainListener() {
		super();
		try {
			ss = new ServerSocket(Consts.SERVER_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		ServerMainListener tcp = new ServerMainListener();
		udp = new UDPListener80();
		ProbeThread pt = new ProbeThread();
		

		Log log = new Log();
		Log.init(log);
		
		Log.getInstance().addDatedText("Starting server.", true);
		
		udp.start();
		pt.start();
		tcp.run();	
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
					synchronized (cd.callMeLock) {
						cd.callMeSocket = s; 
						cd.callMeLock.notify();
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
