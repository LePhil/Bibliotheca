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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
import domain.Loan;

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
	
	// Actions
	private ShowSelectedCustomerAction showSelected;
	private AddCustomerAction addCustomer;
	
	CustomerTab(CustomerTableModel customerTableModel, Library library) {
		super(library);
		this.customerTableModel = customerTableModel;
		
		this.setLayout(new BoxLayout(this,  BoxLayout.Y_AXIS));
		
		/////////////////////////////////////////////////
		// ACTIONS
		/////////////////////////////////////////////////
		{
			// Show/Edit selected customer
			AbstractAction showCustomer = new ShowSelectedCustomerAction( Messages.getString( "CustomerTab.btnShowSelected.text"), "Show the customer that has been selected in the customer list" );
			// Close
			// Add Customer
			AbstractAction addCustomer = new AddCustomerAction("ASd", "Adds a new customer" );
		}

		
		// CustomerStats
		{
			pnlCustomerStats = new JPanel();
			pnlCustomerStats.setBorder(new TitledBorder(null, Messages.getString("CustomerTab.CustomerStats.Title")));
			pnlCustomerStats.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			this.add( pnlCustomerStats );
		
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
			pnlCustomersInvTop.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			
			// Init Filters
			customerFilters = new ArrayList<RowFilter<Object,Object>>();
			
			// Search field i.e. Searchbox
			initSearchField();
			
			Component horizontalStrut_1 = Box.createHorizontalStrut(20);
			pnlCustomersInvTop.add(horizontalStrut_1);
			
			btnShowSelected = new JButton(Messages.getString("CustomerTab.btnShowSelected.text")); //$NON-NLS-1$
			btnShowSelected.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getShowSelectedCustomerAction().actionPerformed(null);
				}
			});
			
			Component horizontalStrut_2 = Box.createHorizontalStrut(20);
			pnlCustomersInvTop.add(horizontalStrut_2);
			pnlCustomersInvTop.add(btnShowSelected);
			
			btnAddNewCustomer = new JButton(Messages.getString("CustomerTab.btnAddNewCustomer.text")); //$NON-NLS-1$
			btnAddNewCustomer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getAddCustomerAction().actionPerformed(null);
				}
			});
			pnlCustomersInvTop.add(btnAddNewCustomer);
			
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
	}
	
	private void initTable() {
		tblCustomers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
	            	getShowSelectedCustomerAction().actionPerformed(null);
	            }
	        }
		});
	}

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
    				}
    			});
    	    }
    	});
		txtSearch.setColumns(10);
		pnlCustomersInvTop.add(txtSearch);
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
		lblNrOfCustomers.setText( Messages.getString("CustomerTab.lblNrOfCustomers.text", String.valueOf(getLibrary().getCustomers().size())) );
	}
	
	/////////////////////////////////////////////////
	// Action Subclasses
	/////////////////////////////////////////////////
	public AbstractAction getShowSelectedCustomerAction() {
		if( showSelected == null ) {
			showSelected = new ShowSelectedCustomerAction("", "");
		}
		return showSelected;
	}
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
			Customer selectedCustomer= getLibrary().getCustomers().get(selectedRow);
			//CustomerDetail.editCustomer(selectedCustomer, getLibrary());
		}	
	}
	public AbstractAction getAddCustomerAction() {
		if( addCustomer == null ) {
			addCustomer= new AddCustomerAction("", "");
		}
		return addCustomer;
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
			//int selectedRow = tblCustomers.getSelectedRow();
			//Customer selectedCustomer= getLibrary().getCustomers().get(selectedRow);
			//CustomerDetail.editCustomer(selectedCustomer, getLibrary());
		}
	}
}
