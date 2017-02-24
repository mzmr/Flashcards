package fiszki.views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import fiszki.common.SetTableModel;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class WordCheckWindow extends JFrame {
	private static final long serialVersionUID = 5L;
	
	// komponenty
	private JButton btnShowWord;
	private JLabel lblWord2;
	private JLabel lblWord1;
	private JButton btnKnow;
	private JButton btnDontKnow;
	
	// zmienne
	private static SetTableModel currentSet = null;
	private Timer tmrShowWindow;
	private static ArrayList<Integer[]> wordQueue = null;
	private JMenuItem miRightBottomCorner;
	private JMenuItem miRightTopCorner;
	private JMenuItem miLeftBottomCorner;
	private JMenuItem miLeftTopCorner;
	private ArrayList<String[]> knownList = null;
	private ArrayList<String[]> unknownList = null;
	private int firstLang;
	private int secondLang;
	
	private SummaryWindow winSummary;
	
	final int WIN_WIDTH = 269;
	final int WIN_HEIGHT = 142;
	final int WIN_X = (int)GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() - WIN_WIDTH - 5;
	final int WIN_Y = (int)GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight() - WIN_HEIGHT - 12;
	private JMenuItem mntmZakoczEfiszki;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WordCheckWindow dialog = new WordCheckWindow();
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public WordCheckWindow() {
		initComponents();
		createEvents();
	}

	private void initComponents() {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setIconImage(MainWindow.FISZKI_ICON_32);
		setTitle("E-Fiszki");
		setBackground(Color.WHITE);
		final int WIN_X = (int)GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() - WIN_WIDTH - 5;
		final int WIN_Y = (int)GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight() - WIN_HEIGHT - 12;
		setBounds(WIN_X, WIN_Y, 269, 150);
		
		winSummary = new SummaryWindow();
		
		lblWord1 = new JLabel("Pierwsze s\u0142\u00F3wko");
		lblWord1.setBounds(10, 11, 243, 20);
		lblWord1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblWord1.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblWord2 = new JLabel("Drugie s\u0142\u00F3wko");
		lblWord2.setBounds(10, 42, 243, 20);
		lblWord2.setHorizontalAlignment(SwingConstants.CENTER);
		lblWord2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		btnShowWord = new JButton("Poka\u017C t\u0142umaczenie");
		final int BTN_WIDTH = 121;
		btnShowWord.setBounds(71, 34, BTN_WIDTH, 40);
		getContentPane().setLayout(null);
		getContentPane().add(lblWord1);
		getContentPane().add(lblWord2);
		getContentPane().add(btnShowWord);
		
		btnKnow = new JButton("Umiem!");
		btnKnow.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnKnow.setIcon(new ImageIcon(WordCheckWindow.class.getResource("/fiszki/resources/img/happy_16.png")));
		btnKnow.setBounds(0, 82, 130, 40);
		getContentPane().add(btnKnow);
		
		btnDontKnow = new JButton("Nie umiem...");
		btnDontKnow.setIcon(new ImageIcon(WordCheckWindow.class.getResource("/fiszki/resources/img/sad_16.png")));
		btnDontKnow.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDontKnow.setBounds(133, 82, 130, 40);
		getContentPane().add(btnDontKnow);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(this, popupMenu);
		
		JMenu mnSetWinLocation = new JMenu("Ustaw pozycj\u0119 okna");
		mnSetWinLocation.setIcon(new ImageIcon(WordCheckWindow.class.getResource("/fiszki/resources/img/directions_16.png")));
		popupMenu.add(mnSetWinLocation);
		
		miRightBottomCorner = new JMenuItem("Prawy dolny r\u00F3g");
		miRightBottomCorner.setIcon(new ImageIcon(WordCheckWindow.class.getResource("/fiszki/resources/img/right_bottom_16.png")));
		mnSetWinLocation.add(miRightBottomCorner);
		
		miLeftBottomCorner = new JMenuItem("Lewy dolny r\u00F3g");
		miLeftBottomCorner.setIcon(new ImageIcon(WordCheckWindow.class.getResource("/fiszki/resources/img/left_bottom_16.png")));
		mnSetWinLocation.add(miLeftBottomCorner);
		
		miRightTopCorner = new JMenuItem("Prawy g\u00F3rny r\u00F3g");
		miRightTopCorner.setIcon(new ImageIcon(WordCheckWindow.class.getResource("/fiszki/resources/img/right_top_16.png")));
		mnSetWinLocation.add(miRightTopCorner);
		
		miLeftTopCorner = new JMenuItem("Lewy g\u00F3rny r\u00F3g");
		miLeftTopCorner.setIcon(new ImageIcon(WordCheckWindow.class.getResource("/fiszki/resources/img/left_top_16.png")));
		mnSetWinLocation.add(miLeftTopCorner);
		
		mntmZakoczEfiszki = new JMenuItem("Zako\u0144cz E-Fiszki");
		mntmZakoczEfiszki.setIcon(new ImageIcon(WordCheckWindow.class.getResource("/fiszki/resources/img/stopEFiszki_16.png")));
		popupMenu.add(mntmZakoczEfiszki);
	}

	private void createEvents() {
		mntmZakoczEfiszki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopEFiszki();
			}
		});
		
		miLeftTopCorner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLocation(5, 5);
			}
		});
		
		miLeftBottomCorner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLocation(5, WIN_Y);
			}
		});
		
		miRightTopCorner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLocation(WIN_X, 5);
			}
		});
		
		miRightBottomCorner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLocation(WIN_X, WIN_Y);
			}
		});
		
		btnKnow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (MainWindow.checkYourself) {
					String[] toAdd = { (String)currentSet.getRow(wordQueue.get(0)[0])[firstLang],
							(String)currentSet.getRow(wordQueue.get(0)[0])[secondLang] };
					knownList.add(toAdd);
				} else if (MainWindow.useAdvancedLearningMode) {
					int howManyTimesKnew = wordQueue.get(0)[1] + 1;
					if (howManyTimesKnew >= 2) {
						currentSet.setValueAt(true, wordQueue.get(0)[0], SetTableModel.KNOWN);
						currentSet.setEdited(true);
		            	MainWindow.winMain.refreshAppStates();
					} else {
						Integer[] toAdd = {wordQueue.get(0)[0], howManyTimesKnew};
						wordQueue.add(toAdd);
					}
				} else {
					currentSet.setValueAt(true, wordQueue.get(0)[0], SetTableModel.KNOWN);
					currentSet.setEdited(true);
	            	MainWindow.winMain.refreshAppStates();
            	}
				
				wordQueue.remove(0);
            	
				if (!prepareNextWord()) {
					if (MainWindow.checkYourself)
						showSummary();
					else
						showHurraMessage();
					
					stopEFiszki();
					return;
				}

				setVisible(false);
			}
		});
		
		btnDontKnow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dontKnow();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				dontKnow();
			}
		});
		
		btnShowWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnShowWord.setVisible(false);
				lblWord2.setVisible(true);
			}
		});
	}
	
	public void runEFiszki(SetTableModel setToSet) {
		cleanWindow();
		currentSet = setToSet;
		
		if (MainWindow.swapLanguages) {
			firstLang = SetTableModel.SECOND_LANG;
			secondLang = SetTableModel.FIRST_LANG;
		} else {
			firstLang = SetTableModel.FIRST_LANG;
			secondLang = SetTableModel.SECOND_LANG;
		}
		
		if (MainWindow.checkYourself) {
			knownList = new ArrayList<String[]>();
			unknownList = new ArrayList<String[]>();
		}
		
		if (!prepareNextWord()) {
			if (MainWindow.checkYourself)
				showSummary();
			else
				showHurraMessage();
			
			MainWindow.winMain.returnComponentsDefaults();
			return;
		}

		setTitle("E-Fiszki - " + currentSet.getName());
		setVisible(true);
	}
	
	public void stopEFiszki() {
		tmrShowWindow.cancel();
		tmrShowWindow.purge();
		MainWindow.winMain.returnComponentsDefaults();
		setVisible(false);
	}

	private boolean createNewWordQueue() {
		// utworzenie kolejki s³ówek w losowej kolejnoœci
		wordQueue = new ArrayList<Integer[]>();
		for (int i = currentSet.getRowCount() - 1; i >= 0; i--) {
			if (MainWindow.hideKnown && !MainWindow.checkYourself && (boolean)currentSet.getRow(i)[SetTableModel.KNOWN])
				continue;
			
			Integer[] toAdd = {i, 0};
			wordQueue.add(toAdd);
		}
		
		if (wordQueue.isEmpty())
			return false;

		long seed = System.nanoTime();
		Collections.shuffle(wordQueue, new Random(seed));
		return true;
	}
	
	private void setWord(Object[] wordData) {
		lblWord1.setText((String) wordData[firstLang]);
		lblWord2.setText((String) wordData[secondLang]);
	}
	
	private void showHurraMessage() {
		JOptionPane.showMessageDialog(WordCheckWindow.this, "Hurraaa! Znasz ju¿ wszystkie s³ówka z zestawu \"" + currentSet.getName() + "\" :)", "Brawo Ty!", JOptionPane.PLAIN_MESSAGE);
	}
	
	private void setDefaults() {
		lblWord2.setVisible(MainWindow.showTranslation);
		btnShowWord.setVisible(!MainWindow.showTranslation);
	}
	
	public void setDefaultSettings() {
		setLocation(WIN_X, WIN_Y);
	}
	
	private boolean prepareNextWord() {
		// ustawienie komponentów
		setDefaults();
		
		// zmiana s³ówek
		if (wordQueue == null) {
			createNewWordQueue();
			
			for (Integer[] word : wordQueue)
				word[1] = 0;
		}
		
		if (wordQueue.isEmpty()) {
			if (MainWindow.useAdvancedLearningMode || MainWindow.checkYourself)
				return false;
			else if (!createNewWordQueue())
				return false;
		}
		
		setWord(currentSet.getRow(wordQueue.get(0)[0]));
		
		// ustawienie timera do kolejnego wyœwietlenia okna
		tmrShowWindow = new Timer();
		tmrShowWindow.schedule(new TimerTask() {
			public void run() {
				setVisible(true);
				tmrShowWindow.cancel();
			}
		}, MainWindow.checkYourself ? 0 : MainWindow.getWordInterval() * 1000);
		
		return true;
	}
	
	private void cleanWindow() {
		setDefaults();
		wordQueue = null;
		currentSet = null;
		knownList = null;
		unknownList = null;
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	private void dontKnow() {
		if (MainWindow.checkYourself) {
			String[] toAdd = { (String)currentSet.getRow(wordQueue.get(0)[0])[firstLang],
					(String)currentSet.getRow(wordQueue.get(0)[0])[secondLang] };
			unknownList.add(toAdd);
		} else if (MainWindow.useAdvancedLearningMode) {
			currentSet.setValueAt(false, wordQueue.get(0)[0], SetTableModel.KNOWN);
			currentSet.setEdited(true);
        	MainWindow.winMain.refreshAppStates();
        	
        	// jeœli nie znamy s³ówka, to przenoszê go o 4 miejsca dalej na liœcie (zaraz siê powtórzy)
        	if (4 >= wordQueue.size())
        		wordQueue.add(wordQueue.get(0));
        	else
        		wordQueue.add(4, wordQueue.get(0));
		}
		
		wordQueue.remove(0);
		
		if (!prepareNextWord()) {
			if (MainWindow.checkYourself)
				showSummary();
			
			stopEFiszki();
			return;
		}
		setVisible(false);
	}
	
	private void showSummary() {
		winSummary.showSummary(knownList, unknownList, currentSet.getColumnNames());
	}
}
