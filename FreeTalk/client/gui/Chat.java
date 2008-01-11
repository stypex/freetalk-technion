package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyleContext.NamedStyle;

import client.Globals;
import client.data.ClientsList;
import client.func.TalkThread;

public class Chat extends JFrame {
	//Field added automatically to avoid warning
	private static final long serialVersionUID = 1L;

	private TalkThread tt;

	private String userName;
	private JTextPane utp;
	private JTextPane ltp;
	private JSplitPane sp1;
	private JSplitPane sp2;
	private JScrollPane usp;
	private JScrollPane lsp;
	private JPanel p;
	private JList lst;
	private DefaultListModel lstModel; 
	private JButton send;
	private JComboBox addToChat;
	private DefaultComboBoxModel cbModel;
	private JScrollPane rsp;
	private JPanel statusP;
	private JLabel statusL;
	/**
	 * Builds the chat GUI window.
	 * @param userName - Nickname of the user on this computer.
	 * @param allUsers - List of all online users
	 * @param tt - TalkThread to which all the messages will be directed from the
	 * chat window.
	 */
	public Chat(String destUserName, TalkThread tt){

		setIconImage(Toolkit.getDefaultToolkit().getImage("Images\\chat-48x48.png"));

		this.tt = tt;

		this.userName = Globals.getClientName();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(destUserName);
		statusP = new JPanel();
		statusP.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusP.setLayout(new BorderLayout());
		statusL = new JLabel();
		statusP.add(statusL, BorderLayout.WEST);

		//Text areas initializations
		utp = new JTextPane();
		utp.setEditable(false);
		ltp = new JTextPane();
		usp = new JScrollPane(utp);
		lsp = new JScrollPane(ltp);

		//Create list of users in chat
		lstModel = new DefaultListModel();
		lstModel.addElement(destUserName);
		lst = new JList(lstModel);
		lst.setSelectionBackground(Color.white);
		lst.setFocusable(false); //to avoid selecting items in the list
		rsp = new JScrollPane(lst);

		//Create ComboBox of users that can be added to chat
		cbModel = new DefaultComboBoxModel();
		cbModel.addElement("Add user to chat");
		for (Object u : ClientsList.getInstance().keySet())
			if (!u.equals(destUserName))
				cbModel.addElement((String)u);
		addToChat = new JComboBox (cbModel);

		//Attach the ComboBox selection to adding the user to the chat
		addToChat.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String client = (String)addToChat.getSelectedItem();
				if (client != null && !client.equals("Add user to chat"))
					addClientToSession(client);
			}

		});


		send = new JButton("Send");
		send.setEnabled(false);

		// This part allows to scroll down scrollBar in case
		// Chat window or utp part of it was resized
