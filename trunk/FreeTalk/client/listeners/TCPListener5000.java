/**
 * 
 */
package client.listeners;

import interfaces.TCPIncomingInterface;
import interfaces.TCPOutgoingInterface;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import messages.ClientsAddedMessage;
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
		boolean success = false;
		int port = Globals.getTCPPort();
		try {
			while (!success) {
				try {
					ss = new ServerSocket(port);
					success = true;
				} catch (BindException e) {
					port++;
				}
			}
			
			if (port != Globals.getTCPPort())
				Globals.setTCPPort(port);
			
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

				if (m instanceof ProbeMessage) {
					SimpleFunctions.replyProbe(out, (ProbeMessage) m);
				}
				if (m instanceof ClientsAddedMessage) {
					ClientsAddedMessage cam = (ClientsAddedMessage) m;
					SimpleFunctions.addClients(cam.getClients());

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
