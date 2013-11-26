package views;

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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import viewModels.BookTableModel;
import viewModels.CustomerTableModel;
import viewModels.LoanTableModel;
import domain.Library;

public class MainView extends javax.swing.JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tbsMain;
	private JMenu viewMenu;
	private JMenuBar jMenuBar;
	
	private JCheckBoxMenuItem showUnavailableMenuItem;
	
	private JMenu loansSubMenu;
	private JMenuItem loansShowAll;
	private JMenuItem loansShowLentOnly;
	private JMenuItem loansShowOverdueOnly;
	
	private BooksTab booksTab;
	private LoansTab loansTab;
	private CustomerTab customerTab;
	
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
		 * Also: customerList
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
		setVisible(true);
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
			AbstractAction close = new CloseAction( Messages.getString( "MainView.btnExit.text"), "Close the application, disregard all unsaved changes" );

			///////////////////////////////////////////////////////////////////
			// INITIAL SETUP
			///////////////////////////////////////////////////////////////////
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle(Messages.getString("BookMaster.frmTodoTitle.title"));
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
			tbsMain.addTab( Messages.getString("BookMaster.Tab.Books" ), new ImageIcon("icons/book_32.png"), booksTab, null );
			tbsMain.setMnemonicAt(0, KeyEvent.VK_1);
			
			///////////////////////////////////////////////////////////////////
			// LOANS TAB
			///////////////////////////////////////////////////////////////////
			loansTab = new LoansTab( loanTableModel, library );
			tbsMain.addTab( Messages.getString("BookMaster.Tab.Loans" ), new ImageIcon("icons/basket_32.png"), loansTab, null );
			tbsMain.setMnemonicAt(1, KeyEvent.VK_2);
			
			///////////////////////////////////////////////////////////////////
			// CUSTOMERS TAB
			///////////////////////////////////////////////////////////////////
			customerTab = new CustomerTab(customerTableModel, library);
			tbsMain.addTab( Messages.getString( "MainView.Tab.Customers"), new ImageIcon("icons/user_32.png"), customerTab, null );
			tbsMain.setMnemonicAt(2, KeyEvent.VK_3);
			
			///////////////////////////////////////////////////////////////////
			// MENU
			///////////////////////////////////////////////////////////////////
			jMenuBar = new JMenuBar();
			getContentPane().add(jMenuBar, BorderLayout.NORTH);
			
			viewMenu = new JMenu();
			viewMenu.setText(Messages.getString("BookMasterTable.viewMenu.text")); //$NON-NLS-1$
			viewMenu.setMnemonic(KeyEvent.VK_V);
			{
				showUnavailableMenuItem = new JCheckBoxMenuItem();
				viewMenu.add(showUnavailableMenuItem);
				showUnavailableMenuItem.setText(Messages.getString("BookMasterTable.showUnavailableMenuItem.text")); //$NON-NLS-1$
				showUnavailableMenuItem.setMnemonic(KeyEvent.VK_U);
			}
			
			loansSubMenu = new JMenu();
			loansSubMenu.setText( Messages.getString( "BookMaster.Tab.Loans" ) );
			loansSubMenu.setMnemonic( KeyEvent.VK_L );
			
			viewMenu.add(loansSubMenu);
			jMenuBar.add(viewMenu);
			showUnavailableMenuItem.setAction(booksTab.getToggleShowUnavailableAction());
			{
				/*
				booksShowAll = new JMenuItem();
				booksShowLentOnly = new JMenuItem();
				booksShowOverdueOnly = new JMenuItem();
				
				booksShowAll.setText( Messages.getString( "BookMastertable.BookTableModes.All" ) );
				booksShowLentOnly.setText( Messages.getString( "BookMastertable.BookTableModes.LentOnly" ) );
				booksShowOverdueOnly.setText( Messages.getString( "BookMastertable.BookTableModes.OverdueOnly" ) );
				
				booksShowAll.setMnemonic( KeyEvent.VK_A );
				booksShowLentOnly.setMnemonic( KeyEvent.VK_L );
				booksShowOverdueOnly.setMnemonic( KeyEvent.VK_O );
				
				booksShowAll.addActionListener( booksTab.getChangeBookTableModeAction() );
				booksShowLentOnly.addActionListener( booksTab.getChangeBookTableModeAction() );
				booksShowOverdueOnly.addActionListener( booksTab.getChangeBookTableModeAction() );
				
				booksSubMenu.add(booksShowAll);
				booksSubMenu.add(booksShowLentOnly);
				booksSubMenu.add(booksShowOverdueOnly);
				*/
			}
						
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
		booksTab.updateShowUnavailableCheckbox();
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
			putValue( MNEMONIC_KEY, KeyEvent.VK_S );
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}
