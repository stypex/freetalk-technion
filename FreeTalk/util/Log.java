/**
 * 
 */
package util;

import messages.Message;

/**
 * Will be an abstract for the log - client or server.
 * @author lenka
 *
 */
public abstract class Log {

	static Log log;
	
	public static void init(Log log) {
		Log.log = log;
	}
	
	public static Log getInstance() {
		return log;
	}
	
	public abstract void addText(String s, boolean bold);
	
	public abstract void setVisible(boolean b);
	
	public abstract void addMessage(Message m);
}
