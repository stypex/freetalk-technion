/**
 * 
 */
package client.listeners;

import interfaces.TCPIncomingInterface;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import messages.Message;
import util.Consts;
import client.Globals;


/**
 * @author Ilya
 *
 */
public class TCPListener5000 extends ClientListener {

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
		if (!Consts.doTCP5000)
			return;
		
		while (isStopped == false) {
			try {
				Socket s = ss.accept();
				TCPIncomingInterface in = new TCPIncomingInterface(s);
				Message m = in.receive(0);

				receiveMessage(m, in);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
