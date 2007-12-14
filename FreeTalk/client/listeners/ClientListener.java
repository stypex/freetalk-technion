/**
 * 
 */
package client.listeners;

import interfaces.IncomingInterface;
import interfaces.OutgoingInterface;
import messages.ClientsAddedMessage;
import messages.InitCallMessage;
import messages.JoinTalkMessage;
import messages.Message;
import messages.ProbeMessage;
import client.ClientMain;
import client.data.ConferenceCallsHash;
import client.func.SimpleFunctions;
import client.func.TalkThread;

/**
 * @author lenka
 *
 */
public abstract class ClientListener extends StoppableThread {

	protected void receiveMessage(Message m, IncomingInterface in,
			OutgoingInterface out) {
		
		if (m instanceof ProbeMessage) {
			SimpleFunctions.replyProbe(out, (ProbeMessage) m);
			return;
		}
		if (m instanceof ClientsAddedMessage) {
			ClientsAddedMessage cam = (ClientsAddedMessage) m;
			SimpleFunctions.addClients(cam.getClients());
			return;
		}
		if (m instanceof JoinTalkMessage) {
			
			JoinTalkMessage jtm = (JoinTalkMessage)m;
			
			TalkThread tt = ConferenceCallsHash.getInstance().get(jtm.getCcid());
			
			if (tt == null) {
				tt = new TalkThread(m.getFrom(), m);
			}
			tt.handleMessage(m, in);
			
			if (tt.getState().equals(State.NEW))
				tt.start();	
			return;
		}
		if (m instanceof InitCallMessage) {					
			TalkThread tt = new TalkThread(m.getFrom(), m);
			tt.start();
			return;
		}
	}
}
