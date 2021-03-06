package views;

import i18n.Messages;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import renderers.LoanTableCellRenderer;
import viewModels.CopyTableModel;
import domain.Book;
import domain.BookList;
import domain.Copy;
import domain.CopyList;
import domain.Library;
import domain.Loan;
import domain.Copy.Condition;
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
	private Book book;
	private Library library;
	private BookList books;
	
	private JTable tblCopies;
	private JScrollPane scrollPane;
	
	// Models
	private CopyTableModel copyTableModel;
	private TableRowSorter<CopyTableModel> sorter;
	
	private CopyList copies;

	private static Dictionary<Book, BookDetail> editFramesDict = new Hashtable<Book, BookDetail>();
	private JPanel pnlButtons;
	private JPanel pnlBookButtons;
	
	// Buttons
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnReset;
	private JButton btnDelete;
	
	
	private Component horizontalGlue;
	
	private static Boolean newlyCreated;
	
	private static BookDetail editFrame;

	/**
	 * Create the application.
	 */
	public BookDetail(Library library, Book book) {
		super();
		setBounds(new Rectangle(59, 24, 500, 500));
		this.library = library;
		this.books = library.getBookList();
		this.book = book;
		
		this.copies = new CopyList();
		this.copies.setCopyList( library.getCopiesOfBook( book ) );
		
		this.copyTableModel = new CopyTableModel( this.copies, library );
		
		initialize();
	}

	public static void editBook(Library library, Book book) {
		newlyCreated = false;
		
		if ( book == null ) {
			book = new Book("");
			newlyCreated = true;
		}
		
		editFrame = editFramesDict.get(book);
		if (editFrame == null) {
			editFrame = new BookDetail(library, book);
			editFramesDict.put(book, editFrame);
		}
		
		editFrame.setVisible(true);
	}

	private void displayBook() {
		if (book == null) {
			txtTitle.setEnabled(false);
			txtPublisher.setEnabled(false);
			txtAuthor.setEnabled(false);
			cmbShelf.setEnabled(false);
		} else {
			setBookValuesToView();
		}
		
		setTitle( Messages.getString("BookDetail.this.title", book.getName() ) );
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			setMinimumSize( new Dimension(500, 444) );
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			getContentPane().setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
			
			/////////////////////////////////////////////////
			// ACTIONS
			/////////////////////////////////////////////////
			// Close (via Esc-Key (?), Button)
			AbstractAction cancel = new CloseAction(
				Messages.getString( "DetailDialogs.btnClose.text"),
				Messages.getString( "DetailDialogs.btnClose.desc")
			);
			// Save (via S, Button)
			AbstractAction save = new SaveAction(
				Messages.getString( "BookDetail.btnSave.text"),
				Messages.getString( "BookDetail.btnSave.desc")
			);
			// Reset (via R, Button)
			AbstractAction reset = new ResetAction(
				Messages.getString( "BookDetail.btnReset.text"),
				Messages.getString( "BookDetail.btnReset.desc")
			);
			// Remove Book (via D, Button)
			AbstractAction delete = new DeleteAction(
				Messages.getString("BookDetail.btnRemove.text"),
				Messages.getString("BookDetail.btnRemove.desc")
			);
			
			// Add Copy (via A, Button)
			AbstractAction addCopy = new AddCopyAction(
				Messages.getString("BookDetail.btnAdd.text"),
				Messages.getString("BookDetail.btnAdd.desc")
			);
			// Remove Selected Copies (via Button)
			AbstractAction removeCopy = new RemoveCopyAction(
				Messages.getString("BookDetail.btnRemoveCopy.text"),
				Messages.getString("BookDetail.btnRemoveCopy.desc")
			);
			
			/////////////////////////////////////////////////
			// INFORMATION PANEL
			/////////////////////////////////////////////////
			pnlInformation = new JPanel();
			pnlInformation .setBorder(new TitledBorder( new LineBorder(new Color(0, 0, 0)), Messages.getString("BookDetail.pnlInformation.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(pnlInformation);
			GridBagLayout gbl_pnlInformation = new GridBagLayout();
			gbl_pnlInformation.columnWidths = new int[] { 0, 0, 0, 0, 0 };
			gbl_pnlInformation.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
			gbl_pnlInformation.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
			gbl_pnlInformation.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
			pnlInformation.setLayout(gbl_pnlInformation);

			lblTitle = new JLabel( Messages.getString("BookDetail.lblTitle.text") );
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
					handleFieldError(txtTitle);
				}
			});
			GridBagConstraints gbc_txtTitle = new GridBagConstraints();
			gbc_txtTitle.insets = new Insets(0, 0, 5, 5);
			gbc_txtTitle.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtTitle.gridx = 2;
			gbc_txtTitle.gridy = 0;
			pnlInformation.add(txtTitle, gbc_txtTitle);
			txtTitle.setColumns(10);

			lblAuthor = new JLabel( Messages.getString("BookDetail.lblAuthor.text") );
			GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
			gbc_lblAuthor.insets = new Insets(0, 0, 5, 5);
			gbc_lblAuthor.anchor = GridBagConstraints.EAST;
			gbc_lblAuthor.gridx = 1;
			gbc_lblAuthor.gridy = 1;
			pnlInformation.add( lblAuthor, gbc_lblAuthor );

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
					handleFieldError(txtAuthor);
				}
			});

			lblPublisher = new JLabel( Messages.getString("BookDetail.lblPublisher.text") );
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
					handleFieldError(txtPublisher);
				}
			});

			lblShelf = new JLabel( Messages.getString("BookDetail.lblShelf.text") );
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
			cmbShelf.addActionListener(new ShelfAction());
			GridBagConstraints gbc_cmbShelf = new GridBagConstraints();
			gbc_cmbShelf.insets = new Insets(0, 0, 5, 5);
			gbc_cmbShelf.fill = GridBagConstraints.HORIZONTAL;
			gbc_cmbShelf.gridx = 2;
			gbc_cmbShelf.gridy = 3;
			
			pnlInformation.add(cmbShelf, gbc_cmbShelf);
			
			pnlBookButtons = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 0, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 2;
			gbc_panel.gridy = 4;
			pnlInformation.add( pnlBookButtons, gbc_panel );
			pnlBookButtons.setLayout( new BoxLayout( pnlBookButtons, BoxLayout.X_AXIS ) );
			
			horizontalGlue = Box.createHorizontalGlue();
			pnlBookButtons.add( horizontalGlue );
			
			///////////////////////////////////////////////////
			// BOOK-BUTTONS
			///////////////////////////////////////////////////
			// DELETE
			btnDelete = new JButton( delete );
			btnDelete.setEnabled( !newlyCreated );
			pnlBookButtons.add( btnDelete );
			
			// RESET
			btnReset = new JButton( reset );
			btnReset.setEnabled( false );
			pnlBookButtons.add( btnReset );
			
			// SAVE
			btnSave = new JButton( save );
			btnSave.setEnabled( false );
			pnlBookButtons.add( btnSave );
						
			/////////////////////////////////////////////////
			// COPY AREA
			/////////////////////////////////////////////////
			pnlCopiesEdit = new JPanel();
			pnlCopiesEdit.setBorder(new TitledBorder( new LineBorder(new Color(0, 0, 0)), Messages.getString("BookDetail.pnlSpecimensEdit.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(pnlCopiesEdit);
			pnlCopiesEdit.setLayout(new BoxLayout(pnlCopiesEdit, BoxLayout.Y_AXIS));

			pnlAction = new JPanel();
			pnlCopiesEdit.add(pnlAction);
			pnlAction.setLayout(new BoxLayout(pnlAction, BoxLayout.X_AXIS));

			lblAmount = new JLabel( Messages.getString("BookDetail.lblAmount.text") );
			pnlAction.add(lblAmount);

			Component hglCopiesEdit = Box.createHorizontalGlue();
			pnlAction.add(hglCopiesEdit);

			///////////////////////////////////////////////////
			// COPY-BUTTONS
			///////////////////////////////////////////////////
			// DELETE
			btnRemove = new JButton( removeCopy );
			btnRemove.setEnabled(false);
			pnlAction.add(btnRemove);

			Component horizontalStrut = Box.createHorizontalStrut(5);
			pnlAction.add(horizontalStrut);
			
			// ADD
			btnAdd = new JButton( addCopy );
			pnlAction.add(btnAdd);
			
			// ///////////////////////////////////////////////
			// COPIES PANEL
			// ///////////////////////////////////////////////
			pnlCopies = new JPanel();
			GridBagConstraints gbc_pnlCopies = new GridBagConstraints();
			gbc_pnlCopies.insets = new Insets(0, 0, 5, 0);
			gbc_pnlCopies.fill = GridBagConstraints.BOTH;
			gbc_pnlCopies.gridx = 0;
			gbc_pnlCopies.gridy = 1;
			pnlCopies.setLayout(new BoxLayout( pnlCopies, BoxLayout.Y_AXIS));
			pnlCopiesEdit.add( pnlCopies, gbc_pnlCopies);

			scrollPane = new JScrollPane();
			{
				tblCopies = new JTable();
				initTable();
			}
			scrollPane.setViewportView(tblCopies);
			pnlCopies.add(scrollPane);
			
			// ///////////////////////////////////////////////
			// BUTTONS PANEL
			// ///////////////////////////////////////////////
			pnlButtons = new JPanel();
			pnlButtons.setMaximumSize(new Dimension(32767, 50));
			getContentPane().add(pnlButtons);
			pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
	
			pnlButtons.add(Box.createHorizontalGlue());
			
			// CANCEL
			btnCancel = new JButton( cancel );
			btnCancel.setEnabled( true );
			btnCancel.setIcon( new ImageIcon("icons/close_32.png") );
			pnlButtons.add( btnCancel );
			
			rootPane.setDefaultButton( btnCancel );
			
			displayBook();
			
			updateLabels();
			updateListButtons();
			
			// Add Escape-Action ( close dialog )
			KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
			InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		    inputMap.put(stroke, "ESCAPE");
		    rootPane.getActionMap().put("ESCAPE", cancel);
			
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initTable() {
		tblCopies.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		tblCopies.setModel( copyTableModel );
		sorter = new TableRowSorter<CopyTableModel>( copyTableModel );
		tblCopies.setRowSorter( sorter );
		
		// Ignore the title and author in this view, because all copies from this book have the same properties in those fields.
		tblCopies.getColumnModel().removeColumn( tblCopies.getColumnModel().getColumn(1) );
		tblCopies.getColumnModel().removeColumn( tblCopies.getColumnModel().getColumn(1) );
		
		tblCopies.getColumnModel().getColumn( 1 ).setCellRenderer(new LoanTableCellRenderer(library) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// Get value from TableModel
				boolean isLent = (boolean) value;
				
				if ( isLent ) {
					label.setText( Messages.getString("LoanTable.CellContent.Lent") );
					label.setIcon( new ImageIcon("icons/warning_16.png") );
				} else {
					label.setText( Messages.getString("LoanTable.CellContent.Available") );
					label.setIcon( null );
				}
				// Bug: every 10th copy or so gets a red text. Overrule!
				label.setForeground( Color.BLACK );
				return label;
			}
		});
		
		JComboBox<Condition> comboBox = new JComboBox<>( Condition.values() );
		
		tblCopies.getColumnModel().getColumn( 2 ).setCellEditor( new DefaultCellEditor( comboBox ) );
		
		// Add Listeners
		tblCopies.getSelectionModel().addListSelectionListener(
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
	
	private void handleFieldError(JTextField field) {
		if("".equals(field.getText())){
			field.setBackground(Color.PINK);
		}else {
			field.setBackground(Color.WHITE);
		}
	}
	
	private void validateInformation(){
		boolean hasValidationError = "".equals(txtAuthor.getText()) || "".equals(txtPublisher.getText()) || "".equals(txtTitle.getText());
		if(hasValidationError){
			btnSave.setEnabled(false);
		}else {
			btnSave.setEnabled(true);
		}
		
		// in any way, enable the reset button
		btnReset.setEnabled( true );
	}
	
	private void setBookValuesToView() {
		txtAuthor.setText(book.getAuthor());
		txtPublisher.setText(book.getPublisher());
		txtTitle.setText(book.getName());
		cmbShelf.setSelectedItem(book.getShelf());
		btnSave.setEnabled(false);
	}
	
	public void updateLabels() {
		lblAmount.setText( Messages.getString("BookDetail.lblAmount.text", String.valueOf( library.getCopiesOfBook( book ).size() ) ) );
	}
	
	public void updateListButtons() {
		// Enables or disables the buttons
		
		// Here we check if this copy is lent. If so, we disable the removeCopyButton, if not, we enable it.
		if ( tblCopies.getSelectedRowCount() > 0 ) {
			Copy copy = copies.getCopyAt( tblCopies.convertRowIndexToModel( tblCopies.getSelectedRow() ) );
			List<Loan> lentCopiesOfBook = library.getLentCopiesOfBook(book);
			boolean copyIsLent = false;
			for(Loan loan : lentCopiesOfBook){
				if(copy.equals(loan.getCopy())){
					copyIsLent = true;
					break;
				}
			}
			// Enable the "Remove Copy" button if this copy isn't lent right now
			// AND if the book has more than 1 copy left.
			btnRemove.setEnabled( !copyIsLent && library.getCopiesOfBook( book ).size() > 1 );
		} else {
			btnRemove.setEnabled( false );
		}
		
		btnDelete.setEnabled( library.getLentCopiesOfBook( book ).size() == 0 );
	}
	
	@Override
	public void update(Observable o, Object arg) {
		displayBook();
		updateLabels();
		updateListButtons();
	}
	
	/////////////////////////////////////////////////
	// Action Subclasses
	/////////////////////////////////////////////////
	/**
	 * Change Shelf Action.
	 * @author PFOR
	 */
	class ShelfAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
	    
		public void actionPerformed(ActionEvent e) {
			validateInformation();
	    }
	}
	/**
	 * Closes the current dialog.
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
	    	dispose();
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
				books.addBook(book);
				
				if ( library.getCopiesOfBook( book ).size() == 0 ) {
					library.createAndAddCopy( book );
				}
				
				updateLabels();
			}
			copies.setCopyList(library.getCopiesOfBook(book));
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
			btnReset.setEnabled( false );
			btnSave.setEnabled( false );
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
			copies.setCopyList( library.getCopiesOfBook( book ) );
			
			updateLabels();
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
	        //putValue( MNEMONIC_KEY, KeyEvent.VK_R ); //TODO: find good key.
	    }
		public void actionPerformed(ActionEvent e) {
			Copy copy = copies.getCopyAt( tblCopies.convertRowIndexToModel( tblCopies.getSelectedRow() ) );
			List<Loan> lentCopiesOfBook = library.getLentCopiesOfBook(book);
			boolean copyIsLent = false;
			for(Loan loan : lentCopiesOfBook){
				if(copy.equals(loan.getCopy())){
					copyIsLent = true;
				}
			}
			
			// The following scenario should never happen, as we disable the removeCopy-Button when the selected copy is lent.
			// We'll leave it here for legacy reasons.
			if(copyIsLent){
				JOptionPane.showMessageDialog(
					editFrame,
					Messages.getString("BookDetail.CantDeleteLentCopy.text"),
					Messages.getString("BookDetail.CantDeleteLentCopy.title"),
					JOptionPane.YES_NO_OPTION
				);
			}else {
				library.removeCopy(copy);				
				copies.setCopyList( library.getCopiesOfBook( book ) );
			}
			
			updateLabels();
		}
	}
	/**
	 * Delete the currently opened book, if there are no currently lent copies.
	 * @author PCHR
	 */
	class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public DeleteAction( String text, String desc ) {
	        super( text );
	        putValue( SHORT_DESCRIPTION, desc );
	        putValue( MNEMONIC_KEY, KeyEvent.VK_D );
	    }
		public void actionPerformed(ActionEvent e) {
			boolean canDelete = false;
			canDelete = library.getLentCopiesOfBook(book).size() == 0;

			if ( canDelete ) {
				// Necessary for setting selected button
				Object[] options = {
					Messages.getString("Common.Yes"),
					Messages.getString("Common.No")
				};
				int delete = JOptionPane.showOptionDialog(
					editFrame,
					Messages.getString("BookDetail.deleteDlg.message"),
					Messages.getString("BookDetail.deleteDlg.title"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, 
	                null, options, options[1]
	            );
				
				if ( delete == 0 ) {
					if ( books.removeBook( book ) ) {
						// SUCCESS
						dispose();
					} else {
						try {
							throw new Exception( "Yeah, deleting that book didn't really work. Sorry about that, please restart the application." );
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
}
