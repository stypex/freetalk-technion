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

import messages.ConnectionId;
import messages.Message;
import messages.ProbeAckMessage;
import messages.ProbeMessage;
import server.data.ClientData;
import util.Consts.ResponseCode;

/**
 * @author lenka
 *
 */
public class Prober {

	ClientData cd;
	ConnectionId cId;

	public Prober(ClientData cd, ConnectionId cId) {
		this.cd = cd;
		this.cId = cId;
	}

	public void execute() {

		synchronized (cd) {
			boolean isUDP = (checkUDP() != ResponseCode.BAD);
			boolean isTCP = (checkTCP() != ResponseCode.BAD);

			if (!isUDP && !isTCP && cd.getTcp80() != null) {
				boolean isTCP80 = (checkTCP80() != ResponseCode.BAD);

				if (!isTCP80) {
					ClientRemover cr = new ClientRemover(cd.getName(), cId);
					cr.execute();
				}
			}
		}
	}

	private ResponseCode checkTCP80() {
		OutgoingInterface out = new TCPOutgoingInterface(cd.getTcp80());
		IncomingInterface in = new TCPIncomingInterface(cd.getTcp80());

		ResponseCode check;
		
		synchronized (cd.getTcp80()) {
			check = checkPort(out, in);
		}
		cd.setPort2open(check);
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
		OutgoingInterface out = new UDPOutgoingInterface(cd.getIp(), 80, cd.getPort1(), cId);
		IncomingInterface in = new UDPIncomingInterface(cId, cd.getIp(), cd.getPort1(), 80);
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
		try {
			ProbeMessage pm = new ProbeMessage("Server", cd.getName(), cId);
			out.send(pm);
			Message m = in.receive(2000);

			if (m instanceof ProbeAckMessage) {
				ProbeAckMessage pam = (ProbeAckMessage) m;

				if (pam.getCId() == cId &&
						pam.getFrom() == cd.getName() &&
						pam.getRCode() == ResponseCode.OK) {

					return pam.getRCode();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseCode.BAD;
	}
}
