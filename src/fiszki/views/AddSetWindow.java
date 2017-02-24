package fiszki.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Insets;

public class AddSetWindow extends JDialog {
	private static final long serialVersionUID = 7L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfSetName;
	private JTextField tfFirstLang;
	private JTextField tfSecondLang;
	private JButton btnAdd;
	private JButton btnCancel;
	private MainWindow winMain;

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
			AddSetWindow dialog = new AddSetWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddSetWindow() {
		initComponents();
		createEvents();
	}

	private void initComponents() {
		setModal(true);
		setTitle("Dodawanie nowego zestawu");
		setIconImage(MainWindow.FISZKI_ICON_16);
		setBounds(100, 100, 287, 171);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblSetName = new JLabel("Nazwa zestawu:");
		
		tfSetName = new JTextField();		
		tfSetName.setMargin(new Insets(2, 6, 2, 2));
		tfSetName.setColumns(10);
		
		JLabel lblFirstLang = new JLabel("Pierwszy j\u0119zyk:");
		
		tfFirstLang = new JTextField();
		tfFirstLang.setMargin(new Insets(2, 6, 2, 2));
		tfFirstLang.setColumns(10);
		
		JLabel lblSecondLang = new JLabel("Drugi j\u0119zyk:");
		
		tfSecondLang = new JTextField();
		tfSecondLang.setMargin(new Insets(2, 6, 2, 2));
		tfSecondLang.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSetName)
						.addComponent(lblFirstLang)
						.addComponent(lblSecondLang))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(tfFirstLang, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
						.addComponent(tfSetName, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
						.addComponent(tfSecondLang, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSetName)
						.addComponent(tfSetName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfFirstLang, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFirstLang))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfSecondLang, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSecondLang))
					.addContainerGap(136, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnAdd = new JButton("Dodaj");
				btnAdd.setEnabled(false);
				btnAdd.setActionCommand("");
				buttonPane.add(btnAdd);
				getRootPane().setDefaultButton(btnAdd);
			}
			{
				btnCancel = new JButton("Anuluj");
				btnCancel.setActionCommand("Cancel");
				buttonPane.add(btnCancel);
			}
		}
	}

	private void createEvents() {
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent arg0) {
				clean();
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [] cols = {tfFirstLang.getText(), tfSecondLang.getText()};
				winMain.addSet(tfSetName.getText(), cols);
				setVisible(false);
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		tfSetName.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				setBtnAddEnabled();
			}
		});
		
		tfFirstLang.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				setBtnAddEnabled();
			}
		});
		
		tfSecondLang.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				setBtnAddEnabled();
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
	}

	private void clean() {
		tfSetName.setText("");
		tfFirstLang.setText("");
		tfSecondLang.setText("");
		tfSetName.requestFocus();
	}
	
	private void setBtnAddEnabled() {
		btnAdd.setEnabled(!(tfSetName.getText().isEmpty() | tfFirstLang.getText().isEmpty() | tfSecondLang.getText().isEmpty()));
	}
	
	public void showWindow(MainWindow parent) {
		winMain = parent;
		setVisible(true);
	}
}
