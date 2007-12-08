/**
 * 
 */
package server.handler;

import interfaces.IncomingInterface;
import interfaces.TCPOutgoingInterface;

import java.io.IOException;

import messages.ClientCheckMessage;
import messages.ConAckMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.ConAckMessage.ConnMethod;
import messages.ErrorMessage.ErrorType;
import server.data.ClientData;
import server.data.ClientsHash;

/**
 * @author lenka
 *
 */
public class ClientCheckHandler extends HandlerThread {

	ClientCheckMessage ccm;

	ConnectionId cId;
	
	/**
	 * @param client
	 * @param in
	 * @param out
	 */
	public ClientCheckHandler(ClientCheckMessage ccm, IncomingInterface in) {
		super(ccm.getFrom(), in, new TCPOutgoingInterface(in.getSocket()));
		
		this.ccm = ccm;
		this.cId = ccm.getCId();
	}

	public void run() {
		super.run();

		try {
			ClientData cd = ClientsHash.getInstance().get(ccm.getFrom());
			if (cd == null) {
				ErrorMessage em = new ErrorMessage("Server", ccm.getFrom(), 
						ccm.getCId(), ErrorType.CLIENT_DOES_NOT_EXIST);
				out.send(em);
				return;
			}

			synchronized (cd) {
				Prober p = new Prober(cd, ccm.getCId());
				p.execute();
				
				ConnMethod com = ConnectionHandler.calcConnType(ccm.getFrom(), ccm.getTarget());
				ConAckMessage cam = new ConAckMessage("Server", ccm.getFrom(), 
						ccm.getCId(), cd.getIp(), com);
				out.send(cam);
			}
			
		} catch (IOException e) {
			if (!isStopped)
				e.printStackTrace();
		} finally {
			unregisterForAllClients();
			in.close();
			out.close();
		}
	}
}
