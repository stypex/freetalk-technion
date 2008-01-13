package tests;

import javax.swing.SwingUtilities;

import client.gui.Relogin;

public class GuiWindowShower {
	
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Relogin().setVisible(true);
			}
		});  
	}
}
