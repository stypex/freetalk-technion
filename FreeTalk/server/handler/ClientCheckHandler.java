/**
 * 
 */
package server.handler;

import interfaces.IncomingInterface;
import interfaces.TCPOutgoingInterface;

import java.io.IOException;

import messages.ClientCheckMessage;
import messages.ConnectMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
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
	long start;

	/**
	 * @param client
	 * @param in
	 * @param out
	 */
	public ClientCheckHandler(ClientCheckMessage ccm, IncomingInterface in) {
		super(ccm.getFrom(), in, new TCPOutgoingInterface(in.getSocket()));

		this.ccm = ccm;
		this.cId = ccm.getCId();
		start = System.currentTimeMillis();
	}

	public void run() {
		super.run();

		try {
			ClientData cd = ClientsHash.getInstance().get(ccm.getTarget());
			if (cd == null) {
				ErrorMessage em = new ErrorMessage("Server", ccm.getFrom(), 
						ccm.getCId(), ErrorType.CLIENT_DOES_NOT_EXIST);
				out.send(em);
				return;
			}

			ClientData cdFrom = ClientsHash.getInstance().get(ccm.getFrom());

			Prober p;

			System.out.println("CCM timestamp: " + start);
			
			if (cd != null) { 
				if (cd.getLastProbed() < start) {
					p = new Prober(cd, ccm.getCId());
					p.execute();
				}
			}

			if (cdFrom != null) { 
				if (cdFrom.getLastProbed() < start) {
					p = new Prober(cdFrom, ccm.getCId());
					p.execute();
				}
			}

			// Now we just activate the connection handler
			ConnectMessage cm = 
				new ConnectMessage(ccm.getFrom(), ccm.getTo(), 
						ccm.getCId(), ccm.getTarget(), ccm.getCCid());

			ConnectionHandler ch = new ConnectionHandler(cm, in);

			ch.start();

		} catch (IOException e) {
			if (!isStopped)
				e.printStackTrace();
		} finally {
			unregisterForAllClients();
		}
	}
}
