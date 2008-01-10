/**
 * 
 */
package client.listeners;

import interfaces.IncomingInterface;
import interfaces.OutgoingInterface;
import interfaces.TCPIncomingInterface;
import interfaces.TCPOutgoingInterface;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import messages.CallMeMessage;
import messages.Message;
import util.Consts;
import client.ClientMain;
import client.Globals;

/**
 * @author lenka
 *
 */
public class TCPListener80 extends ClientListener {

//	Socket socket;
	TCPIncomingInterface in;
	
	public TCPListener80(Socket socket) {
		this.in = new TCPIncomingInterface(socket);
	}

	@Override
	public void run() {
		super.run();
		
		while (isStopped == false) {
			try {
				Message m = in.receive(0);

				IncomingInterface newIn = in;
				OutgoingInterface newOut = in.createMatching();
				
				// If we got a Call Me message, we establish
				// a new connection with the server
				if (m instanceof CallMeMessage) {
					CallMeMessage cmm = (CallMeMessage) m;
					newOut = new TCPOutgoingInterface(Globals.getServerIP(), 
							Consts.SERVER_PORT);
					
					CallMeMessage cmmBack = 
						new CallMeMessage(Globals.getClientName(), 
								"Server", cmm.getCId());
					newOut.send(cmmBack);
					newIn = newOut.createMatching();
					m = newIn.receive(0);
				}
				
				if (!receiveMessage(m, newIn)) {
					tcp80receiveMessage(m, newIn, newOut);
				}
				
			} catch (EOFException e) {
				if (!isStopped)
					System.err.println("The tcp80 socket was closed. Closing the listener.");
				
				return;
			} catch (IOException e) {
				if (!isStopped) {
					ClientMain.setServerOut();
				}
				return;
			}
		}
	}

	private void tcp80receiveMessage(Message m, IncomingInterface newIn, OutgoingInterface newOut) {
		
		// Do nothing for now
	}

	
}
