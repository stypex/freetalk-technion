/**
 * 
 */
package server.handler;

import interfaces.IncomingInterface;
import interfaces.OutgoingInterface;
import interfaces.TCPOutgoingInterface;

import java.io.IOException;
import java.util.HashSet;

import messages.ClientsAddedMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.RegAckMessage;
import messages.RegisterMessage;
import messages.ErrorMessage.ErrorType;
import server.data.ClientData;
import server.data.ClientsHash;
import util.Consts.ResponseCode;

/**
 * @author lenka
 *
 */
public class RegisterHandler extends HandlerThread {

	RegisterMessage m;

	ConnectionId cId;

	public RegisterHandler(RegisterMessage m, IncomingInterface in) {
		super(m.getFrom(), in, new TCPOutgoingInterface(in.getSocket()));

		this.m = m;
		this.cId = m.getCId();
	}

	@Override
	public void run() {
		super.run();

		String client = m.getFrom();

		try {
			ClientData cd = ClientsHash.getInstance().get(client);

			if (cd != null && !cd.getIp().equals(m.getClientIp())) { // Name in use

				ErrorMessage em = 
					new ErrorMessage("Server", client, getCId(), ErrorType.CLIENT_NAME_EXISTS);

				out.send(em);
				return;
			}



			cd = new ClientData(client, m.getClientIp(), m.getPort1(), m.getPort2(),
					ResponseCode.BAD, ResponseCode.BAD, in.getSocket());

			ClientsHash.getInstance().put(client, cd);


			synchronized (cd) {
				ClientsHash.getInstance().registerThread(cd.getName(), this);

				// Probe
				Prober pr = new Prober(cd, getCId());
				pr.execute();

				// REG_ACK
				RegAckMessage ram = new RegAckMessage("Server", cd.getName(), getCId(),
						cd.getPort1open(), cd.getPort2open(), cd.getConnectionMethod());

				out.send(ram);		


				// Clients list for client
				HashSet<String> allClients = new HashSet<String>();
				allClients.addAll(ClientsHash.getInstance().keySet());
				allClients.remove(cd.getName());

				for (String c : allClients) {
					if (!ClientsHash.getInstance().get(c).isConnected())
						allClients.remove(c);
				}

				ClientsAddedMessage cam = new ClientsAddedMessage("Server",
						cd.getName(), getCId(),
						allClients);
				out.send(cam);

				if (cd.isPort1open() || cd.isPort2open()) {
					cd.setTcp80(null);
					in.close();
					out.close();				
				}

				cd.setConnected(true);
			}

			// Clients list for others
			HashSet<String> set = new HashSet<String>();
			set.add(client);

			for (String c : ClientsHash.getInstance().keySet()) {

				if (c.equals(client))
					continue;

				ClientData cData = ClientsHash.getInstance().get(c);

				synchronized (cData) {

					if (!cData.isConnected())
						continue;

					ConnectionId newId = new ConnectionId("Server", cData.getName());
					ClientsAddedMessage cam = new ClientsAddedMessage("Server", cData.getName(),
							newId, set);
					try {
						OutgoingInterface oInt = cData.createOutInterface(newId, true);
						oInt.send(cam);

						if (oInt.getSocket() != cData.getTcp80())
							oInt.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			unregisterForAllClients();
		}
	}


	public ConnectionId getCId() {
		return cId;
	}

}
