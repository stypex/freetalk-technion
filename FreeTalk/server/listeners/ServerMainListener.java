/**
 * 
 */
package server.listeners;

import interfaces.TCPIncomingInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import messages.Message;

import server.handler.HandlerThread;



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
		
		udp.start();
		tcp.run();
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void run()  {
		while (true) {
			try {
				Socket s = ss.accept();
				TCPIncomingInterface in = new TCPIncomingInterface(s);
				Message m = in.receive(0);
				HandlerThread ht = HandlerThread.createHandler(m, in);
				ht.start();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
