package views;

import i18n.Messages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import viewModels.BookTableModel;
import viewModels.CustomerTableModel;
import viewModels.LoanTableModel;
import domain.Library;

public class MainView extends javax.swing.JFrame implements Observer {
	private static final long serialVersionUID = 1L;

	private JTabbedPane tbsMain;
	
	private BooksTab booksTab;
	private LoansTab loansTab;
	private CustomerTab customerTab;
	
	// Menu
	private JMenuBar jMenuBar;
	private JMenu fileMenu;
	private JMenuItem fileExit;
	
	private JMenu helpMenu;
	private JMenuItem helpAbout;
	private JMenuItem helpHelp;
	
	private Library library;
	
	// Models
	private BookTableModel bookTableModel;
	private LoanTableModel loanTableModel;
	private CustomerTableModel customerTableModel;
	private JPanel pnlMainButtons;
	private JButton btnCloseApp;
	
	private static MainView mainFrame;
	
	/**
	 * Create the application.
	 * @param library
	 * @author PCHR
	 */
	public MainView( Library library ) {
		/*
		 * This View should listen to changes in the book list and the loans list!
		 */
		super();
		this.library = library;
		bookTableModel = new BookTableModel( this.library );
		loanTableModel = new LoanTableModel( this.library );
		customerTableModel = new CustomerTableModel( library.getCustomerList() );
		
		mainFrame = this;
		
		initialize();
		library.addObserver( this );
		setLocationRelativeTo(null);
	}

	/**
	 * Initialize the contents of the frames.
	 * @author PFORSTER
	 * @author PCHR
	 */
	private void initialize() {
		try {
			
			setMinimumSize( new Dimension(800, 600) );
			
			///////////////////////////////////////////////////////////////////
			// ACTIONS
			///////////////////////////////////////////////////////////////////
			// CLOSE
			AbstractAction close = new CloseAction(
				Messages.getString( "MainView.btnExit.text"),
				Messages.getString( "MainView.btnExit.desc")
			);
			// ABOUT
			AbstractAction about = new AboutAction(
				Messages.getString("MainView.Menu.Help.about"),
				Messages.getString("MainView.Menu.Help.aboutDesc")
			);
			// HELP
			AbstractAction help = new HelpAction(
				Messages.getString("MainView.Menu.Help.help"),
				Messages.getString("MainView.Menu.Help.helpDesc")
			);

			///////////////////////////////////////////////////////////////////
			// MENU
			///////////////////////////////////////////////////////////////////
			jMenuBar = new JMenuBar();
			getContentPane().add( jMenuBar, BorderLayout.NORTH );
			
			fileMenu = new JMenu( Messages.getString("MainView.Menu.File.title") );
			fileMenu.setMnemonic('E');
			jMenuBar.add( fileMenu );
			{
				fileExit = new JMenuItem( close );
				fileExit.setIcon( new ImageIcon("icons/close_16.png") );
				fileExit.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_X, ActionEvent.CTRL_MASK));
				fileMenu.add( fileExit );
			}
			
