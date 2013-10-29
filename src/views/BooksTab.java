package views;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import viewModels.BookTableModel;
import domain.Book;
import domain.Library;

public class BooksTab extends LibraryTab {
	private static final long serialVersionUID = 1L;
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
	
	GridBagConstraints gbc_scrollPane;
	private JScrollPane scrollPane;
	
	private JTable tblBooks;
	private JTextField txtSearch;
	private JCheckBox chckbxOnlyAvailable;
	
	// Models
	private BookTableModel bookTableModel;
	private TableRowSorter<BookTableModel> sorter;
	
	// Actions
	private AbstractAction toggleShowUnavailabeAction;
	
	// Filter variables. filters contains all filters that can be applied to the jTable.
	private List<RowFilter<Object,Object>> bookFilters;
	private boolean showUnavailable = true;
	private String searchText;
	

	BooksTab(BookTableModel bookTableModel, Library library) {
		super(library);
		this.bookTableModel = bookTableModel;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		pnlBookInventoryStats = new JPanel();
		pnlBookInventoryStats.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlBookInventoryStats.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		this.add(pnlBookInventoryStats);
		pnlBookInventoryStats.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		lblNrOfBooks = new JLabel();
		pnlBookInventoryStats.add(lblNrOfBooks);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		pnlBookInventoryStats.add(horizontalStrut);
		
		lblNrOfCopies = new JLabel(); //$NON-NLS-1$
		pnlBookInventoryStats.add(lblNrOfCopies);
		
		pnlBookInventory = new JPanel();
		pnlBookInventory.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlBookInventory.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		this.add(pnlBookInventory);
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
		
		// Init Filters
		bookFilters = new ArrayList<RowFilter<Object,Object>>();
		
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
		tblBooks= new JTable();
		scrollPane.setViewportView(tblBooks);
			
		initTable();
		updateListButtons();
		updateShowUnavailableCheckbox();
		updateStatistics();
		
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
		availabilityColumn.setCellRenderer(new BookTableCellRenderer(getLibrary()));
		
		TableColumn titleColumn = tblBooks.getColumnModel().getColumn(1);
		titleColumn.setCellRenderer(new BookTableCellRenderer(getLibrary()));
		titleColumn.setCellEditor(new BookTextCellEditor(getLibrary()));

		TableColumn authorColumn = tblBooks.getColumnModel().getColumn(2);
		authorColumn.setCellRenderer(new BookTableCellRenderer(getLibrary()));
		
		TableColumn publisherColumn = tblBooks.getColumnModel().getColumn(3);
		publisherColumn.setMinWidth(100);
		publisherColumn.setMaxWidth(100);
		publisherColumn.setCellRenderer(new BookTableCellRenderer(getLibrary()));
		
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
	
	private void showSelectedButtonActionPerformed(ActionEvent evt) {
		int[] selectedRows = tblBooks.getSelectedRows();

		for (int selectedRow : selectedRows) {
			Book book = getLibrary().getBooks().get(selectedRow);
			book.addObserver(getLibrary());
			BookDetail.editBook( getLibrary(), getLibrary().getBooks().get(selectedRow) );
		}
	}
	
	private void addButtonActionPerformed(ActionEvent evt) {
		// TODO: have to tell DetailDialog somehow that it'll be a new book
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
	
	public AbstractAction getToggleShowUnavailableAction() {
		if(toggleShowUnavailabeAction == null) {
			toggleShowUnavailabeAction = new ToggleShowUnavailableAction();
		}
		return toggleShowUnavailabeAction;
	}
	
	private class ToggleShowUnavailableAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		ToggleShowUnavailableAction() {
			super("Show Unvailable", null);	//TODO: I18N
			putValue(MNEMONIC_KEY, KeyEvent.VK_U);
			putValue(SHORT_DESCRIPTION, "Show or Hide Unavailable Books");	//TODO: I18N
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
	
	public void updateShowUnavailableCheckbox() {
		chckbxOnlyAvailable.setSelected( showUnavailable );
	}
	
	public void updateListButtons() {
		// Enables or disables the "Show Selected" buttons
		// depending on whether a book is selected.
		btnShowSelected.setEnabled(tblBooks.getSelectedRowCount()>0);
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
	
	public void updateStatistics() {
		// Books stats
		lblNrOfBooks.setText( Messages.getString("BookMaster.lblNrOfBooks.text", String.valueOf(getLibrary().getBooks().size())) );
		lblNrOfCopies.setText( Messages.getString("BookMaster.lblNrOfCopies.text", String.valueOf(getLibrary().getCopies().size())) );
	}
}
