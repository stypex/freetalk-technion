/**
 * 
 */
package client.func;

import interfaces.TCPIncomingInterface;
import interfaces.TCPOutgoingInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import messages.ClientsAddedMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.Message;
import messages.ProbeMessage;
import messages.RegAckMessage;
import messages.RegisterMessage;
import util.Consts.ConnectionMethod;
import util.Consts.Protocol;
import util.Consts.ResponseCode;
import client.ClientMain;
import client.Globals;
import client.data.ClientsList;
import client.listeners.TCPListener80;

/**
 * @author lenka
 *
 */
public class Loginner {

	JFrame login;
	
	public Loginner(JFrame login) {
		super();
		this.login = login;
	}

	public boolean doLogin(String username) {

		try {
			TCPOutgoingInterface out = new TCPOutgoingInterface(Globals.getServerIP(), 80);
			TCPIncomingInterface in = new TCPIncomingInterface(out.getSocket());

			ConnectionId cId = new ConnectionId(username, "Server");
			RegisterMessage rm = new RegisterMessage(username, "Server", InetAddress.getLocalHost(),
					cId, Globals.getUDPPort(), Globals.getTCPPort(), Protocol.UDP, Protocol.TCP);

			boolean isConnected = false;

			// Initial connection
			while (!isConnected) {
				try {
					out.send(rm);
					isConnected = true;
				} catch (IOException e) {
					JOptionPane.showMessageDialog(login, "No connection to the server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}

			Message reply = in.receive(0);

			// Error message
			if (reply instanceof ErrorMessage) {
				ErrorMessage err = (ErrorMessage) reply;

				JOptionPane.showMessageDialog(login, err.getEType().toString(), "Registration Error", JOptionPane.ERROR_MESSAGE);

				return false;
			} else if (reply instanceof ProbeMessage) {
				ProbeMessage pm = (ProbeMessage)reply;
				SimpleFunctions.replyProbe(out, pm);
			}

			reply = in.receive(0);
			
			// RegAck handle
			if (!(reply instanceof RegAckMessage)) {
				JOptionPane.showMessageDialog(login, "Error connecting to server", "Registration Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			RegAckMessage ram = (RegAckMessage) reply;

			if (ram.getPort1open() == ResponseCode.BAD)
				ClientMain.udp.doStop();

			if (ram.getPort2open() == ResponseCode.BAD)
				ClientMain.tcp.doStop();	

			// Clients list
			reply = in.receive(0);
			
			if (!(reply instanceof ClientsAddedMessage)) {

				JOptionPane.showMessageDialog(login, "Error connecting to server", "Registration Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			
			ClientsAddedMessage cam = (ClientsAddedMessage) reply;
			
			for (String client : cam.getClients())
				ClientsList.getInstance().put(client, new LinkedList<TalkThread>());

			// TCP80 Thread handle
			if (ram.getConnectionMethod() == ConnectionMethod.Indirect) {
				ClientMain.tcp80 = new TCPListener80(out.getSocket());
			}
			else {
				out.close();
				in.close();
			}

			Globals.setClientName(username);
			
			return true;
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(login, "Error connecting to server", "Registration Error", JOptionPane.ERROR_MESSAGE);
		return false;
	}
}
