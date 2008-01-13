/**
 * 
 */
package server.handler;

import interfaces.IncomingInterface;
import interfaces.TCPOutgoingInterface;
import messages.ClientExitMessage;
import messages.ConnectionId;

/**
 * @author lenka
 *
 */
public class ClientTerminationHandler extends HandlerThread {

	ClientExitMessage tm;
	
	ConnectionId cId;

	public ClientTerminationHandler(ClientExitMessage tm, IncomingInterface in) {
		super(tm.getFrom(), in, new TCPOutgoingInterface(in.getSocket()));
		this.tm = tm;
		cId = tm.getCId();
	}
	
	public void run() {
		super.run();

		yield();
		
		ClientRemover cr = new ClientRemover(tm.getFrom(), cId);
		cr.execute();
		
		unregisterForAllClients();
		in.close();
		out.close();
		
	}
}
