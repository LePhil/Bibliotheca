package views;

import i18n.Messages;

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
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import renderers.LoanTableCellRenderer;
import viewModels.LoanTableModel;
import domain.Library;
import domain.Loan;

public class LoansTab extends LibraryTab {
	private static final long serialVersionUID = 1L;

	GridBagConstraints gbc_scrollPane;
	private JPanel pnlLoansInventoryStats;
	private JPanel pnlLoansInventory;
	private JPanel pnlLoansInvTop;
	private JPanel pnlLoansInvBottom;
	
	private JLabel lblNrOfLoans;
	private JLabel lblNrOfCurrentLoans;
	private JLabel lblNrOfDueLoans;
	
	private Component horizontalStrut_3;
	private Component horizontalStrut_4;
	
	private GridBagLayout gbl_pnlLoansInventory;
	private GridBagLayout gbl_pnlLoansInvBottom;

	private GridBagConstraints gbc_pnlLoansInvTop;
	private GridBagConstraints gbc_pnlLoansInvBottom;
	
	private JButton btnShowSelectedLoans;
	private JButton btnAddNewLoan;
	
	private JTable tblLoans;
	private JTextField txtSearchLoans;
	private JComboBox<String> cmbLoanTableModes;
	
	GridBagConstraints gbc_scrollPaneLoans;
	private JScrollPane scrollPaneLoans;
	
	// Models
	private LoanTableModel loanTableModel;
	private TableRowSorter<LoanTableModel> loanSorter;
	
	// Filter variables. filters contains all filters that can be applied to the jTable.
	private List<RowFilter<Object,Object>> loanFilters;
	private String searchTextLoans;
	private enum loanTableMode {
		ALL, LENTONLY, OVERDUEONLY;
	}
	private loanTableMode currentTableMode;
	
	// Actions:
	private AbstractAction changeLoanTableModeAction;

