package views;

import i18n.Messages;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import viewModels.CustomerTableModel;
import domain.Customer;
import domain.Library;

public class CustomerTab extends LibraryTab {
	private static final long serialVersionUID = 1L;
	private JPanel pnlCustomerStats;
	private JPanel pnlCustomerInventory;
	private JPanel pnlCustomersInvTop;
	private JScrollPane scrollPane;
	private JPanel pnlCustomersInvBottom;
	
	private JLabel lblNrOfCustomers;
	
	private JTable tblCustomers;
	
	private JTextField txtSearch;
	
	// Buttons
	private JButton btnAddNewCustomer;
	private JButton btnShowSelected;
	
	// Models
	private CustomerTableModel customerTableModel;
	private TableRowSorter<CustomerTableModel> sorter;
	
	// Filter variables. filters contains all filters that can be applied to the jTable.
	private List<RowFilter<Object,Object>> customerFilters;
	private String searchText;
	
	CustomerTab(CustomerTableModel customerTableModel, Library library) {
		super(library);
		this.customerTableModel = customerTableModel;
		
		this.setLayout(new BoxLayout(this,  BoxLayout.Y_AXIS));
		
		/////////////////////////////////////////////////
		// ACTIONS
		/////////////////////////////////////////////////
		// Show/Edit selected customer
		AbstractAction showCustomer = new ShowSelectedCustomerAction( Messages.getString( "CustomerTab.btnShowSelected.text"), "Show the customer that has been selected in the customer list" );
		// Add Customer
		AbstractAction addCustomer = new AddCustomerAction( Messages.getString("CustomerTab.btnAddNewCustomer.text"), "Adds a new customer" );

		
		// CustomerStats
		{
			pnlCustomerStats = new JPanel();
			pnlCustomerStats.setBorder(new TitledBorder(null, Messages.getString("CustomerTab.CustomerStats.Title"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
			this.add(pnlCustomerStats);
			pnlCustomerStats.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			
			lblNrOfCustomers = new JLabel();
			pnlCustomerStats.add( lblNrOfCustomers );
		}
		// Inventory (Customer table)
		{
			pnlCustomerInventory = new JPanel();
			pnlCustomerInventory.setBorder(new TitledBorder(null, Messages.getString("Customertab.pnlCustomerInventory.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
			this.add(pnlCustomerInventory);
			GridBagLayout gbl_pnlCustomerInventory = new GridBagLayout();
			gbl_pnlCustomerInventory.columnWidths = new int[] {0};
			gbl_pnlCustomerInventory.rowHeights = new int[] {30, 0};
			gbl_pnlCustomerInventory.columnWeights = new double[]{1.0};
			gbl_pnlCustomerInventory.rowWeights = new double[]{0.0, 1.0};
			pnlCustomerInventory.setLayout(gbl_pnlCustomerInventory);
			
			pnlCustomersInvTop = new JPanel();
			GridBagConstraints gbc_pnlCustomersInvTop = new GridBagConstraints();
			gbc_pnlCustomersInvTop.anchor = GridBagConstraints.NORTH;
			gbc_pnlCustomersInvTop.insets = new Insets(0, 0, 5, 0);
			gbc_pnlCustomersInvTop.fill = GridBagConstraints.HORIZONTAL;
			gbc_pnlCustomersInvTop.gridx = 0;
			gbc_pnlCustomersInvTop.gridy = 0;
			pnlCustomerInventory.add(pnlCustomersInvTop, gbc_pnlCustomersInvTop);

			GridBagLayout gbl_InvTop = new GridBagLayout();
			gbl_InvTop.columnWidths = new int[]{0, 100, 20, 0, 0, 0};
			gbl_InvTop.rowHeights = new int[]{0, 0};
			gbl_InvTop.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_InvTop.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			
			pnlCustomersInvTop.setLayout( gbl_InvTop );
			
			// Init Filters
			customerFilters = new ArrayList<RowFilter<Object,Object>>();
			
			// Search field i.e. Searchbox
			initSearchField();
			
			btnShowSelected = new JButton( showCustomer );
			btnShowSelected.setIcon( new ImageIcon("icons/user_search_32.png") );
			
			GridBagConstraints gbc_btnShow = new GridBagConstraints();
			gbc_btnShow.gridx = 4;
			gbc_btnShow.gridy = 0;
			pnlCustomersInvTop.add(btnShowSelected, gbc_btnShow);

			
			btnAddNewCustomer = new JButton( addCustomer );
			btnAddNewCustomer.setIcon( new ImageIcon("icons/user_add_32.png") );

			GridBagConstraints gbc_btnAdd = new GridBagConstraints();
			gbc_btnAdd.gridx = 5;
			gbc_btnAdd.gridy = 0;
			pnlCustomersInvTop.add( btnAddNewCustomer, gbc_btnAdd );
			
			
			pnlCustomersInvBottom = new JPanel();
			GridBagConstraints gbc_pnlCustomersInvBottom = new GridBagConstraints();
			gbc_pnlCustomersInvBottom.fill = GridBagConstraints.BOTH;
			gbc_pnlCustomersInvBottom.gridx = 0;
			gbc_pnlCustomersInvBottom.gridy = 1;
			pnlCustomerInventory.add(pnlCustomersInvBottom, gbc_pnlCustomersInvBottom);
			GridBagLayout gbl_pnlCustomersInvBottom = new GridBagLayout();
			gbl_pnlCustomersInvBottom.columnWidths = new int[]{0, 0};
			gbl_pnlCustomersInvBottom.rowHeights = new int[]{0, 0, 0};
			gbl_pnlCustomersInvBottom.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_pnlCustomersInvBottom.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			pnlCustomersInvBottom.setLayout(gbl_pnlCustomersInvBottom);
			
			scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.gridheight = 2;
			gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			pnlCustomersInvBottom.add(scrollPane, gbc_scrollPane);
			
			//jTable////////////////////////////////////
			tblCustomers= new JTable();
			scrollPane.setViewportView(tblCustomers);
		}
		
		initTable();
		updateListButtons();
		updateStatistics();
		
		// Enable opening selected items on ENTER
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		tblCustomers.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "showItem");
		tblCustomers.getActionMap().put( "showItem", showCustomer );
	}
	
	private void initTable() {
		tblCustomers.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		scrollPane.setViewportView(tblCustomers);
		tblCustomers.setModel(customerTableModel);
		
		// Make the columns sortable
		sorter = new TableRowSorter<CustomerTableModel>(customerTableModel);
		tblCustomers.setRowSorter(sorter);
		
		// Handle the filtering over there:
		updateFilters();
		
		TableColumn customerNoColumn = tblCustomers.getColumnModel().getColumn(0);
		customerNoColumn.setMinWidth(100);
		customerNoColumn.setMaxWidth(100);
		//customerNoColumn.setCellRenderer(new BookTableCellRenderer(getLibrary()));
		
		// TODO: was haben wir hier versucht? Oo
		tblCustomers.getColumnModel().getColumn(1);
		tblCustomers.getColumnModel().getColumn(2);
		
		// Add Listeners
		tblCustomers.getSelectionModel().addListSelectionListener(
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
		
		// On DoubleClick on an entry, show it in the detail view
		tblCustomers.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 2) {
	            	new ShowSelectedCustomerAction("", "").actionPerformed(null);
	            }
	        }
		});
	}

	private void initSearchField() {
		GridBagConstraints gbc_Icon = new GridBagConstraints();
		gbc_Icon.gridx = 0;
		gbc_Icon.gridy = 0;
		pnlCustomersInvTop.add( new JLabel( new ImageIcon("icons/search_32.png") ), gbc_Icon );
		
		txtSearch = new JTextField( 10 );
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
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
    				}
    			});
    	    }
    	});
		GridBagConstraints gbc_searchField = new GridBagConstraints();
		gbc_searchField.gridx = 1;
		gbc_searchField.gridy = 0;
		pnlCustomersInvTop.add( txtSearch, gbc_searchField );
	}
	
	private void updateFilters() {
		// 1st, clear all filters, if there are any
		if ( !customerFilters.isEmpty() ) {
			customerFilters.clear();
		}

		// 2nd: apply the filter from the search box.  (?i) makes regex ignore cases
		if ( searchText != null ) {
			customerFilters.add( RowFilter.regexFilter( "(?i)" + searchText ) );
		}
		
		sorter.setRowFilter( RowFilter.andFilter(customerFilters) );
	}
	
	public void updateListButtons() {
		// Enables or disables the "Show Selected" buttons
		// depending on whether a book is selected.
		btnShowSelected.setEnabled(tblCustomers.getSelectedRowCount()>0);
	}
	
	public void updateStatistics() {
		// TODO: statistics don't get updated!
		lblNrOfCustomers.setText( Messages.getString("CustomerTab.lblNrOfCustomers.text", String.valueOf(getLibrary().getCustomerList().getCustomers().size())) );
	}
	
	/////////////////////////////////////////////////
	// Action Subclasses
	/////////////////////////////////////////////////
	class ShowSelectedCustomerAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public ShowSelectedCustomerAction( String text, String desc ) {
	        super( text );
	        putValue( SHORT_DESCRIPTION, desc );
	        putValue( MNEMONIC_KEY, KeyEvent.VK_ENTER );
	    }
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("SHOW CUSTOMER");
			int selectedRow = tblCustomers.getSelectedRow();
			
			//Convert to model
			selectedRow = tblCustomers.convertRowIndexToModel( selectedRow );
			System.out.println(selectedRow);
			Customer selectedCustomer= getLibrary().getCustomerList().getCustomers().get(selectedRow);
			CustomerDetail.editCustomer(getLibrary(), getLibrary().getCustomerList(), selectedCustomer);
		}	
	}
	class AddCustomerAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public AddCustomerAction( String text, String desc ) {
	        super( text );
	        putValue( SHORT_DESCRIPTION, desc );
	        putValue( MNEMONIC_KEY, KeyEvent.VK_N );
	    }
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("NEW CUSTOMER");
			CustomerDetail.editCustomer(getLibrary(), getLibrary().getCustomerList(), null );
		}
	}
}
