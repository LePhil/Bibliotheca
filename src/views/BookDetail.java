package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import viewModels.CopyListModel;
import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Shelf;

public class BookDetail extends javax.swing.JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private JTextField txtTitle;
	private JTextField txtAuthor;
	private JTextField txtPublisher;
	private JPanel pnlInformation;
	private JLabel lblTitle;
	private JLabel lblAuthor;
	private JLabel lblPublisher;
	private JLabel lblShelf;
	private JComboBox<Shelf> cmbShelf;
	private JPanel pnlCopiesEdit;
	private JPanel pnlAction;
	private JLabel lblAmount;
	private JButton btnRemove;
	private JButton btnAdd;
	private JPanel pnlCopies;
	private JList<Copy> lstCopy;

	private Book book;
	private Library library;

	private static Dictionary<Book, BookDetail> editFramesDict = new Hashtable<Book, BookDetail>();

	/**
	 * Create the application.
	 */
	public BookDetail(Library library, Book book) {
		super();
		this.library = library;
		this.book = book;
		initialize();
	}

	public static void editBook(Library library, Book book) {
		BookDetail editFrame = editFramesDict.get(book);
		if (editFrame == null) {
			editFrame = new BookDetail(library, book);
			// editFrame.setBook(book);
			editFramesDict.put(book, editFrame);
		}
		editFrame.setVisible(true);
	}

	// TODO pforster remove
	// private void setBook(Book newBook) {
	// if (book != null) {
	// // replacing currently editing Book
	// book.deleteObserver(this);
	// }
	// this.book = newBook;
	// displayBook();
	// if (newBook != null) {
	// newBook.addObserver(this);
	// }
	// }

	private void displayBook() {
		if (book == null) {
			// TODO: disable everything
		} else {
			// TODO: fill everything
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			setBounds(100, 100, 450, 300);
			getContentPane().setLayout(
					new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

			pnlInformation = new JPanel();
			pnlInformation
					.setBorder(new TitledBorder(
							new LineBorder(new Color(0, 0, 0)),
							Messages.getString("BookDetail.pnlInformation.borderTitle"), TitledBorder.LEADING, //$NON-NLS-1$
							TitledBorder.TOP, null, null));
			getContentPane().add(pnlInformation);
			GridBagLayout gbl_pnlInformation = new GridBagLayout();
			gbl_pnlInformation.columnWidths = new int[] { 0, 0, 0, 0, 0 };
			gbl_pnlInformation.rowHeights = new int[] { 0, 0, 0, 0, 0 };
			gbl_pnlInformation.columnWeights = new double[] { 0.0, 0.0, 1.0,
					0.0, Double.MIN_VALUE };
			gbl_pnlInformation.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
					Double.MIN_VALUE };
			pnlInformation.setLayout(gbl_pnlInformation);

			lblTitle = new JLabel(
					Messages.getString("BookDetail.lblTitle.text")); //$NON-NLS-1$
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
			txtTitle.setText(book.getName());

			lblAuthor = new JLabel(
					Messages.getString("BookDetail.lblAuthor.text")); //$NON-NLS-1$
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
			txtAuthor.setText(book.getAuthor());

			lblPublisher = new JLabel(
					Messages.getString("BookDetail.lblPublisher.text")); //$NON-NLS-1$
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
			txtPublisher.setText(book.getPublisher());

			lblShelf = new JLabel(
					Messages.getString("BookDetail.lblShelf.text")); //$NON-NLS-1$
			GridBagConstraints gbc_lblShelf = new GridBagConstraints();
			gbc_lblShelf.insets = new Insets(0, 0, 0, 5);
			gbc_lblShelf.anchor = GridBagConstraints.EAST;
			gbc_lblShelf.gridx = 1;
			gbc_lblShelf.gridy = 3;
			pnlInformation.add(lblShelf, gbc_lblShelf);

			cmbShelf = new JComboBox<Shelf>();
			for (Shelf shelf : Shelf.values()) {
				cmbShelf.addItem(shelf);
			}
			cmbShelf.setSelectedItem(book.getShelf());
			GridBagConstraints gbc_cmbShelf = new GridBagConstraints();
			gbc_cmbShelf.insets = new Insets(0, 0, 0, 5);
			gbc_cmbShelf.fill = GridBagConstraints.HORIZONTAL;
			gbc_cmbShelf.gridx = 2;
			gbc_cmbShelf.gridy = 3;
			pnlInformation.add(cmbShelf, gbc_cmbShelf);

			pnlCopiesEdit = new JPanel();
			pnlCopiesEdit
					.setBorder(new TitledBorder(
							new LineBorder(new Color(0, 0, 0)),
							Messages.getString("BookDetail.pnlSpecimensEdit.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, //$NON-NLS-1$
							null, null));
			getContentPane().add(pnlCopiesEdit);
			pnlCopiesEdit.setLayout(new BoxLayout(pnlCopiesEdit,
					BoxLayout.Y_AXIS));

			pnlAction = new JPanel();
			pnlCopiesEdit.add(pnlAction);
			pnlAction.setLayout(new BoxLayout(pnlAction, BoxLayout.X_AXIS));

			lblAmount = new JLabel(
					Messages.getString("BookDetail.lblAmount.text")); //$NON-NLS-1$
			pnlAction.add(lblAmount);

			Component hglCopiesEdit = Box.createHorizontalGlue();
			pnlAction.add(hglCopiesEdit);

			btnRemove = new JButton(
					Messages.getString("BookDetail.btnRemove.text")); //$NON-NLS-1$
			btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO
				}
			});
			pnlAction.add(btnRemove);

			Component horizontalStrut = Box.createHorizontalStrut(5);
			pnlAction.add(horizontalStrut);

			btnAdd = new JButton(Messages.getString("BookDetail.btnAdd.text")); //$NON-NLS-1$
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					library.createAndAddCopy(book);
					lstCopy.setModel(new CopyListModel(library.getCopiesOfBook(book)));
				}
			});
			pnlAction.add(btnAdd);

			pnlCopies = new JPanel();
			pnlCopiesEdit.add(pnlCopies);
			pnlCopies.setLayout(new BorderLayout(0, 0));

			lstCopy = new JList<Copy>();
			lstCopy.setModel(new CopyListModel(library.getCopiesOfBook(book)));
			pnlCopies.add(lstCopy);

			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		displayBook();
	}
}
