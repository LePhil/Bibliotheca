package views;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
	
	// Buttons
	private JButton btnAddNewCustomer;
	private JButton btnShowSelected;
	
	// Models
	private CustomerTableModel customerTableModel;
	private TableRowSorter<CustomerTableModel> sorter;
	
	// Actions
	private AbstractAction toggleShowUnavailabeAction;
	
	// Filter variables. filters contains all filters that can be applied to the jTable.
	private List<RowFilter<Object,Object>> customerFilters;
	private boolean showUnavailable = true;
	private String searchText;
	
	CustomerTab(CustomerTableModel customerTableModel, Library library) {
		super(library);
		this.customerTableModel = customerTableModel;
		
		this.setLayout(new BoxLayout(this,  BoxLayout.Y_AXIS));
		
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
			pnlCustomerInventory.setBorder(new TitledBorder(null, Messages.getString("CustomerMaster.pnlCustomerInventory.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
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
			//initSearchField();
			
			Component horizontalStrut_1 = Box.createHorizontalStrut(20);
			pnlCustomersInvTop.add(horizontalStrut_1);
			
			btnShowSelected = new JButton(Messages.getString("CustomerMaster.btnShowSelected.text")); //$NON-NLS-1$
			btnShowSelected.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//showSelectedButtonActionPerformed(e);
				}
			});
			
			//chckbxOnlyAvailable = new JCheckBox(Messages.getString("CustomerMasterTable.chckbxOnlySelected.text")); //$NON-NLS-1$
			//chckbxOnlyAvailable.setAction(getToggleShowUnavailableAction());
			//pnlCustomersInvTop.add(chckbxOnlyAvailable);
			
			Component horizontalStrut_2 = Box.createHorizontalStrut(20);
			pnlCustomersInvTop.add(horizontalStrut_2);
			pnlCustomersInvTop.add(btnShowSelected);
			
			btnAddNewCustomer = new JButton(Messages.getString("CustomerMaster.btnAddNewCustomer.text")); //$NON-NLS-1$
			btnAddNewCustomer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//addButtonActionPerformed(e);
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
	}
	
	private void initTable() {
		tblCustomers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(tblCustomers);
		tblCustomers.setModel(customerTableModel);
		
		// Make the columns sortable
		sorter = new TableRowSorter<CustomerTableModel>(customerTableModel);
		tblCustomers.setRowSorter(sorter);
		
		// Handle the filtering over there:
		//updateFilters();
		
		TableColumn customerNoColumn = tblCustomers.getColumnModel().getColumn(0);
		customerNoColumn.setMinWidth(100);
		customerNoColumn.setMaxWidth(100);
		//customerNoColumn.setCellRenderer(new BookTableCellRenderer(getLibrary()));
		
		TableColumn nameColumn = tblCustomers.getColumnModel().getColumn(1);
		//nameColumn.setCellRenderer(new BookTableCellRenderer(getLibrary()));

		TableColumn addressColumn = tblCustomers.getColumnModel().getColumn(2);
		//addressColumn.setCellRenderer(new BookTableCellRenderer(getLibrary()));
		
		// Add Listeners
		tblCustomers.getSelectionModel().addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent evt) {
					SwingUtilities.invokeLater(new Runnable() {  
						public void run() {
							// Update the "Show Selected" button
							// updateListButtons();
						}
					});
				}
			}
		);
		
		// On DoubleClick on an entry, show it in the detail view
		tblCustomers.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 2) {
	                int selectedRow = tblCustomers.getSelectedRow();
	                //editCustomer( getLibrary().getBooks().get( selectedRow ) );
	            }
	        }
		});
	}

}
