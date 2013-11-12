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

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import viewModels.CopyListModel;
import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Shelf;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import com.sun.corba.se.spi.orbutil.fsm.Action;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
	private JPanel pnlButtons;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnReset;
	private Component horizontalGlue;
	
	private static Boolean newlyCreated = false;

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
		if ( book == null ) {
			System.out.println("CREATE NEW BOOK");
			book = new Book("");
			newlyCreated = true;
		}
		BookDetail editFrame = editFramesDict.get(book);
		if (editFrame == null) {
			editFrame = new BookDetail(library, book);
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
			txtTitle.setEnabled(false);
			txtPublisher.setEnabled(false);
			txtAuthor.setEnabled(false);
			cmbShelf.setEnabled(false);
		} else {
			setBookValuesToView();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
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
			// Remove Copy (via D, Button)
			AbstractAction remove = new RemoveCopyAction( Messages.getString("BookDetail.btnRemove.text"), "Remove selected copies" );
			// Add Copy (via A, Button)
			AbstractAction addCopy = new AddCopyAction( Messages.getString("BookDetail.btnAdd.text"), "Add a copy of this book" );
			
			/////////////////////////////////////////////////
			// INFORMATION PANEL
			/////////////////////////////////////////////////
			pnlInformation = new JPanel();
			pnlInformation
					.setBorder(new TitledBorder(
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

			lblTitle = new JLabel(
					Messages.getString("BookDetail.lblTitle.text")); //$NON-NLS-1$
			GridBagConstraints gbc_lblTitle = new GridBagConstraints();
			gbc_lblTitle.anchor = GridBagConstraints.EAST;
			gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
			gbc_lblTitle.gridx = 1;
			gbc_lblTitle.gridy = 0;
			pnlInformation.add(lblTitle, gbc_lblTitle);

			txtTitle = new JTextField();
			txtTitle.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					validateInformation();
				}
			});
			GridBagConstraints gbc_txtTitle = new GridBagConstraints();
			gbc_txtTitle.insets = new Insets(0, 0, 5, 5);
			gbc_txtTitle.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtTitle.gridx = 2;
			gbc_txtTitle.gridy = 0;
			pnlInformation.add(txtTitle, gbc_txtTitle);
			txtTitle.setColumns(10);

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
			txtAuthor.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					validateInformation();
				}
			});

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
			txtPublisher.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					validateInformation();
				}
			});

			lblShelf = new JLabel(
					Messages.getString("BookDetail.lblShelf.text")); //$NON-NLS-1$
			GridBagConstraints gbc_lblShelf = new GridBagConstraints();
			gbc_lblShelf.insets = new Insets(0, 0, 5, 5);
			gbc_lblShelf.anchor = GridBagConstraints.EAST;
			gbc_lblShelf.gridx = 1;
			gbc_lblShelf.gridy = 3;
			pnlInformation.add(lblShelf, gbc_lblShelf);

			cmbShelf = new JComboBox<Shelf>();
			for (Shelf shelf : Shelf.values()) {
				cmbShelf.addItem(shelf);
			}
			GridBagConstraints gbc_cmbShelf = new GridBagConstraints();
			gbc_cmbShelf.insets = new Insets(0, 0, 5, 5);
			gbc_cmbShelf.fill = GridBagConstraints.HORIZONTAL;
			gbc_cmbShelf.gridx = 2;
			gbc_cmbShelf.gridy = 3;
			pnlInformation.add(cmbShelf, gbc_cmbShelf);
			
			pnlButtons = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 0, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 2;
			gbc_panel.gridy = 4;
			pnlInformation.add(pnlButtons, gbc_panel);
			pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
			
			horizontalGlue = Box.createHorizontalGlue();
			pnlButtons.add(horizontalGlue);
			
			///////////////////////////////////////////////////
			// BUTTONS
			///////////////////////////////////////////////////
			// SAVE
			btnSave = new JButton( save );
			btnSave.setEnabled(false);
			pnlButtons.add(btnSave);
			
			// RESET
			btnReset = new JButton( reset ); //$NON-NLS-1$
			pnlButtons.add(btnReset);
			
			// CANCEL
			btnCancel = new JButton( cancel );
			pnlButtons.add( btnCancel );
		
			/////////////////////////////////////////////////
			// COPY AREA
			/////////////////////////////////////////////////
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

			///////////////////////////////////////////////////
			// COPY-BUTTONS
			///////////////////////////////////////////////////
			// REMOVE
			btnRemove = new JButton( remove );
			btnRemove.setEnabled(false);
			pnlAction.add(btnRemove);

			Component horizontalStrut = Box.createHorizontalStrut(5);
			pnlAction.add(horizontalStrut);
			
			// ADD
			btnAdd = new JButton( addCopy );
			pnlAction.add(btnAdd);

			pnlCopies = new JPanel();
			pnlCopiesEdit.add(pnlCopies);
			pnlCopies.setLayout(new BorderLayout(0, 0));
			
			lstCopy = new JList<Copy>();
			lstCopy.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					updateRemoveCopyButton(lstCopy.getSelectedValue() != null);
				}
			});
			lstCopy.setModel(new CopyListModel(library.getCopiesOfBook(book)));
			
			pnlCopies.add( getContentPane().add(new JScrollPane(lstCopy)) );
			
			displayBook();
			
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void validateInformation(){
		boolean hasValidationError = "".equals(txtAuthor.getText()) || "".equals(txtPublisher.getText()) || "".equals(txtTitle.getText());
		if(hasValidationError){
			// TODO pforster: handle validation error
			btnSave.setEnabled(false);
		}else {
			btnSave.setEnabled(true);
		}
	}
	
	private void setBookValuesToView() {
		txtAuthor.setText(book.getAuthor());
		txtPublisher.setText(book.getPublisher());
		txtTitle.setText(book.getName());
		cmbShelf.setSelectedItem(book.getShelf());
	}

	private void updateRemoveCopyButton(boolean copySelected) {
		btnRemove.setEnabled(copySelected);
	}

	@Override
	public void update(Observable o, Object arg) {
		displayBook();
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
	 * Saves the (changed) entries for the currently opened book.
	 * @author PFORSTER, PCHR
	 */
	class SaveAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public SaveAction( String text, String desc ) {
	        super( text );
	        putValue( SHORT_DESCRIPTION, desc );
	        putValue( MNEMONIC_KEY, KeyEvent.VK_S );
	    }
	    public void actionPerformed(ActionEvent e) {
	    	book.setAuthor(txtAuthor.getText());
			book.setName(txtTitle.getText());
			book.setPublisher(txtPublisher.getText());
			book.setShelf((Shelf) cmbShelf.getSelectedItem());
			btnSave.setEnabled(false);
			
			if ( newlyCreated ) {
				// Saving a book that we just created.
				// we can only add it now, because before it shouldn't
				// belong to the library, only on saving.
				library.addBook(book);
				
				if ( library.getCopiesOfBook( book ).size() == 0 ) {
					library.createAndAddCopy( book );
				}
			}
	    }
	}
	/**
	 * Reset the form
	 * @author PFORSTER, PCHR
	 */
	class ResetAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public ResetAction( String text, String desc ) {
	        super( text );
	        putValue( SHORT_DESCRIPTION, desc );
	        putValue( MNEMONIC_KEY, KeyEvent.VK_R );
	    }
		public void actionPerformed(ActionEvent e) {
			setBookValuesToView();
			btnSave.setEnabled(false);
		}
	}
	// Copy:
	/**
	 * Add a copy to the currently opened book
	 * @author PFORSTER, PCHR
	 */
	class AddCopyAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public AddCopyAction( String text, String desc ) {
	        super( text );
	        putValue( SHORT_DESCRIPTION, desc );
	        putValue( MNEMONIC_KEY, KeyEvent.VK_A );
	    }
		public void actionPerformed(ActionEvent e) {
			library.createAndAddCopy(book);
			lstCopy.setModel(new CopyListModel(library.getCopiesOfBook(book)));
			// TODO: throws a stackTrace (ArrayIndexOutOfBoundsException)
		}
	}
	/**
	 * Remove the currently selected copies from the currently opened book.
	 * @author PFORSTER, PCHR
	 */
	class RemoveCopyAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public RemoveCopyAction( String text, String desc ) {
	        super( text );
	        putValue( SHORT_DESCRIPTION, desc );
	        putValue( MNEMONIC_KEY, KeyEvent.VK_D );
	    }
		public void actionPerformed(ActionEvent e) {
			library.removeCopy(lstCopy.getSelectedValue());
			lstCopy.setModel(new CopyListModel(library.getCopiesOfBook(book)));
		}
	}
	
}
