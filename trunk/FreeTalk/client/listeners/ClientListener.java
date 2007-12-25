/**
 * 
 */
package client.listeners;

import interfaces.IncomingInterface;
import interfaces.OutgoingInterface;
import messages.ClientExitMessage;
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

	protected boolean receiveMessage(Message m, IncomingInterface in,
			OutgoingInterface out) {
		
		if (m instanceof ProbeMessage) {
			SimpleFunctions.replyProbe(out, (ProbeMessage) m);
			return true;
		}
		if (m instanceof ClientsAddedMessage) {
			ClientsAddedMessage cam = (ClientsAddedMessage) m;
			SimpleFunctions.addClients(cam.getClients());
			return true;
		}
		if (m instanceof JoinTalkMessage) {
			
			JoinTalkMessage jtm = (JoinTalkMessage)m;
			
			TalkThread tt = ConferenceCallsHash.getInstance().get(jtm.getCcid());
			
			if (tt == null) {
				tt = new TalkThread(m.getFrom(), jtm.getCcid());
			}
			tt.handleMessage(m, in);
			
			if (tt.getState().equals(State.NEW))
				tt.start();	
			return true;
		}
		if (m instanceof InitCallMessage) {		
			InitCallMessage icm = (InitCallMessage)m;
			
			TalkThread tt = 
				ConferenceCallsHash.getInstance().get(icm.getCCid());
			
			if (tt == null) {
				tt = new TalkThread(icm.getDest(), icm.getCCid());
			}
			tt.handleMessage(m, in);
			
			if (tt.getState().equals(State.NEW))
				tt.start();
			return true;
		}
		if (m instanceof ClientExitMessage) {
			ClientExitMessage cem = (ClientExitMessage) m;
			ClientMain.getMainWindow().removeClient(cem.getClient());
			return true;
		}
		
		return false;
	}
}
