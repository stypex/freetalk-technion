/**
 * 
 */
package server.handler;

import interfaces.IncomingInterface;
import interfaces.OutgoingInterface;
import interfaces.TCPIncomingInterface;
import interfaces.TCPOutgoingInterface;
import interfaces.UDPIncomingInterface;
import interfaces.UDPOutgoingInterface;

import java.io.IOException;
import java.net.SocketTimeoutException;

import messages.ConnectionId;
import messages.Message;
import messages.ProbeAckMessage;
import messages.ProbeMessage;
import server.data.ClientData;
import util.Consts;
import util.Consts.ResponseCode;

/**
 * @author lenka
 *
 */
public class Prober {

	ClientData cd;
	ConnectionId cId;

	public Prober(ClientData cd) {
		this.cd = cd;
		this.cId = new ConnectionId("Server", cd.getName());
	}

	public void execute() {

		synchronized (cd) {
			boolean isUDP = (!checkUDP().equals(ResponseCode.BAD));
			boolean isTCP = (!checkTCP().equals(ResponseCode.BAD));
			
			boolean isTCP80 = false;
			
			// Failed both checks
			if (!isUDP && !isTCP) {
				// Has a tcp80 connection - check that one
				if (cd.getTcp80() != null) 
					isTCP80 = (!checkTCP80().equals(ResponseCode.BAD));

				if (!isTCP80) { // No success			
					// Check if it's time to do total client deletion
					if (cd.setCantProbe()) { 
						ClientRemover cr = 
							new ClientRemover(cd.getName());
						cr.execute();
					}
					return;
				}
			}

			cd.setProbed(); // Register successful probe
		}
	}

	private ResponseCode checkTCP80() {
		OutgoingInterface out = new TCPOutgoingInterface(cd.getTcp80());
		IncomingInterface in = new TCPIncomingInterface(cd.getTcp80());

		ResponseCode check;
		
		synchronized (cd.getTcp80()) {
			check = checkPort(out, in);
		}
		cd.setPort80open(check);
		return check;
	}

	private ResponseCode checkTCP() {
		OutgoingInterface out;
		ResponseCode check = ResponseCode.BAD;
		try {
			out = new TCPOutgoingInterface(cd.getIp(), cd.getPort2());
		} catch (IOException e) {
			cd.setPort2open(ResponseCode.BAD);
			return check;
		}

		IncomingInterface in = new TCPIncomingInterface(out.getSocket());

		try {
			check = checkPort(out, in);
			cd.setPort2open(check);
			return check;
		} finally {
			out.close();
			in.close();
		}
	}

	private ResponseCode checkUDP() {
		OutgoingInterface out = new UDPOutgoingInterface(cd.getIp(), Consts.SERVER_PORT, cd.getPort1(), cId);
		IncomingInterface in = new UDPIncomingInterface(cId, cd.getIp(), cd.getPort1(), Consts.SERVER_PORT);
		try {
			ResponseCode check = checkPort(out, in);
			cd.setPort1open(check);
			return check;
		} finally {
			out.close();
			in.close();
		}
	}

	private ResponseCode checkPort(OutgoingInterface out, IncomingInterface in){
		ProbeMessage pm = new ProbeMessage("Server", cd.getName(), cId);

		Message m = null;
		try {
			out.send(pm);

			m = in.receive(6000);
		} catch (SocketTimeoutException e) {
			return ResponseCode.BAD;
		} catch (IOException e) {
			return ResponseCode.BAD;
		}

		if (m instanceof ProbeAckMessage) {
			ProbeAckMessage pam = (ProbeAckMessage) m;

			if (pam.getCId().equals(cId) &&
					pam.getFrom().equals(cd.getName()) &&
					pam.getRCode().equals(ResponseCode.OK)) {

				return pam.getRCode();
			}

		}
		return ResponseCode.BAD;
	}
}
