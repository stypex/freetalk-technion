/**
 * 
 */
package server.handler;

import messages.ConnectionId;
import server.data.ClientData;
import server.data.ClientsHash;
import util.Consts;


/**
 * @author lenka
 *
 */
public class ProbeThread extends HandlerThread {

	public ProbeThread() {
		super(null, null, null);
		// TODO Auto-generated constructor stub
	}

	public void run() {

		Prober p;
		ConnectionId cId;

		while (true) {
			for (ClientData cd : ClientsHash.getInstance().values()) {
				cId = new ConnectionId("Server", cd.getName());
				p = new Prober(cd, cId);
				p.execute();
			}
			
			try {
				Thread.sleep(Consts.PROBE_WAIT);
			} catch (InterruptedException e) {}
		}
	}
}
