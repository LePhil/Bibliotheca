package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class BookDetail {

	private JFrame frame;
	private JTextField txtTitle;
	private JTextField txtAuthor;
	private JTextField txtPublisher;
	private JPanel pnlInformation;
	private JLabel lblTitle;
	private JLabel lblAuthor;
	private JLabel lblPublisher;
	private JLabel lblShelf;
	private JComboBox cmbShelf;
	private JPanel pnlSpecimensEdit;
	private JPanel pnlAction;
	private JLabel lblAmount;
	private JButton btnRemove;
	private JButton btnAdd;
	private JPanel pnlSpecimens;
	private JList lstSpecimens;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookDetail window = new BookDetail();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BookDetail() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		pnlInformation = new JPanel();
		pnlInformation.setBorder(new TitledBorder(new LineBorder(new Color(0,
				0, 0)), Messages.getString("BookDetail.pnlInformation.borderTitle"), TitledBorder.LEADING, //$NON-NLS-1$
				TitledBorder.TOP, null, null));
		frame.getContentPane().add(pnlInformation);
		GridBagLayout gbl_pnlInformation = new GridBagLayout();
		gbl_pnlInformation.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_pnlInformation.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_pnlInformation.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_pnlInformation.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		pnlInformation.setLayout(gbl_pnlInformation);

		lblTitle = new JLabel(Messages.getString("BookDetail.lblTitle.text")); //$NON-NLS-1$
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.anchor = GridBagConstraints.EAST;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 1;
		gbc_lblTitle.gridy = 0;
		pnlInformation.add(lblTitle, gbc_lblTitle);

		txtTitle = new JTextField();
		GridBagConstraints gbc_txtTitle = new GridBagConstraints();
		gbc_txtTitle.insets = new Insets(0, 0, 5, 5);
		gbc_txtTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTitle.gridx = 2;
		gbc_txtTitle.gridy = 0;
		pnlInformation.add(txtTitle, gbc_txtTitle);
		txtTitle.setColumns(10);

		lblAuthor = new JLabel(Messages.getString("BookDetail.lblAuthor.text")); //$NON-NLS-1$
		GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
		gbc_lblAuthor.insets = new Insets(0, 0, 5, 5);
		gbc_lblAuthor.anchor = GridBagConstraints.EAST;
		gbc_lblAuthor.gridx = 1;
		gbc_lblAuthor.gridy = 1;
		pnlInformation.add(lblAuthor, gbc_lblAuthor);

		txtAuthor = new JTextField();
		GridBagConstraints gbc_txtAuthor = new GridBagConstraints();
		gbc_txtAuthor.insets = new Insets(0, 0, 5, 5);
		gbc_txtAuthor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAuthor.gridx = 2;
		gbc_txtAuthor.gridy = 1;
		pnlInformation.add(txtAuthor, gbc_txtAuthor);
		txtAuthor.setColumns(10);

		lblPublisher = new JLabel(Messages.getString("BookDetail.lblPublisher.text")); //$NON-NLS-1$
		GridBagConstraints gbc_lblPublisher = new GridBagConstraints();
		gbc_lblPublisher.insets = new Insets(0, 0, 5, 5);
		gbc_lblPublisher.anchor = GridBagConstraints.EAST;
		gbc_lblPublisher.gridx = 1;
		gbc_lblPublisher.gridy = 2;
		pnlInformation.add(lblPublisher, gbc_lblPublisher);

		txtPublisher = new JTextField();
		GridBagConstraints gbc_txtPublisher = new GridBagConstraints();
		gbc_txtPublisher.insets = new Insets(0, 0, 5, 5);
		gbc_txtPublisher.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPublisher.gridx = 2;
		gbc_txtPublisher.gridy = 2;
		pnlInformation.add(txtPublisher, gbc_txtPublisher);
		txtPublisher.setColumns(10);

		lblShelf = new JLabel(Messages.getString("BookDetail.lblShelf.text")); //$NON-NLS-1$
		GridBagConstraints gbc_lblShelf = new GridBagConstraints();
		gbc_lblShelf.insets = new Insets(0, 0, 0, 5);
		gbc_lblShelf.anchor = GridBagConstraints.EAST;
		gbc_lblShelf.gridx = 1;
		gbc_lblShelf.gridy = 3;
		pnlInformation.add(lblShelf, gbc_lblShelf);

		cmbShelf = new JComboBox();
		GridBagConstraints gbc_cmbShelf = new GridBagConstraints();
		gbc_cmbShelf.insets = new Insets(0, 0, 0, 5);
		gbc_cmbShelf.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbShelf.gridx = 2;
		gbc_cmbShelf.gridy = 3;
		pnlInformation.add(cmbShelf, gbc_cmbShelf);

		pnlSpecimensEdit = new JPanel();
		pnlSpecimensEdit.setBorder(new TitledBorder(new LineBorder(new Color(0,
				0, 0)), Messages.getString("BookDetail.pnlSpecimensEdit.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, //$NON-NLS-1$
				null, null));
		frame.getContentPane().add(pnlSpecimensEdit);
		pnlSpecimensEdit.setLayout(new BoxLayout(pnlSpecimensEdit,
				BoxLayout.Y_AXIS));

		pnlAction = new JPanel();
		pnlSpecimensEdit.add(pnlAction);
		pnlAction.setLayout(new BoxLayout(pnlAction, BoxLayout.X_AXIS));

		lblAmount = new JLabel(Messages.getString("BookDetail.lblAmount.text")); //$NON-NLS-1$
		pnlAction.add(lblAmount);

		Component hglSpecimensEdit = Box.createHorizontalGlue();
		pnlAction.add(hglSpecimensEdit);

		btnRemove = new JButton(Messages.getString("BookDetail.btnRemove.text")); //$NON-NLS-1$
		pnlAction.add(btnRemove);

		Component horizontalStrut = Box.createHorizontalStrut(5);
		pnlAction.add(horizontalStrut);

		btnAdd = new JButton(Messages.getString("BookDetail.btnAdd.text")); //$NON-NLS-1$
		pnlAction.add(btnAdd);

		pnlSpecimens = new JPanel();
		pnlSpecimensEdit.add(pnlSpecimens);
		pnlSpecimens.setLayout(new BorderLayout(0, 0));

		lstSpecimens = new JList();
		pnlSpecimens.add(lstSpecimens);
	}

}
