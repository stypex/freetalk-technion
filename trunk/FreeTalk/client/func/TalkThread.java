/**
 * 
 */
package client.func;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import client.gui.Chat;

/**
 * @author lenka
 *
 */
public class TalkThread extends Thread {
	
	public Chat c;
	
	String dest;
	
	
	/**
	 * @param userName - Name of the client on this computer.
	 * @param dest - Name of the client in the destination computer with
	 * whom the chat is initiated.
	 * @param allUsers - a list of all the users that are online except
	 * userName but including dest. 
	 */
	public TalkThread(String dest, Object[] allUsers){
		c = new Chat(dest, allUsers, this);
		
		/*Attach the event of closing a chat window with the actions
		 * taken when it happens*/
		c.addWindowListener(new WindowListener(){
			
			public void windowClosed(WindowEvent arg0) {
				//TODO should send TERMINATE to all clients in the chat
			}
			
			//unnecessary functions that have to be implemented
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
	}
	
	public void run() {
		doConnect(dest);
        c.setVisible(true);
      }

	private void doConnect(String dest2) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Sends a message to all the participants of the chat.
	 * @param msg - Message to send.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	public void send(String msg){	
		//TODO I'm empty :(
		System.out.println(msg);
	}

	public void addClientToGUI(String client) {
		c.addClient(client);
	}

	public void removeClientFromGUI(String client) {
		c.removeClient(client);
	}
	
}
