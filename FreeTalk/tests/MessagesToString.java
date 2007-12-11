package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

import messages.CallMeMessage;
import messages.ClientCheckMessage;
import messages.ClientExitMessage;
import messages.ClientsAddedMessage;
import messages.ConAckMessage;
import messages.ConnectMessage;
import messages.ConnectionId;
import messages.ErrorMessage;
import messages.InitCallMessage;
import messages.ProbeAckMessage;
import messages.ProbeMessage;
import messages.RegAckMessage;
import messages.RegisterMessage;
import messages.TerminationMessage;
import util.Consts;

public class MessagesToString {

	/**
	 * Prints all possible messages
	 * @param args
	 * @author Arthur Kiyanovsky
	 * Dec 12, 2007
	 */
	public static void main(String[] args) {
		System.out.println(new CallMeMessage("1","2",new ConnectionId("1","2")));
		
		System.out.println(new ClientCheckMessage("1","2",new ConnectionId("1","2"),"3"));
		
		System.out.println(new ClientExitMessage("1","2",new ConnectionId("1","2"),"3"));
		
		HashSet<String> h = new HashSet<String>();
		h.add("client1");
		h.add("client2");
		h.add("client3");
		h.add("client4");
		h.add("client5");
		
		System.out.println(new ClientsAddedMessage("1","2",new ConnectionId("1","2"),h));
		try {
			System.out.println(new ConAckMessage("1","2",new ConnectionId("1","2"), InetAddress.getByName("1.2.3.4"),new ConAckMessage.ConnMethod(Consts.ConnectionMethod.TCPDirect,456)));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new ConnectMessage("1","2",new ConnectionId("1","2"),"3"));
		
		System.out.println(new ErrorMessage("1","2",new ConnectionId("1","2"),ErrorMessage.ErrorType.CLIENT_NAME_EXISTS));

		try {
			System.out.println(new InitCallMessage("1","2",new ConnectionId("1","2"),"3",InetAddress.getByName("1.2.3.4"),189));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(new ProbeAckMessage("1","2",new ConnectionId("1","2"),Consts.ResponseCode.BAD));
		
		System.out.println(new ProbeMessage("1","2",new ConnectionId("1","2")));
		
		System.out.println(new RegAckMessage("1","2",new ConnectionId("1","2"),Consts.ResponseCode.BAD,Consts.ResponseCode.OK,Consts.ConnectionMethod.TCPDirect));

		try {
			System.out.println(new RegisterMessage("1","2",new ConnectionId("1","2"),InetAddress.getByName("1.2.3.4"),5,6,Consts.Protocol.TCP,Consts.Protocol.UDP));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new TerminationMessage("1","2",new ConnectionId("1","2")));

	}

}
