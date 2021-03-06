package views;

import i18n.Messages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import domain.Customer;
import domain.CustomerList;
import domain.Library;
import domain.Loan;

public class CustomerDetail extends JFrame {
	private static final long serialVersionUID = 1L;

	private static Dictionary<Customer, CustomerDetail> editFramesDict = new Hashtable<Customer, CustomerDetail>();

	private Library library;
	private Customer customer;
	private CustomerList customers;

	// Buttons
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnReset;
	private JButton btnDelete;

	// Labels
	private JLabel lblCustomerNo;
	private JLabel lblName;
	private JLabel lblSurname;
	private JLabel lblStreet;
	private JLabel lblZip;
	private JLabel lblCity;

	// Textfields
	private JTextField txtCustomerNo;
	private JTextField txtName;
	private JTextField txtSurname;
	private JTextField txtStreet;
	private JTextField txtZip;
	private JTextField txtCity;

	private static boolean newlyCreated;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;

	private static CustomerDetail editFrame;

	public CustomerDetail(Library library, CustomerList customers, Customer customer) {
		super();
		this.library = library;
		this.customers = customers;
		this.customer = customer;
		
		initialize();
	}

	public static void editCustomer(Library library, CustomerList customers, Customer customer) {
		newlyCreated = false;

		if (customer == null) {
			// create new customer!
			customer = new Customer(customers.getLatestCustomerNo() + 1, "", "");
			newlyCreated = true;
		}

		editFrame = editFramesDict.get(customer);
		if (editFrame == null) {
			editFrame = new CustomerDetail(library, customers, customer);
			editFramesDict.put(customer, editFrame);
		}
		editFrame.setVisible(true);
	}

	private void initialize() {
		try {
			setTitle( Messages.getString("CustomerDetail.this.title", customer.getName()+" "+customer.getSurname() ) );
			setMinimumSize( new Dimension(400, 300) );
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			getContentPane().setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );

			// ///////////////////////////////////////////////
			// ACTIONS
			// ///////////////////////////////////////////////
			// Close (via Esc-Key (?), Button)
			AbstractAction cancel = new CloseAction(
				Messages.getString("DetailDialogs.btnClose.text"),
				Messages.getString("DetailDialogs.btnClose.desc")
			);
			// Save (via S, Button)
			AbstractAction save = new SaveAction(
				Messages.getString("CustomerDetail.btnSave.text"),
				Messages.getString("CustomerDetail.btnSave.desc")
			);
			// Reset (via R, Button)
			AbstractAction reset = new ResetAction(
				Messages.getString("CustomerDetail.btnReset.text"),
				Messages.getString("CustomerDetail.btnReset.desc")
			);
			// Delete customer (via D, Button)
			AbstractAction delete = new DeleteAction(
				Messages.getString("CustomerDetail.btnDelete.text"),
				Messages.getString("CustomerDetail.btnDelete.desc")
			);

			// ///////////////////////////////////////////////
			// INFORMATION PANEL
			// ///////////////////////////////////////////////
			JPanel pnlInformation = new JPanel();
			pnlInformation.setBorder(new TitledBorder(new LineBorder(new Color(
					0, 0, 0)), Messages.getString("CustomerDetail.CustomerInfo.border.title"), TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			getContentPane().add(pnlInformation);

			GridBagLayout gbl_pnlInformation = new GridBagLayout();
			gbl_pnlInformation.columnWidths = new int[] { 0, 0, 0, 0, 0 };
			gbl_pnlInformation.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
			gbl_pnlInformation.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
			gbl_pnlInformation.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			pnlInformation.setLayout(gbl_pnlInformation);

			// ///////////////////////////////////////////////
			// FIELDS
			// ///////////////////////////////////////////////
			// CUSTOMER NO
			{
				lblCustomerNo = new JLabel( Messages.getString("CustomerDetail.CustomerNo") );
				GridBagConstraints gbc_lblCustomerNo = new GridBagConstraints();
				gbc_lblCustomerNo.anchor = GridBagConstraints.EAST;
				gbc_lblCustomerNo.insets = new Insets(0, 0, 5, 5);
				gbc_lblCustomerNo.gridx = 1;
				gbc_lblCustomerNo.gridy = 0;
				pnlInformation.add( lblCustomerNo, gbc_lblCustomerNo );

				txtCustomerNo = new JTextField();
				txtCustomerNo.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						validateInformation();
					}
				});
				GridBagConstraints gbc_txtCustomerNo = new GridBagConstraints();
				gbc_txtCustomerNo.insets = new Insets(0, 0, 5, 5);
				gbc_txtCustomerNo.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtCustomerNo.gridx = 2;
				gbc_txtCustomerNo.gridy = 0;
				pnlInformation.add(txtCustomerNo, gbc_txtCustomerNo);
				txtCustomerNo.setColumns(10);
				txtCustomerNo.setEnabled(false);
			}

