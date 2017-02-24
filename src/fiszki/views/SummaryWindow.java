package fiszki.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class SummaryWindow extends JDialog {
	private static final long serialVersionUID = -530595649127150898L;
	
	private final JPanel contentPanel = new JPanel();
	private JButton btnOK;
	private JProgressBar pbResult;
	private JLabel lblResult;
	private JLabel lblSentenceResult;
	private JTabbedPane tpWords;

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
			SummaryWindow dialog = new SummaryWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SummaryWindow() {
		setModal(true);
		setType(Type.POPUP);
		setTitle("Podsumowanie");
		setIconImage(Toolkit.getDefaultToolkit().getImage(SummaryWindow.class.getResource("/fiszki/resources/img/ikona_16.png")));
		initComponents();
		createEvents();
	}

	private void initComponents() {
		setBounds(100, 100, 261, 357);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblTwjWynik = new JLabel("Tw\u00F3j wynik:");
		lblTwjWynik.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblResult = new JLabel("AB%");
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblResult.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		lblSentenceResult = new JLabel("Znasz CC z DD s\u0142\u00F3wek.");
		lblSentenceResult.setHorizontalAlignment(SwingConstants.CENTER);
		
		pbResult = new JProgressBar();
		pbResult.setString("");
		pbResult.setStringPainted(true);
		pbResult.setValue(50);
		{
			btnOK = new JButton("OK");
			btnOK.setActionCommand("OK");
			getRootPane().setDefaultButton(btnOK);
		}
		
		tpWords = new JTabbedPane(JTabbedPane.TOP);
		tpWords.setBorder(null);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(8)
					.addComponent(pbResult, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(lblTwjWynik, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
				.addComponent(lblResult, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
				.addComponent(lblSentenceResult, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(94)
					.addComponent(btnOK, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(94))
				.addComponent(tpWords, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTwjWynik)
					.addGap(18)
					.addComponent(lblResult)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pbResult, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblSentenceResult)
					.addGap(18)
					.addComponent(btnOK)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(tpWords, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
	}

	private void createEvents() {
		
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				btnOK.requestFocus();
			}
		});
	}
	
	public void showSummary(ArrayList<String[]> knownList, ArrayList<String[]> unknownList, String[] languages) {
		int procent = knownList.size() * 100 / (knownList.size() + unknownList.size());
		lblResult.setText(Integer.toString(procent) + "%");
		Color clr;
		if (procent <= 50)
			clr = new Color(255, (procent * 255) / 50, 0);
		else
			clr = new Color(255 - ((procent - 50) * 255) / 50, 255, 0);
		
		pbResult.setValue(procent);
		pbResult.setForeground(clr);
		
		lblSentenceResult.setText("Znasz " + knownList.size() + " z " + (knownList.size() + unknownList.size()) + " s³ówek.");
		
		tpWords.removeAll();
		
		// znane
		String[] colNames = {languages[0], languages[1]};
		
		String[][] wordsTabKnown = new String[knownList.size()][2];
		for (int i = knownList.size() - 1; i >= 0; i--)
			wordsTabKnown[i] = knownList.get(i);

		JTable tabKnown = new JTable(wordsTabKnown, colNames);
		tabKnown.setFillsViewportHeight(true);
		JScrollPane spKnown = new JScrollPane(tabKnown);
		spKnown.setBorder(new EmptyBorder(0,0,0,0));
		tpWords.add("Umiesz", spKnown);
		
		// nieznane
		String[][] wordsTabUnknown = new String[unknownList.size()][2];
		for (int i = unknownList.size() - 1; i >= 0; i--)
			wordsTabUnknown[i] = unknownList.get(i);

		JTable tabUnknown = new JTable(wordsTabUnknown, colNames);
		tabUnknown.setFillsViewportHeight(true);
		JScrollPane spUnknown = new JScrollPane(tabUnknown);
		spUnknown.setBorder(new EmptyBorder(0,0,0,0));
		tpWords.add("Nie umiesz", spUnknown);
		
		setVisible(true);
	}
}