	public LoansTab(LoanTableModel loanTableModel, Library library) {
		super(library);
		this.loanTableModel = loanTableModel;
		
		// Init filter lists
		loanFilters = new ArrayList<RowFilter<Object,Object>>();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		/////////////////////////////////////////////////
		// ACTIONS
		/////////////////////////////////////////////////
		// Show/Edit selected loans
		AbstractAction showSelected = new ShowSelectedLoanAction(
			Messages.getString("BookMaster.btnShowSelectedLoans.text"),
			Messages.getString("BookMaster.btnShowSelectedLoans.desc")
		);
		// Add Loan
		AbstractAction addLoan = new AddLoanAction(
			Messages.getString("BookMaster.btnAddNewLoan.text"),
			Messages.getString("BookMaster.btnAddNewLoan.desc")
		);
	
		// Inventory
		{
			pnlLoansInventoryStats = new JPanel();
			pnlLoansInventoryStats.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlLoansInventoryStats.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			this.add(pnlLoansInventoryStats);
			pnlLoansInventoryStats.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			
			lblNrOfLoans = new JLabel();
			lblNrOfLoans.setText(Messages.getString("BookMasterTable.lblNrOfLoans.text"));
			pnlLoansInventoryStats.add(lblNrOfLoans);
			
			horizontalStrut_3 = Box.createHorizontalStrut(20);
			pnlLoansInventoryStats.add(horizontalStrut_3);
			
			lblNrOfCurrentLoans = new JLabel();
			lblNrOfCurrentLoans.setText(Messages.getString("BookMasterTable.lblNrOfCurrentLoans.text"));
			pnlLoansInventoryStats.add(lblNrOfCurrentLoans);
			
			horizontalStrut_4 = Box.createHorizontalStrut(20);
			pnlLoansInventoryStats.add(horizontalStrut_4);
			
			lblNrOfDueLoans = new JLabel();
			lblNrOfDueLoans.setText(Messages.getString("BookMasterTable.lblNrOfDueLoans.text"));
			pnlLoansInventoryStats.add(lblNrOfDueLoans);
		}
		
		pnlLoansInventory = new JPanel();
		pnlLoansInventory.setBorder(new TitledBorder(null, Messages.getString("BookMasterTable.pnlLoansInventory.TitledBorder.text"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.add(pnlLoansInventory);
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
		
		{
			GridBagLayout gbl_LoansInvTop = new GridBagLayout();
			gbl_LoansInvTop.columnWidths = new int[]{0, 100, 0, 20, 0, 0, 0};
			gbl_LoansInvTop.rowHeights = new int[]{0, 0};
			gbl_LoansInvTop.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_LoansInvTop.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			pnlLoansInvTop.setLayout( gbl_LoansInvTop );
			
			// Search field i.e. Searchbox
			initLoansSearchField();
			
			///////////////////////////////////////////////////////////
			// ComboBox
			///////////////////////////////////////////////////////////
			String[] strLoanTableModes = {
				Messages.getString( "BookMastertable.BookTableModes.All" ),
				Messages.getString( "BookMastertable.BookTableModes.LentOnly" ),
				Messages.getString( "BookMastertable.BookTableModes.OverdueOnly" )
			};
			
			cmbLoanTableModes = new JComboBox<String>( strLoanTableModes );
			cmbLoanTableModes.setAction( getChangeLoanTableModeAction() );
			cmbLoanTableModes.setToolTipText(Messages.getString("LoansTab.cmbLoanTableModes.desc"));
			
			GridBagConstraints gbc_cmbBox = new GridBagConstraints();
			gbc_cmbBox.insets = new Insets(0, 0, 0, 5);
			gbc_cmbBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_cmbBox.gridx = 2;
			gbc_cmbBox.gridy = 0;
			pnlLoansInvTop.add( cmbLoanTableModes, gbc_cmbBox );
			
			btnShowSelectedLoans = new JButton( showSelected );
			btnShowSelectedLoans.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showSelectedLoansButtonActionPerformed(e);
				}
			});
			btnShowSelectedLoans.setIcon( new ImageIcon("icons/basket_search_32.png") );
			
			GridBagConstraints gbc_btnShow = new GridBagConstraints();
			gbc_btnShow.insets = new Insets(0, 0, 0, 5);
			gbc_btnShow.gridx = 4;
			gbc_btnShow.gridy = 0;
			pnlLoansInvTop.add(btnShowSelectedLoans, gbc_btnShow);
			
			btnAddNewLoan= new JButton( addLoan );
			btnAddNewLoan.setIcon( new ImageIcon("icons/basket_add_32.png") );
			GridBagConstraints gbc_btnAdd = new GridBagConstraints();
			gbc_btnAdd.gridx = 5;
			gbc_btnAdd.gridy = 0;
			pnlLoansInvTop.add( btnAddNewLoan, gbc_btnAdd );
		}
		
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
		tblLoans= new JTable();
		scrollPaneLoans.setViewportView(tblLoans);
		
		initLoansTable();
		
		// Enable opening selected items on ENTER
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		tblLoans.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "showItem");
		tblLoans.getActionMap().put( "showItem", showSelected );
	}
	
	private void initLoansTable() {
		tblLoans.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPaneLoans.setViewportView(tblLoans);
		tblLoans.setModel(loanTableModel);
		
		// Make the columns sortable
		loanSorter = new TableRowSorter<LoanTableModel>(loanTableModel);
		tblLoans.setRowSorter(loanSorter);
		
		// Handle the filtering over there:
		updateFilters();
		
		TableColumn statusColumn = tblLoans.getColumnModel().getColumn(0);
		statusColumn.setMinWidth(100);
		statusColumn.setMaxWidth(100);
		statusColumn.setCellRenderer(new LoanTableCellRenderer(getLibrary()) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				// get value from TableModel
				boolean isOverdue = (Boolean) value;
				
				if ( isOverdue ) {
					label.setIcon( new ImageIcon("icons/warning_16.png") );
					label.setText( Messages.getString("LoanTable.CellContent.Overdue") );
				} else {
					label.setText( Messages.getString("LoanTable.CellContent.OK") );
					label.setIcon( new ImageIcon("icons/ok_button_16.png") );
				}
				
				
				return label;
			}
		});		
		
		TableColumn copyIDColumn = tblLoans.getColumnModel().getColumn(1);
		copyIDColumn.setMinWidth(50);
		copyIDColumn.setMaxWidth(50);
		copyIDColumn.setCellRenderer(new LoanTableCellRenderer(getLibrary()));
		
		TableColumn copyTitleColumn = tblLoans.getColumnModel().getColumn(2);
		copyTitleColumn.setCellRenderer(new LoanTableCellRenderer(getLibrary()));
		
		TableColumn lentUntilColumn = tblLoans.getColumnModel().getColumn(3);
		lentUntilColumn.setCellRenderer(new LoanTableCellRenderer(getLibrary()));
		
		TableColumn lentAtColumn = tblLoans.getColumnModel().getColumn(4);
		lentAtColumn.setCellRenderer(new LoanTableCellRenderer(getLibrary()));
		
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
		
		// On DoubleClick on an entry, show it in the detail view
		tblLoans.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 2) {
	            	showSelectedLoansButtonActionPerformed(null);
	            }
	        }
		});
	}
	
	/**
	 * 
	 */
	private void initLoansSearchField() {
		GridBagConstraints gbc_Icon = new GridBagConstraints();
		gbc_Icon.insets = new Insets(0, 0, 0, 5);
		gbc_Icon.anchor = GridBagConstraints.EAST;
		gbc_Icon.gridx = 0;
		gbc_Icon.gridy = 0;
		pnlLoansInvTop.add( new JLabel( new ImageIcon("icons/search_32.png") ), gbc_Icon );
		
		txtSearchLoans = new JTextField(10);
		txtSearchLoans.setToolTipText( Messages.getString( "LoansTab.txtSearchLoans.desc") );
		// enables to focus on the searchfield by pressing Alt-F
		txtSearchLoans.setFocusAccelerator('F');
		txtSearchLoans.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchTextLoans = txtSearchLoans.getText();
				updateFilters();
			}
		});
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
    				}
    			});
    	    }
    	});
		txtSearchLoans.setColumns(10);
		
		GridBagConstraints gbc_searchField = new GridBagConstraints();
		gbc_searchField.insets = new Insets(0, 0, 0, 5);
		gbc_searchField.anchor = GridBagConstraints.EAST;
		gbc_searchField.gridx = 1;
		gbc_searchField.gridy = 0;
		pnlLoansInvTop.add( txtSearchLoans, gbc_searchField );
	}

	/**
	 *  @author PCHR
	 */
	private void updateFilters() {
		// 1st, clear all filters, if there are any
		if ( !loanFilters.isEmpty() ) {
			loanFilters.clear();
		}
		
		// 2nd: apply the "loanTableMode" filter if applicable (e.g. if not ALL)
		if ( currentTableMode != loanTableMode.ALL ) {
			loanFilters.add( new RowFilter<Object, Object>() {
				@Override
				public boolean include(	javax.swing.RowFilter.Entry<? extends Object, ? extends Object> entry ) {
					LoanTableModel loanModel = (LoanTableModel) entry.getModel();
					Loan loan = loanModel.getLoan( entry.getIdentifier() );
					
					if ( currentTableMode == loanTableMode.LENTONLY ) {
						return loan.isLent();	
					} else if ( currentTableMode == loanTableMode.OVERDUEONLY) {
						return loan.isOverdue();
					}
					return true;
				}
			} );
		}
		
		// 3rd: apply the filter from the search box. (?i) makes regex ignore cases
		if ( searchTextLoans != null ) {
			loanFilters.add( RowFilter.regexFilter( "(?i)" + searchTextLoans ) );
		}
		
		loanSorter.setRowFilter( RowFilter.andFilter(loanFilters) );
	}
	
	public void updateListButtons() {
		// Enables or disables the "Show Selected" buttons
		// depending on whether a book/loan is selected.
		btnShowSelectedLoans.setEnabled( tblLoans.getSelectedRowCount()>0);
	}
	
	///////////////////////////////////////
	// Combobox stuff
	///////////////////////////////////////
	public AbstractAction getChangeLoanTableModeAction() {
		if( changeLoanTableModeAction == null ) {
			changeLoanTableModeAction = new ChangeLoanTableModeAction();
		}
		return changeLoanTableModeAction;
	}
	private class ChangeLoanTableModeAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentTableMode = loanTableMode.values()[cmbLoanTableModes.getSelectedIndex()];
			updateFilters();
		}
	}
	///////////////////////////////////////
	// ACTIONS
	///////////////////////////////////////
	// Show selected Loans
	private void showSelectedLoansButtonActionPerformed(ActionEvent event){
		int[] selectedRows = tblLoans.getSelectedRows();
		
		for (int selectedRow : selectedRows) {
			selectedRow = tblLoans.convertRowIndexToModel( selectedRow );
			Loan selectedLoan = getLibrary().getLoans().get(selectedRow);
			LoanDetail.editLoan(selectedLoan, getLibrary());
		}
	}
	class ShowSelectedLoanAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public ShowSelectedLoanAction( String text, String desc ) {
	        super( text );
	        putValue( SHORT_DESCRIPTION, desc );
	        putValue( MNEMONIC_KEY, KeyEvent.VK_S );
	    }
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedRows = tblLoans.getSelectedRows();
			
			for (int selectedRow : selectedRows) {
				Loan loan = getLibrary().getLoans().get( tblLoans.convertRowIndexToModel( selectedRow ) );
				LoanDetail.editLoan(loan, getLibrary() );
			}
		}	
	}
	class AddLoanAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public AddLoanAction( String text, String desc ) {
	        super( text );
	        putValue( SHORT_DESCRIPTION, desc );
	        putValue( MNEMONIC_KEY, KeyEvent.VK_N );
	    }
		@Override
		public void actionPerformed(ActionEvent e) {
			LoanDetail.editLoan( null, getLibrary() );
		}
	}

	/**
	 * Updates the labels that contain statistical information.
	 * @author PCHR
	 */
	public void updateStatistics() {
		lblNrOfCurrentLoans.setText( Messages.getString( "BookMasterTable.lblNrOfCurrentLoans.text", String.valueOf( getLibrary().getLentOutBooks().size() ) ) );
		lblNrOfDueLoans.setText( Messages.getString( "BookMasterTable.lblNrOfDueLoans.text", String.valueOf( getLibrary().getOverdueLoans().size() ) ) );
		lblNrOfLoans.setText( Messages.getString( "BookMasterTable.lblNrOfLoans.text", String.valueOf( getLibrary().getLoans().size() ) ) );
	}
}