//		usp.addComponentListener(new ComponentListener() {
//		public void componentResized(ComponentEvent ce){
//		if (utp.getDocument().getLength()>0)
//		utp.setCaretPosition(utp.getDocument().getLength()-1);
//		utp.setCaretPosition(utp.getDocument().getLength());
//		}
//		public void componentHidden(ComponentEvent ce){}
//		public void componentMoved(ComponentEvent ce){}
//		public void componentShown(ComponentEvent ce){}			
//		});
		usp.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce){
				if (utp.getDocument().getLength()>0)
					utp.setCaretPosition(utp.getDocument().getLength()-1);
				utp.setCaretPosition(utp.getDocument().getLength());
			}
		});		

		//This part doesn't allow sending text that is only whitespaces
		//by disabling the "Send" button.
		ltp.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent arg0) {
				checkTextEntered();
			}

			public void insertUpdate(DocumentEvent arg0) {
				checkTextEntered();
			}

			public void removeUpdate(DocumentEvent arg0) {
				checkTextEntered();
			}
		});


		//Attaches between pressing the "Send" button and the message being sent
		//as well as shown in the chat text area
		send.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (send())
					moveTextToChatWindow();
			}
		});

		p = new JPanel();
		//Set the layout of the right side of the window
		GroupLayout pLayout = new GroupLayout(p);
		p.setLayout(pLayout);
		pLayout.setHorizontalGroup(
				pLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(pLayout.createSequentialGroup()
						.addComponent(addToChat, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
						.addGroup(pLayout.createSequentialGroup()
								.addComponent(rsp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
								.addGroup(pLayout.createSequentialGroup()
										.addComponent(send, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))

		);
		pLayout.setVerticalGroup(
				pLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(pLayout.createSequentialGroup()
						.addComponent(addToChat, 25, 25, 25)
						.addComponent(rsp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addComponent(send, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))       
		);


		usp.setMinimumSize(new Dimension(100,25));
		lsp.setMinimumSize(new Dimension(100,25));

		//Group components for easier setup of the layout
		sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,usp,lsp);
		sp1.setResizeWeight(0.8);

		sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sp1,p);
		sp2.setResizeWeight(1);

		//Set the layout of the window
		GroupLayout chatLayout = new GroupLayout(getContentPane());
		getContentPane().setLayout(chatLayout);
		chatLayout.setHorizontalGroup(
				chatLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(chatLayout.createSequentialGroup()
						.addGap(10)
						.addComponent(sp2, GroupLayout.DEFAULT_SIZE, 450, GroupLayout.DEFAULT_SIZE)
						.addGap(10))
						.addGroup(chatLayout.createSequentialGroup()
								.addComponent(statusP, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
		);
		chatLayout.setVerticalGroup(
				chatLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(chatLayout.createSequentialGroup()
						.addGap(10)
						.addComponent(sp2, GroupLayout.DEFAULT_SIZE, 300, GroupLayout.DEFAULT_SIZE)
						.addGap(10)
						.addComponent(statusP, 20, 20, 20))
		);

		pack();

		//set the minimum dimensions of the different components in the window
		send.setMinimumSize(new Dimension(p.getWidth(), 20));
		p.setMinimumSize(new Dimension(p.getWidth(), addToChat.getHeight()*2 + send.getHeight()));
		setMinimumSize(new Dimension( 50 + p.getWidth() + usp.getMinimumSize().width, 80 + addToChat.getHeight()*2 + statusP.getHeight()));

		//Set window position
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( ( d.width - getSize().width ) / 3, ( d.height - getSize().height ) / 3);

		ltp.requestFocusInWindow();
	}

	/**
	 * Checks if the text entered is only white spaces.
	 * If so disables the send button.
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	private void checkTextEntered() {
		if (!ltp.getText().matches("\\s*"))
			send.setEnabled(true);
		else
			send.setEnabled(false);
	}

	/**
	 * delegates to TalkThread.send()
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	private boolean send(){
		return tt.send(ltp.getText());
	}

	/**
	 * Copies the text from where the user entered it to the chat 
	 * text area in the correct format. 
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	private void moveTextToChatWindow() { 		
		putTextInChatWindow(ltp.getText(), userName);

		ltp.setText(null);
		ltp.requestFocusInWindow();
	}

	/**
	 * Adds text to the chat window
	 * @param text
	 */
	public void putTextInChatWindow(String text, String from) {
		Document d = utp.getDocument();
		StyleContext sc = new StyleContext();
		NamedStyle s = sc.new NamedStyle();
		StyleConstants.setBold(s, true);

		try {
			synchronized (d) {
				d.insertString(d.getLength(), from + " <" + util.Func.getDateTime() + ">\n", s);
				StyleConstants.setBold(s, false);
				d.insertString(d.getLength(), text + "\n\n", s);
				// lowers scrollBar to its lowest position upon new message in utp
				utp.setCaretPosition(utp.getDocument().getLength());	// Ilya - my 1st line in this proj :)
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Removes the selected client from the "Add client to conference 
	 * call" ComboBox and adds it to the conference list of clients. 
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	public void moveFromComboToList(String client){
		synchronized (cbModel){
			synchronized (lstModel) {
				cbModel.removeElement(client);

				Func.addAlphabeticallyToLM(client,lstModel);

				addToChat.setSelectedIndex(0);

				setTitle();
			}
		}
	}



	/**
	 * Removes a given client from the conference list of clients
	 * and adds it back to the "Add client to conference call" ComboBox. 
	 * @param client - Name of client that needs to be removed 
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	private void moveFromListToCombo(String client){
		synchronized (cbModel){
			synchronized (lstModel) {
				lstModel.removeElement(client);

				Func.addAlphabeticallyToCBM(client,cbModel);
			}
		}
		setTitle();
	}

	/**
	 * Adds a client to the "clients that can be added" ComboBox.
	 * @param client - Client to be added.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	public void addClient(String client){
		synchronized (cbModel){
			Func.addAlphabeticallyToCBM(client,cbModel);
		}
	}

	/**
	 * Adds a client to the conference call list of clients
	 * and removes it from the "clients that can be added" ComboBox.
	 * @param client - Client to be added.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	private void addClientToSession(String client){
		moveFromComboToList(client);
		tt.addclientToSession(client);
	}

	/**
	 * Removes a client from both "chat list" and "clients 
	 * that can be added" ComboBox. Use this method when client
	 * disconnected from the system (sent CLIENT_EXIT).
	 * @param client - Client to be removed.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	public void removeClient(String client){
		synchronized (cbModel){
			synchronized (lstModel) {
				cbModel.removeElement(client);
				lstModel.removeElement(client);
			}
		}
		setTitle();
		/* in case the last client in the conference disconnected no need to stay in
		 * the receiveMessages() while loop that eats up CPU
		 */ 
		/*if ( lstModel.size() == 0 )
			tt.doStop();*/
	}

	/**
	 * Removes a client from "chat list" and puts it back to
	 * "clients that can be added" ComboBox  when the client
	 * sent a TERMINATE message.
	 * @param client - Client to be removed.
	 * @author Arthur Kiyanovsky
	 * Dec 8, 2007
	 */
	public void removeClientFromSession(String client){
		moveFromListToCombo(client);

		setTitle();
	}

	public void setStatusBarText(String s){
		statusL.setText(" " + s);
	}

	private void setTitle() {

		synchronized (lstModel) {
			if (lstModel.getSize() >= 2)
				setTitle("Conference");
			else if (lstModel.getSize() == 1)
				setTitle(lstModel.get(0).toString());
			else 
				setTitle("Nobody");
		}
	}
	
	public void setServerOut() {
		send.setEnabled(false);
		ltp.setEditable(false);
	}

	public void setServerOk() {	
		ltp.setEditable(true);
		checkTextEntered();
	}
}
