/**
 * 
 */
package client.listeners;

import interfaces.IncomingInterface;
import messages.ClientExitMessage;
import messages.ClientsAddedMessage;
import messages.IndependantMessage;
import messages.InitCallMessage;
import messages.JoinTalkMessage;
import messages.Message;
import messages.ProbeMessage;
import client.ClientMain;
import client.Globals;
import client.data.ClientsList;
import client.data.ConferenceCallsHash;
import client.func.SimpleFunctions;
import client.func.TalkThread;

/**
 * @author lenka
 *
 */
public abstract class ClientListener extends StoppableThread {

	protected boolean receiveMessage(Message m, IncomingInterface in) {

		if (!(m instanceof IndependantMessage))
			return false;
		
		if (!m.getTo().equals(Globals.getClientName()))
			return false;
		
		if (m instanceof ProbeMessage) {
			SimpleFunctions.replyProbe(in, (ProbeMessage) m);
			return true;
		}
		if (m instanceof ClientsAddedMessage) {
			ClientsAddedMessage cam = (ClientsAddedMessage) m;
			SimpleFunctions.addClients(cam.getClients());
			return true;
		}
		if (m instanceof JoinTalkMessage) {
			JoinTalkMessage jtm = (JoinTalkMessage)m;
			TalkThread tt;

			// We synchronize it to prevent this: two threads go to hash
			// both get null and both create TalkThreads
			synchronized (ConferenceCallsHash.getInstance()) {
				tt = ConferenceCallsHash.getInstance().get(jtm.getCcid());

				if (tt == null) {
					tt = new TalkThread(m.getFrom(), jtm.getCcid(), false);
				}
			}

			// To prevent trouble
			synchronized (tt) {
				tt.handleMessage(m, in);
				if (tt.getState().equals(State.NEW))
					tt.start();	
			}
			return true;
		}
		if (m instanceof InitCallMessage) {		
			InitCallMessage icm = (InitCallMessage)m;
			TalkThread tt;

			// We synchronize it to prevent this: two threads go to hash
			// both get null and both create TalkThreads
			synchronized (ConferenceCallsHash.getInstance()) {
				tt = ConferenceCallsHash.getInstance().get(icm.getCCid());

				if (tt == null) {
					tt = new TalkThread(icm.getDest(), icm.getCCid(), false);
				}
			}
			
			// To prevent trouble
			synchronized (tt) {
				tt.handleMessage(m, in);
				if (tt.getState().equals(State.NEW))
					tt.start();
			}
			return true;
		}
		if (m instanceof ClientExitMessage) {
			ClientExitMessage cem = (ClientExitMessage) m;
			ClientMain.getMainWindow().removeClient(cem.getClient());
			ClientsList.getInstance().remove(cem.getClient());
			return true;
		}

		return false;
	}
}
