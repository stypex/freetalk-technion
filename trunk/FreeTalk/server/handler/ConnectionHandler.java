package server.handler;

import interfaces.IncomingInterface;
import interfaces.OutgoingInterface;
import interfaces.TCPOutgoingInterface;

import java.io.IOException;
import java.net.SocketTimeoutException;

import messages.ConAckMessage;
import messages.ConnectMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.InitCallMessage;
import messages.Message;
import messages.TerminationMessage;
import messages.ConAckMessage.ConnMethod;
import messages.ErrorMessage.ErrorType;
import server.data.ClientData;
import server.data.ClientsHash;
import util.Consts;
import util.Consts.ConnectionMethod;

public class ConnectionHandler extends HandlerThread {

	ConnectMessage cm;

	ConnectionId cId;

	public ConnectionHandler(ConnectMessage cm, IncomingInterface inInter) {
		super(cm.getFrom(), inInter, new TCPOutgoingInterface(inInter.getSocket()));

		this.cm = cm;
		this.cId = cm.getCId();
	}

	@Override
	public void run() {
		super.run();

		try {
			ClientData cd = ClientsHash.getInstance().get(cm.getConnTo());
			if (cd == null) {
				ErrorMessage em = new ErrorMessage("Server", cm.getFrom(), 
						cm.getCId(), ErrorType.CLIENT_DOES_NOT_EXIST);
				out.send(em);
				return;
			}

			OutgoingInterface out2;
			IncomingInterface in2;

			synchronized (cd) {
				ConnMethod com = calcConnType(cm.getFrom(), cm.getConnTo());

				ConAckMessage cam = new ConAckMessage("Server", cm.getFrom(), 
						cm.getCId(), cd.getIp(), com);
				out.send(cam);

				if (com.getCm().equals(ConnectionMethod.TCPReverse)) {

					ClientData cd1 = ClientsHash.getInstance().get(cm.getFrom());
					InitCallMessage ic;

					synchronized (cd1) {
						ic = new InitCallMessage("Server", cd.getName(), 
								cId, cm.getFrom(), cd1.getIp(), cd1.getPort2(),
								cm.getCCid());
					}

					out2 = cd.createOutInterface(cId, true);
					out2.send(ic);
					
					if (out2.getSocket() != cd.getTcp80())
						out2.close();
				}

				if (!com.getCm().equals(ConnectionMethod.Indirect))
					return;

				// From this point on we handle the 
				// indirect connection
				registerForClient(cd.getName());

				out2 = cd.createOutInterface(cId, false);
				in2 = out2.createMatching();
			}

			Message received = null;

			do {
				try {
					received = in.receive(500);

					out2.send(received);

					if (received instanceof TerminationMessage)
						break;

				} catch (SocketTimeoutException e) {}

				try {
					received = in2.receive(500);


					out.send(received);
				} catch (SocketTimeoutException e) {}

			} while ((received == null || 
					!(received instanceof TerminationMessage))
					&& !isStopped);


			in2.close();
			out2.close();


		} catch (IOException e) {
			if (isStopped)
				return;

			e.printStackTrace();
		} finally {
			unregisterForAllClients();
			in.close();
			out.close();
		}
	}

	public static ConnMethod calcConnType(String from, String connTo) {
		ClientData cd1 = ClientsHash.getInstance().get(from);
		ClientData cd2 = ClientsHash.getInstance().get(connTo);

		synchronized (cd1) {
			synchronized (cd2) {
				if (cd1.isPort1open() && cd2.isPort1open())
					return new ConnMethod(ConnectionMethod.UDPDirect, cd2.getPort1());
				if (cd2.isPort2open())
					return new ConnMethod(ConnectionMethod.TCPDirect, cd2.getPort2());
				if (cd1.isPort2open())
					return new ConnMethod(ConnectionMethod.TCPReverse, cd2.getPort2());
				if (cd2.isPort1open() || cd2.isPort80open()) // Server can get to cd2
					return new ConnMethod(ConnectionMethod.Indirect, Consts.SERVER_PORT);

				// No connection possible
				return new ConnMethod(ConnectionMethod.None, Consts.SERVER_PORT);
			}
		}
	}
}
