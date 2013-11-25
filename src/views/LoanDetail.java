package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
	private JButton btnSelektierteAusleiheAbschliessen;
	private JButton btnExemplarAusleihen;
	private JButton btnClose;

	// Labels
	private JLabel lblCustomerId;
	private JLabel lblCustomer;
	private JLabel lblAnzahlAusleihen;

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

	private static boolean newlyCreated;
	private static LoanDetail editFrame;

	private static int customerNo;

	/**
	 * Create the application.
	 */
	public LoanDetail(Loan loan, LoanList loanList, Library library) {
		super();
		this.loan = loan;
		this.loans = loanList;
		this.copies = new CopyList();
		this.copies.setCopyList(library.getAvailableCopies());
		this.library = library;

		this.customerLoanTableModel = new CustomerLoanTableModel(loanList);
		this.copyTableModel = new CopyTableModel(this.copies);
		this.copyFilters = new ArrayList<RowFilter<Object,Object>>();
		initialize();
	}

	public static void editLoan( Loan loan, Library library ) {
		newlyCreated = false;

		if ( loan == null ) {
			// create new loan
			loan = new Loan( null, null ); // TODO: add new loan. feels weird.
			newlyCreated = true;
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
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			setBounds(100, 100, 900, 600);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			getContentPane().setLayout(
					new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

			// ///////////////////////////////////////////////
			// CUSTOMER PANEL
			// ///////////////////////////////////////////////
			pnlCustomer = new JPanel();
			pnlCustomer.setMaximumSize(new Dimension(32767, 50));
			pnlCustomer.setBorder(new TitledBorder(new LineBorder(new Color(0,
					0, 0)), Messages
					.getString("LoanDetail.CustomerSelection.text"),
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(pnlCustomer);
			GridBagLayout gbl_pnlCustomer = new GridBagLayout();
			gbl_pnlCustomer.columnWidths = new int[] { 0, 0, 0, 0, 0 };
			gbl_pnlCustomer.rowHeights = new int[] { 0, 0, 0 };
			gbl_pnlCustomer.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0,
					Double.MIN_VALUE };
			gbl_pnlCustomer.rowWeights = new double[] { 0.0, 0.0,
					Double.MIN_VALUE };
			pnlCustomer.setLayout(gbl_pnlCustomer);

			lblCustomerId = new JLabel(
					Messages.getString("LoanDetail.lblCustomerId.text")); //$NON-NLS-1$
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
			
			if(loan.getCustomer() != null){
				txtCustomerId.setText("" + loan.getCustomer().getCustomerNo());
			}
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
						}
			        } catch (NumberFormatException ex) {
				           handleInvalidCustomerID();
			        }
				}

				private void handleInvalidCustomerID() {
					System.out.println("Invalid Customer ID");
					txtCustomerId.setBackground(Color.PINK);
				}
			});

			lblCustomer = new JLabel(
					Messages.getString("LoanDetail.lblCustomer.text")); //$NON-NLS-1$
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
			for (Customer customer : library.getCustomerList().getCustomers()) {
				cmbCustomer.addItem(customer);
			}
			cmbCustomer.setSelectedItem(loan.getCustomer());
			cmbCustomer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Customer customer = (Customer) cmbCustomer.getSelectedItem();
					List<Loan> customerLoans = library.getCustomerLoans(customer);
					loans.setLoanList(customerLoans);
					txtCustomerId.setText("" + customer.getCustomerNo());
					txtCustomerId.setBackground(Color.WHITE);
					lblAnzahlAusleihen.setText(Messages.getString("LoanDetail.nrOfLoansOfCustomer.text", customerLoans.size() + ""));
					btnSelektierteAusleiheAbschliessen.setEnabled(false);
				}
			});
			pnlCustomer.add(cmbCustomer, gbc_cmbCustomer);

			// ///////////////////////////////////////////////
			// LOANS PANEL
			// ///////////////////////////////////////////////
			pnlLoans = new JPanel();
			pnlLoans.setBorder(new TitledBorder(new LineBorder(new Color(0, 0,
					0)), "Loans of $PARAM", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			pnlLoans.setMaximumSize(new Dimension(32767, 50));
			getContentPane().add(pnlLoans);
			GridBagLayout gbl_pnlLoans = new GridBagLayout();
			gbl_pnlLoans.columnWidths = new int[] { 0, 0 };
			gbl_pnlLoans.rowHeights = new int[] { 0, 0, 0, 0 };
			gbl_pnlLoans.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_pnlLoans.rowWeights = new double[] { 0.0, 1.0, 0.0,
					Double.MIN_VALUE };
			pnlLoans.setLayout(gbl_pnlLoans);

			pnlCustomerLoanInfo = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnlCustomerLoanInfo
					.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			GridBagConstraints gbc_pnlCustomerLoanInfo = new GridBagConstraints();
			gbc_pnlCustomerLoanInfo.insets = new Insets(0, 0, 5, 0);
			gbc_pnlCustomerLoanInfo.fill = GridBagConstraints.BOTH;
			gbc_pnlCustomerLoanInfo.gridx = 0;
			gbc_pnlCustomerLoanInfo.gridy = 0;
			pnlLoans.add(pnlCustomerLoanInfo, gbc_pnlCustomerLoanInfo);

			lblAnzahlAusleihen = new JLabel(Messages.getString(
					"LoanDetail.nrOfLoansOfCustomer.text", loans.getLoanList().size() + ""));
			pnlCustomerLoanInfo.add(lblAnzahlAusleihen);

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

			btnSelektierteAusleiheAbschliessen = new JButton( Messages.getString( "LoanDetail.returnCopyBtn.text" ) );
			GridBagConstraints gbc_btnSelektierteAusleiheAbschliessen = new GridBagConstraints();
			gbc_btnSelektierteAusleiheAbschliessen.anchor = GridBagConstraints.WEST;
			gbc_btnSelektierteAusleiheAbschliessen.gridx = 0;
			gbc_btnSelektierteAusleiheAbschliessen.gridy = 2;
			btnSelektierteAusleiheAbschliessen.setEnabled(false);
			btnSelektierteAusleiheAbschliessen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int index = customerLoanTable.getSelectedRow();
					if(index < loans.getLoanList().size()){
						Loan loan = loans.getLoanAt(index);
						for(Loan l : library.getLoans()){
							if(loan.equals(l)){
								l.returnCopy();
							}
						}
						loan.returnCopy();
						loans.notifyObservers();
						copies.setCopyList(library.getAvailableCopies());
						btnSelektierteAusleiheAbschliessen.setEnabled(false);
					}
				}
			});
			
			pnlLoans.add( btnSelektierteAusleiheAbschliessen, gbc_btnSelektierteAusleiheAbschliessen );

			// ///////////////////////////////////////////////
			// COPIES PANEL
			// ///////////////////////////////////////////////
			pnlCopies = new JPanel();
			pnlCopies.setBorder(new TitledBorder(new LineBorder(new Color(0, 0,
					0)), "Exemplare", TitledBorder.LEADING, TitledBorder.TOP,
					null, null));
			getContentPane().add(pnlCopies);
			GridBagLayout gbl_pnlCopies = new GridBagLayout();
			gbl_pnlCopies.columnWidths = new int[] { 0, 0 };
			gbl_pnlCopies.rowHeights = new int[] { 0, 0, 0, 0 };
			gbl_pnlCopies.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_pnlCopies.rowWeights = new double[] { 0.0, 1.0, 0.0,
					Double.MIN_VALUE };
			pnlCopies.setLayout(gbl_pnlCopies);
			
			pnlFilterCopies = new JPanel();
			FlowLayout flowLayoutFilter = (FlowLayout) pnlFilterCopies
					.getLayout();
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
			pnlAvailableCopies.setLayout(new BoxLayout(pnlAvailableCopies,
					BoxLayout.Y_AXIS));
			pnlCopies.add(pnlAvailableCopies, gbc_pnlCopies);

			scrollPaneCopies = new JScrollPane();
			{
				copyTable = new JTable();
				initCopyTable();
			}
			scrollPaneCopies.setViewportView(copyTable);
			pnlAvailableCopies.add(scrollPaneCopies);

			btnExemplarAusleihen = new JButton("Exemplar ausleihen");
			GridBagConstraints gbc_btnExemplarAusleihen = new GridBagConstraints();
			gbc_btnExemplarAusleihen.anchor = GridBagConstraints.WEST;
			gbc_btnExemplarAusleihen.gridx = 0;
			gbc_btnExemplarAusleihen.gridy = 2;
			btnExemplarAusleihen.setEnabled( false );
			btnExemplarAusleihen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = copyTable.convertRowIndexToModel(copyTable.getSelectedRow());
					Copy copy = copies.getCopyAt(selectedRow);
					Customer customer = (Customer) cmbCustomer.getSelectedItem();
					Loan newLoan = new Loan(customer, copy);
					library.getLoans().add(newLoan);
					copies.setCopyList(library.getAvailableCopies());
					loans.addLoan(newLoan); 
					lblAnzahlAusleihen.setText(Messages.getString("LoanDetail.nrOfLoansOfCustomer.text", loans.getLoanList().size() + ""));
				}
			});
			pnlCopies.add(btnExemplarAusleihen, gbc_btnExemplarAusleihen);
			
			
			// ///////////////////////////////////////////////
			// BUTTONS PANEL
			// ///////////////////////////////////////////////
			pnlButtons = new JPanel();
			pnlButtons.setMaximumSize(new Dimension(32767, 50));
			getContentPane().add(pnlButtons);
			pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
	
			pnlButtons.add(Box.createHorizontalGlue());
			
			btnClose = new JButton();
			btnClose.setText(Messages.getString( "MainView.btnExit.text"));
			btnClose.setIcon( new ImageIcon("icons/close_32.png") );
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editFrame.setVisible(false);
				}
			});
			pnlButtons.add(btnClose);
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
		// TODO pforster: why aren't the loans which are overdue not red?
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
				boolean isOld = value.equals(2);
				
				if ( isOverdue ) {
					label.setIcon( new ImageIcon("icons/warning_16.png") );
					label.setText( Messages.getString("LoanTable.CellContent.Overdue") );
				} else if ( isLent ) {
					label.setText( Messages.getString("LoanTable.CellContent.Lent") );
					label.setIcon( new ImageIcon("icons/ok_button_16.png") );
				} else {
					label.setText( Messages.getString("LoanTable.CellContent.Old") );
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
							int index = customerLoanTable.getSelectedRow();
							if(index >= 0 && index < loans.getLoanList().size()){
								Loan loan = loans.getLoanAt(index);
								boolean isLent = loan != null && loan.isLent();
								btnSelektierteAusleiheAbschliessen.setEnabled(isLent);
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
		btnExemplarAusleihen.setEnabled( copyTable.getSelectedRowCount()>0);
	}
	
	private void updateCustomerDropdown(Customer customer) {
		if (customer != null) {
			cmbCustomer.setSelectedItem(customer);
		} else {
			cmbCustomer.setSelectedIndex(0);
		}
	}

	// ///////////////////////////////////////////////
	// Action Subclasses TODO pforster: use them or delete them
	// ///////////////////////////////////////////////
	/**
	 * Closes the current dialog. TODO: Close dialog, disregard changes. Don't
	 * save!
	 * 
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
			System.out.println("CLOSE DIALOG NOW.");
			editFrame.setVisible(false);
		}
	}

	/**
	 * Saves the (changed) entries for the currently opened loan.
	 * 
	 * @author PCHR
	 */
	class SaveAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SaveAction(String text, String desc) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, KeyEvent.VK_S);
		}

		public void actionPerformed(ActionEvent e) {
			/*
			 * if ( customer.getCity() != txtCity.getText() ) {
			 * customer.setCity( txtCity.getText() ); } if ( customer.getName()
			 * != txtName.getText() ) { customer.setName( txtName.getText() ); }
			 * if ( customer.getSurname() != txtSurname.getText() ) {
			 * customer.setSurname( txtSurname.getText() ); } if (
			 * customer.getStreet() != txtStreet.getText() ) {
			 * customer.setStreet( txtStreet.getText() ); } if (
			 * customer.getZip() != Integer.valueOf( txtZip.getText() ) ) {
			 * customer.setZip( Integer.valueOf( txtZip.getText() ) ); } if (
			 * customer.getCity() != txtCity.getText() ) { customer.setCity(
			 * txtCity.getText() ); }
			 * 
			 * btnSave.setEnabled(false); btnReset.setEnabled(false);
			 */

			if (newlyCreated) {
				// Saving a loan that we just created.
				// we can only add it now, because before it shouldn't
				// belong to the library, only on saving.

				loans.addLoan(loan);

				newlyCreated = false;
				// btnDelete.setEnabled(true);
			}
		}
	}

	/**
	 * Reset the form
	 * 
	 * @author PCHR
	 */
	class ResetAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public ResetAction(String text, String desc) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, KeyEvent.VK_R);
		}

		public void actionPerformed(ActionEvent e) {
			// fillForm();
			// btnSave.setEnabled(false);
		}
	}

}
