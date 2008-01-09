package client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import client.func.Loginner;

public class Relogin extends JFrame {
	
	private static final long serialVersionUID = -7081090189215762449L;
	private Main main; 
	private JLabel l;
	
	
	public Relogin(Main m){
		main = m;
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("Images\\disconnect-256x256.png"));
		
		setTitle("Disconnected from server");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		l = new JLabel("Trying to reconnect. Please wait...");
		
		//Set the layout of the window
		GroupLayout loginLayout = new GroupLayout(getContentPane());
		getContentPane().setLayout(loginLayout);
		loginLayout.setHorizontalGroup(
				loginLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(loginLayout.createSequentialGroup()
						.addGap(20)
						.addComponent(l, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addGap(20))
						    
		);
		loginLayout.setVerticalGroup(
				loginLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(loginLayout.createSequentialGroup()
						.addGap(30)
						.addComponent(l, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addGap(30))     
		);

		pack();
		
		//set the window in the middle of the screen regardless of screen resolution
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( ( d.width - getSize().width ) / 2, ( d.height - getSize().height ) / 2);
	}
	
	/**
	 * Tries to login again to the server using the previous
	 * client name.
	 */
	public void login() {
		Loginner loginner = new Loginner(this);
		loginner.doLogin(main.getUserName());
		dispose(); 
	}

	
}
