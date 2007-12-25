package client.func;

import interfaces.TCPOutgoingInterface;

import java.io.IOException;

import messages.ClientExitMessage;
import messages.ConnectionId;
import messages.Message;
import util.Consts;
import client.Globals;

public class Exiter {

	public static void doExit(){
		try {
			TCPOutgoingInterface out = new TCPOutgoingInterface(Globals.getServerIP(), Consts.SERVER_PORT);
			ConnectionId cid = new ConnectionId(Globals.getClientName(), "Server");
			Message m = new ClientExitMessage(Globals.getClientName(), "Server", cid, Globals.getClientName());
			out.send(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
