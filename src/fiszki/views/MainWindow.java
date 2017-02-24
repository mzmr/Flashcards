package fiszki.views;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import fiszki.common.FindWordsThread;
import fiszki.common.SetTableModel;

public class MainWindow extends JFrame {
	// komponenty
	private static final long serialVersionUID = 1L;
	public HintTextField htfNew2;
	public HintTextField htfNew1;
	private IconTextField itfFindWord;
	public JTable tabWords;
	private JButton btnAddWord;
	private JButton btnRmWord;
	private JButton btnAddSet;
	private JButton btnRmSet;
	private JButton btnResetSet;
	private JComboBox<SetTableModel> cbSets;
	private JMenu mnOpen;
	private JMenuItem miImport;
	private JMenuItem miSave;
	private JMenuItem miSaveAs;
	private JMenuItem miPDF;
	private JMenuItem miExit;
	private JMenuItem miAbout;
	private JMenuItem miOpen;
    private JMenuItem miExport;
    private JMenuItem miSettings;
    private JMenuItem miStopEFiszki;
    
	// tray
	private PopupMenu pmTray;
	private Menu tmnRunEFiszki;
	private MenuItem tmiCheckYourself;
	private MenuItem tmiLearn;
	private MenuItem tmiOpen;
	private MenuItem tmiExit;
	private MenuItem tmiStopEFiszki;
	private static TrayIcon trayIcon = null;
    private static SystemTray tray = null;
	
    // file choosers
	private JFileChooser fcOpenSet;
	private JFileChooser fcSaveSet;
	private JFileChooser fcImportWords;
	private JFileChooser fcExportWords;
    private JFileChooser fcCreatePdf;
	
    // okna
    public static MainWindow winMain;
    private AboutWindow winAbout;
    private WordCheckWindow winWordCheck;
    private SettingsWindow winSettings;
    private AddSetWindow winAddSet;
    
    
    // sta³e i zmienne globalne
	private static final Charset charset = Charset.forName("UTF-8");
    public static final Image FISZKI_ICON_32 = Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/fiszki/resources/img/ikona_32.png"));
    public static final Image FISZKI_ICON_16 = Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/fiszki/resources/img/ikona_16.png"));
    private final int RECENT_FILES_COUNT = 5;
    private final ArrayList<String> recentFilesList = new ArrayList<String>(RECENT_FILES_COUNT);
    private final ArrayList<JMenuItem> recentFilesMI = new ArrayList<JMenuItem>();
    public static boolean checkYourself;
    
    // ustawienia programu
    public static Calendar wordInterval;
    public static boolean hideKnown;
    public static boolean showTranslation;
    public static boolean saveWithoutAskingOnExit;
    public static boolean rememberLastSets;
    public static boolean useAdvancedLearningMode;
    public static boolean swapLanguages;

    // w¹tek wyszukuj¹cy s³owa w czasie rzeczywistym
    private FindWordsThread thFind = null;
    private JMenuItem miCheckYourself;
    private JMenuItem miLearn;
    

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		initComponents();
		setDefaultSettings();
		createEvents();
		loadSettings();
		
