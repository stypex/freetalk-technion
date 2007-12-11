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
			w = new BufferedWriter(new FileWriter(f, true));
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
		try {
			w.write(m.toString());
			w.newLine();
			w.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	/* (non-Javadoc)
	 * @see util.Log#addText(java.lang.String, boolean)
	 */
	@Override
	public void addText(String s, boolean bold) {
		
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
	 * @see util.Log#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub

	}

}
