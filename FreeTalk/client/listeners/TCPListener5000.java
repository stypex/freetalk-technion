/**
 * 
 */
package client.listeners;

import interfaces.TCPIncomingInterface;
import interfaces.TCPOutgoingInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import messages.Message;
import messages.ProbeMessage;
import client.Globals;
import client.func.SimpleFunctions;


/**
 * @author Ilya
 *
 */
public class TCPListener5000 extends StoppableThread {

	ServerSocket ss;
	
	public TCPListener5000() {
		super();
		try {
			ss = new ServerSocket(Globals.getTCPPort());
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
				
				TCPOutgoingInterface out = new TCPOutgoingInterface(in.getSocket());
				
				String from = m.getFrom();
				String to = m.getTo();
				
				System.out.println("from=");
				System.out.println(from);
				System.out.println("to=");
				System.out.println(to);
				
				if (m instanceof ProbeMessage) {
					SimpleFunctions.replyProbe(out, (ProbeMessage) m);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
