/**
 * 
 */
package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import messages.Message;

/**
 * The text log for the server
 * @author lenka
 *
 */
public class Log extends util.Log {

	private static final String LOGFILE = "server.log";
	
	BufferedWriter w;
	
	
	public Log() {
		super();
		
		File f = new File(LOGFILE);
		
		try {
			f.createNewFile();
			w = new BufferedWriter(new FileWriter(f, false));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds the string s to the Log.
	 * @param s
	 * @param bold
	 * @author Arthur Kiyanovsky
	 * Dec 14, 2007
	 */
	private void addText(String s, boolean bold){
		try {
			w.write(s);
			w.newLine();
			w.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see util.Log#addMessage(messages.Message)
	 */
	@Override
	public void addMessage(Message m) {
		addText(m.toString(),false);
	}

	/* (non-Javadoc)
	 * @see util.Log#addText(java.lang.String, boolean)
	 */
	@Override
	public void addDatedText(String s, boolean bold) {
		addText(s + " <" + util.Func.getDateTime() + ">" ,bold);
	}

	/* (non-Javadoc)
	 * @see util.Log#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub

	}

}
