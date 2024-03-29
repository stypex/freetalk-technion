/**
 * 
 */
package client.func;

import interfaces.TCPIncomingInterface;
import interfaces.TCPOutgoingInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import messages.ClientsAddedMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.Message;
import messages.ProbeMessage;
import messages.RegAckMessage;
import messages.RegisterMessage;
import util.Consts;
import util.Consts.ConnectionMethod;
import util.Consts.Protocol;
import client.ClientMain;
import client.Globals;
import client.listeners.TCPListener80;

/**
 * @author lenka
 *
 */
public class Loginner {

	JFrame login;
	
	static private ProbeMonitor pm;
	
	public Loginner(JFrame login) {
		super();
		this.login = login;
	}

	public boolean doLogin(String username) {

		try {
			Globals.setClientName(username);
			SimpleFunctions.loggedIn = false;
			ClientMain.startListeners();
			
			TCPOutgoingInterface out = new TCPOutgoingInterface(Globals.getServerIP(), Consts.SERVER_PORT);
			TCPIncomingInterface in = new TCPIncomingInterface(out.getSocket());

			ConnectionId cId = new ConnectionId(username, "Server");
			RegisterMessage rm = new RegisterMessage(username, "Server", cId, InetAddress.getLocalHost(),
					 Globals.getUDPPort(), Globals.getTCPPort(), Protocol.UDP, Protocol.TCP);

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
				SimpleFunctions.replyProbe(in, pm);
				
				reply = in.receive(0);
			}
			
			
			// RegAck handle
			if (!(reply instanceof RegAckMessage)) {
				JOptionPane.showMessageDialog(login, "Error connecting to server", "Registration Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			RegAckMessage ram = (RegAckMessage) reply;
//
//			if (ram.getPort1open().equals(ResponseCode.BAD))
//				ClientMain.udp.doStop();
//
//			if (ram.getPort2open().equals(ResponseCode.BAD))
//				ClientMain.tcp.doStop();	

			// Clients list
			reply = in.receive(0);
			
			if (!(reply instanceof ClientsAddedMessage)) {

				JOptionPane.showMessageDialog(login, "Error connecting to server", "Registration Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			
			ClientsAddedMessage cam = (ClientsAddedMessage) reply;
			
			SimpleFunctions.addClients(cam.getClients());
			
			// TCP80 Thread handle
			if (ram.getConnectionMethod().
					equals(ConnectionMethod.Indirect)) {
				if (ClientMain.tcp80 != null)
					ClientMain.tcp80.doStop();
				
				ClientMain.tcp80 = new TCPListener80(out.getSocket());
				
				ClientMain.tcp80.start();
			}
			else {
				out.close();
				in.close();
			}
			

			// Start the probe monitor
			if (pm == null) {
				pm = new ProbeMonitor();
				pm.start();
			}
			
			ClientMain.setServerOk();
			
			return true;
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			ClientMain.setServerOut();
		}
		JOptionPane.showMessageDialog(login, "Error connecting to server", "Registration Error", JOptionPane.ERROR_MESSAGE);
		return false;
	}
}
