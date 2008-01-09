package tests;

import javax.swing.SwingUtilities;

import client.gui.Main;
import client.gui.Relogin;

public class GuiWindowShower {
	
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Relogin(new Main(new String[1])).setVisible(true);
			}
		});  
	}
}
