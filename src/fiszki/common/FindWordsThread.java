package fiszki.common;

import javax.swing.JTable;

public class FindWordsThread extends Thread {
	private volatile boolean stopMe = false;
	public String toFind = "";
	public SetTableModel wordsTM = null;
	public JTable tabWords = null;
	
	public void run() {
		if (toFind.isEmpty()) {
			tabWords.setModel(wordsTM);
			tabWords.getColumnModel().getColumn(SetTableModel.KNOWN).setMaxWidth(40);
		} else {
			SetTableModel wordsFound = new SetTableModel("Wyszukane s³ówka");
			wordsFound.setColumnsNames(wordsTM.getColumnNames());
			
			for (Object[] word : wordsTM.getData()) {
				for (int i = 0; i < 2; i++) {
					if (stopMe) {
//						System.out.println("koniec w¹tku (zabity)");
						return;
					}

					if (((String)word[i]).contains(toFind)) {
						wordsFound.addData(word);
						break;
					}
				}
			}
			
			tabWords.setModel(wordsFound);
			tabWords.getColumnModel().getColumn(SetTableModel.KNOWN).setMaxWidth(40);
		}

//		System.out.println("koniec w¹tku");
	}
	
	public void setParams(String strToFind, JTable tabOfWords, SetTableModel curTM) {
		toFind = strToFind;
		wordsTM = curTM;
		tabWords = tabOfWords;
	}
	
	public void killMePlease() {
		stopMe = true;
	}
}
