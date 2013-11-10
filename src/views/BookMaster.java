package views;

import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import viewModels.BookListModel;

import domain.Book;
import domain.Library;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BookMaster extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tbsMain;
	
	private JPanel pnlBooksTab;
	private JPanel pnlInventoryStats;
	private JPanel pnlBookInventory;
	private JPanel pnlBooksInvTop;
	private JPanel pnlBooksInvBottom;
	private JPanel pnlLoansTab;
	
	private JLabel lblNrOfBooks;
	private JLabel lblNrOfCopies;
	private JLabel lblChosen;
	
	private JButton btnShowSelected;
	private JButton btnAddNewBook;
	
	private JList<domain.Book> lstBooks;
	
	private Component horizontalStrut;
	private Component horizontalStrut_1;
	
	private GridBagLayout gbl_pnlBookInventory;
	private GridBagLayout gbl_pnlBooksInvBottom;
	private GridBagLayout gbl_pnlLoansTab;

	private GridBagConstraints gbc_pnlBooksInvTop;
	private GridBagConstraints gbc_pnlBooksInvBottom;
	private GridBagConstraints gbc_lstBooks;
	
	private Library library;
	private Book editBook;
	private BooksChangedObserver booksChangedObserver = new BooksChangedObserver();
	private BookListModel bookListModel;

	/**
	 * Create the application.
	 * @param bookList 
	 */
	public BookMaster( Library library ) {
		/*
		 * This View should listen to changes in the book list and the loans list!
		 */
		super();
		this.library = library;
		library.addObserver( booksChangedObserver );
		bookListModel = new BookListModel( this.library );
		initialize();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle(Messages.getString("BookMaster.frmTodoTitle.title")); //$NON-NLS-1$
			this.setBounds(100, 100, 600, 400);
			
			tbsMain = new JTabbedPane(JTabbedPane.TOP);
			tbsMain.setToolTipText(Messages.getString("BookMaster.tbsMain.toolTipText")); //$NON-NLS-1$
			this.getContentPane().add(tbsMain, BorderLayout.CENTER);
			
			pnlBooksTab = new JPanel();
			tbsMain.addTab("Bücher", null, pnlBooksTab, null);
			pnlBooksTab.setLayout(new BoxLayout(pnlBooksTab, BoxLayout.Y_AXIS));
			
			pnlInventoryStats = new JPanel();
			pnlInventoryStats.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlInventoryStats.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
			pnlBooksTab.add(pnlInventoryStats);
			pnlInventoryStats.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			
			lblNrOfBooks = new JLabel(Messages.getString("BookMaster.lblNrOfBooks.text")); //$NON-NLS-1$
			pnlInventoryStats.add(lblNrOfBooks);
			
			horizontalStrut = Box.createHorizontalStrut(20);
			pnlInventoryStats.add(horizontalStrut);
			
			lblNrOfCopies = new JLabel(Messages.getString("BookMaster.lblNrOfCopies.text")); //$NON-NLS-1$
			pnlInventoryStats.add(lblNrOfCopies);
			
			pnlBookInventory = new JPanel();
			pnlBookInventory.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlBookInventory.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
			pnlBooksTab.add(pnlBookInventory);
			gbl_pnlBookInventory = new GridBagLayout();
			gbl_pnlBookInventory.columnWidths = new int[] {0};
			gbl_pnlBookInventory.rowHeights = new int[] {30, 0};
			gbl_pnlBookInventory.columnWeights = new double[]{1.0};
			gbl_pnlBookInventory.rowWeights = new double[]{0.0, 1.0};
			pnlBookInventory.setLayout(gbl_pnlBookInventory);
			
			pnlBooksInvTop = new JPanel();
			gbc_pnlBooksInvTop = new GridBagConstraints();
			gbc_pnlBooksInvTop.anchor = GridBagConstraints.NORTH;
			gbc_pnlBooksInvTop.insets = new Insets(0, 0, 5, 0);
			gbc_pnlBooksInvTop.fill = GridBagConstraints.HORIZONTAL;
			gbc_pnlBooksInvTop.gridx = 0;
			gbc_pnlBooksInvTop.gridy = 0;
			pnlBookInventory.add(pnlBooksInvTop, gbc_pnlBooksInvTop);
			pnlBooksInvTop.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			
			lblChosen = new JLabel(Messages.getString("BookMaster.lblChosen.text")); //$NON-NLS-1$
			pnlBooksInvTop.add(lblChosen);
			
			horizontalStrut_1 = Box.createHorizontalStrut(20);
			pnlBooksInvTop.add(horizontalStrut_1);
			
			btnShowSelected = new JButton(Messages.getString("BookMaster.btnShowSelected.text")); //$NON-NLS-1$
			btnShowSelected.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showSelectedButtonActionPerformed(e);
				}
			});
			pnlBooksInvTop.add(btnShowSelected);
			
			btnAddNewBook = new JButton(Messages.getString("BookMaster.btnAddNewBook.text")); //$NON-NLS-1$
			btnAddNewBook.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addButtonActionPerformed(e);
				}
			});
			pnlBooksInvTop.add(btnAddNewBook);
			
			pnlBooksInvBottom = new JPanel();
			gbc_pnlBooksInvBottom = new GridBagConstraints();
			gbc_pnlBooksInvBottom.fill = GridBagConstraints.BOTH;
			gbc_pnlBooksInvBottom.gridx = 0;
			gbc_pnlBooksInvBottom.gridy = 1;
			pnlBookInventory.add(pnlBooksInvBottom, gbc_pnlBooksInvBottom);
			gbl_pnlBooksInvBottom = new GridBagLayout();
			gbl_pnlBooksInvBottom.columnWidths = new int[]{0, 0};
			gbl_pnlBooksInvBottom.rowHeights = new int[]{0, 0};
			gbl_pnlBooksInvBottom.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_pnlBooksInvBottom.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			pnlBooksInvBottom.setLayout(gbl_pnlBooksInvBottom);
			
			////////////// JList /////////////////////
			lstBooks = new JList<domain.Book>();
			lstBooks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			lstBooks.setLayoutOrientation(JList.VERTICAL);
			lstBooks.setVisibleRowCount(-1);
			lstBooks.setModel(bookListModel);
			lstBooks.setCellRenderer( new BookListCellRenderer() );
			lstBooks.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					updateListButtons(lstBooks.getSelectedValues().length !=0 );
					//updateDetailPanel();
				}
			});
			
			
			gbc_lstBooks = new GridBagConstraints();
			gbc_lstBooks.fill = GridBagConstraints.BOTH;
			gbc_lstBooks.gridx = 0;
			gbc_lstBooks.gridy = 0;
			pnlBooksInvBottom.add(lstBooks, gbc_lstBooks);
			
			pnlLoansTab = new JPanel();
			tbsMain.addTab("Ausleihen", null, pnlLoansTab, null);
			gbl_pnlLoansTab = new GridBagLayout();
			gbl_pnlLoansTab.columnWidths = new int[]{0};
			gbl_pnlLoansTab.rowHeights = new int[]{0};
			gbl_pnlLoansTab.columnWeights = new double[]{Double.MIN_VALUE};
			gbl_pnlLoansTab.rowWeights = new double[]{Double.MIN_VALUE};
			pnlLoansTab.setLayout(gbl_pnlLoansTab);
			
			updateListButtons(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateListButtons(boolean bookItemIsSelected) {
		btnShowSelected.setEnabled(bookItemIsSelected);
	}
	
	//TODO: do we still need this? (PCHR)
	private void updateDetailPanel() {
		if (editBook!=null) {
			editBook.deleteObserver(booksChangedObserver);
			editBook=null;
		}
		editBook=(Book)lstBooks.getSelectedValue();
		/*
		if (editBook==null){
			toDoTextJField.setText("");
			importanceJComboBoxModel.setSelectedItem(importanceJComboBoxModel.getElementAt(0));
			startDateJText.setText("");
			dueDateJText.setText("");				
		}else{
			editBook.addObserver(editToDoChangedObserver);
			toDoTextJField.setText(editToDo.getText());
			importanceJComboBoxModel.setSelectedItem(importanceJComboBoxModel.getElementAt(editBook.getImportance()));
			startDateJText.setText(String.format("%tD", editBook.getStartDate()));
			dueDateJText.setText(String.format("%tD", editBook.getDueDate()));		
		}
		*/
	}
	
	private void showSelectedButtonActionPerformed(ActionEvent evt) {
		List<Book> selectedBooks = lstBooks.getSelectedValuesList();
		for (Book selectedBook : selectedBooks) {
			BookDetail.editBook(library, selectedBook);
		}
	}
	
	private void addButtonActionPerformed(ActionEvent evt) {
		// TODO
	}
	
	private class BooksChangedObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			updateDetailPanel();			
		}
		
	}

}
