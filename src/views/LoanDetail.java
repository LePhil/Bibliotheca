package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import viewModels.CopyTableModel;
import viewModels.CustomerLoanTableModel;
import domain.Copy;
import domain.CopyList;
import domain.Customer;
import domain.Library;
import domain.Loan;
import domain.LoanList;

public class LoanDetail extends JFrame {
	private static final long serialVersionUID = 1L;

	private static Dictionary<Loan, LoanDetail> loanFramesDict = new Hashtable<Loan, LoanDetail>();

	private CustomerLoanTableModel customerLoanTableModel;
	private CopyTableModel copyTableModel;
	private Loan loan;
	private LoanList loans;
	private CopyList copies;
	private Library library;

	// Buttons
	private JButton btnReturnSelectedLoan;
	private JButton btnAddLoan;
	private JButton btnClose;

	// Labels
	private JLabel lblCustomerId;
	private JLabel lblCustomer;
	private JLabel lblAnzahlAusleihen;
	private TitledBorder customerBorder;

	// Textfields
	private JTextField txtCustomerId;
	private JTextField txtSearchCopies;

	// Tables
	private JTable customerLoanTable;
	private JTable copyTable;

	private TableRowSorter<CustomerLoanTableModel> loanSorter;
	private TableRowSorter<CopyTableModel> copySorter;
	
	private List<RowFilter<Object,Object>> copyFilters;

	private JComboBox<Customer> cmbCustomer;

	// Panels
	private JPanel pnlLoans;
	private JPanel pnlCustomerLoanInfo;
	private JPanel pnlCustomerLoans;
	private JPanel pnlCopies;
	private JPanel pnlFilterCopies;
	private JPanel pnlAvailableCopies;
	private JScrollPane scrollPaneLoans;
	private JScrollPane scrollPaneCopies;
	private JPanel pnlCustomer;
	private JPanel pnlButtons;

	private static LoanDetail editFrame;

	private static int customerNo;
	private JPanel panel;

	/**
	 * Create the application.
	 */
	public LoanDetail(Loan loan, LoanList loanList, Library library) {
		super();
		setTitle(Messages.getString("LoanDetail.this.title"));
		this.loan = loan;
		this.loans = loanList;
		this.copies = new CopyList();
		this.copies.setCopyList(library.getAvailableCopies());
		this.library = library;

		this.customerLoanTableModel = new CustomerLoanTableModel(loanList);
		this.copyTableModel = new CopyTableModel( this.copies, library );
		this.copyFilters = new ArrayList<RowFilter<Object,Object>>();
		initialize();
	}

