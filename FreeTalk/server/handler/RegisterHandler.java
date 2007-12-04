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
		super(m.getFrom());
		setIn(in);
		setOut(new TCPOutgoingInterface(in.getSocket()));
		
		this.m = m;
		this.cId = m.getCId();
	}

	@Override
	public void run() {
		super.run();
		
		try {
			ClientData cd = ClientsHash.getInstance().get(m.getFrom());
			
			if (cd != null && cd.getIp() != m.getClientIp()) { // Name in use
				
				ErrorMessage em = 
					new ErrorMessage("Server", m.getFrom(), getCId(), ErrorType.CLIENT_NAME_EXISTS);
				
				out.send(em);
				return;
			}
			
			if (cd == null) {	// New client
				
				cd = new ClientData(m.getFrom(), m.getClientIp(), m.getPort1(), m.getPort2(),
						ResponseCode.BAD, ResponseCode.BAD, in.getSocket());
				
				ClientsHash.getInstance().put(m.getFrom(), cd);
			}
			
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
			
			ClientsAddedMessage cam = new ClientsAddedMessage("Server",
					cd.getName(), getCId(),
					allClients);
			out.send(cam);
			
			if (cd.isPort1open() || cd.isPort2open()) {
				cd.setTcp80(null);
				in.close();
				out.close();				
			}
						
			// Clients list for others
			HashSet<String> set = new HashSet<String>();
			set.add(cd.getName());
			
			for (String c : ClientsHash.getInstance().keySet()) {
				
				if (c.equals(cd.getName()))
					continue;
				
				ClientData cData = ClientsHash.getInstance().get(c);
				
				ConnectionId newId = new ConnectionId("Server", cData.getName());
				cam = new ClientsAddedMessage("Server", cData.getName(),
						newId, set);
				try {
					OutgoingInterface oInt = cData.createOutInterface(newId);
					oInt.send(cam);
					oInt.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ClientsHash.getInstance().unRegisterThread(client, this);
		}
	}


	public ConnectionId getCId() {
		return cId;
	}

}
