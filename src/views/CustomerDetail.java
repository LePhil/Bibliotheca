package views;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import viewModels.CopyListModel;

import domain.*;

public class CustomerDetail extends JFrame {
	private static final long serialVersionUID = 1L;

	private static Dictionary<Customer, CustomerDetail> editFramesDict = new Hashtable<Customer, CustomerDetail>();
	
	private Customer customer;
	private Library library;
	
	// Buttons
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnReset;
	private JButton btnDelete;
	
	// Labels
	private JLabel lblCustomerNo;
	private JLabel lblName;
	private JLabel lblAddress;
	
	// Textfields
	private JTextField txtCustomerNo;
	private JTextField txtName;
	private JTextField txtAddress;
	
	
	private static boolean newlyCreated = false;
	
	public CustomerDetail( Library library, Customer customer ) {
		super();
		this.library = library;
		this.customer = customer;
		initialize();
	}
	
	public static void editCustomer(Library library, Customer customer ) {
		if ( customer == null ) {
			// create new customer!
			customer = new Customer("", "");
			newlyCreated = true;
		}
		
		CustomerDetail editFrame = editFramesDict.get(customer);
		if ( editFrame == null ) {
			editFrame = new CustomerDetail(library, customer);
			editFramesDict.put( customer, editFrame );
		}
		
		editFrame.setVisible(true);
	}
	
	private void initialize() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			setBounds(100, 100, 466, 281);
			getContentPane().setLayout(
					new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

			/////////////////////////////////////////////////
			// ACTIONS
			/////////////////////////////////////////////////
			// Close (via Esc-Key (?), Button)
			AbstractAction cancel = new CloseAction( Messages.getString( "BookDetail.btnCancel.text"), "Revert Changes, close dialog" );
			// Save (via S, Button)
			AbstractAction save = new SaveAction( Messages.getString( "BookDetail.btnNewButton.text"), "Save changes" );
			// Reset (via R, Button)
			AbstractAction reset = new ResetAction(Messages.getString( "BookDetail.btnNewButton_1.text"), "Revert changes" );
			// Delete customer (via D, Button)
			
			/////////////////////////////////////////////////
			// INFORMATION PANEL
			/////////////////////////////////////////////////
			JPanel pnlInformation = new JPanel();
			pnlInformation.setBorder(new TitledBorder(
					new LineBorder(new Color(0, 0, 0)),
					Messages.getString("BookDetail.pnlInformation.borderTitle"), TitledBorder.LEADING, //$NON-NLS-1$
					TitledBorder.TOP, null, null));
			getContentPane().add(pnlInformation);
			
			GridBagLayout gbl_pnlInformation = new GridBagLayout();
			gbl_pnlInformation.columnWidths = new int[] { 0, 0, 0, 0, 0 };
			gbl_pnlInformation.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
			gbl_pnlInformation.columnWeights = new double[] { 0.0, 0.0, 1.0,
					0.0, Double.MIN_VALUE };
			gbl_pnlInformation.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0,
					Double.MIN_VALUE };
			pnlInformation.setLayout(gbl_pnlInformation);
			
			/////////////////////////////////////////////////
			// FIELDS
			/////////////////////////////////////////////////
			// CUSTOMER NO
			{
				lblCustomerNo = new JLabel( Messages.getString("BookDetail.lblTitle.text") );
				GridBagConstraints gbc_lblCustomerNo = new GridBagConstraints();
				gbc_lblCustomerNo.anchor = GridBagConstraints.EAST;
				gbc_lblCustomerNo.insets = new Insets(0, 0, 5, 5);
				gbc_lblCustomerNo.gridx = 1;
				gbc_lblCustomerNo.gridy = 0;
				pnlInformation.add(lblCustomerNo, gbc_lblCustomerNo);
				
				txtCustomerNo = new JTextField();
				txtCustomerNo.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						//validateInformation();
					}
				});
				GridBagConstraints gbc_txtCustomerNo = new GridBagConstraints();
				gbc_txtCustomerNo.insets = new Insets(0, 0, 5, 5);
				gbc_txtCustomerNo.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtCustomerNo.gridx = 2;
				gbc_txtCustomerNo.gridy = 0;
				pnlInformation.add(txtCustomerNo, gbc_txtCustomerNo);
				txtCustomerNo.setColumns(10);
			}
			
			// NAME
			{
				lblName = new JLabel( Messages.getString("BookDetail.lblTitle.text") );
				GridBagConstraints gbc_lblName = new GridBagConstraints();
				gbc_lblName.anchor = GridBagConstraints.EAST;
				gbc_lblName.insets = new Insets(0, 0, 5, 5);
				gbc_lblName.gridx = 1;
				gbc_lblName.gridy = 1;
				pnlInformation.add(lblName, gbc_lblName);
				
				txtName = new JTextField();
				txtName.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						//validateInformation();
					}
				});
				GridBagConstraints gbc_txtName = new GridBagConstraints();
				gbc_txtName.insets = new Insets(0, 0, 5, 5);
				gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtName.gridx = 2;
				gbc_txtName.gridy = 1;
				pnlInformation.add(txtName, gbc_txtName);
				txtName.setColumns(10);
			}
			
			// Address
			{
				lblAddress = new JLabel( Messages.getString("BookDetail.lblTitle.text") );
				GridBagConstraints gbc_lblAddress = new GridBagConstraints();
				gbc_lblAddress.anchor = GridBagConstraints.EAST;
				gbc_lblAddress.insets = new Insets(0, 0, 5, 5);
				gbc_lblAddress.gridx = 1;
				gbc_lblAddress.gridy = 2;
				pnlInformation.add(lblAddress, gbc_lblAddress);
				
				txtAddress = new JTextField();
				txtAddress.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						//validateInformation();
					}
				});
				GridBagConstraints gbc_txtAddress = new GridBagConstraints();
				gbc_txtAddress.insets = new Insets(0, 0, 5, 5);
				gbc_txtAddress.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtAddress.gridx = 2;
				gbc_txtAddress.gridy = 2;
				pnlInformation.add(txtAddress, gbc_txtAddress);
				txtAddress.setColumns(10);
			}
			
			fillForm();
			
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fill all textFields with the corresponding values from the model.
	 */
	private void fillForm() {
		txtCustomerNo.setText("0");
		txtName.setText( customer.getName()+" "+customer.getSurname() );
		txtAddress.setText( customer.getStreet()+"\n"+customer.getZip() );
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
		}
	}
	/**
	 * Saves the (changed) entries for the currently opened customer.
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
			
			/*book.setAuthor(txtAuthor.getText());
			book.setName(txtTitle.getText());
			book.setPublisher(txtPublisher.getText());
			book.setShelf((Shelf) cmbShelf.getSelectedItem());
			btnSave.setEnabled(false);
			*/
			
			if ( newlyCreated ) {
				// Saving a book that we just created.
				// we can only add it now, because before it shouldn't
				// belong to the library, only on saving.
				
				//library.addBook(book);
				
				//if ( library.getCopiesOfBook( book ).size() == 0 ) {
				//	library.createAndAddCopy( book );
				//}
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
			fillForm();
			btnSave.setEnabled(false);
		}
	}
}
