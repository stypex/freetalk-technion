/**
 * 
 */
package server.handler;

import java.util.ArrayList;

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
			
			long start = System.currentTimeMillis();
			
			ArrayList<ProbeWrapper> wrappers = 
				new ArrayList<ProbeWrapper>();
			
			// For each client create a thread which will probe the
			// client
			for (ClientData cd : ClientsHash.getInstance().values()) {
				cId = new ConnectionId("Server", cd.getName());
				p = new Prober(cd, cId);
				
				ProbeWrapper pw = new ProbeWrapper(p);
				wrappers.add(pw);
				pw.start();
			}
			
			// Wait for all the probe threads to die
			for (ProbeWrapper pw : wrappers) {
				try {
					pw.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// Check how long we have left to sleep
			long end = System.currentTimeMillis();
			long wait = Consts.PROBE_WAIT > (end - start) ?
					Consts.PROBE_WAIT - (end - start) : 0;
			try {		
				if (wait > 0)
					Thread.sleep(wait);
			} catch (InterruptedException e) {}
		}
	}
	
	public static class ProbeWrapper extends Thread {

		Prober p;
		
		public ProbeWrapper(Prober p) {
			super();
			
			this.p = p;
		}

		@Override
		public void run() {
			p.execute();
		}
		
		
	}
}
