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

import client.func.TalkThread;

/** 
 * @author Arthur
 * The main GUI window.
 */
public class Main extends JFrame {
	
	//Field added automatically to avoid warning
	private static final long serialVersionUID = 1L;
	
	//private static Main singleton; 
	
	public static Log log;
	
	private ArrayList<TalkThread> talkThreads;
	private String userName;
	private JMenuBar mb;
	private JMenu m;
	private JMenuItem exitmi;
	private JMenuItem logmi;
	private JLabel lbl;
	private JScrollPane s;
	private DefaultListModel lstModel;
	private JList lst;
	private JButton b;
	
	/**
	 * Builds the main window GUI
	 */
	public Main(String[] users){
		
		log = new Log();
		Log.init(log);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("Images\\group-of-users-48x48.png"));
		
		//log.addText("hello", true);
		
		//Initialize all window components
		talkThreads = new ArrayList<TalkThread>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.userName = null; // will be set after login
		
		mb = new JMenuBar();
		m = new JMenu("File");
		logmi = new JMenuItem("Log");
		exitmi = new JMenuItem("Exit");
		m.add(logmi);
		m.add(exitmi);
		mb.add(m);
		setJMenuBar(mb);
		
		lbl = new JLabel("Online");
		
		b = new JButton("Chat");
		//b.setEnabled(false);
		
		
		//Attach exit() function to "Exit" button
		logmi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				log.setVisible(true);
        			}
        		});  
            }
        });
		
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
                chat((String)lst.getSelectedValue());
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

	/**
	 * Sets the userName of the user on this computer.
	 * Shoud be called before showing the window.
	 * @param userName - user name of teh user on this computer.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	public void setUserName(String userName){
		this.userName = userName;
		setTitle("P2P - " + this.userName);
	}
	
	/**
	 * exits the main GUI window and initiates the exit procedure of the 
	 * application.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	private void exit() { 
		 dispose();
		 //TODO other things to be done to exit the program
	 }
	
	/**
	 * Initializes and shows a chat window to the given client.
	 * @param dest - Client to which the chat window is opened.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	public void chat(String dest) { 
		TalkThread tt = new TalkThread(dest, null);
		
//		SwingUtilities.invokeLater(tt);
		tt.start();
	 }

	/**
	 * Needs to be called when a new TalkTeread is created.
	 * Called from the TalkThread constructor
	 * @param tt
	 */
	public void registerTalkThread(TalkThread tt) {
		/*Attach the event of closing a chat window with removing
		the window from the list of TalkThreads*/
		tt.c.addWindowListener(new WindowListener(){
			
			public void windowClosed(WindowEvent arg0) {
				removeClosedTalkThread();
			}
			
			//unnecessary functions that have to be implemented
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
		
		talkThreads.add(tt);
	}
	
	/**
	 * Removes the client that exited from the list of online clients 
	 * @param client - The client that exited
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	public void removeClient(String client){
		for ( TalkThread tt : talkThreads){
			tt.removeClientFromGUI(client);
		}
		lstModel.removeElement(client);
		
		
	}

	/**
	 * Adds a new client to the list of online clients.
	 * @param client - the client to add.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	public void addClient(String client){
		Func.addAlphabeticallyToLM(client, lstModel);
		
		for (TalkThread tt : talkThreads)
			tt.addClientToGUI(client);
	}
	
	
	/**
	 * Removes the TalkThread of a chat of which the window was closed
	 * from the TalkThreads list.
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	private void removeClosedTalkThread() {
		TalkThread tempTt = null;
		
		for (TalkThread tt1 : talkThreads)
			if ( tt1.c.isDisplayable() == false ) {
				tempTt = tt1;
				break;
			}
		talkThreads.remove(tempTt);
	}
}