			helpMenu = new JMenu( Messages.getString("MainView.Menu.Help.title") );
			helpMenu.setMnemonic('H');
			jMenuBar.add( helpMenu );
			{
				helpAbout = new JMenuItem( about );
				helpAbout.setIcon( new ImageIcon("icons/heart_16.png") );
				helpAbout.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_A, ActionEvent.CTRL_MASK));
				helpMenu.add( helpAbout );
				
				helpHelp = new JMenuItem( help );
				helpHelp.setIcon( new ImageIcon("icons/info_button_16.png") );
				helpHelp.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_H, ActionEvent.CTRL_MASK));
				helpMenu.add( helpHelp );
			}
			
			///////////////////////////////////////////////////////////////////
			// INITIAL SETUP
			///////////////////////////////////////////////////////////////////
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle(Messages.getString("MainView.title"));
			this.setBounds(100, 100, 700, 550);
			
			pnlMainButtons = new JPanel();
			FlowLayout fl_pnlMainButtons = (FlowLayout) pnlMainButtons.getLayout();
			fl_pnlMainButtons.setAlignment(FlowLayout.RIGHT);
			getContentPane().add(pnlMainButtons, BorderLayout.SOUTH);
			
			btnCloseApp = new JButton( close );
			btnCloseApp.setIcon( new ImageIcon("icons/close_32.png") );
			pnlMainButtons.add(btnCloseApp);
			
			tbsMain = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(tbsMain, BorderLayout.CENTER);
			
			///////////////////////////////////////////////////////////////////
			// BOOKS TAB
			///////////////////////////////////////////////////////////////////
			booksTab = new BooksTab( bookTableModel, library );
			tbsMain.addTab(
				Messages.getString("BookMaster.Tab.Books"),
				new ImageIcon("icons/book_32.png"),
				booksTab, 
				Messages.getString("BookMaster.Tab.Books.desc") );
			tbsMain.setMnemonicAt(0, KeyEvent.VK_1);
			
			///////////////////////////////////////////////////////////////////
			// LOANS TAB
			///////////////////////////////////////////////////////////////////
			loansTab = new LoansTab( loanTableModel, library );
			tbsMain.addTab(
				Messages.getString("BookMaster.Tab.Loans"),
				new ImageIcon("icons/basket_32.png"),
				loansTab,
				Messages.getString("BookMaster.Tab.Loans.desc")
			);
			tbsMain.setMnemonicAt(1, KeyEvent.VK_2);
			
			///////////////////////////////////////////////////////////////////
			// CUSTOMERS TAB
			///////////////////////////////////////////////////////////////////
			customerTab = new CustomerTab(customerTableModel, library);
			tbsMain.addTab(
				Messages.getString( "MainView.Tab.Customers"),
				new ImageIcon("icons/user_32.png"),
				customerTab,
				Messages.getString( "MainView.Tab.Customers.desc")
			);
			tbsMain.setMnemonicAt(2, KeyEvent.VK_3);
						
			///////////////////////////////////////////////////////////////////
			// Initialize the buttons, actions, etc.
			///////////////////////////////////////////////////////////////////
			updateButtons();
			updateStatistics();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		updateButtons();
		updateStatistics();
	}
		
	/**
	 * Updates the labels that contain statistical information.
	 * @author PCHR
	 */
	private void updateStatistics() {
		booksTab.updateStatistics();
		loansTab.updateStatistics();
		customerTab.updateStatistics();
	}
	
	/**
	 * Updates all buttons on the view
	 * @author PCHR
	 */
	public void updateButtons() {
		booksTab.updateListButtons();
		loansTab.updateListButtons();
		customerTab.updateListButtons();
	}
	
	/////////////////////////////////////////////////
	// Action Subclasses
	/////////////////////////////////////////////////
	/**
	 * Closes the current dialog.
	 * @author PCHR
	 */
	class CloseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public CloseAction( String text, String desc ) {
			super( text );
			putValue( SHORT_DESCRIPTION, desc );
			putValue( MNEMONIC_KEY, KeyEvent.VK_X );
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	class AboutAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public AboutAction( String text, String desc ) {
			super( text );
			putValue( SHORT_DESCRIPTION, desc );
			putValue( MNEMONIC_KEY, KeyEvent.VK_A );
		}
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog( mainFrame,
			    Messages.getString("MainView.AboutDlg.text"),
			    Messages.getString("MainView.AboutDlg.title"),
			    JOptionPane.PLAIN_MESSAGE
			);
		}
	}
	class HelpAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public HelpAction( String text, String desc ) {
			super( text );
			putValue( SHORT_DESCRIPTION, desc );
			putValue( MNEMONIC_KEY, KeyEvent.VK_H );
		}
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog( mainFrame,
			    Messages.getString("MainView.HelpDlg.text"),
			    Messages.getString("MainView.HelpDlg.title"),
			    JOptionPane.PLAIN_MESSAGE
			);
		}
	}
}
