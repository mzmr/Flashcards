package fiszki.views;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class AboutWindow extends JDialog {
	private static final long serialVersionUID = 4L;
	
	// komponenty
	private JButton btnClose;

	
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
					AboutWindow dialog = new AboutWindow();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
	public AboutWindow() {
		initComponents();
		createEvents();
	}

	private void initComponents() {
		setModal(true);
		setTitle("O programie");
		setIconImage(MainWindow.FISZKI_ICON_16);
		setResizable(false);
		setBounds(100, 100, 279, 220);
		
		JLabel lblName = new JLabel("Fiszki");
		lblName.setIconTextGap(6);
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblName.setIcon(new ImageIcon(AboutWindow.class.getResource("/fiszki/resources/img/ikona_20.png")));
		
		btnClose = new JButton("Zamknij");
		
		JLabel lblVersion = new JLabel("Wersja 1.1");
		lblVersion.setIconTextGap(6);
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblVersion.setIcon(new ImageIcon(AboutWindow.class.getResource("/fiszki/resources/img/hashtag_20.png")));
		
		JLabel lblAuthor = new JLabel("Maciej Znamirowski");
		lblAuthor.setIconTextGap(6);
		lblAuthor.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAuthor.setIcon(new ImageIcon(AboutWindow.class.getResource("/fiszki/resources/img/head_20.png")));
		
		JLabel lblEmail = new JLabel("maciek@znamirowski.pl");
		lblEmail.setIconTextGap(6);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblEmail.setIcon(new ImageIcon(AboutWindow.class.getResource("/fiszki/resources/img/email_20.png")));
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(102)
							.addComponent(btnClose))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(100)
							.addComponent(lblName))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(55)
							.addComponent(lblVersion))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(55)
							.addComponent(lblAuthor))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(55)
							.addComponent(lblEmail)))
					.addContainerGap(55, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblName)
					.addGap(18)
					.addComponent(lblVersion)
					.addGap(11)
					.addComponent(lblAuthor)
					.addGap(11)
					.addComponent(lblEmail)
					.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
					.addComponent(btnClose)
					.addContainerGap())
		);
		groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {lblVersion, lblAuthor, lblEmail});
		getContentPane().setLayout(groupLayout);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnClose, getContentPane(), lblName}));
	}

	private void createEvents() {
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
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
}
