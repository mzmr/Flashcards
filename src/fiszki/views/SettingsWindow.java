package fiszki.views;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;

public class SettingsWindow extends JDialog {
	private static final long serialVersionUID = 6L;
	
	// komponenty
	private final JPanel pnlContent = new JPanel();
	private JSpinner spH;
	private JSpinner spM;
	private JSpinner spS;
	private JCheckBox chkUnknownWords;
	private JCheckBox chkTranslation;
	private JButton btnOK;
	private JButton btnCancel;
	private JPanel pnlButtons;
	private JButton btnSetDefaultSettings;
	private JCheckBox chkSaveWithoutAskingOnExit;
	private JCheckBox chkRememberLastSets;
	private JCheckBox chkUseAdvancedLearningMode;
	private JCheckBox chkSwapLanguages;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			SettingsWindow dialog = new SettingsWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SettingsWindow() {
		setResizable(false);
		initComponents();
		createEvents();
	}

	private void initComponents() {
		setTitle("Ustawienia");
		setModal(true);
		setIconImage(MainWindow.FISZKI_ICON_16);
		setBounds(100, 100, 390, 316);
		pnlContent.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JPanel pnlGeneralSettings = new JPanel();
		pnlGeneralSettings.setBorder(new TitledBorder(null, "Ustawienia og\u00F3lne", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel pnlEFiszki = new JPanel();
		pnlEFiszki.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "E-Fiszki", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GroupLayout gl_pnlContent = new GroupLayout(pnlContent);
		gl_pnlContent.setHorizontalGroup(
			gl_pnlContent.createParallelGroup(Alignment.TRAILING)
				.addComponent(pnlGeneralSettings, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
				.addComponent(pnlEFiszki, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 374, Short.MAX_VALUE)
		);
		gl_pnlContent.setVerticalGroup(
			gl_pnlContent.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlContent.createSequentialGroup()
					.addComponent(pnlGeneralSettings, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlEFiszki, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(67, Short.MAX_VALUE))
		);
		
		chkUnknownWords = new JCheckBox("Nie wy\u015Bwietlaj s\u0142\u00F3wek, kt\u00F3re znam");
		chkUnknownWords.setSelected(true);
		
		chkTranslation = new JCheckBox("Poka\u017C t\u0142umaczenie");
		
		spH = new JSpinner();
		spH.setModel(new SpinnerNumberModel(0, 0, 23, 1));
		
		JLabel lblInterval = new JLabel("Przerwa mi\u0119dzy s\u0142\u00F3wkami:");
		
		JLabel lblH = new JLabel("h");
		
		spM = new JSpinner();
		spM.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		
		JLabel lblM = new JLabel("m");
		
		spS = new JSpinner();
		spS.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		
		JLabel lblS = new JLabel("s");
		
		chkUseAdvancedLearningMode = new JCheckBox("U\u017Cywaj zaawansowanego trybu nauki");
		chkUseAdvancedLearningMode.setSelected(true);
		
		chkSwapLanguages = new JCheckBox("Zamie\u0144 j\u0119zyki");
		GroupLayout gl_pnlEFiszki = new GroupLayout(pnlEFiszki);
		gl_pnlEFiszki.setHorizontalGroup(
			gl_pnlEFiszki.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlEFiszki.createSequentialGroup()
					.addGroup(gl_pnlEFiszki.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlEFiszki.createSequentialGroup()
							.addGap(6)
							.addGroup(gl_pnlEFiszki.createParallelGroup(Alignment.LEADING)
								.addComponent(chkTranslation)
								.addGroup(gl_pnlEFiszki.createSequentialGroup()
									.addComponent(lblInterval)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(spH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblH)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(spM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblM)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(spS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblS))
								.addComponent(chkUnknownWords)))
						.addGroup(gl_pnlEFiszki.createSequentialGroup()
							.addContainerGap()
							.addComponent(chkUseAdvancedLearningMode))
						.addGroup(gl_pnlEFiszki.createSequentialGroup()
							.addContainerGap()
							.addComponent(chkSwapLanguages)))
					.addContainerGap(65, Short.MAX_VALUE))
		);
		gl_pnlEFiszki.setVerticalGroup(
			gl_pnlEFiszki.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlEFiszki.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlEFiszki.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblInterval)
						.addComponent(spH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblH)
						.addComponent(spM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblM)
						.addComponent(spS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblS))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chkUnknownWords)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chkTranslation)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chkUseAdvancedLearningMode)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chkSwapLanguages)
					.addContainerGap(41, Short.MAX_VALUE))
		);
		pnlEFiszki.setLayout(gl_pnlEFiszki);
		
		chkRememberLastSets = new JCheckBox("Pami\u0119taj zestawy z poprzedniej sesji");
		chkRememberLastSets.setSelected(true);
		
		chkSaveWithoutAskingOnExit = new JCheckBox("Zapisz wszystkie zmiany bez pytania podczas zamykania programu");
		chkSaveWithoutAskingOnExit.setSelected(true);
		GroupLayout gl_pnlGeneralSettings = new GroupLayout(pnlGeneralSettings);
		gl_pnlGeneralSettings.setHorizontalGroup(
			gl_pnlGeneralSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlGeneralSettings.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlGeneralSettings.createParallelGroup(Alignment.LEADING)
						.addComponent(chkRememberLastSets)
						.addComponent(chkSaveWithoutAskingOnExit))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		gl_pnlGeneralSettings.setVerticalGroup(
			gl_pnlGeneralSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlGeneralSettings.createSequentialGroup()
					.addContainerGap()
					.addComponent(chkRememberLastSets)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chkSaveWithoutAskingOnExit)
					.addContainerGap(106, Short.MAX_VALUE))
		);
		pnlGeneralSettings.setLayout(gl_pnlGeneralSettings);
		pnlContent.setLayout(gl_pnlContent);
		{
			pnlButtons = new JPanel();
			{
				btnOK = new JButton("OK");
				btnOK.setActionCommand("OK");
				getRootPane().setDefaultButton(btnOK);
			}
			{
				btnCancel = new JButton("Anuluj");
				btnCancel.setActionCommand("Cancel");
			}
			
			btnSetDefaultSettings = new JButton("Przywr\u00F3\u0107 domy\u015Blne");
			GroupLayout gl_pnlButtons = new GroupLayout(pnlButtons);
			gl_pnlButtons.setHorizontalGroup(
				gl_pnlButtons.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_pnlButtons.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnSetDefaultSettings)
						.addGap(113)
						.addComponent(btnOK)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnCancel)
						.addContainerGap())
			);
			gl_pnlButtons.setVerticalGroup(
				gl_pnlButtons.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_pnlButtons.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_pnlButtons.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnSetDefaultSettings)
							.addComponent(btnCancel)
							.addComponent(btnOK)))
			);
			pnlButtons.setLayout(gl_pnlButtons);
		}
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(pnlContent, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(pnlButtons, GroupLayout.PREFERRED_SIZE, 384, Short.MAX_VALUE))
					.addGap(0))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addComponent(pnlContent, GroupLayout.PREFERRED_SIZE, 232, Short.MAX_VALUE)
					.addGap(1)
					.addComponent(pnlButtons, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(groupLayout);
	}

	private void createEvents() {
		btnSetDefaultSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(SettingsWindow.this, "Czy na pewno chcesz przywróciæ domyœlne ustawienia programu?",
						"Przywróæ domyœlne ustawienia", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (result != JOptionPane.YES_OPTION)
					return;
				
				MainWindow.winMain.setDefaultSettings();
				loadSettings();
			}
		});
		
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainWindow.setWordInterval((int)spH.getValue(), (int)spM.getValue(), (int)spS.getValue());
				MainWindow.hideKnown = chkUnknownWords.isSelected();
				MainWindow.showTranslation = chkTranslation.isSelected();
				MainWindow.saveWithoutAskingOnExit = chkSaveWithoutAskingOnExit.isSelected();
				MainWindow.rememberLastSets = chkRememberLastSets.isSelected();
				MainWindow.useAdvancedLearningMode = chkUseAdvancedLearningMode.isSelected();
				MainWindow.swapLanguages = chkSwapLanguages.isSelected();
				setVisible(false);
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		  .addKeyEventDispatcher(new KeyEventDispatcher() {
		      @Override
		      public boolean dispatchKeyEvent(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		        	setVisible(false);		        
		        return false;
		      }
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent arg0) {
				loadSettings();
			}
		});
	}
	
	private void loadSettings() {
		// ustawienie odpowiednich danych dla komponentów
		spH.setValue(MainWindow.getWordIntervalHours());
		spM.setValue(MainWindow.getWordIntervalMinutes());
		spS.setValue(MainWindow.getWordIntervalSeconds());
		
		chkUnknownWords.setSelected(MainWindow.hideKnown);
		chkTranslation.setSelected(MainWindow.showTranslation);
		chkSaveWithoutAskingOnExit.setSelected(MainWindow.saveWithoutAskingOnExit);
		chkRememberLastSets.setSelected(MainWindow.rememberLastSets);
		chkUseAdvancedLearningMode.setSelected(MainWindow.useAdvancedLearningMode);
		chkSwapLanguages.setSelected(MainWindow.swapLanguages);
	}
}