		if (rememberLastSets)
			loadLastSets();
	}

	///////////////////////////////////////////////////////////////////////////
	// Utworzenie i inicjalizacja wszystkich komponentów.
	///////////////////////////////////////////////////////////////////////////
	private void initComponents() {
		winMain = MainWindow.this;
		
		thFind = new FindWordsThread();
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(FISZKI_ICON_32);	
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("Plik");
		menuBar.add(mnFile);
		
		winAbout = new AboutWindow();
		
		winWordCheck = new WordCheckWindow();
		
		winSettings = new SettingsWindow();
		
		winAddSet = new AddSetWindow();
		
		fcOpenSet = new JFileChooser();
		fcOpenSet.setFileFilter(new FileNameExtensionFilter("Zestawy fiszek (*.f)", "f"));
		fcOpenSet.setDialogTitle("Otwórz zestaw s³ówek");
		
		fcSaveSet = new JFileChooser();
		fcSaveSet.setFileFilter(new FileNameExtensionFilter("Zestawy fiszek (*.f)", "f"));
		fcSaveSet.setDialogTitle("Zapisz zestaw s³ówek");
		
		fcImportWords = new JFileChooser();
		fcImportWords.setDialogTitle("Importuj s³ówka do zestawu");
		fcImportWords.setFileFilter(new FileNameExtensionFilter("S³ówka (*.csv;*.txt)", "txt", "csv"));
		
		fcExportWords = new JFileChooser();
		fcExportWords.addChoosableFileFilter(new FileNameExtensionFilter("S³ówka (*.txt)", "txt"));
		fcExportWords.setFileFilter(new FileNameExtensionFilter("S³ówka (*.csv)", "csv"));
		fcExportWords.setDialogTitle("Eksportuj s³ówka z zestawu");
		
		fcCreatePdf = new JFileChooser();
		fcCreatePdf.setFileFilter(new FileNameExtensionFilter("Plik PDF (*.pdf)", "pdf"));
		fcCreatePdf.setDialogTitle("Utwórz plik PDF z fiszkami do wydruku");
		
		mnOpen = new JMenu("Otw\u00F3rz");
		mnOpen.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/open_16.png")));
		mnFile.add(mnOpen);
		
		miOpen = new JMenuItem("Wybierz plik");
		miOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		miOpen.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/file_16.png")));
		mnOpen.add(miOpen);
		
		mnOpen.addSeparator();
		
		miSave = new JMenuItem("Zapisz");
		miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		miSave.setEnabled(false);
		miSave.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/save_blue_16.png")));
		mnFile.add(miSave);
		
		miSaveAs = new JMenuItem("Zapisz jako...");
		miSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		miSaveAs.setEnabled(false);
		miSaveAs.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/save_as_16.png")));
		mnFile.add(miSaveAs);
		
		miImport = new JMenuItem("Importuj s\u0142\u00F3wka");
		miImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		miImport.setEnabled(false);
		miImport.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/import_16.png")));
		mnFile.add(miImport);
		
		miExport = new JMenuItem("Eksportuj s\u0142\u00F3wka");
		miExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		miExport.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/export_16.png")));
		miExport.setEnabled(false);
		mnFile.add(miExport);
		
		mnFile.addSeparator();
		
		
		miPDF = new JMenuItem("Generuj PDF");
		miPDF.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		miPDF.setEnabled(false);
		miPDF.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/pdf_16.png")));
		mnFile.add(miPDF);
		
		mnFile.addSeparator();
		
		miSettings = new JMenuItem("Ustawienia");
		mnFile.add(miSettings);
		miSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
		miSettings.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/settings_16.png")));
		
		mnFile.addSeparator();
		
		miExit = new JMenuItem("Zako\u0144cz");
		miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		miExit.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/close_16.png")));
		mnFile.add(miExit);
		
		JMenu mnEfiszki = new JMenu("E-Fiszki");
		menuBar.add(mnEfiszki);
		
		miStopEFiszki = new JMenuItem("Zatrzymaj");
		miStopEFiszki.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/stopEFiszki_16.png")));
		miStopEFiszki.setVisible(false);
		mnEfiszki.add(miStopEFiszki);
		
		miCheckYourself = new JMenuItem("Sprawd\u017A si\u0119!");
		miCheckYourself.setEnabled(false);
		mnEfiszki.add(miCheckYourself);
		miCheckYourself.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/check_yourself_16.png")));
		
		miLearn = new JMenuItem("Ucz si\u0119!");
		miLearn.setEnabled(false);
		mnEfiszki.add(miLearn);
		miLearn.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/book_16.png")));
		
		JMenu mnHelp = new JMenu("Pomoc");
		menuBar.add(mnHelp);
		
		miAbout = new JMenuItem("O programie");
		miAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		miAbout.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/help_16.png")));
		mnHelp.add(miAbout);
		JPanel pnlMain = new JPanel();
		pnlMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(pnlMain);
		
		JPanel pnlSets = new JPanel();
		pnlSets.setBorder(new TitledBorder(null, "Zestawy", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel pnlWords = new JPanel();
		pnlWords.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "S\u0142\u00F3wka", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JScrollPane spWordsTable = new JScrollPane();
		GroupLayout gl_pnlMain = new GroupLayout(pnlMain);
		gl_pnlMain.setHorizontalGroup(
			gl_pnlMain.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlMain.createSequentialGroup()
					.addGroup(gl_pnlMain.createParallelGroup(Alignment.LEADING)
						.addComponent(pnlSets, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
						.addComponent(pnlWords, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spWordsTable, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
		);
		gl_pnlMain.setVerticalGroup(
			gl_pnlMain.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlMain.createSequentialGroup()
					.addGroup(gl_pnlMain.createParallelGroup(Alignment.TRAILING)
						.addComponent(spWordsTable, Alignment.LEADING, 0, 324, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_pnlMain.createSequentialGroup()
							.addComponent(pnlSets, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(pnlWords, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)))
					.addGap(0))
		);
		
		tabWords = new JTable();
		tabWords.setFocusable(false);
		tabWords.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		tabWords.setIntercellSpacing(new Dimension(0, 0));
		tabWords.setShowGrid(false);
		tabWords.setRowHeight(20);
		tabWords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabWords.setFillsViewportHeight(true);
		tabWords.setModel(new SetTableModel());
		DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 9L;
			Border padding = BorderFactory.createEmptyBorder(0, 7, 0, 7);
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		    		boolean hasFocus, int row, int column) {
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        setBorder(BorderFactory.createCompoundBorder(getBorder(), padding));
		        return this;
		    }
		};
		tabWords.setDefaultRenderer(Object.class, r);
		spWordsTable.setViewportView(tabWords);
		setLastColWidth();
		
		htfNew2 = new HintTextField("Drugi jêzyk");
		htfNew2.setMargin(new Insets(2, 6, 2, 2));
		htfNew2.setColumns(10);
		
		htfNew1 = new HintTextField("Pierwszy jêzyk");
		htfNew1.setMargin(new Insets(2, 6, 2, 2));
		htfNew1.setColumns(10);
		
		btnAddWord = new JButton("Dodaj s\u0142\u00F3wko");
		btnAddWord.setEnabled(false);
		btnAddWord.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/plus_16.png")));
		
		btnRmWord = new JButton("Usu\u0144 s\u0142\u00F3wko");
		btnRmWord.setEnabled(false);
		btnRmWord.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/minus_16.png")));
		
		JSeparator separator = new JSeparator();
		
		itfFindWord = new IconTextField("Szukaj");
		itfFindWord.setIconURL( MainWindow.class.getResource("/fiszki/resources/img/search_rev_14.png"));
		itfFindWord.setMargin(new Insets(2, 4, 2, 2));
		itfFindWord.setColumns(10);
		
		GroupLayout gl_pnlWords = new GroupLayout(pnlWords);
		gl_pnlWords.setHorizontalGroup(
			gl_pnlWords.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlWords.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlWords.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddWord, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
						.addComponent(btnRmWord, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
						.addComponent(itfFindWord, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
						.addComponent(htfNew2, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
						.addComponent(htfNew1, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_pnlWords.setVerticalGroup(
			gl_pnlWords.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlWords.createSequentialGroup()
					.addContainerGap()
					.addComponent(htfNew1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(htfNew2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddWord)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRmWord)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(itfFindWord, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(43, Short.MAX_VALUE))
		);
		pnlWords.setLayout(gl_pnlWords);
		
		btnAddSet = new JButton("Dodaj zestaw");
		btnAddSet.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/addFile_16.png")));
		
		btnRmSet = new JButton("");
		btnRmSet.setFocusable(false);
		btnRmSet.setEnabled(false);
		btnRmSet.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/close_set_12.png")));
		
		cbSets = new JComboBox<SetTableModel>();
		
		btnResetSet = new JButton("Resetuj zestaw");
		btnResetSet.setEnabled(false);
		btnResetSet.setIcon(new ImageIcon(MainWindow.class.getResource("/fiszki/resources/img/resetFile_16.png")));
		GroupLayout gl_pnlSets = new GroupLayout(pnlSets);
		gl_pnlSets.setHorizontalGroup(
			gl_pnlSets.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlSets.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlSets.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddSet, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
						.addComponent(btnResetSet, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_pnlSets.createSequentialGroup()
							.addComponent(cbSets, 0, 121, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnRmSet, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_pnlSets.setVerticalGroup(
			gl_pnlSets.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlSets.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAddSet)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnResetSet)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlSets.createParallelGroup(Alignment.LEADING)
						.addComponent(btnRmSet, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbSets, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(48, Short.MAX_VALUE))
		);
		pnlSets.setLayout(gl_pnlSets);
		pnlMain.setLayout(gl_pnlMain);
		
		// ustawienia tray'a
		try {
			tray = SystemTray.getSystemTray();
			final Image imgTray = FISZKI_ICON_16;
			pmTray = new PopupMenu();
			tmiStopEFiszki = new MenuItem("Zatrzymaj");
			tmnRunEFiszki = new Menu("E-Fiszki");
			tmnRunEFiszki.setEnabled(false);
			tmiCheckYourself = new MenuItem("SprawdŸ siê!");
			tmiLearn = new MenuItem("Ucz siê!");
			tmiOpen = new MenuItem("Otwórz");
			tmiExit = new MenuItem("Wyjœcie");

			tmnRunEFiszki.add(tmiCheckYourself);
			tmnRunEFiszki.add(tmiLearn);
			
			pmTray.add(tmnRunEFiszki);
			pmTray.add(tmiOpen);
			pmTray.add(tmiExit);
			trayIcon = new TrayIcon(imgTray, "Fiszki", pmTray);
		} catch (Exception e) {
			showError(e);
		}
		
		refreshAppStates();
	}

	///////////////////////////////////////////////////////////////////////////
	// Utworzenie zdarzeñ.
	///////////////////////////////////////////////////////////////////////////
	private void createEvents() {
		tmiCheckYourself.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbSets.getSelectedIndex() != -1)
					startEFiszki(true);
			}
		});
		
		tmiLearn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbSets.getSelectedIndex() != -1)
					startEFiszki(false);
			}
		});
		
		miCheckYourself.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbSets.getSelectedIndex() != -1)
					startEFiszki(true);
			}
		});
		
		miLearn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbSets.getSelectedIndex() != -1)
					startEFiszki(false);
			}
		});
		
		miSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAsInterface();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
		
		itfFindWord.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				if (cbSets.getSelectedIndex() == -1)
					return;
				
				thFind.killMePlease();
				try {
					thFind.join();
				} catch (InterruptedException exc) {
					showError(exc);
				}
				
				thFind = new FindWordsThread();
				thFind.setParams(itfFindWord.getText().toLowerCase(), tabWords, (SetTableModel)cbSets.getSelectedItem());
				thFind.start();
			}
		});
		
		miStopEFiszki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				endEFiszki();
			}
		});
		
		htfNew1.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				setBtnAddWordEnabled();
			}
		});
		
		htfNew2.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				setBtnAddWordEnabled();
			}
		});
		
		btnResetSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SetTableModel curTM = ((SetTableModel)cbSets.getSelectedItem());
				int wynik = JOptionPane.showConfirmDialog(MainWindow.this,
						"Czy na pewno chcesz zresetowaæ zestaw \"" + curTM.getName() +"\"?",
						"Resetowanie zestawu", JOptionPane.YES_NO_CANCEL_OPTION);
				if (wynik == JOptionPane.YES_OPTION) {
					for (int i = curTM.getRowCount() - 1; i >= 0; i--)
						curTM.setValueAt(false, i, SetTableModel.KNOWN);
				}
				
				((SetTableModel) cbSets.getSelectedItem()).setEdited(true);
				refreshAppStates();
			}
		});
		
		miSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int x = getX() + getWidth() / 2 - winSettings.getWidth() / 2;
				int y = getY() + getHeight() / 2 - winSettings.getHeight() / 2;
				winSettings.setLocation(x, y);
				winSettings.setVisible(true);
			}
		});
		
		tmiStopEFiszki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endEFiszki();
			}
		});
		
		tmiOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(true);
				setExtendedState(JFrame.NORMAL);
			}
		});
		
		tmiExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		miAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int x = getX() + getWidth() / 2 - winAbout.getWidth() / 2;
				int y = getY() + getHeight() / 2 - winAbout.getHeight() / 2;
				winAbout.setLocation(x, y);
				winAbout.setVisible(true);
			}
		});
		
		miExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				System.exit(0);
				closeWindow();
			}
		});
		
		addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                    	showError(ex);
                    }
                }
		        if (e.getNewState() == 7) {
		            try {
			            tray.add(trayIcon);
			            setVisible(false);
		            } catch (AWTException ex) {
		            	showError(ex);
		            }
		        }
		        if(e.getNewState() == MAXIMIZED_BOTH){
                    tray.remove(trayIcon);
                    setVisible(true);
                }
                if(e.getNewState() == NORMAL){
                    tray.remove(trayIcon);
                    setVisible(true);
                }
            }
        });
		
		htfNew1.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyChar() == KeyEvent.VK_ENTER)
		        	btnAddWord.doClick();
			}
		});
		
		htfNew2.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
		        	btnAddWord.doClick();
			}
		});
		
		miPDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int returnVal = fcCreatePdf.showSaveDialog(MainWindow.this);
					
					if (returnVal != JFileChooser.APPROVE_OPTION)
						return;
					
					createPdf(fcCreatePdf.getSelectedFile());
				} catch (Exception ex) {
					showError(ex);
				}
			}
		});
		
		miOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fcOpenSet.showOpenDialog(MainWindow.this);

		        if (returnVal != JFileChooser.APPROVE_OPTION)
		        	return;

	            File file = fcOpenSet.getSelectedFile();
	            String path = file.getAbsolutePath();
	            
	            if (fileIsOpened(path)) {
	            	showError("Plik " + path + " jest ju¿ otwarty.");
            		return;
	            }

	            if (openSet(file)) {
	            	addRecentFile(path);
	            	refreshAppStates();
	            } else
	            	showError("Plik " + path + " nie istnieje.");
			}
		});
		
		miImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fcImportWords.showOpenDialog(MainWindow.this);

		        if (returnVal != JFileChooser.APPROVE_OPTION)
		        	return;
		        
	            File file = fcImportWords.getSelectedFile();
	            
	            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
				    String line = null;
				    
				    while ((line = reader.readLine()) != null) {
				        String[] words = line.split(";");
				        Object[] toAdd = {words[SetTableModel.FIRST_LANG], words[SetTableModel.SECOND_LANG], false};
				        addWord(toAdd);
				    }
				} catch (IOException x) {
					showError(x);
				}
			}
		});
		
		miExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fcExportWords.showSaveDialog(MainWindow.this);
				
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;
				
				File fileToSave = fcExportWords.getSelectedFile();
				exportSet(fileToSave);
			}
		});
		
		miSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (((SetTableModel)cbSets.getSelectedItem()).getDiskPath().isEmpty())
					saveAsInterface();
				else
					saveInterface(null);
			}
		});
		
		btnRmWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetTableModel tabTM = (SetTableModel)tabWords.getModel();
				SetTableModel cbTM = (SetTableModel)cbSets.getSelectedItem();
				int[] selected = tabWords.getSelectedRows();
				if (cbSets.getModel().getSize() > 0) {
					for (int i : selected) {
						int modelIndex = tabWords.convertRowIndexToModel(i);

						if (cbTM != tabTM)
							cbTM.remove(cbTM.getItemIndex(tabTM.getRow(modelIndex)));
						
						tabTM.remove(modelIndex);
					}
				}
				
				((SetTableModel) cbSets.getSelectedItem()).setEdited(true);
				refreshAppStates();
			}
		});
		
		btnAddWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// dodanie nowej pary s³ów
				Object[] newWord = {htfNew1.getText(), htfNew2.getText(), false};
				addWord(newWord);
				
				((SetTableModel) cbSets.getSelectedItem()).setEdited(true);
				refreshAppStates();
				
				// wyczyszczenie pól tekstowych
				htfNew1.setText("");
				htfNew2.setText("");
				htfNew1.requestFocusInWindow();
			}
		});
		
		cbSets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idx = cbSets.getSelectedIndex();
				// zmiana bie¿¹cego modelu poprzez wybór na liœcie JComboBox'a
				if (idx == -1) {
					tabWords.setModel(new SetTableModel());
					setLastColWidth();
					
					tmnRunEFiszki.setEnabled(false);
					miCheckYourself.setEnabled(false);
					miLearn.setEnabled(false);
					miPDF.setEnabled(false);
					btnRmSet.setEnabled(false);
					miSaveAs.setEnabled(false);
					miImport.setEnabled(false);
					miExport.setEnabled(false);
					btnResetSet.setEnabled(false);
				} else {
					SetTableModel curTM = (SetTableModel) cbSets.getSelectedItem();
					tabWords.setModel(curTM);
					setLastColWidth();
					
					// ustawienie przycisków dot. e-fiszek w tray'u i w menu	
					tmnRunEFiszki.setEnabled(true);
					miCheckYourself.setEnabled(true);
					miLearn.setEnabled(true);
					miPDF.setEnabled(true);
					btnRmSet.setEnabled(true);
					miSaveAs.setEnabled(true);
					miImport.setEnabled(true);
					miExport.setEnabled(true);
					btnResetSet.setEnabled(true);
				}
				
				refreshAppStates();
			}
		});
		
		btnRmSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SetTableModel curTM = (SetTableModel) cbSets.getSelectedItem();
				
				if (!curTM.isEdited()) {
					cbSets.removeItem(curTM);
					return;
				}
				
				int wynik = JOptionPane.showConfirmDialog(MainWindow.this,
						"Czy na pewno chcesz zamkn¹æ zestaw \"" + curTM.getName() +"\"? Wszystkie niezapisane dane zostan¹ utracone.",
						"Zamykanie zestawu", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (wynik == JOptionPane.YES_OPTION) {
					// usuniecie bie¿¹cego zestawu
					cbSets.removeItem(curTM);
				}
			}
		});
		
		btnAddSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int x = getX() + getWidth() / 2 - winAddSet.getWidth() / 2;
				int y = getY() + getHeight() / 2 - winAddSet.getHeight() / 2;
				winAddSet.setLocation(x, y);
				winAddSet.showWindow(MainWindow.this);
			}
		});
		
		tabWords.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
            	btnRmWord.setEnabled(tabWords.getSelectedRow() == -1 ? false : true);
            }
        });
	}
	
	public void setDefaultSettings() {
		setBounds(100, 100, 502, 392);
		
		hideKnown = true;
		showTranslation = false;
		saveWithoutAskingOnExit = true;
		rememberLastSets = true;
		useAdvancedLearningMode = true;
		swapLanguages = false;
		
		JOptionPane.setDefaultLocale(getLocale());
		wordInterval = Calendar.getInstance();
		wordInterval.set(0, 0, 0, 0, 0, 0);
		
		fcOpenSet.setCurrentDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
		fcSaveSet.setCurrentDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
		fcImportWords.setCurrentDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
		fcExportWords.setCurrentDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
		fcCreatePdf.setCurrentDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
		
		winWordCheck.setDefaultSettings();

		recentFilesList.clear();
		recentFilesMI.clear();
		for (int i = 0; i < RECENT_FILES_COUNT; i++)
			recentFilesList.add(i, "");
	}
	
	private void closeWindow() {
		if (thereAreUnsavedSets()) {
			if (!saveWithoutAskingOnExit) {
				int wynik = JOptionPane.showConfirmDialog(MainWindow.this, "Czy chcesz zapisaæ zmiany przed wyjœciem?",
						"Zamykanie programu", JOptionPane.YES_NO_CANCEL_OPTION);
				
				if (wynik == JOptionPane.YES_OPTION) {
					if (saveUnsavedSets()) {
						saveSettings();
						System.exit(0);
					}
				} else if (wynik == JOptionPane.NO_OPTION) {
					saveSettings();
					System.exit(0);
				}
			} else {
				if (saveUnsavedSets()) {
					saveSettings();
					System.exit(0);
				}
			}
		} else {
			saveSettings();
			System.exit(0);
		}
	}
	
	public static void setWordInterval(int hours, int minutes, int seconds) {
		wordInterval.set(0, 0, 0, hours, minutes, seconds);
	}

	public static int getWordInterval() {
		int suma = wordInterval.get(Calendar.HOUR_OF_DAY) * 3600;
		suma += wordInterval.get(Calendar.MINUTE) * 60;
		suma += wordInterval.get(Calendar.SECOND);
		return suma;
	}
	
	public static int getWordIntervalHours() {
		return wordInterval.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getWordIntervalMinutes() {
		return wordInterval.get(Calendar.MINUTE);
	}
	
	public static int getWordIntervalSeconds() {
		return wordInterval.get(Calendar.SECOND);
	}
	
	private void createPdf(File file) throws FileNotFoundException, SecurityException, DocumentException {		
		Document document = new Document(PageSize.A4.rotate(), 15, 15, 15, 15);
		SetTableModel curTM = (SetTableModel) cbSets.getSelectedItem();

		String filePath = file.getParent() + "\\" + makeExtension(file.getName(), ".pdf");
		PdfWriter.getInstance(document, new FileOutputStream(filePath));
		document.open();
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		Font fnt = FontFactory.getFont("/Resources/OpenSans-Semibold.ttf", BaseFont.IDENTITY_H, true); 

		final int TAB_WIDTH = 6;
		final int TAB_HEIGHT = 7;
		final float CELL_HEIGHT = 80.0f;
		final float CELL_PADDING = 15.0f;
		int wordCount = curTM.getRowCount();
		int oneTabSize = TAB_WIDTH * TAB_HEIGHT;
		int tabCount = wordCount / oneTabSize;
		if (wordCount % oneTabSize > 0)
			tabCount++;
		int[] idx = new int[2];
		
		// utworzenie tabel
		// pêtla dla ka¿dej nowej pary tabel
		for (int i = 0; i < tabCount; i++) {
			
			// pêtla dla pierwszego z dwóch jêzyków
			// pêtla dla ka¿dego z wierszy tabeli
			for (int j = 0; j < TAB_HEIGHT; j++) {
				
				// pêtla dla ka¿dego jednego s³ówka (pojedynczej komórki tabeli)
				for (int k = 0; k < TAB_WIDTH; k++) {
					String txtToAdd = "";
					if (idx[SetTableModel.FIRST_LANG] < wordCount) {
						txtToAdd = (String) curTM.getValueAt(idx[SetTableModel.FIRST_LANG], SetTableModel.FIRST_LANG);
						idx[SetTableModel.FIRST_LANG]++;
					}
					
					PdfPCell cell = new PdfPCell(new Phrase(txtToAdd, fnt));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setPadding(CELL_PADDING);
					cell.setFixedHeight(CELL_HEIGHT);
					cell.setBorderColor(BaseColor.LIGHT_GRAY);
					table.addCell(cell);
				}
			}
			
			// pêtla dla drugiego z dwóch jêzyków
			// pêtla dla ka¿dego z wierszy tabeli
			for (int j = 0; j < TAB_HEIGHT; j++) {
				
				// pêtla dla ka¿dego jednego s³ówka (pojedynczej komórki tabeli)
				for (int k = idx[SetTableModel.SECOND_LANG] + TAB_WIDTH - 1, stop = idx[SetTableModel.SECOND_LANG]; k >= stop; k--) {
					String txtToAdd = "";
					if (k < wordCount) {
						txtToAdd = (String) curTM.getValueAt(k, SetTableModel.SECOND_LANG);
						idx[SetTableModel.SECOND_LANG]++;
					}
					PdfPCell cell = new PdfPCell(new Phrase(txtToAdd, fnt));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setPadding(CELL_PADDING);
					cell.setFixedHeight(CELL_HEIGHT);
					cell.setBorderColor(BaseColor.LIGHT_GRAY);
					table.addCell(cell);
				}
			}
		}
		
		document.add(table);
		document.close();
	}
	
	public SetTableModel addSet(String setName, String[] cols) {
		SetTableModel newSTM = new SetTableModel(setName);
		String[] colNames = {cols[SetTableModel.FIRST_LANG], cols[SetTableModel.SECOND_LANG], "Znane"};
		newSTM.setColumnsNames(colNames);
		cbSets.addItem(newSTM);
		cbSets.setSelectedItem(newSTM);
		tabWords.setModel((SetTableModel) cbSets.getSelectedItem());
		
		return (SetTableModel) cbSets.getSelectedItem();
	}
	
	private void addWord(Object[] newWord) {
		((SetTableModel)cbSets.getSelectedItem()).addData(newWord);
		((SetTableModel)cbSets.getSelectedItem()).fireTableDataChanged();
	}
	
	public void showError(Throwable e) {
		JOptionPane.showMessageDialog(MainWindow.this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
	}
	
	public void showError(String e) {
		JOptionPane.showMessageDialog(MainWindow.this, e, "B³¹d", JOptionPane.ERROR_MESSAGE);
	}
	
	private void setBtnAddWordEnabled() {
		btnAddWord.setEnabled(!(htfNew1.getText().isEmpty() | htfNew2.getText().isEmpty() | cbSets.getSelectedIndex() == -1));
	}
	
	private void setLastColWidth() {
		tabWords.getColumnModel().getColumn(SetTableModel.KNOWN).setMaxWidth(40);
	}
	
	private void startEFiszki(boolean checkYourself) {
		MainWindow.checkYourself = checkYourself;
		
		// menu
		miStopEFiszki.setVisible(true);
		miCheckYourself.setVisible(false);
		miLearn.setVisible(false);
		
		// tray
		pmTray.insert(tmiStopEFiszki, 0);
		pmTray.remove(tmnRunEFiszki);
		
		winWordCheck.runEFiszki((SetTableModel)cbSets.getSelectedItem());
	}
	
	private void endEFiszki() {
		winWordCheck.stopEFiszki();
	}
	
	public void returnComponentsDefaults() {
		// menu
		miStopEFiszki.setVisible(false);
		miCheckYourself.setVisible(true);
		miLearn.setVisible(true);
		
		// tray
		pmTray.remove(tmiStopEFiszki);
		pmTray.insert(tmnRunEFiszki, 0);
		tmnRunEFiszki.setEnabled(true);
	}
	
	private void loadSettings() {
		final String MAIN_WINDOW_HEIGHT = "MAIN_WINDOW_HEIGHT";
		final String MAIN_WINDOW_WIDTH = "MAIN_WINDOW_WIDTH";
		final String MAIN_WINDOW_X = "MAIN_WINDOW_X";
		final String MAIN_WINDOW_Y = "MAIN_WINDOW_Y";
		final String WORD_CHECK_WINDOW_X = "WORD_CHECK_WINDOW_X";
		final String WORD_CHECK_WINDOW_Y = "WORD_CHECK_WINDOW_Y";
		final String HIDE_KNOWN = "HIDE_KNOWN";
		final String SHOW_TRANSLATION = "SHOW_TRANSLATION";
		final String SAVE_WITHOUT_ASKING_ON_EXIT = "SAVE_WITHOUT_ASKING_ON_EXIT";
		final String REMEMBER_LAST_SETS = "REMEMBER_LAST_SETS";
		final String USE_ADVANCED_LEARNING_MODE = "USE_ADVANCED_LEARNING_MODE";
		final String SWAP_LANGUAGES = "SWAP_LANGUAGES";
		final String WORD_INTERVAL_S = "WORD_INTERVAL_S";
		final String WORD_INTERVAL_M = "WORD_INTERVAL_M";
		final String WORD_INTERVAL_H = "WORD_INTERVAL_H";
		final String FC_OPEN_SET_PATH = "FC_OPEN_SET_PATH";
		final String FC_SAVE_SET_PATH = "FC_SAVE_SET_PATH";
		final String FC_IMPORT_WORDS_PATH = "FC_IMPORT_WORDS_PATH";
		final String FC_EXPORT_WORDS_PATH = "FC_EXPORT_WORDS_PATH";
		final String FC_CREATE_PDF_PATH = "FC_CREATE_PDF_PATH";
		final String[] RECENT_FILES = {"RECENT_FILE_1", "RECENT_FILE_2", "RECENT_FILE_3", "RECENT_FILE_4", "RECENT_FILE_5"};
		
		File settingsFile = new File(/*FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Fiszki\\*/"FiszkiSettings.txt");
		if (!settingsFile.exists())
			return;
		
		Properties props = new Properties();

		try {
			InputStream in = new FileInputStream(settingsFile);
			props.load(in);
			
			// --------------------------------------------
			int mwHeight = getHeight();
			String property = props.getProperty(MAIN_WINDOW_HEIGHT);
			if (property != null)
				mwHeight = Integer.parseInt(property);
			
			int mwWidth = getWidth();
			property = props.getProperty(MAIN_WINDOW_WIDTH);
			if (property != null)
				mwWidth = Integer.parseInt(property);
			
			int mwX = getX();
			property = props.getProperty(MAIN_WINDOW_X);
			if (property != null)
				mwX = Integer.parseInt(property);
			
			int mwY = getY();
			property = props.getProperty(MAIN_WINDOW_Y);
			if (property != null)
				mwY = Integer.parseInt(property);
			
			setBounds(mwX, mwY, mwWidth, mwHeight);
			// --------------------------------------------
			
			// --------------------------------------------
			int wcwX = winWordCheck.getX();
			property = props.getProperty(WORD_CHECK_WINDOW_X);
			if (property != null)
				wcwX = Integer.parseInt(property);
			
			int wcwY = winWordCheck.getY();
			property = props.getProperty(WORD_CHECK_WINDOW_Y);
			if (property != null)
				wcwY = Integer.parseInt(property);
			
			winWordCheck.setLocation(wcwX, wcwY);
			// --------------------------------------------
			
			property = props.getProperty(HIDE_KNOWN);
			if (property != null)
				hideKnown = Boolean.parseBoolean(property);
			
			property = props.getProperty(SHOW_TRANSLATION);
			if (property != null)
				showTranslation = Boolean.parseBoolean(property);
			
			property = props.getProperty(SAVE_WITHOUT_ASKING_ON_EXIT);
			if (property != null)
				saveWithoutAskingOnExit = Boolean.parseBoolean(property);
			
			property = props.getProperty(REMEMBER_LAST_SETS);
			if (property != null)
				rememberLastSets = Boolean.parseBoolean(property);
			
			property = props.getProperty(USE_ADVANCED_LEARNING_MODE);
			if (property != null)
				useAdvancedLearningMode = Boolean.parseBoolean(property);
			
			property = props.getProperty(SWAP_LANGUAGES);
			if (property != null)
				swapLanguages = Boolean.parseBoolean(property);
				
			// --------------------------------------------
			int sec = getWordIntervalSeconds();
			int min = getWordIntervalMinutes();
			int hrs = getWordIntervalHours();
			
			property = props.getProperty(WORD_INTERVAL_S);
			if (property != null)
				sec = Integer.parseInt(property);
			
			property = props.getProperty(WORD_INTERVAL_M);
			if (property != null)
				min = Integer.parseInt(property);
			
			property = props.getProperty(WORD_INTERVAL_H);
			if (property != null)
				hrs = Integer.parseInt(property);
			
			setWordInterval(hrs, min, sec);
			// --------------------------------------------
			
			property = props.getProperty(FC_OPEN_SET_PATH);
			if (property != null)
				fcOpenSet.setCurrentDirectory(new File(property));
			
			property = props.getProperty(FC_SAVE_SET_PATH);
			if (property != null)
				fcSaveSet.setCurrentDirectory(new File(property));
			
			property = props.getProperty(FC_IMPORT_WORDS_PATH);
			if (property != null)
				fcImportWords.setCurrentDirectory(new File(property));
			
			property = props.getProperty(FC_EXPORT_WORDS_PATH);
			if (property != null)
				fcExportWords.setCurrentDirectory(new File(property));
			
			property = props.getProperty(FC_CREATE_PDF_PATH);
			if (property != null)
				fcCreatePdf.setCurrentDirectory(new File(property));
			
			// --------------------------------------------
			for (int i = RECENT_FILES_COUNT - 1; i >= 0; i--) {
				String pathToAdd = props.getProperty(RECENT_FILES[i], "");
				recentFilesList.add(pathToAdd);
				addRecentFile(pathToAdd);
			}
			// --------------------------------------------
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveSettings() {
		final String MAIN_WINDOW_HEIGHT = "MAIN_WINDOW_HEIGHT";
		final String MAIN_WINDOW_WIDTH = "MAIN_WINDOW_WIDTH";
		final String MAIN_WINDOW_X = "MAIN_WINDOW_X";
		final String MAIN_WINDOW_Y = "MAIN_WINDOW_Y";
		final String WORD_CHECK_WINDOW_X = "WORD_CHECK_WINDOW_X";
		final String WORD_CHECK_WINDOW_Y = "WORD_CHECK_WINDOW_Y";
		final String HIDE_KNOWN = "HIDE_KNOWN";
		final String SHOW_TRANSLATION = "SHOW_TRANSLATION";
		final String SAVE_WITHOUT_ASKING_ON_EXIT = "SAVE_WITHOUT_ASKING_ON_EXIT";
		final String REMEMBER_LAST_SETS = "REMEMBER_LAST_SETS";
		final String USE_ADVANCED_LEARNING_MODE = "USE_ADVANCED_LEARNING_MODE";
		final String SWAP_LANGUAGES = "SWAP_LANGUAGES";
		final String WORD_INTERVAL_S = "WORD_INTERVAL_S";
		final String WORD_INTERVAL_M = "WORD_INTERVAL_M";
		final String WORD_INTERVAL_H = "WORD_INTERVAL_H";
		final String FC_OPEN_SET_PATH = "FC_OPEN_SET_PATH";
		final String FC_SAVE_SET_PATH = "FC_SAVE_SET_PATH";
		final String FC_IMPORT_WORDS_PATH = "FC_IMPORT_WORDS_PATH";
		final String FC_EXPORT_WORDS_PATH = "FC_EXPORT_WORDS_PATH";
		final String FC_CREATE_PDF_PATH = "FC_CREATE_PDF_PATH";
		final String[] RECENT_FILES = {"RECENT_FILE_1", "RECENT_FILE_2", "RECENT_FILE_3", "RECENT_FILE_4", "RECENT_FILE_5"};
        
		try {
	        Properties props = new Properties();
	        props.setProperty(MAIN_WINDOW_HEIGHT, Integer.toString(this.getHeight()));
	        props.setProperty(MAIN_WINDOW_WIDTH, Integer.toString(this.getWidth()));
	        props.setProperty(MAIN_WINDOW_X, Integer.toString(getX()));
	        props.setProperty(MAIN_WINDOW_Y, Integer.toString(getY()));
	        props.setProperty(WORD_CHECK_WINDOW_X, Integer.toString(winWordCheck.getX()));
	        props.setProperty(WORD_CHECK_WINDOW_Y, Integer.toString(winWordCheck.getY()));
	        props.setProperty(HIDE_KNOWN, Boolean.toString(hideKnown));
	        props.setProperty(SHOW_TRANSLATION, Boolean.toString(showTranslation));
	        props.setProperty(SAVE_WITHOUT_ASKING_ON_EXIT, Boolean.toString(saveWithoutAskingOnExit));
	        props.setProperty(REMEMBER_LAST_SETS, Boolean.toString(rememberLastSets));
	        props.setProperty(USE_ADVANCED_LEARNING_MODE, Boolean.toString(useAdvancedLearningMode));
	        props.setProperty(SWAP_LANGUAGES, Boolean.toString(swapLanguages));
	        props.setProperty(WORD_INTERVAL_S, Integer.toString(getWordIntervalSeconds()));
	        props.setProperty(WORD_INTERVAL_M, Integer.toString(getWordIntervalMinutes()));
	        props.setProperty(WORD_INTERVAL_H, Integer.toString(getWordIntervalHours()));
	        props.setProperty(FC_OPEN_SET_PATH, fcOpenSet.getCurrentDirectory().getAbsolutePath());
	        props.setProperty(FC_SAVE_SET_PATH, fcSaveSet.getCurrentDirectory().getAbsolutePath());
	        props.setProperty(FC_IMPORT_WORDS_PATH, fcImportWords.getCurrentDirectory().getAbsolutePath());
	        props.setProperty(FC_EXPORT_WORDS_PATH, fcExportWords.getCurrentDirectory().getAbsolutePath());
	        props.setProperty(FC_CREATE_PDF_PATH, fcCreatePdf.getCurrentDirectory().getAbsolutePath());
	        for (int i = 0; i < RECENT_FILES_COUNT; i++)
	        	props.setProperty(RECENT_FILES[i], recentFilesList.get(i));
	        
	        File settingsFile = new File(/*FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Fiszki\\*/"FiszkiSettings.txt");
	        OutputStream out = new FileOutputStream(settingsFile);
	        props.store(out, "Ustawienia programu Fiszki, Copyright 2016 Maciej Znamirowski");
	    }
	    catch (Exception e ) {
	        e.printStackTrace();
	    }
		
		final String OPENED_SETS_COUNT = "OPENED_SETS_COUNT";
		final String SET_NO_ = "SET_NO_";
		
		// otwarte zestawy
		try {
	        Properties props = new Properties();
	        props.setProperty(OPENED_SETS_COUNT, Integer.toString(cbSets.getItemCount()));
	        
	        for (int i = 0; i < cbSets.getItemCount(); i++)
	        	props.setProperty(SET_NO_ + Integer.toString(i), ((SetTableModel)cbSets.getItemAt(i)).getDiskPath());
	        
	        File setsFile = new File(/*FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Fiszki\\*/"FiszkiLastSets.txt");
	        OutputStream out = new FileOutputStream(setsFile);
	        props.store(out, "Otwarte zestawy Fiszek, Copyright 2016 Maciej Znamirowski");
	    }
	    catch (Exception e ) {
	        e.printStackTrace();
	    }
	}
	
	private boolean openSet(File fileToOpen) {
		try (BufferedReader reader = Files.newBufferedReader(fileToOpen.toPath(), charset)) {
		    String line = reader.readLine();
		    String name = line;
		    line = reader.readLine();
		    
		    // dodanie nowego zestawu z pliku
		    String[] cols = line.split(";");
		    addSet(name, cols).setDiskPath(fileToOpen.getAbsolutePath());
		    ((SetTableModel)cbSets.getSelectedItem()).setEdited(false);
		    cbSets.setSelectedIndex(cbSets.getSelectedIndex());
		    
		    while ((line = reader.readLine()) != null) {
		    	String[] words = line.split(";");
		        Object[] toAdd = {words[SetTableModel.FIRST_LANG], words[SetTableModel.SECOND_LANG], Boolean.parseBoolean(words[SetTableModel.KNOWN])};
		        addWord(toAdd);
		    }
		} catch (IOException x) {
//			showError(x);
			return false;
		}

		return true;
	}
	
	private boolean saveSet(File fileToSave) {
		SetTableModel curTM = (SetTableModel) cbSets.getSelectedItem();
		String fileName = fileToSave.getName();
		Path filePath = Paths.get(fileToSave.getParent() + "\\" + makeExtension(fileName, ".f"));
		
		try (BufferedWriter writer = Files.newBufferedWriter(filePath, charset)) {
			writer.write(curTM.getName());
			writer.newLine();
		    writer.append(curTM.getColumnName(SetTableModel.FIRST_LANG) + ";" + curTM.getColumnName(SetTableModel.SECOND_LANG) + ";" + curTM.getColumnName(SetTableModel.KNOWN));
		    
		    for (Object[] toSave : curTM.getData()) {
		    	writer.newLine();
		    	writer.append(toSave[SetTableModel.FIRST_LANG] + ";" + toSave[SetTableModel.SECOND_LANG] + ";" + toSave[SetTableModel.KNOWN]);
		    }
		} catch (IOException ex) {
			showError(ex);
			return false;
		}
		
		return true;
	}
	
	private boolean exportSet(File fileToSave) {
		SetTableModel curTbModel = (SetTableModel) cbSets.getSelectedItem();
		String fileName = fileToSave.getName();
		Path filePath = Paths.get(fileToSave.getParent() + "\\" + makeExtension(fileName, ".csv"));
		
		try (BufferedWriter writer = Files.newBufferedWriter(filePath, charset)) {
		    for (Object[] toSave : curTbModel.getData()) {
		    	writer.append(toSave[SetTableModel.FIRST_LANG] + ";" + toSave[SetTableModel.SECOND_LANG]);
		    	writer.newLine();
		    }
		} catch (IOException ex) {
			showError(ex);
			return false;
		}
		
		return true;
	}
	
	private void addRecentFile(String newPath) {
		if (newPath.isEmpty())
			return;
		
		int idx = recentFilesList.indexOf(newPath);
		int i;
		
		if (idx != -1)
			i = idx;
		else
			i = RECENT_FILES_COUNT - 1;
		
		// jeœli dana œcie¿ka ju¿ jest na liœcie to przesuniêcie jej na górê listy
		// jeœli nie, to przesuniêcie wszystkich œcie¿ek w dó³ listy (zrobienie miejsca na now¹)
		for (; i > 0; i--)
			recentFilesList.set(i, recentFilesList.get(i - 1));

		recentFilesList.set(0, newPath);
		
		for (JMenuItem toRemove : recentFilesMI)
			mnOpen.remove(toRemove);
		
		recentFilesMI.clear();
		
		for (int j = 0; j < RECENT_FILES_COUNT; j++) {
			String pathToAdd = recentFilesList.get(j);
			if (pathToAdd.isEmpty())
				break;
			
			JMenuItem miToAdd = new JMenuItem(Integer.toString(j + 1) + ": " + pathToAdd);
			miToAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					File file = new File(pathToAdd);
//					String path = file.getAbsolutePath();
					
					if (fileIsOpened(pathToAdd)) {
		            	showError("Plik " + pathToAdd + " jest ju¿ otwarty.");
	            		return;
		            }
					
					if (!openSet(file))
		            	showError("Plik " + pathToAdd + " nie istnieje.");
					else {
						addRecentFile(pathToAdd);
						refreshAppStates();
					}
				}
			});
			mnOpen.add(miToAdd, -1);
			recentFilesMI.add(miToAdd);
		}
	}
	
	private String makeExtension(String fileName, String ext) {
		if (fileName.length() > ext.length() && fileName.endsWith(ext))
			return fileName;
		else
			return fileName + ext;
	}
	
	private boolean fileIsOpened(String path) {
		for (int i = cbSets.getItemCount() - 1; i >= 0; i--) {
        	if (((SetTableModel)cbSets.getItemAt(i)).getDiskPath().equals(path))
        		return true;
        }
		
		return false;
	}
	
	public void refreshAppStates() {
		if (cbSets.getSelectedIndex() == -1) {
			setTitle("Fiszki");
			return;
		}
		
		SetTableModel curTM = (SetTableModel) cbSets.getSelectedItem();
		if (curTM.getDiskPath().isEmpty())
			setTitle("Fiszki - *" + curTM.getName());
		else
			setTitle("Fiszki - " + (curTM.isEdited() ? "*" : "") + curTM.getDiskPath());
		
		if (curTM.isEdited())
			miSave.setEnabled(true);
		else
			miSave.setEnabled(false);
	}
	
	private boolean saveAsInterface() {
		int returnVal = fcSaveSet.showSaveDialog(MainWindow.this);
		
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return false;
		
		File fileToSave = fcSaveSet.getSelectedFile();
		
		if (saveInterface(fileToSave))
			return true;
		else
			return false;
	}
	
	private boolean saveInterface(File file) {
		File fileToSave;
		
		if (file == null)
			fileToSave = new File(((SetTableModel)cbSets.getSelectedItem()).getDiskPath());
		else
			fileToSave = file;
		
		if (saveSet(fileToSave)) {
			String filePath = fileToSave.getParent() + "\\" + makeExtension(fileToSave.getName(), ".f");
			addRecentFile(filePath);
			((SetTableModel) cbSets.getSelectedItem()).setDiskPath(filePath);
			((SetTableModel) cbSets.getSelectedItem()).setEdited(false);
			refreshAppStates();
			return true;
		} else
			return false;
	}
	
	private boolean thereAreUnsavedSets() {
		for (int i = cbSets.getItemCount() - 1; i >= 0; i--) {
			if (((SetTableModel)cbSets.getItemAt(i)).isEdited())
				return true;
		}
		
		return false;
	}
	
	private boolean saveUnsavedSets() {
		for (int i = cbSets.getItemCount() - 1; i >= 0; i--) {
			cbSets.setSelectedIndex(i);
			SetTableModel curTM = (SetTableModel) cbSets.getSelectedItem();
			if (curTM.isEdited()) {
				if (curTM.getDiskPath().isEmpty()) {
					if (!saveAsInterface())
						return false;
				} else {
					if (!saveInterface(new File(curTM.getDiskPath())))
						return false;
				}
			}
		}
		
		return true;
	}
	
	private void loadLastSets() {
		final String OPENED_SETS_COUNT = "OPENED_SETS_COUNT";
		final String SET_NO_ = "SET_NO_";
		
		File setsFile = new File(/*FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Fiszki\\*/"FiszkiLastSets.txt");
		if (!setsFile.exists())
			return;
		
		Properties props = new Properties();

		try {
			InputStream in = new FileInputStream(setsFile);
			props.load(in);

			
			int openedSetsCount = 0;
			String property = props.getProperty(OPENED_SETS_COUNT);
			if (property != null)
				openedSetsCount = Integer.parseInt(property);
			
			for (int i = 0; i < openedSetsCount; i++) {
				property = props.getProperty(SET_NO_ + Integer.toString(i));
				if (property != null) {
					if (!openSet(new File(property)))
		            	showError("Plik " + property + " nie istnieje.");
					else {
						addRecentFile(property);
						refreshAppStates();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}