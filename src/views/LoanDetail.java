package views;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

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
	private JTextField txtCustomerId;
	private JTextField txtCopyId;
	private JTextField txtReturnDate;
	private JTable customerLoanTable;
	private CustomerLoanTableModel customerLoanTableModel;
	private Loan loan;
	private Library library;
	private TableRowSorter<CustomerLoanTableModel> sorter;
	private static Dictionary<Loan, LoanDetail> loanFramesDict = new Hashtable<Loan, LoanDetail>();
	
	private JLabel lblCustomerId;
	private JLabel lblCustomer;
	private JComboBox<Customer> cmbCustomer;
	private JPanel pnlLoanCopy;
	private JPanel pnlLoans;
	private JButton btnCopy;
	private JLabel lblReturnDate;
	private JPanel pnlCustomerLoanInfo;
	private JLabel lblAnzahlAusleihen;
	private JPanel pnlCustomerLoans;
	private JScrollPane scrollPane;
	private JPanel pnlCustomer;

	// /**
	// * Launch the application.
	// */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// LoanDetail window = new LoanDetail();
	// window.frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	/**
	 * Create the application.
	 */
	public LoanDetail(Loan loan, LoanList loanList, Library library) {
		this.loan = loan;
		this.customerLoanTableModel = new CustomerLoanTableModel(loanList);
		this.library = library;
		initialize();
	}

	public static void editLoan(Loan loan, Library library) {
		LoanList loans = new LoanList();
		if(loan.getCustomer() != null){
			List<Loan> customerLoans = library.getCustomerLoans(loan.getCustomer());
			if(customerLoans != null){
				loans.setLoanList(customerLoans);
			}
		}
		
		LoanDetail loanFrame = loanFramesDict.get(loan);
		if (loanFrame == null) {
			loanFrame = new LoanDetail(loan, loans, library);
			// editFrame.setBook(book);
			loanFramesDict.put(loan, loanFrame);
		}
		loanFrame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		pnlCustomer = new JPanel();
		pnlCustomer.setBorder(new TitledBorder(new LineBorder(
				new Color(0, 0, 0)), "Kundenauswahl", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		getContentPane().add(pnlCustomer);
		GridBagLayout gbl_pnlCustomer = new GridBagLayout();
		gbl_pnlCustomer.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_pnlCustomer.rowHeights = new int[] { 0, 0, 0 };
		gbl_pnlCustomer.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_pnlCustomer.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		pnlCustomer.setLayout(gbl_pnlCustomer);

		lblCustomerId = new JLabel("Kennung");
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

		lblCustomer = new JLabel("Kunde");
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
		for(Customer customer : library.getCustomers()){
			cmbCustomer.addItem(customer);
		}
		cmbCustomer.setSelectedItem(loan.getCustomer());
		pnlCustomer.add(cmbCustomer, gbc_cmbCustomer);
		

		pnlLoanCopy = new JPanel();
		pnlLoanCopy.setBorder(new TitledBorder(new LineBorder(
				new Color(0, 0, 0)), "Neues Exemplar ausleihen",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(pnlLoanCopy);
		GridBagLayout gbl_pnlLoanCopy = new GridBagLayout();
		gbl_pnlLoanCopy.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_pnlLoanCopy.rowHeights = new int[] { 0, 0, 0 };
		gbl_pnlLoanCopy.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_pnlLoanCopy.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		pnlLoanCopy.setLayout(gbl_pnlLoanCopy);

		JLabel lblCopyId = new JLabel("Exemplar-ID");
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
		txtCopyId.setText(loan.getCopy().getInventoryNumber() + "");

		btnCopy = new JButton("Exemplar");
		GridBagConstraints gbc_btnCopy = new GridBagConstraints();
		gbc_btnCopy.insets = new Insets(0, 0, 5, 5);
		gbc_btnCopy.gridx = 3;
		gbc_btnCopy.gridy = 0;
		pnlLoanCopy.add(btnCopy, gbc_btnCopy);

		lblReturnDate = new JLabel("Zurück Am");
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
		txtReturnDate.setText(loan.getReturnDate().toString());

		pnlLoans = new JPanel();
		pnlLoans.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)),
				"Ausleihen von Peter Possum", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
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
		lblAnzahlAusleihen = new JLabel("Anzahl Ausleihen: 2");
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

}
