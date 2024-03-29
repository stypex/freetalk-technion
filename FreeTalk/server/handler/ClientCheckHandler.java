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
	
	static Object lock = new Object();

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

			synchronized (lock) {
				if (cdFrom != null) {
					if (cdFrom.getProbed() < start) {
						p = new Prober(cdFrom);
						p.execute();
					}
				}
				
				if (cd != null) {
					if (cd.getProbed() < start) {
						p = new Prober(cd);
						p.execute();
					}
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
