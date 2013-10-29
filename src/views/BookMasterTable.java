package views;

import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import viewModels.BookTableModel;
import viewModels.LoanTableModel;

import domain.Library;

import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class BookMasterTable extends javax.swing.JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tbsMain;
	private JMenu viewMenu;
	private JMenuBar jMenuBar;
	
	private JCheckBoxMenuItem showUnavailableMenuItem;
	
	private BooksTab pnlBooksTab;
	private LoansTab pnlLoansTab;
	
	private Library library;
	
	// Models
	private BookTableModel bookTableModel;
	private LoanTableModel loanTableModel;

	
	/**
	 * Create the application.
	 * @param library
	 * @author PCHR
	 */
	public BookMasterTable( Library library ) {
		/*
		 * This View should listen to changes in the book list and the loans list!
		 */
		super();
		this.library = library;
		bookTableModel = new BookTableModel( this.library );
		loanTableModel = new LoanTableModel( this.library );
		
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
			pnlBooksTab = new BooksTab( bookTableModel, library );
			tbsMain.addTab( Messages.getString("BookMaster.Tab.Books" ), null, pnlBooksTab, null);
			
			///////////////////////////////////////////////////////////////////
			// LOANS TAB
			///////////////////////////////////////////////////////////////////
			pnlLoansTab = new LoansTab( loanTableModel, library );
			tbsMain.addTab( Messages.getString("BookMaster.Tab.Loans" ), null, pnlLoansTab, null);
			
			// Menu
			jMenuBar = new JMenuBar();
			setJMenuBar(jMenuBar);
			
			viewMenu = new JMenu();
			jMenuBar.add(viewMenu);
			viewMenu.setText(Messages.getString("BookMasterTable.viewMenu.text")); //$NON-NLS-1$
			viewMenu.setMnemonic(KeyEvent.VK_V);
			{
				showUnavailableMenuItem = new JCheckBoxMenuItem();
				viewMenu.add(showUnavailableMenuItem);
				showUnavailableMenuItem.setText(Messages.getString("BookMasterTable.showUnavailableMenuItem.text")); //$NON-NLS-1$
				showUnavailableMenuItem.setMnemonic(KeyEvent.VK_U);
				showUnavailableMenuItem.setAction(pnlBooksTab.getToggleShowUnavailableAction());
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
		pnlBooksTab.updateShowUnavailableCheckbox();
		pnlLoansTab.updateShowDueLoansCheckbox();
		updateStatistics();
	}
		
	/**
	 * Updates the labels that contain statistical information.
	 * @author PCHR
	 */
	private void updateStatistics() {
		pnlBooksTab.updateStatistics();
		pnlLoansTab.updateStatistics();
	}
	
	/**
	 * Updates all buttons on the view
	 * @author PCHR
	 */
	public void updateButtons() {
		pnlBooksTab.updateListButtons();
		pnlLoansTab.updateListButtons();
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
