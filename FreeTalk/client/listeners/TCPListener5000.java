/**
 * 
 */
package client.listeners;

import interfaces.TCPIncomingInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import messages.Message;
import java.util.List;
import client.data.ClientsList;
import client.data.ConferenceCallsHash;
import client.func.TalkThread;
import client.listeners.StoppableThread;


/**
 * @author Ilya
 *
 */
public class TCPListener5000 extends StoppableThread {

	ServerSocket ss;
	
	public TCPListener5000() {
		super();
		try {
			ss = new ServerSocket(5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run()  {
		while (isStopped == false) {
			try {
				Socket s = ss.accept();
				TCPIncomingInterface in = new TCPIncomingInterface(s);
				Message m = in.receive(0);
				
				String from = m.getFrom();
				String to = m.getTo();
				
				System.out.println("from=");
				System.out.println(from);
				System.out.println("to=");
				System.out.println(to);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
