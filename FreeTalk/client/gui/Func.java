package client.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

/**
 * A class of GUI general purpose functions. 
 * @author Arthur
 */
public class Func {
	
	/**
	 * Adds a client to a DefaultListModel saving the alphabetical order.
	 * @param client - Inserted client
	 * @param lstModel - DefaultListModel to which the client is inserted.
	 * @author Arthur Kiyanovsky
	 * Dec 10, 2007
	 */
	public static void addAlphabeticallyToLM(String client, DefaultListModel lstModel) {
		if (lstModel.contains(client))
			return;
		
		int i = 0;
		while ( i < lstModel.getSize() && 
				client.compareTo((String)lstModel.getElementAt(i)) >= 0 )
			++i;
		lstModel.insertElementAt(client, i);
	}
	
	/**
	 * Adds a client to a DefaultComboBoxModel saving the alphabetical order.
	 * @param client - Inserted client
	 * @param cbModel - DefaultComboBoxModel to which the client is inserted.
	 * @author Arthur Kiyanovsky
	 * Dec 10, 2007
	 */
	public static void addAlphabeticallyToCBM(String client, DefaultComboBoxModel cbModel) {
		if (cbModel.getIndexOf(client) >= 0)
			return;
		
		int i = 1; //not including the first "add client to chat" dummy row.
		while ( i < cbModel.getSize() && 
				client.compareTo((String)cbModel.getElementAt(i)) >= 0 )
			++i;
		cbModel.insertElementAt(client, i);
	}
}
