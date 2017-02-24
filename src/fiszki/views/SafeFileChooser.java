package fiszki.views;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SafeFileChooser extends JFileChooser {
	private static final long serialVersionUID = 10L;
	
	@Override
	public void approveSelection(){
		File f = getSelectedFile();
		
		if(f.exists() && getDialogType() == SAVE_DIALOG){
			int result = JOptionPane.showConfirmDialog(this,"Plik istnieje, czy chcesz go nadpisaæ?","Plik istnieje",JOptionPane.YES_NO_CANCEL_OPTION);
			switch(result){
				case JOptionPane.YES_OPTION:
					super.approveSelection();
					return;
//				case JOptionPane.NO_OPTION:
//					return;
//				case JOptionPane.CLOSED_OPTION:
//					return;
				case JOptionPane.CANCEL_OPTION:
					cancelSelection();
					return;
				default:
					return;
			}
		}
		super.approveSelection();
	}
}