	public static void editLoan( Loan loan, Library library ) {
		if ( loan == null ) {
			// create new loan
			loan = new Loan( null, null );
			customerNo = -1;
		} else {
			customerNo = loan.getCustomer().getCustomerNo();
		}

		LoanList loans = new LoanList();
		// get other loans from this customer and show them in the loanTable
		if (loan.getCustomer() != null) {
			List<Loan> customerLoans = library.getCustomerLoans( loan.getCustomer() );
			if ( customerLoans != null ) {
				loans.setLoanList( customerLoans );
			}
		}

		editFrame = loanFramesDict.get( loan );
		if ( editFrame == null ) {
			editFrame = new LoanDetail( loan, loans, library );
			loanFramesDict.put( loan, editFrame );
		}
		editFrame.setVisible( true );
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			setMinimumSize( new Dimension(700, 600) );
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			setBounds(100, 100, 900, 600);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS ) );

			// ///////////////////////////////////////////////
			// ACTIONS
			// ///////////////////////////////////////////////
			
			AddLoanAction addLoanAction = new AddLoanAction( Messages.getString("LoanDetail.addLoanBtn.text"), "" );
			CloseAction closeAction = new CloseAction( Messages.getString( "MainView.btnExit.text"), "" );
			ReturnLoanAction returnLoanAction = new ReturnLoanAction( Messages.getString( "LoanDetail.returnCopyBtn.text" ), "" );
			ChangeCustomerAction changeCustomerSelection = new ChangeCustomerAction(  );
			
			// ///////////////////////////////////////////////
			// CUSTOMER PANEL
			// ///////////////////////////////////////////////
			pnlCustomer = new JPanel();
			pnlCustomer.setMaximumSize(new Dimension(32767, 50));
			pnlCustomer.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), Messages.getString("LoanDetail.CustomerSelection.text"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(pnlCustomer);
			GridBagLayout gbl_pnlCustomer = new GridBagLayout();
			gbl_pnlCustomer.columnWidths = new int[] { 0, 0, 0, 0, 0 };
			gbl_pnlCustomer.rowHeights = new int[] { 0, 0, 0 };
			gbl_pnlCustomer.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
			gbl_pnlCustomer.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			pnlCustomer.setLayout(gbl_pnlCustomer);

			lblCustomerId = new JLabel( Messages.getString( "LoanDetail.lblCustomerId.text" ) );
			GridBagConstraints gbc_lblCustomerId = new GridBagConstraints();
			gbc_lblCustomerId.insets = new Insets( 0, 0, 5, 5 );
			gbc_lblCustomerId.anchor = GridBagConstraints.EAST;
			gbc_lblCustomerId.gridx = 1;
			gbc_lblCustomerId.gridy = 0;
			pnlCustomer.add(lblCustomerId, gbc_lblCustomerId);

			txtCustomerId = new JTextField();
			GridBagConstraints gbc_txtCustomerId = new GridBagConstraints();
			gbc_txtCustomerId.insets = new Insets(0, 0, 5, 5);
			gbc_txtCustomerId.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtCustomerId.gridx = 2;
			gbc_txtCustomerId.gridy = 0;
			pnlCustomer.add(txtCustomerId, gbc_txtCustomerId);
			txtCustomerId.setColumns(10);
			txtCustomerId.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					txtCustomerId.setBackground(Color.WHITE);
					String text = txtCustomerId.getText();
					try {
						customerNo = Integer.parseInt(text);
						if(customerNo < 0 || customerNo >= library.getCustomerList().getCustomers().size()){
							handleInvalidCustomerID();
						}else {
							updateCustomerDropdown(library.getCustomerList().getByID(customerNo));
							updateLabels();
						}
			        } catch (NumberFormatException ex) {
				           handleInvalidCustomerID();
			        }
				}

				private void handleInvalidCustomerID() {
					txtCustomerId.setBackground(Color.PINK);
				}
			});

			lblCustomer = new JLabel( Messages.getString("LoanDetail.lblCustomer.text") );
			GridBagConstraints gbc_lblCustomer = new GridBagConstraints();
			gbc_lblCustomer.anchor = GridBagConstraints.EAST;
			gbc_lblCustomer.insets = new Insets(0, 0, 0, 5);
			gbc_lblCustomer.gridx = 1;
			gbc_lblCustomer.gridy = 1;
			pnlCustomer.add(lblCustomer, gbc_lblCustomer);

			cmbCustomer = new JComboBox<Customer>();
			GridBagConstraints gbc_cmbCustomer = new GridBagConstraints();
			gbc_cmbCustomer.insets = new Insets(0, 0, 0, 5);
			gbc_cmbCustomer.fill = GridBagConstraints.HORIZONTAL;
			gbc_cmbCustomer.gridx = 2;
			gbc_cmbCustomer.gridy = 1;
			
			// Fill Combobox with customers
			for (Customer customer : library.getCustomerList().getCustomers()) {
				cmbCustomer.addItem(customer);
			}
			cmbCustomer.setSelectedItem( loan.getCustomer() );
			cmbCustomer.setAction( changeCustomerSelection );
			pnlCustomer.add(cmbCustomer, gbc_cmbCustomer);

			// ///////////////////////////////////////////////
			// LOANS PANEL
			// ///////////////////////////////////////////////
			pnlLoans = new JPanel();
			LineBorder customerLineBorder = new LineBorder( new Color (0,0,0));
			customerBorder = new TitledBorder(customerLineBorder, Messages.getString("LoanDetail.LoansOfCustomer.text") );
			pnlLoans.setBorder( customerBorder );
			pnlLoans.setMaximumSize(new Dimension(32767, 50));
			getContentPane().add(pnlLoans);
			GridBagLayout gbl_pnlLoans = new GridBagLayout();
			gbl_pnlLoans.columnWidths = new int[] { 0, 0 };
			gbl_pnlLoans.rowHeights = new int[] { 0, 0, 0, 0 };
			gbl_pnlLoans.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_pnlLoans.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
			pnlLoans.setLayout(gbl_pnlLoans);

			pnlCustomerLoanInfo = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnlCustomerLoanInfo.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			GridBagConstraints gbc_pnlCustomerLoanInfo = new GridBagConstraints();
			gbc_pnlCustomerLoanInfo.insets = new Insets(0, 0, 5, 0);
			gbc_pnlCustomerLoanInfo.fill = GridBagConstraints.BOTH;
			gbc_pnlCustomerLoanInfo.gridx = 0;
			gbc_pnlCustomerLoanInfo.gridy = 0;
			pnlLoans.add( pnlCustomerLoanInfo, gbc_pnlCustomerLoanInfo );

			lblAnzahlAusleihen = new JLabel( Messages.getString( "LoanDetail.nrOfLoansOfCustomer.text", loans.getLoanList().size() + "") );
			pnlCustomerLoanInfo.add( lblAnzahlAusleihen );

			pnlCustomerLoans = new JPanel();
			GridBagConstraints gbc_pnlCustomerLoans = new GridBagConstraints();
			gbc_pnlCustomerLoans.insets = new Insets(0, 0, 5, 0);
			gbc_pnlCustomerLoans.fill = GridBagConstraints.BOTH;
			gbc_pnlCustomerLoans.gridx = 0;
			gbc_pnlCustomerLoans.gridy = 1;
			pnlCustomerLoans.setLayout(new BoxLayout(pnlCustomerLoans,
					BoxLayout.Y_AXIS));
			pnlLoans.add(pnlCustomerLoans, gbc_pnlCustomerLoans);

			scrollPaneLoans = new JScrollPane();
			{
				customerLoanTable = new JTable();
				initLoanTable();
			}
			scrollPaneLoans.setViewportView(customerLoanTable);
			pnlCustomerLoans.add(scrollPaneLoans);

			btnReturnSelectedLoan = new JButton( returnLoanAction );
			GridBagConstraints gbc_btnSelektierteAusleiheAbschliessen = new GridBagConstraints();
			gbc_btnSelektierteAusleiheAbschliessen.anchor = GridBagConstraints.WEST;
			gbc_btnSelektierteAusleiheAbschliessen.gridx = 0;
			gbc_btnSelektierteAusleiheAbschliessen.gridy = 2;
			btnReturnSelectedLoan.setEnabled(false);
			pnlLoans.add(btnReturnSelectedLoan,	gbc_btnSelektierteAusleiheAbschliessen);

			// ///////////////////////////////////////////////
			// COPIES PANEL
			// ///////////////////////////////////////////////
			pnlCopies = new JPanel();
			pnlCopies.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Exemplare", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(pnlCopies);
			GridBagLayout gbl_pnlCopies = new GridBagLayout();
			gbl_pnlCopies.columnWidths = new int[] { 0, 0 };
			gbl_pnlCopies.rowHeights = new int[] { 0, 0, 0, 0 };
			gbl_pnlCopies.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_pnlCopies.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
			pnlCopies.setLayout(gbl_pnlCopies);
			
			pnlFilterCopies = new JPanel();
			FlowLayout flowLayoutFilter = (FlowLayout) pnlFilterCopies.getLayout();
			flowLayoutFilter.setAlignment(FlowLayout.LEFT);
			GridBagConstraints gbc_pnlFilterCopies = new GridBagConstraints();
			gbc_pnlFilterCopies.insets = new Insets(0, 0, 5, 0);
			gbc_pnlFilterCopies.fill = GridBagConstraints.BOTH;
			gbc_pnlFilterCopies.gridx = 0;
			gbc_pnlFilterCopies.gridy = 0;
			pnlCopies.add(pnlFilterCopies, gbc_pnlFilterCopies);

			pnlFilterCopies.add( new JLabel( new ImageIcon("icons/search_32.png") ));
			
			txtSearchCopies = new JTextField();
			initCopySearchField();
			pnlFilterCopies.add(txtSearchCopies);

			pnlAvailableCopies = new JPanel();
			GridBagConstraints gbc_pnlCopies = new GridBagConstraints();
			gbc_pnlCopies.insets = new Insets(0, 0, 5, 0);
			gbc_pnlCopies.fill = GridBagConstraints.BOTH;
			gbc_pnlCopies.gridx = 0;
			gbc_pnlCopies.gridy = 1;
			pnlAvailableCopies.setLayout( new BoxLayout( pnlAvailableCopies, BoxLayout.Y_AXIS ) );
			pnlCopies.add(pnlAvailableCopies, gbc_pnlCopies);

			scrollPaneCopies = new JScrollPane();
			{
				copyTable = new JTable();
				initCopyTable();
			}
			scrollPaneCopies.setViewportView(copyTable);
			pnlAvailableCopies.add(scrollPaneCopies);
			
			panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			Date returnDate = new Date( new Date().getTime() + (30 * 1000 * 60 * 60 * 24));
			pnlCopies.add(panel, gbc_panel);
			panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
	
			btnAddLoan = new JButton( addLoanAction );
			panel.add(btnAddLoan);
			btnAddLoan.setEnabled( false );
			JLabel lblReturnDate = new JLabel( returnDate.toString() );
			panel.add(lblReturnDate);

			updateBtnAddLoan();
			
			// ///////////////////////////////////////////////
			// BUTTONS PANEL
			// ///////////////////////////////////////////////
			pnlButtons = new JPanel();
			pnlButtons.setMaximumSize(new Dimension(32767, 50));
			getContentPane().add(pnlButtons);
			pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
	
			pnlButtons.add(Box.createHorizontalGlue());
			
			btnClose = new JButton( closeAction );
			btnClose.setIcon( new ImageIcon("icons/close_32.png") );
			pnlButtons.add(btnClose);
			
			updateLabels();
			updateListButtons();
			
			// Add Escape-Action ( close dialog )
			KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
			InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		    inputMap.put(stroke, "ESCAPE");
		    rootPane.getActionMap().put( "ESCAPE", closeAction );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initCopySearchField() {
		txtSearchCopies.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
					String searchText= txtSearchCopies.getText();
					updateFilters(searchText);
			}
		});
		txtSearchCopies.setText(Messages.getString("BookMasterTable.textField.text")); //$NON-NLS-1$
		txtSearchCopies.addFocusListener(new java.awt.event.FocusAdapter() {
			// Mark the whole text when the text field gains focus
		    public void focusGained(java.awt.event.FocusEvent evt) {
		    	SwingUtilities.invokeLater( new Runnable() {

					@Override
					public void run() {
						txtSearchCopies.selectAll();
					}
				});
		    }
		    
		    // "unmark" everything (=mark nothing) when losing focus
		    public void focusLost(java.awt.event.FocusEvent evt) {
		    	SwingUtilities.invokeLater( new Runnable() {

					@Override
					public void run() {
						txtSearchCopies.select(0, 0);
					}
				});
		    }
		});
		txtSearchCopies.setColumns(10);
	}
	
	private void updateLabels() {
		if ( cmbCustomer.getSelectedItem() != null ) {
			Customer customer = (Customer) cmbCustomer.getSelectedItem();
			
			txtCustomerId.setText("" + customer.getCustomerNo());		
			customerBorder.setTitle( Messages.getString( "LoanDetail.LoansOfCustomer.text", customer.getName() + " " + customer.getSurname() ) );
			
			lblAnzahlAusleihen.setText(Messages.getString("LoanDetail.nrOfLoansOfCustomer.text", loans.getLoanList().size() + ""));
		}
	}

	private void updateFilters(String searchText) {
		// 1st, clear all filters, if there are any
		if ( !copyFilters.isEmpty() ) {
			copyFilters.clear();
		}
		
		// 2nd: apply the filter from the search box. (?i) makes regex ignore cases
		if ( searchText != null ) {
			copyFilters.add( RowFilter.regexFilter( "(?i)" + searchText ) );
		}
		
		copySorter.setRowFilter( RowFilter.andFilter(copyFilters) );
	}
	
	private void initLoanTable() {
		customerLoanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customerLoanTable.setModel(customerLoanTableModel);
		loanSorter = new TableRowSorter<CustomerLoanTableModel>(
				customerLoanTableModel);
		customerLoanTable.setRowSorter(loanSorter);
		
		customerLoanTable.getColumnModel().getColumn(0).setCellRenderer(new LoanTableCellRenderer(library));
		customerLoanTable.getColumnModel().getColumn(1).setCellRenderer(new LoanTableCellRenderer(library));
		customerLoanTable.getColumnModel().getColumn(2).setCellRenderer(new LoanTableCellRenderer(library));
		customerLoanTable.getColumnModel().getColumn(3).setCellRenderer(new LoanTableCellRenderer(library));
		
		customerLoanTable.getColumnModel().getColumn( 1 ).setCellRenderer(new LoanTableCellRenderer( library ) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				// get value from TableModel
				boolean isOverdue = value.equals(0);
				boolean isLent = value.equals(1);
				
				if ( isOverdue ) {
					label.setIcon( new ImageIcon("icons/warning_16.png") );
					label.setText( Messages.getString("LoanTable.CellContent.Overdue") );
					label.setForeground( Color.RED );
				} else if ( isLent ) {
					label.setText( Messages.getString("LoanTable.CellContent.Lent") );
					label.setIcon( new ImageIcon("icons/ok_button_16.png") );
				} else {
					label.setText( Messages.getString("LoanTable.CellContent.Old") );
					label.setIcon( null );
				}
				
				return label;
			}
		});
		// Add Listeners
		customerLoanTable.getSelectionModel().addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent evt) {
					SwingUtilities.invokeLater(new Runnable() {  
						public void run() {
							if(customerLoanTable.getSelectedRow() >= 0){
								int index = customerLoanTable.convertRowIndexToModel(customerLoanTable.getSelectedRow());
								if(index >= 0 && index < loans.getLoanList().size()){
									Loan loan = loans.getLoanAt(index);
									boolean isLent = loan != null && loan.isLent();
									btnReturnSelectedLoan.setEnabled(isLent);
								}
							}
						}
					});
				}
			}
		);
	}

	private void initCopyTable() {
		copyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		copyTable.setModel(copyTableModel);
		copySorter = new TableRowSorter<CopyTableModel>(copyTableModel);
		copyTable.setRowSorter(copySorter);
		
		// Ignore the status in this view, because only available copies should show up here anyway!
		copyTable.getColumnModel().removeColumn( copyTable.getColumnModel().getColumn(3) );
		
		// Add Listeners
		copyTable.getSelectionModel().addListSelectionListener(
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

	public void updateListButtons() {
		// Enables or disables the buttons
		btnAddLoan.setEnabled( copyTable.getSelectedRowCount()>0);
		updateBtnAddLoan();
	}
	
	private void updateCustomerDropdown(Customer customer) {
		if (customer != null) {
			cmbCustomer.setSelectedItem(customer);
		} else {
			cmbCustomer.setSelectedIndex(0);
		}
	}
	
	private void updateBtnAddLoan(){
		boolean displayBtn = true;
		int countLents = 0;
		for(Loan loan : this.loans.getLoanList()){
			if(loan.isOverdue()){
				displayBtn = false;
			}
			if(loan.isLent()){
				countLents++;
			}
		}
		displayBtn = displayBtn && countLents < 3;
		btnAddLoan.setEnabled(displayBtn);
	}

	// ///////////////////////////////////////////////
	// Action Subclasses
	// ///////////////////////////////////////////////
	/**
	 * @author PCHR
	 */
	class CloseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public CloseAction(String text, String desc) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, KeyEvent.VK_C);
		}

		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	class AddLoanAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public AddLoanAction( String text, String desc ) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, KeyEvent.VK_H );
		}
		
		public void actionPerformed(ActionEvent e) {
			int selectedRow = copyTable.convertRowIndexToModel(copyTable.getSelectedRow());
			Copy copy = copies.getCopyAt(selectedRow);
			Customer customer = (Customer) cmbCustomer.getSelectedItem();
			Loan newLoan = library.createAndAddLoan(customer, copy);
			copies.setCopyList(library.getAvailableCopies());
			loans.addLoan(newLoan);
			
			updateLabels();
			updateBtnAddLoan();
		}
	}
	
	class ReturnLoanAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public ReturnLoanAction( String text, String desc ) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, KeyEvent.VK_R );
		}
		
		public void actionPerformed(ActionEvent e) {
			int index = customerLoanTable.getSelectedRow();
			if(index < loans.getLoanList().size()){
				Loan loan = loans.getLoanAt(index);
				if(loan.isOverdue()){
					JOptionPane.showMessageDialog(
						editFrame,
						"Diese Ausleihe ist überfallig. Es muss eine Gebühr von 3.- CHF bezahlt werden.",
						Messages.getString("Ausleihe überfällig"),
						JOptionPane.YES_NO_OPTION
					);
				}
				library.returnLoan(loan);
				loan.returnCopy();
				copies.setCopyList(library.getAvailableCopies());
				btnReturnSelectedLoan.setEnabled(false);
				updateBtnAddLoan();
			}
		}
	}
	
	class ChangeCustomerAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public ChangeCustomerAction() {
			super();
		}
		
		public void actionPerformed(ActionEvent e) {
			Customer customer = (Customer) cmbCustomer.getSelectedItem();
			List<Loan> customerLoans = library.getCustomerLoans(customer);
			loans.setLoanList(customerLoans);
			txtCustomerId.setText("" + customer.getCustomerNo());
			txtCustomerId.setBackground(Color.WHITE);
			btnReturnSelectedLoan.setEnabled(false);
			updateBtnAddLoan();
			updateLabels();
		}
	}
}
