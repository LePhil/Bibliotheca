package views;

import java.awt.Color;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

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
	private Component horizontalStrut_5;
	private Component horizontalStrut_6;
	
	private GridBagLayout gbl_pnlLoansInventory;
	private GridBagLayout gbl_pnlLoansInvBottom;

	private GridBagConstraints gbc_pnlLoansInvTop;
	private GridBagConstraints gbc_pnlLoansInvBottom;
	
	private JButton btnShowSelectedLoans;
	private JButton btnAddNewLoan;
	
	private JTable tblLoans;
	private JTextField txtSearchLoans;
	private JCheckBox chckbxOnlyDueLoans;
	private JComboBox<String> cmbLoanTableModes;
	
	GridBagConstraints gbc_scrollPaneLoans;
	private JScrollPane scrollPaneLoans;
	
	// Models
	private LoanTableModel loanTableModel;
	private TableRowSorter<LoanTableModel> loanSorter;
	
	// Filter variables. filters contains all filters that can be applied to the jTable.
	private List<RowFilter<Object,Object>> loanFilters;
	private boolean showDueLoans = true;
	private String searchTextLoans;
	private enum loanTableMode {
		ALL, LENTONLY, OVERDUEONLY;
	}
	private loanTableMode currentTableMode;
	
	// Actions:
	private AbstractAction toggleShowDueLoansAction;
	private AbstractAction changeLoanTableModeAction;

	public LoansTab(LoanTableModel loanTableModel, Library library) {
		super(library);
		this.loanTableModel = loanTableModel;
		
		// Init filter lists
		loanFilters = new ArrayList<RowFilter<Object,Object>>();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Inventory
		{
			pnlLoansInventoryStats = new JPanel();
			pnlLoansInventoryStats.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlLoansInventoryStats.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
			this.add(pnlLoansInventoryStats);
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
		pnlLoansInvTop.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		// Search field i.e. Searchbox
		initLoansSearchField();
		
		horizontalStrut_5 = Box.createHorizontalStrut(20);
		pnlLoansInvTop.add(horizontalStrut_5);
		
		btnShowSelectedLoans = new JButton(Messages.getString("BookMaster.btnShowSelectedLoans.text")); //$NON-NLS-1$
		btnShowSelectedLoans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSelectedLoansButtonActionPerformed(e);
			}
		});
		
		chckbxOnlyDueLoans = new JCheckBox( Messages.getString("BookMasterTable.chckbxOnlyDueLoans.text") ); //$NON-NLS-1$
		chckbxOnlyDueLoans.setAction( getToggleShowDueLoansAction() );
		pnlLoansInvTop.add(chckbxOnlyDueLoans);
		
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
		
		pnlLoansInvTop.add( cmbLoanTableModes );
		
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
		tblLoans= new JTable();
		scrollPaneLoans.setViewportView(tblLoans);
		
		initLoansTable();

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
		statusColumn.setMinWidth(50);
		statusColumn.setMaxWidth(50);
		statusColumn.setCellRenderer(new LoanTableCellRenderer(getLibrary()));
		
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
		txtSearchLoans = new JTextField();
		txtSearchLoans.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				searchTextLoans = txtSearchLoans.getText();
				updateFilters();
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
    				}
    			});
    	    }
    	});
		txtSearchLoans.setColumns(10);
		pnlLoansInvTop.add(txtSearchLoans);
	}

	/**
	 *  @author PCHR
	 */
	private void updateFilters() {
		// 1st, clear all filters, if there are any
		if ( !loanFilters.isEmpty() ) {
			loanFilters.clear();
		}
		
		// 2nd: apply the "showDueLoans" filter if applicable
		// also, check here if the loan is currently lent.
		// TODO: PCHR - CHECK IF THIS WORKS. LOOKS GOOD, BUT FEELS BAD
		/*
		loanFilters.add( new RowFilter<Object, Object>() {
			@Override
			public boolean include(RowFilter.Entry<? extends Object, ? extends Object> entry) {
				 LoanTableModel loanModel = (LoanTableModel) entry.getModel();
				 Loan loan = loanModel.getLoan(entry.getIdentifier());
				 
				 if ( loan.isLent() ) {
					 if (showDueLoans){
						 return loan.isOverdue();
					 } else {
						 return true;
					 }
				 }
				 return false;
			}
		} );
		*/
		
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
	
	private AbstractAction getToggleShowDueLoansAction() {
		if(toggleShowDueLoansAction == null) {
			toggleShowDueLoansAction = new ToggleShowDueLoansAction();
		}
		return toggleShowDueLoansAction;
	}
	
	private class ToggleShowDueLoansAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		ToggleShowDueLoansAction() {
			super( Messages.getString("BookMasterTable.chckbxOnlyDueLoans.text") , null);
			putValue(MNEMONIC_KEY, KeyEvent.VK_D);
			putValue(SHORT_DESCRIPTION, Messages.getString("BookMasterTable.chckbxOnlyDueLoans.desc") );
			putValue(ACCELERATOR_KEY, 
					KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			showDueLoans=!showDueLoans;
			
			// Re-Filter		
			updateFilters();
			updateListButtons();
			updateShowDueLoansCheckbox();
		}
	}
	
	public void updateListButtons() {
		// Enables or disables the "Show Selected" buttons
		// depending on whether a book/loan is selected.
		btnShowSelectedLoans.setEnabled( tblLoans.getSelectedRowCount()>0);
	}
	
	public void updateShowDueLoansCheckbox() {
		chckbxOnlyDueLoans.setSelected( showDueLoans );
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
		public void actionPerformed(int i) {
			currentTableMode = loanTableMode.values()[i];
			updateFilters();
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
	
	private void showSelectedLoansButtonActionPerformed(ActionEvent event){
		int selectedRow = tblLoans.getSelectedRow();
		Loan selectedLoan = getLibrary().getLoans().get(selectedRow);
		LoanDetail.editLoan(selectedLoan, getLibrary());
	}
}