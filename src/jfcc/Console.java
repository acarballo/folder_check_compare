package jfcc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Console extends JFrame implements WindowListener, ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton oSearchFolder = null;

	public Console(){
		this.addWindowListener(this);
		this.setTitle("Folder Check Compare "+Config.VERSION);
		this.setSize(300, 200);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setContentPane(getMainPanel());
	}	
	
	private JPanel getMainPanel(){
		JPanel oJPanel = new JPanel();
		
		JLabel olabel = new JLabel("Command version: jfcc.jar [folder] [folder]");
		
		oJPanel.add(olabel);
		oJPanel.add(getSearchFolder());
		
		return oJPanel;
	}
	
	private JButton getSearchFolder(){
		if(this.oSearchFolder == null){
			this.oSearchFolder = new JButton();
			this.oSearchFolder.setName("oSearchFolder");
			this.oSearchFolder.setText("Search");
			this.oSearchFolder.addActionListener(this);	
		}
		return this.oSearchFolder;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(getSearchFolder())){
			JFileChooser oChooser = new JFileChooser();
			oChooser.setDialogTitle("Select directory");
			oChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = oChooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				String sPath = oChooser.getSelectedFile().getAbsolutePath();
				Snapshot oSnapshot = new Snapshot(sPath);
				oSnapshot.addFileIgnored(".svn");
				oSnapshot.loadSnapshot();
				oSnapshot.refreshSnapshotCRC32();
				//oSnapshot.show();
				oSnapshot.export();
			}
		}
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
