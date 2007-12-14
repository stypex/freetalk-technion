package client.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyleContext.NamedStyle;

import messages.Message;

public class Log extends util.Log {

	//Field added automatically to avoid warning
	private static final long serialVersionUID = 1L;
	
	private JFrame f;
	private JTextPane t;
	private JScrollPane s;
	private JMenuBar mb;
	private JMenu m;
	private JMenuItem exitmi;
	private JMenuItem savemi;
	private JFileChooser fc;
	
	public Log() {
		
		fc = new JFileChooser();
		
		f = new JFrame();
		f.setIconImage(Toolkit.getDefaultToolkit().getImage("Images\\text-file-48x48.png"));
		
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		f.setTitle("Log");
		
		mb = new JMenuBar();
		m = new JMenu("File");
		savemi = new JMenuItem("Save as");
		exitmi = new JMenuItem("Exit");
		m.add(savemi);
		m.add(exitmi);
		mb.add(m);
		f.setJMenuBar(mb);
		
		
		//Attach exit() function to "Exit" button
		exitmi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exit();
            }
        });
		
		//Attach exit() function to "Save" button of the file chooser
		fc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                save();
            }
        });
		
		//Attach exit() function to "Save as" button
		savemi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	fc.showSaveDialog(f);
            }
        });
		
		
		//Text areas initializations
		t = new JTextPane();
		s = new JScrollPane(t);
		t.setEditable(false);
		
		//Set the layout of the window
		GroupLayout chatLayout = new GroupLayout(f.getContentPane());
	    f.getContentPane().setLayout(chatLayout);
	    chatLayout.setHorizontalGroup(
	    		chatLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	        .addGroup(chatLayout.createSequentialGroup()
	        	.addGap(10)
	        	.addComponent(s, GroupLayout.DEFAULT_SIZE, 400, GroupLayout.DEFAULT_SIZE)
	        	.addGap(10))  
	    );
	    chatLayout.setVerticalGroup(
	    		chatLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	        .addGroup(chatLayout.createSequentialGroup()
	        	.addGap(10)
		        .addComponent(s, GroupLayout.DEFAULT_SIZE, 300, GroupLayout.DEFAULT_SIZE)
	        	.addGap(10))       
	    );
	    
	    f.pack();
	}
	
	
	/**
	 * Adds text to the log window.
	 * @param s - text to be added.
	 * @param bold - true if the text is to be shown bold false otherwise.
	 * @author Arthur Kiyanovsky
	 * Dec 10, 2007
	 */
	private void addText(String s, boolean bold){
		Document d = t.getDocument();
		StyleContext sc = new StyleContext();
		NamedStyle ns = sc.new NamedStyle();
		if (bold)
			StyleConstants.setBold(ns, true);
		
		try {
			d.insertString(d.getLength(), s, ns);
			d.insertString(d.getLength(), System.getProperty("line.separator"), ns);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds text to the log window with the date it was added in.
	 * @param s - text to be added.
	 * @param bold - true if the text is to be shown bold false otherwise.
	 * @author Arthur Kiyanovsky
	 * Dec 10, 2007
	 */
	public void addDatedText(String s, boolean bold){
		addText(s + " <" + util.Func.getDateTime() + ">",bold);
	}

	/**
	 * Adds a message to the log.
	 * @param origM - message to be added to the log.
	 * @author Arthur Kiyanovsky
	 * Dec 10, 2007
	 */
	public void addMessage(Message m){
		addText(m.toString(),false);
	}

	public void setVisible(boolean b) {
		f.setVisible(b);
	}
	
	private void exit() { 
		 f.dispose();
		 //TODO other things to be done to exit the program
	 }


	/**
	 * writes the Log in the log window to a selected in the GUI file.
	 * @author Arthur Kiyanovsky
	 * Dec 14, 2007
	 */
	private void save() {
		Document d = t.getDocument();
		try {
			FileWriter fw = new FileWriter(fc.getSelectedFile());
			fw.write(d.getText(0, d.getLength()));
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