			// NAME
			{
				lblName = new JLabel(Messages.getString("CustomerDetail.Name"));
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
						validateInformation();
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

			// SURNAME
			{
				lblSurname = new JLabel( Messages.getString("CustomerDetail.Surname") );
				GridBagConstraints gbc_lblSurname = new GridBagConstraints();
				gbc_lblSurname.anchor = GridBagConstraints.EAST;
				gbc_lblSurname.insets = new Insets(0, 0, 5, 5);
				gbc_lblSurname.gridx = 1;
				gbc_lblSurname.gridy = 2;
				pnlInformation.add(lblSurname, gbc_lblSurname);

				txtSurname = new JTextField();
				txtSurname.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						validateInformation();
					}
				});
				GridBagConstraints gbc_txtSurname = new GridBagConstraints();
				gbc_txtSurname.insets = new Insets(0, 0, 5, 5);
				gbc_txtSurname.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtSurname.gridx = 2;
				gbc_txtSurname.gridy = 2;
				pnlInformation.add(txtSurname, gbc_txtSurname);
				txtSurname.setColumns(10);
			}

			// Street
			{
				lblStreet = new JLabel( Messages.getString("CustomerDetail.Street") );
				GridBagConstraints gbc_lblStreet = new GridBagConstraints();
				gbc_lblStreet.anchor = GridBagConstraints.EAST;
				gbc_lblStreet.insets = new Insets(0, 0, 5, 5);
				gbc_lblStreet.gridx = 1;
				gbc_lblStreet.gridy = 3;
				pnlInformation.add(lblStreet, gbc_lblStreet);

				txtStreet = new JTextField();
				txtStreet.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						validateInformation();
					}
				});
				GridBagConstraints gbc_txtStreet = new GridBagConstraints();
				gbc_txtStreet.insets = new Insets(0, 0, 5, 5);
				gbc_txtStreet.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtStreet.gridx = 2;
				gbc_txtStreet.gridy = 3;
				pnlInformation.add(txtStreet, gbc_txtStreet);
				txtStreet.setColumns(10);
			}

			// ZIP
			lblZip = new JLabel(Messages.getString("CustomerDetail.Zip"));
			GridBagConstraints gbc_lblZip = new GridBagConstraints();
			gbc_lblZip.insets = new Insets(0, 0, 5, 5);
			gbc_lblZip.gridx = 1;
			gbc_lblZip.gridy = 4;
			pnlInformation.add(lblZip, gbc_lblZip);

			{
				panel = new JPanel();
				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.insets = new Insets(0, 0, 5, 5);
				gbc_panel.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel.gridx = 2;
				gbc_panel.gridy = 4;
				pnlInformation.add(panel, gbc_panel);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[] { 100, 0, 0, 0 };
				gbl_panel.rowHeights = new int[] { 0, 0 };
				gbl_panel.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
				gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
				panel.setLayout(gbl_panel);

				// ZIP Part 2
				{
					txtZip = new JTextField();
					txtZip.addKeyListener(new KeyAdapter() {
						@Override
						public void keyReleased(KeyEvent e) {
							validateInformation();
						}
					});
					GridBagConstraints gbc_txtZip = new GridBagConstraints();
					gbc_txtZip.insets = new Insets(0, 0, 0, 5);
					gbc_txtZip.fill = GridBagConstraints.HORIZONTAL;
					gbc_txtZip.gridx = 0;
					gbc_txtZip.gridy = 0;
					panel.add(txtZip, gbc_txtZip);
					txtZip.setColumns(3);

					// CITY
					lblCity = new JLabel( Messages.getString("CustomerDetail.City") );
					GridBagConstraints gbc_lblCity = new GridBagConstraints();
					gbc_lblCity.insets = new Insets(0, 0, 0, 5);
					gbc_lblCity.gridx = 1;
					gbc_lblCity.gridy = 0;
					panel.add(lblCity, gbc_lblCity);

					txtCity = new JTextField();
					txtCity.addKeyListener(new KeyAdapter() {
						@Override
						public void keyReleased(KeyEvent e) {
							validateInformation();
						}
					});
					GridBagConstraints gbc_txtCity = new GridBagConstraints();
					gbc_txtCity.fill = GridBagConstraints.HORIZONTAL;
					gbc_txtCity.gridx = 2;
					gbc_txtCity.gridy = 0;
					panel.add( txtCity, gbc_txtCity );
					txtCity.setColumns(10);
				}
			}

			panel_2 = new JPanel();
			FlowLayout flowLayout = ( FlowLayout ) panel_2.getLayout();
			flowLayout.setAlignment( FlowLayout.RIGHT );
			GridBagConstraints gbc_panel_2 = new GridBagConstraints();
			gbc_panel_2.insets = new Insets(0, 0, 0, 5);
			gbc_panel_2.fill = GridBagConstraints.BOTH;
			gbc_panel_2.gridx = 2;
			gbc_panel_2.gridy = 5;
			pnlInformation.add(panel_2, gbc_panel_2);

			btnDelete = new JButton(delete);
			panel_2.add(btnDelete);
			
			btnReset = new JButton( reset );
			panel_2.add(btnReset);

			btnSave = new JButton(save);
			panel_2.add(btnSave);
			btnSave.setEnabled(false);

			// Buttons
			{
				panel_1 = new JPanel();
				getContentPane().add(panel_1);
				panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

				btnCancel = new JButton(cancel);
				btnCancel.setIcon( new ImageIcon("icons/close_32.png") );
				panel_1.add(btnCancel);
			}
			
			rootPane.setDefaultButton( btnCancel );

			fillForm();
			
			// Add Escape-Action ( close dialog )
			KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
			InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		    inputMap.put(stroke, "ESCAPE");
		    rootPane.getActionMap().put( "ESCAPE", cancel );

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fill all textFields with the corresponding values from the model.
	 * 
	 * @author PCHR
	 */
	private void fillForm() {
		if (newlyCreated) {
			txtCustomerNo.setText("" + customer.getCustomerNo());
			txtName.setText("");
			txtSurname.setText("");
			txtStreet.setText("");
			txtZip.setText("");
			txtCity.setText("");

			btnDelete.setEnabled(false);
		} else {
			txtCustomerNo.setText("" + customer.getCustomerNo());
			txtName.setText(customer.getName());
			txtSurname.setText(customer.getSurname());
			txtStreet.setText(customer.getStreet());
			txtZip.setText("" + customer.getZip());
			txtCity.setText(customer.getCity());
		}
		btnReset.setEnabled(false);
	}

	/**
	 * Checks if the entered data is valid.
	 * 
	 * @author PCHR
	 */
	private void validateInformation() {
		boolean validated = true;

		if (txtName.getText().length() == 0
				|| txtSurname.getText().length() == 0
				|| txtStreet.getText().length() == 0
				|| txtZip.getText().length() == 0
				|| txtCity.getText().length() == 0) {
			validated = false;
		}

		try {
			Integer.parseInt(txtZip.getText());
		} catch (NumberFormatException e) {
			validated = false;
		}

		// enable the save button if the form has been validated.
		btnSave.setEnabled(validated);
		
		// in any way, enable the reset button
		btnReset.setEnabled( true );
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

	/**
	 * Saves the (changed) entries for the currently opened customer.
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
			if (customer.getCity() != txtCity.getText()) {
				customer.setCity(txtCity.getText());
			}
			if (customer.getName() != txtName.getText()) {
				customer.setName(txtName.getText());
			}
			if (customer.getSurname() != txtSurname.getText()) {
				customer.setSurname(txtSurname.getText());
			}
			if (customer.getStreet() != txtStreet.getText()) {
				customer.setStreet(txtStreet.getText());
			}
			if (customer.getZip() != Integer.valueOf(txtZip.getText())) {
				customer.setZip(Integer.valueOf(txtZip.getText()));
			}
			if (customer.getCity() != txtCity.getText()) {
				customer.setCity(txtCity.getText());
			}

			btnSave.setEnabled(false);
			btnReset.setEnabled(false);

			if (newlyCreated) {
				// Saving a book that we just created.
				// we can only add it now, because before it shouldn't
				// belong to the library, only on saving.

				customers.addCustomer(customer);

				newlyCreated = false;
				btnDelete.setEnabled(true);
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
			fillForm();
			btnSave.setEnabled(false);
		}
	}

	/**
	 * Deletes this customer.
	 * question "really wanna do this?"
	 * 
	 * @author PCHR
	 */
	class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public DeleteAction(String text, String desc) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, KeyEvent.VK_D);
		}

		public void actionPerformed(ActionEvent e) {
			// default icon, custom title
			Object[] options = {
				Messages.getString("Common.Yes"),
				Messages.getString("Common.No")
			};
			int delete = JOptionPane.showOptionDialog(
				editFrame,
				Messages.getString("CustomerDetail.deleteDlg.message"),
				Messages.getString("CustomerDetail.deleteDlg.title"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, 
                null, options, options[1]
            );
			
			if (delete == 0) {
				List<Loan> customerLoans = library.getCustomerLoans(customer);
				List<Loan> lentLoans = new ArrayList<>();
				for(Loan loan : customerLoans){
					if(loan.isLent()){
						lentLoans.add(loan);
					}
				}
				if(lentLoans.size() > 0){
					JOptionPane.showMessageDialog(editFrame, Messages.getString("CustomerDetail.deleteDlg.error"));
				}else {
					if (customers.removeCustomer(customer)) { // SUCCESS
						dispose();
					} else { // FAILED.
						try {
							throw new Exception( "Deleting that customer didn't really work. Sorry about that, please restart the application.");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
}
