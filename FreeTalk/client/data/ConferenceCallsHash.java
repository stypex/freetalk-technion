/**
 * 
 */
package client.data;

import java.util.concurrent.ConcurrentHashMap;

import messages.ConnectionId;
import client.talk.TalkThread;

/**
 * @author lenka
 *
 */
public class ConferenceCallsHash extends ConcurrentHashMap<ConnectionId, TalkThread> {

	private static final long serialVersionUID = 3222918723101144580L;

	private static ConferenceCallsHash singleton;
	
	/**
	 * A singleton initializer
	 * @return
	 */
	public static ConferenceCallsHash getInstance() {
		if (singleton == null) {
			singleton = new ConferenceCallsHash();
		}
			
		return singleton;
	}
	
	private ConferenceCallsHash() {
		super();
	}
}
