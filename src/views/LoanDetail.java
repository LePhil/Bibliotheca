package views;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableRowSorter;

import viewModels.CustomerLoanTableModel;
import domain.Customer;
import domain.Library;
import domain.Loan;
import domain.LoanList;

public class LoanDetail extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static Dictionary<Loan, LoanDetail> loanFramesDict = new Hashtable<Loan, LoanDetail>();
	
	private CustomerLoanTableModel customerLoanTableModel;
	private Loan loan;
	private LoanList loans;
	private Library library;
	
	// Buttons
	private JButton btnCopy;
	private JButton btnCancel;
	private JButton btnSave;
	
	// Labels
	private JLabel lblCustomerId;
	private JLabel lblCustomer;
	private JLabel lblReturnDate;
	private JLabel lblAnzahlAusleihen;
	
	// Textfields
	private JTextField txtCustomerId;
	private JTextField txtCopyId;
	private JTextField txtReturnDate;
	
	private JTable customerLoanTable;
	
	private TableRowSorter<CustomerLoanTableModel> sorter;
	
	private JComboBox<Customer> cmbCustomer;
	
	// Panels
	private JPanel pnlLoanCopy;
	private JPanel pnlLoans;
	private JPanel pnlCustomerLoanInfo;
	private JPanel pnlCustomerLoans;
	private JScrollPane scrollPane;
	private JPanel pnlCustomer;
	
	private static boolean newlyCreated;
	private static LoanDetail editFrame;
	private JPanel panel;

	/**
	 * Create the application.
	 */
	public LoanDetail(Loan loan, LoanList loanList, Library library) {
		super();
		this.loan = loan;
		this.loans = loanList;
		this.library = library;
		
		this.customerLoanTableModel = new CustomerLoanTableModel(loanList);
		initialize();
	}

	public static void editLoan(Loan loan, Library library) {
		newlyCreated = false;
		
		if ( loan == null ) {
			// create new loan
			loan = new Loan( null, null ); // TODO: add new loan. feels weird.
			newlyCreated = true;
		}
		
		LoanList loans = new LoanList();
		// get other loans from this customer and show them in the loanTable
		if(loan.getCustomer() != null){
			List<Loan> customerLoans = library.getCustomerLoans(loan.getCustomer());
			if(customerLoans != null){
				loans.setLoanList(customerLoans);
			}
		}
		
		editFrame = loanFramesDict.get(loan);
		if ( editFrame== null ) {
			editFrame = new LoanDetail(loan, loans, library);
			loanFramesDict.put( loan, editFrame );
		}
		editFrame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			setBounds(100, 100, 450, 300);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			getContentPane().setLayout(
					new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	
			/////////////////////////////////////////////////
			// ACTIONS
			/////////////////////////////////////////////////
			// Close (via Esc-Key (?), Button)
			AbstractAction cancel = new CloseAction( Messages.getString( "CustomerDetail.btnCancel.text"), "Revert Changes, close dialog" );
			// Save (via S, Button)
			AbstractAction save = new SaveAction( Messages.getString( "CustomerDetail.btnSave.text"), "Save changes" );
			// Delete customer (via D, Button)
			//AbstractAction delete = new DeleteAction( Messages.getString( "CustomerDetail.btnDelete.text"), "Delete this customer" );
					
			/////////////////////////////////////////////////
			// CUSTOMER PANEL
			/////////////////////////////////////////////////
			pnlCustomer = new JPanel();
			pnlCustomer.setBorder(new TitledBorder(new LineBorder(
					new Color(0, 0, 0)), Messages.getString( "LoanDetail.CustomerSelection.text" ), TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			getContentPane().add(pnlCustomer);
			GridBagLayout gbl_pnlCustomer = new GridBagLayout();
			gbl_pnlCustomer.columnWidths = new int[] { 0, 0, 0, 0, 0 };
			gbl_pnlCustomer.rowHeights = new int[] { 0, 0, 0 };
			gbl_pnlCustomer.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0,
					Double.MIN_VALUE };
			gbl_pnlCustomer.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			pnlCustomer.setLayout(gbl_pnlCustomer);
	
			lblCustomerId = new JLabel(Messages.getString("LoanDetail.lblCustomerId.text")); //$NON-NLS-1$
			GridBagConstraints gbc_lblCustomerId = new GridBagConstraints();
			gbc_lblCustomerId.insets = new Insets(0, 0, 5, 5);
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
			// TODO pforster: welche nummer ist die Kennnummer?
			txtCustomerId.setText("156");
	
			lblCustomer = new JLabel(Messages.getString("LoanDetail.lblCustomer.text")); //$NON-NLS-1$
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
			for(Customer customer : library.getCustomerList().getCustomers()){
				cmbCustomer.addItem(customer);
			}
			cmbCustomer.setSelectedItem(loan.getCustomer());
			pnlCustomer.add(cmbCustomer, gbc_cmbCustomer);
			
	
			pnlLoanCopy = new JPanel();
			pnlLoanCopy.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), Messages.getString( "LoanDetail.loanNewTitle.text" ), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(pnlLoanCopy);
			GridBagLayout gbl_pnlLoanCopy = new GridBagLayout();
			gbl_pnlLoanCopy.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
			gbl_pnlLoanCopy.rowHeights = new int[] { 0, 0, 0 };
			gbl_pnlLoanCopy.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0,
					Double.MIN_VALUE };
			gbl_pnlLoanCopy.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			pnlLoanCopy.setLayout(gbl_pnlLoanCopy);
	
			JLabel lblCopyId = new JLabel(Messages.getString("LoanDetail.lblCopyId.text")); //$NON-NLS-1$
			GridBagConstraints gbc_lblCopyId = new GridBagConstraints();
			gbc_lblCopyId.insets = new Insets(0, 0, 5, 5);
			gbc_lblCopyId.anchor = GridBagConstraints.EAST;
			gbc_lblCopyId.gridx = 1;
			gbc_lblCopyId.gridy = 0;
			pnlLoanCopy.add(lblCopyId, gbc_lblCopyId);
	
			txtCopyId = new JTextField();
			GridBagConstraints gbc_txtCopyId = new GridBagConstraints();
			gbc_txtCopyId.insets = new Insets(0, 0, 5, 5);
			gbc_txtCopyId.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtCopyId.gridx = 2;
			gbc_txtCopyId.gridy = 0;
			pnlLoanCopy.add(txtCopyId, gbc_txtCopyId);
			txtCopyId.setColumns(10);
			
			if ( loan.getCopy() != null ) {
				txtCopyId.setText( loan.getCopy().getInventoryNumber() + "" );
			} else {
				txtCopyId.setText( "" );
			}
	
			btnCopy = new JButton(Messages.getString("LoanDetail.btnCopy.text")); //$NON-NLS-1$
			GridBagConstraints gbc_btnCopy = new GridBagConstraints();
			gbc_btnCopy.insets = new Insets(0, 0, 5, 5);
			gbc_btnCopy.gridx = 3;
			gbc_btnCopy.gridy = 0;
			pnlLoanCopy.add(btnCopy, gbc_btnCopy);
	
			lblReturnDate = new JLabel(Messages.getString("LoanDetail.lblReturnDate.text")); //$NON-NLS-1$
			GridBagConstraints gbc_lblReturnDate = new GridBagConstraints();
			gbc_lblReturnDate.anchor = GridBagConstraints.EAST;
			gbc_lblReturnDate.insets = new Insets(0, 0, 0, 5);
			gbc_lblReturnDate.gridx = 1;
			gbc_lblReturnDate.gridy = 1;
			pnlLoanCopy.add(lblReturnDate, gbc_lblReturnDate);
	
			txtReturnDate = new JTextField();
			txtReturnDate.setEnabled(false);
			GridBagConstraints gbc_txtReturnDate = new GridBagConstraints();
			gbc_txtReturnDate.insets = new Insets(0, 0, 0, 5);
			gbc_txtReturnDate.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtReturnDate.gridx = 2;
			gbc_txtReturnDate.gridy = 1;
			pnlLoanCopy.add(txtReturnDate, gbc_txtReturnDate);
			txtReturnDate.setColumns(10);
			
			// PCHR: only show returnDate if copy was acutally returned!
			if ( !loan.isLent() ) {
				txtReturnDate.setText(loan.getReturnDate().toString());
			} else {
				txtReturnDate.setText("-");
				txtReturnDate.setEnabled(false);
			}
	
			pnlLoans = new JPanel();
			pnlLoans.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Loans of $PARAM", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(pnlLoans);
			GridBagLayout gbl_pnlLoans = new GridBagLayout();
			gbl_pnlLoans.columnWidths = new int[] { 0, 0 };
			gbl_pnlLoans.rowHeights = new int[] { 0, 0, 0 };
			gbl_pnlLoans.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_pnlLoans.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
			pnlLoans.setLayout(gbl_pnlLoans);
	
			pnlCustomerLoanInfo = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnlCustomerLoanInfo.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			GridBagConstraints gbc_pnlCustomerLoanInfo = new GridBagConstraints();
			gbc_pnlCustomerLoanInfo.insets = new Insets(0, 0, 5, 0);
			gbc_pnlCustomerLoanInfo.fill = GridBagConstraints.BOTH;
			gbc_pnlCustomerLoanInfo.gridx = 0;
			gbc_pnlCustomerLoanInfo.gridy = 0;
			pnlLoans.add(pnlCustomerLoanInfo, gbc_pnlCustomerLoanInfo);
	
			// TODO pforster: make dynamic
			lblAnzahlAusleihen = new JLabel( Messages.getString( "LoanDetail.nrOfLoansOfCustomer.text", "2") );
			pnlCustomerLoanInfo.add(lblAnzahlAusleihen);
	
			pnlCustomerLoans = new JPanel();
			GridBagConstraints gbc_pnlCustomerLoans = new GridBagConstraints();
			gbc_pnlCustomerLoans.fill = GridBagConstraints.BOTH;
			gbc_pnlCustomerLoans.gridx = 0;
			gbc_pnlCustomerLoans.gridy = 1;
			pnlLoans.add(pnlCustomerLoans, gbc_pnlCustomerLoans);
			pnlCustomerLoans.setLayout(new BoxLayout(pnlCustomerLoans,
					BoxLayout.X_AXIS));
	
			scrollPane = new JScrollPane();
			scrollPane.setPreferredSize(new java.awt.Dimension(200, 100));
			{
				customerLoanTable = new JTable();
				initTable();
			}
			scrollPane.setViewportView(customerLoanTable);
			pnlCustomerLoans.add(scrollPane);
			
			panel = new JPanel();
			getContentPane().add(panel);
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}

	private void initTable() {
		customerLoanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customerLoanTable.setModel(customerLoanTableModel);
		sorter = new TableRowSorter<CustomerLoanTableModel>(
				customerLoanTableModel);
		customerLoanTable.setRowSorter(sorter);

		// filter = new RowFilter<Object, Object>() {
		// public boolean include(Entry entry) {
		// if (showCompleted){
		// return true;
		// }
		// // get value of Completed column (column 2)
		// Boolean completed = (Boolean) entry.getValue(2);
		// return ! completed.booleanValue();
		// }
		// };
		//
		// sorter.setRowFilter(filter);

		// TableColumn copyIdColumn =
		// customerLoanTable.getColumnModel().getColumn(0);
		// TableColumn copyTitleColumn =
		// customerLoanTable.getColumnModel().getColumn(1);
		// TableColumn copyAuthorColumn =
		// customerLoanTable.getColumnModel().getColumn(2);
	}
	
	/////////////////////////////////////////////////
	// Action Subclasses
	/////////////////////////////////////////////////
	/**
	 * Closes the current dialog.
	 * TODO: Close dialog, disregard changes. Don't save!
	 * @author PCHR
	 */
	class CloseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public CloseAction( String text, String desc ) {
			super( text );
			putValue( SHORT_DESCRIPTION, desc );
			putValue( MNEMONIC_KEY, KeyEvent.VK_C );
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("CLOSE DIALOG NOW.");
			editFrame.setVisible(false);
		}
	}
	/**
	 * Saves the (changed) entries for the currently opened loan.
	 * @author PCHR
	 */
	class SaveAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public SaveAction( String text, String desc ) {
			super( text );
			putValue( SHORT_DESCRIPTION, desc );
			putValue( MNEMONIC_KEY, KeyEvent.VK_S );
		}
		public void actionPerformed(ActionEvent e) {
			/*
			if ( customer.getCity() != txtCity.getText() ) {
				customer.setCity( txtCity.getText() );
			}
			if ( customer.getName() != txtName.getText() ) {
				customer.setName( txtName.getText() );
			}
			if ( customer.getSurname() != txtSurname.getText() ) {
				customer.setSurname( txtSurname.getText() );
			}
			if ( customer.getStreet() != txtStreet.getText() ) {
				customer.setStreet( txtStreet.getText() );
			}
			if ( customer.getZip() != Integer.valueOf( txtZip.getText() ) ) {
				customer.setZip( Integer.valueOf( txtZip.getText() ) );
			}
			if ( customer.getCity() != txtCity.getText() ) {
				customer.setCity( txtCity.getText() );
			}
			
			btnSave.setEnabled(false);
			btnReset.setEnabled(false);
			*/
			
			if ( newlyCreated ) {
				// Saving a loan that we just created.
				// we can only add it now, because before it shouldn't
				// belong to the library, only on saving.
				
				loans.addLoan( loan );
				
				newlyCreated = false;
				//btnDelete.setEnabled(true);
			}
		}
	}
	/**
	 * Reset the form
	 * @author PCHR
	 */
	class ResetAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public ResetAction( String text, String desc ) {
			super( text );
			putValue( SHORT_DESCRIPTION, desc );
			putValue( MNEMONIC_KEY, KeyEvent.VK_R );
		}
		public void actionPerformed(ActionEvent e) {
			//fillForm();
			//btnSave.setEnabled(false);
		}
	}

}
