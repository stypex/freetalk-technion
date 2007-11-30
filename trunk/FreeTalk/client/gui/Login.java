package client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Arthur
 * Represents the Login window.
 */
public class Login extends JFrame{
	// Field added automatically to avoid warning
	private static final long serialVersionUID = 1L;
	
	private JTextField t;
	private JLabel l;
	private JButton b;
	
	/**
	 * Builds the Login GUI menu
	 */
	public Login(){
		//Initialize all window components
		setTitle("Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t = new JTextField();
	    l = new JLabel("Enter your nickname");
	    b = new JButton("Login");
	    b.setEnabled(false);
	    
	    //Attach login() function to "Login" button
	    b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                login();
            }
        });
	    
	    
	    //Makes the "Login" button unpressable if the username begins with whitespaces
	    t.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent arg0) {
				checkUserName();
			}

			public void insertUpdate(DocumentEvent arg0) {
				checkUserName();
			}

			public void removeUpdate(DocumentEvent arg0) {
				checkUserName();
			}
        });
	    
	    
	    //Set the layout of the window
	    GroupLayout loginLayout = new GroupLayout(getContentPane());
	    getContentPane().setLayout(loginLayout);
	    loginLayout.setHorizontalGroup(
	    	loginLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	        .addGroup(loginLayout.createSequentialGroup()
	        	.addGap(10)
	        	.addComponent(l, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
	        	.addGap(10))
	    	.addGroup(loginLayout.createSequentialGroup()
	    		.addGap(10)
	    		.addComponent(t, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
	    		.addGap(10))
	        .addGroup(loginLayout.createSequentialGroup()
	    		.addGap(10)
	    		.addComponent(b, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
	    		.addGap(10))    
	    );
	    loginLayout.setVerticalGroup(
	    	loginLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	        .addGroup(loginLayout.createSequentialGroup()
        		.addGap(10)
        		.addComponent(l, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
        		.addGap(10)
        		.addComponent(t, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
        		.addGap(10)
        		.addComponent(b, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
        		.addGap(10))       
	    );
	    
	    pack();
	    
	    //set the window in the middle of the screen regardless of screen resolution
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( ( d.width - getSize().width ) / 2, ( d.height - getSize().height ) / 2);
	}
	
	/**
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 * Allows Names user NickNames that don't start with whitespaces
	 * but may have whitespaces in them
	 */ 
	private void checkUserName() {
		if (t.getText().matches("((\\S)+(\\s)*)+"))
			b.setEnabled(true);
		else
			b.setEnabled(false);
	}
	
	/**
	 * Reads the username and performs the login
	 * @author Arthur Kiyanovsky
	 * Nov 30, 2007
	 */
	private void login() {
		 //TODO here comes the login to the server using the username
		 SwingUtilities.invokeLater(new Runnable() {
	          public void run() {
	        	//STUB here comes the "gettig userlist" part
	     		String[] users = new String[4];
	     		users[0] = "user0";
	     		users[1] = "user1";
	     		users[2] = "user2";
	     		users[3] = "user3";
	     		//STUB END
	        	  new Main(t.getText(),users).setVisible(true);
	          }
	      }); 
		 dispose(); 
	 }
}
