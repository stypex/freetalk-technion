package client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/** 
 * @author Arthur
 * The main GUI window.
 */
public class Main extends JFrame {

	/**
	 * @author Arthur
	 * Represents a Runnable that runs a Chat window
	 */
	private class ChatRunner implements Runnable{
		public Chat c;
		
		public ChatRunner(){
			c = new Chat(userName, (String)lst.getSelectedValue(), lstModel.toArray());
		}
		
		public void run() {
            c.setVisible(true);
          }
	}
	
	//Field added automatically to avoid warning
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ChatRunner> chatRunners;
	private String userName;
	private JMenuBar mb;
	private JMenu m;
	private JMenuItem exitmi;
	private JLabel lbl;
	private JScrollPane s;
	private DefaultListModel lstModel;
	private JList lst;
	private JButton b;
	
	/**
	 * Builds the main window GUI
	 * @param userName - User's nickname
	 */
	public Main(String userName, String[] users){
		//Initialize all window components
		chatRunners = new ArrayList<ChatRunner>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("P2P - " + userName);
		this.userName = userName;
		
		mb = new JMenuBar();
		m = new JMenu("File");
		exitmi = new JMenuItem("Exit");
		m.add(exitmi);
		mb.add(m);
		setJMenuBar(mb);
		
		lbl = new JLabel("Online");
		
		b = new JButton("Chat");
		b.setEnabled(false);
		
		//Attach exit() function to "Exit" button
		exitmi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exit();
            }
        });
		
		//Create the list of online clients 
		lstModel = new DefaultListModel();
		for ( String u : users )
			lstModel.addElement(u);
		lst = new JList(lstModel);
		
		//Enable the "Chat" button only when a client was selected
		lst.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent arg0) {
				b.setEnabled(true);
			}
		});
		
		s = new JScrollPane(lst);
		
		//Attach chat() function to "Chat" button
		b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                chat();
            }
        });
		
		//Set the layout of the window
		GroupLayout mainLayout = new GroupLayout(getContentPane());
	    getContentPane().setLayout(mainLayout);
	    mainLayout.setHorizontalGroup(
	    		mainLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	        .addGroup(mainLayout.createSequentialGroup()
	        	.addGap(10)
	        	.addComponent(lbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
	        	.addGap(10))	
	    	.addGroup(mainLayout.createSequentialGroup()
	        	.addGap(10)
	        	.addComponent(s, GroupLayout.DEFAULT_SIZE, 150, GroupLayout.DEFAULT_SIZE)
	        	.addGap(10))
	        .addGroup(mainLayout.createSequentialGroup()
	    		.addGap(10)
	    		.addComponent(b, 150, 150, 150)
	    		.addGap(10))   
	    );
	    mainLayout.setVerticalGroup(
	    		mainLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	        .addGroup(mainLayout.createSequentialGroup()
	        	.addGap(10)
		        .addComponent(lbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
	        	.addGap(10)
        		.addComponent(s, GroupLayout.DEFAULT_SIZE, 300, GroupLayout.DEFAULT_SIZE)
        		.addGap(10)
        		.addComponent(b, 50, 50, 50)
        		.addGap(10))       
	    );
		
		pack();
		
		setMinimumSize(new Dimension(178,180));
		
		//Set window position
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( ( d.width - getSize().width ), 0 );
	}

	private void exit() { 
		 dispose();
		 //TODO other things to be done to exit the program
	 }
	
	private void chat() { 
		ChatRunner cr = new ChatRunner();
		
		/*Attach the event of closing a chat window with removing
		the window from the list of ChatRunners*/
		cr.c.addWindowListener(new WindowListener(){
			
			public void windowClosed(WindowEvent arg0) {
				removeClosedChatRunner();
			}
			
			//unnecessary functions that have to be implemented
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
		
		chatRunners.add(cr);
		SwingUtilities.invokeLater(cr); 
	 }
	
	/**
	 * Removes the client that exited from the list of online clients 
	 * as well as from all of the chat windows in which he was
	 * a member of the chat.
	 * @param client - The client that exited
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	public void clientExit(String client){
		for ( ChatRunner cr1 : chatRunners){
			cr1.c.clientExit(client);
		}
		lstModel.removeElement(client);
	}

	/**
	 * Removes the ChatRunner of a chat of which the window was closed
	 * from the ChatRunners list.
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	private void removeClosedChatRunner() {
		ChatRunner tempCr = null;;
		
		for (ChatRunner cr1 : chatRunners)
			if ( cr1.c.isDisplayable() == false ) {
				tempCr = cr1;
				break;
			}
		chatRunners.remove(tempCr);
	}
}
