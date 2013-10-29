package views;

import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.FlowLayout;
import java.awt.Component;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import viewModels.BookTableModel;
import viewModels.LoanTableModel;

import domain.Book;
import domain.Library;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class BookMasterTable extends javax.swing.JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tbsMain;
	private JMenu viewMenu;
	private JMenuBar jMenuBar;
	
	// Books tab
	private JCheckBoxMenuItem showUnavailableMenuItem;
	
	private JPanel pnlBooksTab;
	private JPanel pnlBookInventoryStats;
	private JPanel pnlBookInventory;
	private JPanel pnlBooksInvTop;
	private JPanel pnlBooksInvBottom;
	
	private JLabel lblNrOfBooks;
	private JLabel lblNrOfCopies;
	
	private JButton btnShowSelected;
	private JButton btnAddNewBook;
	
	private Component horizontalStrut;
	private Component horizontalStrut_1;
	private Component horizontalStrut_2;
	
	private GridBagLayout gbl_pnlBookInventory;
	private GridBagLayout gbl_pnlBooksInvBottom;

	private GridBagConstraints gbc_pnlBooksInvTop;
	private GridBagConstraints gbc_pnlBooksInvBottom;
	
	private Library library;
	GridBagConstraints gbc_scrollPane;
	private JScrollPane scrollPane;
	
	private JTable tblBooks;
	private JTextField txtSearch;
	private JCheckBox chckbxOnlyAvailable;
	
	// Loans tab
	private JPanel pnlLoansTab;
	private JPanel pnlLoansInventoryStats;
	private JPanel pnlLoansInventory;
	private JPanel pnlLoansInvTop;
	private JPanel pnlLoansInvBottom;
	
	private JLabel lblNrOfLoans;
	private JLabel lblNrOfCurrentLoans;
	private JLabel lblNrOfDueLoans;
	
	private Component horizontalStrut_3;
	private Component horizontalStrut_4;
	private Component horizontalStrut_5;
	private Component horizontalStrut_6;
	
	private GridBagLayout gbl_pnlLoansTab;
	private GridBagLayout gbl_pnlLoansInventory;
	private GridBagLayout gbl_pnlLoansInvBottom;

	private GridBagConstraints gbc_pnlLoansInvTop;
	private GridBagConstraints gbc_pnlLoansInvBottom;
	
	private JButton btnShowSelectedLoans;
	private JButton btnAddNewLoan;
	
	private JTable tblLoans;
	private JTextField txtSearchLoans;
	private JCheckBox chckbxOnlyDueLoans;
	
	GridBagConstraints gbc_scrollPaneLoans;
	private JScrollPane scrollPaneLoans;
	
	// Models
	private BookTableModel bookTableModel;
	private TableRowSorter<BookTableModel> sorter;
	private LoanTableModel loanTableModel;
	private TableRowSorter<LoanTableModel> loanSorter;
	
	// Filter variables. filters contains all filters that can be applied to the jTable.
	private List<RowFilter<Object,Object>> bookFilters;
	private List<RowFilter<Object,Object>> loanFilters;
	private boolean showUnavailable = true;
	private boolean showDueLoans = true;
	private String searchText;
	private String searchTextLoans;
	
	// Actions:
	private AbstractAction toggleShowUnavailabeAction;
	private AbstractAction toggleShowDueLoansAction;

	/**
	 * Create the application.
	 * @param bookList 
	 */
	public BookMasterTable( Library library ) {
		/*
		 * This View should listen to changes in the book list and the loans list!
		 */
		super();
		this.library = library;
		bookTableModel = new BookTableModel( this.library );
		loanTableModel = new LoanTableModel( this.library );
		
		// Init filter lists
		bookFilters = new ArrayList<RowFilter<Object,Object>>();
		loanFilters = new ArrayList<RowFilter<Object,Object>>();
		
		initialize();
		library.addObserver( this );
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initialize the contents of the frames.
	 * @author Pascal Forster
	 * @author Philipp Christen
	 */
	private void initialize() {
		try {
			
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle(Messages.getString("BookMaster.frmTodoTitle.title")); //$NON-NLS-1$
			this.setBounds(100, 100, 700, 550);
			
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
				showUnavailableMenuItem.setAction(getToggleShowUnavailableAction());
			}
			
			
			tbsMain = new JTabbedPane(JTabbedPane.TOP);
			tbsMain.setToolTipText(Messages.getString("BookMaster.tbsMain.toolTipText")); //$NON-NLS-1$
			tbsMain.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent e) {
		            changedTab();
		        }
		    });
			this.getContentPane().add(tbsMain, BorderLayout.CENTER);
			
			///////////////////////////////////////////////////////////////////
			// BOOKS TAB
			///////////////////////////////////////////////////////////////////
			{
				pnlBooksTab = new JPanel();
				tbsMain.addTab( Messages.getString("BookMaster.Tab.Books" ), null, pnlBooksTab, null);
				pnlBooksTab.setLayout(new BoxLayout(pnlBooksTab, BoxLayout.Y_AXIS));
				
				pnlBookInventoryStats = new JPanel();
				pnlBookInventoryStats.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlBookInventoryStats.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
				pnlBooksTab.add(pnlBookInventoryStats);
				pnlBookInventoryStats.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
				
				lblNrOfBooks = new JLabel();
				pnlBookInventoryStats.add(lblNrOfBooks);
				
				horizontalStrut = Box.createHorizontalStrut(20);
				pnlBookInventoryStats.add(horizontalStrut);
				
				lblNrOfCopies = new JLabel(); //$NON-NLS-1$
				pnlBookInventoryStats.add(lblNrOfCopies);
				
				pnlBookInventory = new JPanel();
				pnlBookInventory.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlBookInventory.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
				pnlBooksTab.add(pnlBookInventory);
				gbl_pnlBookInventory = new GridBagLayout();
				gbl_pnlBookInventory.columnWidths = new int[] {0};
				gbl_pnlBookInventory.rowHeights = new int[] {30, 0};
				gbl_pnlBookInventory.columnWeights = new double[]{1.0};
				gbl_pnlBookInventory.rowWeights = new double[]{0.0, 1.0};
				pnlBookInventory.setLayout(gbl_pnlBookInventory);
				
				pnlBooksInvTop = new JPanel();
				gbc_pnlBooksInvTop = new GridBagConstraints();
				gbc_pnlBooksInvTop.anchor = GridBagConstraints.NORTH;
				gbc_pnlBooksInvTop.insets = new Insets(0, 0, 5, 0);
				gbc_pnlBooksInvTop.fill = GridBagConstraints.HORIZONTAL;
				gbc_pnlBooksInvTop.gridx = 0;
				gbc_pnlBooksInvTop.gridy = 0;
				pnlBookInventory.add(pnlBooksInvTop, gbc_pnlBooksInvTop);
				pnlBooksInvTop.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
				
				// Search field i.e. Searchbox
				initSearchField();
				
				horizontalStrut_1 = Box.createHorizontalStrut(20);
				pnlBooksInvTop.add(horizontalStrut_1);
				
				btnShowSelected = new JButton(Messages.getString("BookMaster.btnShowSelected.text")); //$NON-NLS-1$
				btnShowSelected.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showSelectedButtonActionPerformed(e);
					}
				});
				
				chckbxOnlyAvailable = new JCheckBox(Messages.getString("BookMasterTable.chckbxOnlySelected.text")); //$NON-NLS-1$
				chckbxOnlyAvailable.setAction(getToggleShowUnavailableAction());
				pnlBooksInvTop.add(chckbxOnlyAvailable);
				
				horizontalStrut_2 = Box.createHorizontalStrut(20);
				pnlBooksInvTop.add(horizontalStrut_2);
				pnlBooksInvTop.add(btnShowSelected);
				
				btnAddNewBook = new JButton(Messages.getString("BookMaster.btnAddNewBook.text")); //$NON-NLS-1$
				btnAddNewBook.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addButtonActionPerformed(e);
					}
				});
				pnlBooksInvTop.add(btnAddNewBook);
				
				pnlBooksInvBottom = new JPanel();
				gbc_pnlBooksInvBottom = new GridBagConstraints();
				gbc_pnlBooksInvBottom.fill = GridBagConstraints.BOTH;
				gbc_pnlBooksInvBottom.gridx = 0;
				gbc_pnlBooksInvBottom.gridy = 1;
				pnlBookInventory.add(pnlBooksInvBottom, gbc_pnlBooksInvBottom);
				gbl_pnlBooksInvBottom = new GridBagLayout();
				gbl_pnlBooksInvBottom.columnWidths = new int[]{0, 0};
				gbl_pnlBooksInvBottom.rowHeights = new int[]{0, 0, 0};
				gbl_pnlBooksInvBottom.columnWeights = new double[]{1.0, Double.MIN_VALUE};
				gbl_pnlBooksInvBottom.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
				pnlBooksInvBottom.setLayout(gbl_pnlBooksInvBottom);
				
				scrollPane = new JScrollPane();
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				gbc_scrollPane = new GridBagConstraints();
				gbc_scrollPane.gridheight = 2;
				gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
				gbc_scrollPane.fill = GridBagConstraints.BOTH;
				gbc_scrollPane.gridx = 0;
				gbc_scrollPane.gridy = 0;
				pnlBooksInvBottom.add(scrollPane, gbc_scrollPane);
				
				//jTable////////////////////////////////////
				tblBooks = new JTable();
				initTable();
	
				scrollPane.setViewportView(tblBooks);
			}
			
			///////////////////////////////////////////////////////////////////
			// LOANS TAB
			///////////////////////////////////////////////////////////////////
			pnlLoansTab = new JPanel();
			tbsMain.addTab( Messages.getString("BookMaster.Tab.Loans" ), null, pnlLoansTab, null);
			pnlLoansTab.setLayout(new BoxLayout(pnlLoansTab, BoxLayout.Y_AXIS));
			
			// Inventory
			{
				pnlLoansInventoryStats = new JPanel();
				pnlLoansInventoryStats.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlLoansInventoryStats.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
				pnlLoansTab.add(pnlLoansInventoryStats);
				pnlLoansInventoryStats.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
				
				lblNrOfLoans = new JLabel();
				lblNrOfLoans.setText(Messages.getString("BookMasterTable.lblNrOfLoans.text")); //$NON-NLS-1$
				pnlLoansInventoryStats.add(lblNrOfLoans);
				
				horizontalStrut_3 = Box.createHorizontalStrut(20);
				pnlLoansInventoryStats.add(horizontalStrut_3);
				
				lblNrOfCurrentLoans = new JLabel(); //$NON-NLS-1$
				lblNrOfCurrentLoans.setText(Messages.getString("BookMasterTable.lblNrOfCurrentLoans.text")); //$NON-NLS-1$
				pnlLoansInventoryStats.add(lblNrOfCurrentLoans);
				
				horizontalStrut_4 = Box.createHorizontalStrut(20);
				pnlLoansInventoryStats.add(horizontalStrut_4);
				
				lblNrOfDueLoans = new JLabel(); //$NON-NLS-1$
				lblNrOfDueLoans.setText(Messages.getString("BookMasterTable.lblNrOfDueLoans.text")); //$NON-NLS-1$
				pnlLoansInventoryStats.add(lblNrOfDueLoans);
			}
			
			pnlLoansInventory = new JPanel();
			pnlLoansInventory.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), Messages.getString("BookMasterTable.pnlLoansInventory.TitledBorder.text"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
			pnlLoansTab.add(pnlLoansInventory);
			gbl_pnlLoansInventory = new GridBagLayout();
			gbl_pnlLoansInventory.columnWidths = new int[] {0};
			gbl_pnlLoansInventory.rowHeights = new int[] {30, 0};
			gbl_pnlLoansInventory.columnWeights = new double[]{1.0};
			gbl_pnlLoansInventory.rowWeights = new double[]{0.0, 1.0};
			pnlLoansInventory.setLayout(gbl_pnlLoansInventory);
			
			pnlLoansInvTop = new JPanel();
			gbc_pnlLoansInvTop = new GridBagConstraints();
			gbc_pnlLoansInvTop.anchor = GridBagConstraints.NORTH;
			gbc_pnlLoansInvTop.insets = new Insets(0, 0, 5, 0);
			gbc_pnlLoansInvTop.fill = GridBagConstraints.HORIZONTAL;
			gbc_pnlLoansInvTop.gridx = 0;
			gbc_pnlLoansInvTop.gridy = 0;
			pnlLoansInventory.add(pnlLoansInvTop, gbc_pnlLoansInvTop);
			pnlLoansInvTop.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			
			// Search field i.e. Searchbox
			initLoansSearchField();
			
			horizontalStrut_5 = Box.createHorizontalStrut(20);
			pnlLoansInvTop.add(horizontalStrut_5);
			
			btnShowSelectedLoans = new JButton(Messages.getString("BookMaster.btnShowSelectedLoans.text")); //$NON-NLS-1$
			btnShowSelectedLoans.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//showSelectedLoansButtonActionPerformed(e);
				}
			});
			
			chckbxOnlyDueLoans = new JCheckBox(Messages.getString("BookMasterTable.chckbxOnlyDueLoans.text")); //$NON-NLS-1$
			chckbxOnlyDueLoans.setAction( getToggleShowDueLoansAction() );
			pnlLoansInvTop.add(chckbxOnlyDueLoans);
			
			horizontalStrut_6 = Box.createHorizontalStrut(20);
			pnlLoansInvTop.add(horizontalStrut_6);
			pnlLoansInvTop.add(btnShowSelectedLoans);
			
			btnAddNewLoan= new JButton(Messages.getString("BookMaster.btnAddNewLoan.text")); //$NON-NLS-1$
			btnAddNewLoan.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//addLoanButtonActionPerformed(e);
				}
			});
			pnlLoansInvTop.add(btnAddNewLoan);
			pnlLoansInvBottom = new JPanel();
			
			gbc_pnlLoansInvBottom = new GridBagConstraints();
			gbc_pnlLoansInvBottom.fill = GridBagConstraints.BOTH;
			gbc_pnlLoansInvBottom.gridx = 0;
			gbc_pnlLoansInvBottom.gridy = 1;
			pnlLoansInventory.add(pnlLoansInvBottom, gbc_pnlLoansInvBottom);
			gbl_pnlLoansInvBottom = new GridBagLayout();
			gbl_pnlLoansInvBottom.columnWidths = new int[]{0, 0};
			gbl_pnlLoansInvBottom.rowHeights = new int[]{0, 0, 0};
			gbl_pnlLoansInvBottom.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_pnlLoansInvBottom.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			pnlLoansInvBottom.setLayout(gbl_pnlLoansInvBottom);
			
			scrollPaneLoans = new JScrollPane();
			scrollPaneLoans.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			gbc_scrollPaneLoans = new GridBagConstraints();
			gbc_scrollPaneLoans.gridheight = 2;
			gbc_scrollPaneLoans.insets = new Insets(0, 0, 5, 0);
			gbc_scrollPaneLoans.fill = GridBagConstraints.BOTH;
			gbc_scrollPaneLoans.gridx = 0;
			gbc_scrollPaneLoans.gridy = 0;
			pnlLoansInvBottom.add(scrollPaneLoans, gbc_scrollPaneLoans);
			
			//jTable////////////////////////////////////
			tblBooks= new JTable();
			tblLoans= new JTable();
			scrollPane.setViewportView(tblBooks);
			scrollPaneLoans.setViewportView(tblLoans);
			
			initTable();
			initLoansTable();
			
			///////////////////////////////////////////////////////////////////
			// Initialize the buttons, actions, etc.
			///////////////////////////////////////////////////////////////////
			updateListButtons();
			updateShowUnavailableCheckbox();
			updateStatistics();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showSelectedButtonActionPerformed(ActionEvent evt) {
		int[] selectedRows = tblBooks.getSelectedRows();

		for (int selectedRow : selectedRows) {
			BookDetail.editBook( library, library.getBooks().get(selectedRow) );
		}
	}
	
	private void addButtonActionPerformed(ActionEvent evt) {
		// TODO: have to tell DetailDialog somehow that it'll be a new book
	}
	
	private void initTable() {
		tblBooks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(tblBooks);
		tblBooks.setModel(bookTableModel);
		
		// Make the columns sortable
		sorter = new TableRowSorter<BookTableModel>(bookTableModel);
		tblBooks.setRowSorter(sorter);
		
		// Handle the filtering over there:
		updateFilters();
		
		TableColumn availabilityColumn = tblBooks.getColumnModel().getColumn(0);
		availabilityColumn.setMinWidth(100);
		availabilityColumn.setMaxWidth(100);
		availabilityColumn.setCellRenderer(new BookTableCellRenderer(library));
		
		TableColumn titleColumn = tblBooks.getColumnModel().getColumn(1);
		titleColumn.setCellRenderer(new BookTableCellRenderer(library));
		titleColumn.setCellEditor(new BookTextCellEditor(library));

		TableColumn authorColumn = tblBooks.getColumnModel().getColumn(2);
		authorColumn.setCellRenderer(new BookTableCellRenderer(library));
		
		TableColumn publisherColumn = tblBooks.getColumnModel().getColumn(3);
		publisherColumn.setMinWidth(100);
		publisherColumn.setMaxWidth(100);
		publisherColumn.setCellRenderer(new BookTableCellRenderer(library));
		
		tblBooks.getSelectionModel().addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent evt) {
					SwingUtilities.invokeLater(new Runnable() {  
						public void run() {
							// Update the "Show Selected" button
							updateListButtons();
						}
					});
				}
			}
		);
	}
	
	private void initLoansTable() {
		tblLoans.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPaneLoans.setViewportView(tblLoans);
		tblLoans.setModel(loanTableModel);
		
		// Make the columns sortable
		loanSorter = new TableRowSorter<LoanTableModel>(loanTableModel);
		tblLoans.setRowSorter(loanSorter);
		
		// Handle the filtering over there:
		updateLoanFilters();
		
		TableColumn statusColumn = tblLoans.getColumnModel().getColumn(0);
		statusColumn.setMinWidth(50);
		statusColumn.setMaxWidth(50);
		statusColumn.setCellRenderer(new LoanTableCellRenderer(library));
		
		TableColumn copyIDColumn = tblLoans.getColumnModel().getColumn(1);
		copyIDColumn.setMinWidth(50);
		copyIDColumn.setMaxWidth(50);
		copyIDColumn.setCellRenderer(new LoanTableCellRenderer(library));
		
		TableColumn copyTitleColumn = tblLoans.getColumnModel().getColumn(2);
		copyTitleColumn.setCellRenderer(new LoanTableCellRenderer(library));
		
		TableColumn lentUntilColumn = tblLoans.getColumnModel().getColumn(3);
		lentUntilColumn.setCellRenderer(new LoanTableCellRenderer(library));
		
		TableColumn lentAtColumn = tblLoans.getColumnModel().getColumn(4);
		lentAtColumn.setCellRenderer(new LoanTableCellRenderer(library));
		
		tblLoans.getSelectionModel().addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent evt) {
					SwingUtilities.invokeLater(new Runnable() {  
						public void run() {
							// Update the "Show Selected" button
							updateListButtons();
						}
					});
				}
			}
		);
	}
	
	/**
	 * 
	 */
	private void initSearchField() {
		txtSearch = new JTextField();
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				searchText = txtSearch.getText();
				updateFilters();
			}
		});
		txtSearch.setText(Messages.getString("BookMasterTable.txtSearch.text")); //$NON-NLS-1$
		txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
			// Mark the whole text when the text field gains focus
    	    public void focusGained(java.awt.event.FocusEvent evt) {
    	    	SwingUtilities.invokeLater( new Runnable() {

    				@Override
    				public void run() {
    					txtSearch.selectAll();
    				}
    			});
    	    }
    	    
    	    // "unmark" everything (=mark nothing) when losing focus
    	    public void focusLost(java.awt.event.FocusEvent evt) {
    	    	SwingUtilities.invokeLater( new Runnable() {

    				@Override
    				public void run() {
    					txtSearch.select(0, 0);
    					searchText = null;
    				}
    			});
    	    }
    	});
		txtSearch.setColumns(10);
		pnlBooksInvTop.add(txtSearch);
	}
	
	/**
	 * 
	 */
	private void initLoansSearchField() {
		txtSearchLoans = new JTextField();
		txtSearchLoans.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				searchText = txtSearch.getText();
				updateLoanFilters();
			}
		});
		txtSearchLoans.setText(Messages.getString("BookMasterTable.textField.text")); //$NON-NLS-1$
		txtSearchLoans.addFocusListener(new java.awt.event.FocusAdapter() {
			// Mark the whole text when the text field gains focus
    	    public void focusGained(java.awt.event.FocusEvent evt) {
    	    	SwingUtilities.invokeLater( new Runnable() {

    				@Override
    				public void run() {
    					txtSearchLoans.selectAll();
    				}
    			});
    	    }
    	    
    	    // "unmark" everything (=mark nothing) when losing focus
    	    public void focusLost(java.awt.event.FocusEvent evt) {
    	    	SwingUtilities.invokeLater( new Runnable() {

    				@Override
    				public void run() {
    					txtSearchLoans.select(0, 0);
    					searchTextLoans = null;
    				}
    			});
    	    }
    	});
		txtSearchLoans.setColumns(10);
		pnlLoansInvTop.add(txtSearchLoans);
	}
	
	/**
	 * @author PCHR
	 */
	private void updateFilters() {
		// 1st, clear all filters, if there are any
		if ( !bookFilters.isEmpty() ) {
			bookFilters.clear();
		}
		
		// 2nd: apply the "showUnavailable" filter if applicable
		if ( !showUnavailable ) {
			bookFilters.add( new RowFilter<Object, Object>() {
		        public boolean include(Entry entry) {
		        	if (showUnavailable){
		        		return true;
		        	}
		        	System.out.println("Filter");
		        	// get value of Available column (column 0)
		        	//TODO: get available copies. Can't do it like this because
		        	// there's a string in that row.
		        	//Boolean completed = (Boolean) entry.getValue(0);
		        	//return ! completed.booleanValue();
		        	return false;
		        }
		    } );
		}
		
		// 3rd: apply the filter from the search box.
		if ( searchText != null ) {
			bookFilters.add( RowFilter.regexFilter( searchText ) );
		}
		
		sorter.setRowFilter( RowFilter.andFilter(bookFilters) );
	}	

	/**
	 *  @author PCHR
	 */
	private void updateLoanFilters() {
		// 1st, clear all filters, if there are any
		if ( !loanFilters.isEmpty() ) {
			loanFilters.clear();
		}
		
		// 2nd: apply the "showDueLoans" filter if applicable
		if ( !showDueLoans) {
			loanFilters.add( new RowFilter<Object, Object>() {
		        public boolean include(Entry entry) {
		        	
		        	if (showDueLoans){
		        		return true;
		        	}
		        	System.out.println("Filter");
		        	// get value of Available column (column 0)
		        	//TODO: get available copies. Can't do it like this because
		        	// there's a string in that row.
		        	//Boolean completed = (Boolean) entry.getValue(0);
		        	//return ! completed.booleanValue();
		        	return false;
		        }
		    } );
		}
		
		// 3rd: apply the filter from the search box.
		if ( searchTextLoans != null ) {
			loanFilters.add( RowFilter.regexFilter( searchTextLoans ) );
		}
		
		loanSorter.setRowFilter( RowFilter.andFilter(loanFilters) );
	}
	
	
	private AbstractAction getToggleShowUnavailableAction() {
		if(toggleShowUnavailabeAction == null) {
			toggleShowUnavailabeAction = new ToggleShowUnavailableAction();
		}
		return toggleShowUnavailabeAction;
	}
	
	private class ToggleShowUnavailableAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		ToggleShowUnavailableAction() {
			super("Show Unvailable", null);
			putValue(MNEMONIC_KEY, KeyEvent.VK_U);
			putValue(SHORT_DESCRIPTION, "Show or Hide Unavailable Books");
			putValue(ACCELERATOR_KEY, 
					KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			showUnavailable=!showUnavailable;
			
			// Re-Filter		
			updateFilters();
			updateListButtons();
			updateShowUnavailableCheckbox();
		}
	}
	
	private AbstractAction getToggleShowDueLoansAction() {
		if(toggleShowDueLoansAction == null) {
			toggleShowDueLoansAction = new ToggleShowDueLoansAction();
		}
		return toggleShowDueLoansAction;
	}
	
	private class ToggleShowDueLoansAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		ToggleShowDueLoansAction() {
			super("Show Due Loans", null);	//TODO i8n
			putValue(MNEMONIC_KEY, KeyEvent.VK_D);
			putValue(SHORT_DESCRIPTION, "Show or Hide Due Loans");
			putValue(ACCELERATOR_KEY, 
					KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			showDueLoans=!showDueLoans;
			
			// Re-Filter		
			updateLoanFilters();
			updateListButtons();
			updateShowDueLoansCheckbox();
		}
	}
	
	private void updateListButtons() {
		// Enables or disables the "Show Selected" buttons
		// depending on whether a book/loan is selected.
		btnShowSelected.setEnabled(tblBooks.getSelectedRowCount()>0);
		btnShowSelectedLoans.setEnabled( tblLoans.getSelectedRowCount()>0);
	}
	
	private void updateShowUnavailableCheckbox() {
		chckbxOnlyAvailable.setSelected( showUnavailable );
	}
	
	private void updateShowDueLoansCheckbox() {
		chckbxOnlyDueLoans.setSelected( showDueLoans );
	}
	
	/**
	 * Updates the labels that contain statistical information.
	 * @author Philipp Christen
	 */
	private void updateStatistics() {
		// Books stats
		lblNrOfBooks.setText( Messages.getString("BookMaster.lblNrOfBooks.text", String.valueOf(library.getBooks().size())) );
		lblNrOfCopies.setText( Messages.getString("BookMaster.lblNrOfCopies.text", String.valueOf(library.getCopies().size())) );
		
		// Loans stats
		lblNrOfCurrentLoans.setText( Messages.getString( "BookMasterTable.lblNrOfCurrentLoans.text", String.valueOf( library.getLentOutBooks().size() ) ) );
		lblNrOfDueLoans.setText( Messages.getString( "BookMasterTable.lblNrOfDueLoans.text", String.valueOf( library.getOverdueLoans().size() ) ) );
		lblNrOfLoans.setText( Messages.getString( "BookMasterTable.lblNrOfLoans.text", String.valueOf( library.getLoans().size() ) ) );
	}

	@Override
	public void update(Observable o, Object arg) {
		updateListButtons();
		updateShowUnavailableCheckbox();
		updateStatistics();
	}

	public class BookTextCellEditor extends AbstractBookTableCellEditor {
		private static final long serialVersionUID = 1L;

		public BookTextCellEditor (Library library) {
			super(new JTextField(), library);
		}

		@Override
		public boolean setBookValue(Book book) {
			JTextField textField = (JTextField)getComponent();
			book.setName(textField.getText());
			return true; //false would disallow leaving the field
		}
	}

	public void changedTab() {
		System.out.println("Tab: " + tbsMain.getSelectedIndex());
		switch ( tbsMain.getSelectedIndex() ) {
		case 0:	// Books
			break;
		case 1: // Loans
			break;
		}
	}
}
