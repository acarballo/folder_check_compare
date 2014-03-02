package jfcc;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Console extends JFrame implements WindowListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Console(){
		this.addWindowListener(this);
		this.setTitle("Folder Check Compare "+Config.VERSION);
		this.setSize(300, 200);
		this.setLocationRelativeTo(null);
		
		JLabel olabel = new JLabel("Only command version: jfcc.jar [folder] [folder]", JLabel.CENTER);
		this.add(olabel);

	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Console oConsole = new Console();
		oConsole.setVisible(true);
	}

}
