package views;

import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import viewModels.BookTableModel;
import viewModels.CustomerTableModel;
import viewModels.LoanTableModel;

import domain.Library;

import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class MainView extends javax.swing.JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tbsMain;
	private JMenu viewMenu;
	private JMenuBar jMenuBar;
	
	private JCheckBoxMenuItem showUnavailableMenuItem;	//TODO: remove
	
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
			
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle(Messages.getString("BookMaster.frmTodoTitle.title")); //$NON-NLS-1$
			this.setBounds(100, 100, 700, 550);
			
			tbsMain = new JTabbedPane(JTabbedPane.TOP);
			tbsMain.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent e) {
		            changedTab();
		        }
		    });
			this.getContentPane().add(tbsMain, BorderLayout.CENTER);
			
			///////////////////////////////////////////////////////////////////
			// BOOKS TAB
			///////////////////////////////////////////////////////////////////
			booksTab = new BooksTab( bookTableModel, library );
			tbsMain.addTab( Messages.getString("BookMaster.Tab.Books" ), null, booksTab, null );
			
			///////////////////////////////////////////////////////////////////
			// LOANS TAB
			///////////////////////////////////////////////////////////////////
			loansTab = new LoansTab( loanTableModel, library );
			tbsMain.addTab( Messages.getString("BookMaster.Tab.Loans" ), null, loansTab, null );
				
			///////////////////////////////////////////////////////////////////
			// CUSTOMERS TAB
			///////////////////////////////////////////////////////////////////
			customerTab = new CustomerTab(customerTableModel, library);
			tbsMain.addTab( Messages.getString( "MainView.Tab.Customers"), null, customerTab, null );
			
			///////////////////////////////////////////////////////////////////
			// MENU
			///////////////////////////////////////////////////////////////////
			jMenuBar = new JMenuBar();
			setJMenuBar(jMenuBar);
			
			viewMenu = new JMenu();
			viewMenu.setText(Messages.getString("BookMasterTable.viewMenu.text")); //$NON-NLS-1$
			viewMenu.setMnemonic(KeyEvent.VK_V);
			{
				showUnavailableMenuItem = new JCheckBoxMenuItem();
				viewMenu.add(showUnavailableMenuItem);
				showUnavailableMenuItem.setText(Messages.getString("BookMasterTable.showUnavailableMenuItem.text")); //$NON-NLS-1$
				showUnavailableMenuItem.setMnemonic(KeyEvent.VK_U);
				showUnavailableMenuItem.setAction(booksTab.getToggleShowUnavailableAction());
			}
			
			loansSubMenu = new JMenu();
			loansSubMenu.setText( Messages.getString( "BookMaster.Tab.Loans" ) );
			loansSubMenu.setMnemonic( KeyEvent.VK_L );
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
			
			viewMenu.add(loansSubMenu);
			jMenuBar.add(viewMenu);
						
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
		loansTab.updateShowDueLoansCheckbox();
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

	public void changedTab() {
		switch ( tbsMain.getSelectedIndex() ) {
		case 0:	// Books
			break;
		case 1: // Loans
			break;
		}
	}
}
