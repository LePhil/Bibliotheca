package views;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.RowFilter.Entry;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.FlowLayout;
import java.awt.Component;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import viewModels.BookListModel;
import viewModels.BookTableModel;

import domain.Book;
import domain.Library;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class BookMasterTable extends javax.swing.JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tbsMain;
	
	private JMenuBar jMenuBar;
	private JMenu viewMenu;
	private JCheckBoxMenuItem showUnavailableMenuItem;
	
	private JPanel pnlBooksTab;
	private JPanel pnlInventoryStats;
	private JPanel pnlBookInventory;
	private JPanel pnlBooksInvTop;
	private JPanel pnlBooksInvBottom;
	private JPanel pnlLoansTab;
	
	private JLabel lblNrOfBooks;
	private JLabel lblNrOfCopies;
	
	private JButton btnShowSelected;
	private JButton btnAddNewBook;
	
	private Component horizontalStrut;
	private Component horizontalStrut_1;
	
	private GridBagLayout gbl_pnlBookInventory;
	private GridBagLayout gbl_pnlBooksInvBottom;
	private GridBagLayout gbl_pnlLoansTab;

	private GridBagConstraints gbc_pnlBooksInvTop;
	private GridBagConstraints gbc_pnlBooksInvBottom;
	
	private Library library;
	private Book editBook;
	private JScrollPane scrollPane;
	
	private JTable tblBooks;
	private JTextField txtSearch;
	private JCheckBox chckbxOnlyAvailable;
	private Component horizontalStrut_2;
	
	private BookTableModel bookTableModel;
	private TableRowSorter<BookTableModel> sorter;
	private RowFilter<Object, Object> filter;
	
	private boolean showUnavailable = true;
	
	// Actions:
	private AbstractAction toggleShowUnavailabeAction;

	/**
	 * Create the application.
	 * @param bookList 
	 */
	public BookMasterTable( Library library ) {
		/*
		 * This View should listen to changes in the book list and the loans list!
		 */
		super();
		this.library = library;
		bookTableModel = new BookTableModel( this.library );
		initialize();
		library.addObserver( this );
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
			this.setBounds(100, 100, 631, 400);
			
			// Menu
			jMenuBar = new JMenuBar();
			setJMenuBar(jMenuBar);
			
			viewMenu = new JMenu();
			jMenuBar.add(viewMenu);
			viewMenu.setText("View");
			viewMenu.setMnemonic(KeyEvent.VK_V);
			{
				showUnavailableMenuItem = new JCheckBoxMenuItem();
				viewMenu.add(showUnavailableMenuItem);
				showUnavailableMenuItem.setText("Show Completed");
				showUnavailableMenuItem.setMnemonic(KeyEvent.VK_U);
				showUnavailableMenuItem.setAction(getToggleShowUnavailableAction());
			}
			
			
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
			
			txtSearch = new JTextField();
			txtSearch.setText(Messages.getString("BookMasterTable.textField.text")); //$NON-NLS-1$
			pnlBooksInvTop.add(txtSearch);
			txtSearch.setColumns(10);
			
			horizontalStrut_1 = Box.createHorizontalStrut(20);
			pnlBooksInvTop.add(horizontalStrut_1);
			
			btnShowSelected = new JButton(Messages.getString("BookMaster.btnShowSelected.text")); //$NON-NLS-1$
			btnShowSelected.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showSelectedButtonActionPerformed(e);
				}
			});
			
			chckbxOnlyAvailable = new JCheckBox(Messages.getString("BookMasterTable.chckbxOnlySelected.text")); //$NON-NLS-1$
			chckbxOnlyAvailable.setAction(getToggleShowUnavailableAction());
			pnlBooksInvTop.add(chckbxOnlyAvailable);
			
			horizontalStrut_2 = Box.createHorizontalStrut(20);
			pnlBooksInvTop.add(horizontalStrut_2);
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
			gbl_pnlBooksInvBottom.rowHeights = new int[]{0, 0, 0};
			gbl_pnlBooksInvBottom.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_pnlBooksInvBottom.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			pnlBooksInvBottom.setLayout(gbl_pnlBooksInvBottom);
			
			scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.gridheight = 2;
			gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			pnlBooksInvBottom.add(scrollPane, gbc_scrollPane);
			
			//jTable////////////////////////////////////
			tblBooks = new JTable();
			initTable();

			scrollPane.setViewportView(tblBooks);
			
			pnlLoansTab = new JPanel();
			tbsMain.addTab("Ausleihen", null, pnlLoansTab, null);
			gbl_pnlLoansTab = new GridBagLayout();
			gbl_pnlLoansTab.columnWidths = new int[]{0};
			gbl_pnlLoansTab.rowHeights = new int[]{0};
			gbl_pnlLoansTab.columnWeights = new double[]{Double.MIN_VALUE};
			gbl_pnlLoansTab.rowWeights = new double[]{Double.MIN_VALUE};
			pnlLoansTab.setLayout(gbl_pnlLoansTab);
			
			updateListButtons();
			updateShowUnavailableCheckbox();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showSelectedButtonActionPerformed(ActionEvent evt) {
		int[] selectedRows = tblBooks.getSelectedRows();

		for (int selectedRow : selectedRows) {
			BookDetail.editBook( library, library.getBooks().get(selectedRow) );
		}
	}
	
	private void addButtonActionPerformed(ActionEvent evt) {
		// TODO: have to tell DetailDialog somehow that it'll be a new book
	}
	
	private void initTable() {
		tblBooks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(tblBooks);
		tblBooks.setModel(bookTableModel);
		
		// Make the columns sortable
		sorter = new TableRowSorter<BookTableModel>(bookTableModel);
		tblBooks.setRowSorter(sorter);
		
		// Filters are awesome!
		filter = new RowFilter<Object, Object>() {
	        public boolean include(Entry entry) {
	        	if (showUnavailable){
	        		return true;
	        	}
	        	// get value of Available column (column 0)
	        	//TODO: get available copies. Can't do it like this because
	        	// there's a string in that row.
	        	//Boolean completed = (Boolean) entry.getValue(0);
	        	//return ! completed.booleanValue();
	        	return false;
	        }
	    };
		sorter.setRowFilter(filter);
		
		TableColumn availabilityColumn = tblBooks.getColumnModel().getColumn(0);
		availabilityColumn.setCellRenderer(new BookTableCellRenderer(library));
		
		TableColumn titleColumn = tblBooks.getColumnModel().getColumn(1);
		titleColumn.setCellRenderer(new BookTableCellRenderer(library));

		TableColumn authorColumn = tblBooks.getColumnModel().getColumn(2);
		authorColumn.setCellRenderer(new BookTableCellRenderer(library));
		
		TableColumn publisherColumn = tblBooks.getColumnModel().getColumn(3);
		publisherColumn.setCellRenderer(new BookTableCellRenderer(library));
		
		tblBooks.getSelectionModel().addListSelectionListener(
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
	
	private AbstractAction getToggleShowUnavailableAction() {
		if(toggleShowUnavailabeAction == null) {
			toggleShowUnavailabeAction = new ToggleShowUnavailableAction();
		}
		return toggleShowUnavailabeAction;
	}
	
	private class ToggleShowUnavailableAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		ToggleShowUnavailableAction() {
			super("Show Unvailable", null);
			putValue(MNEMONIC_KEY, KeyEvent.VK_U);
			putValue(SHORT_DESCRIPTION, "Show or Hide Unavailable Books");
			putValue(ACCELERATOR_KEY, 
					KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			showUnavailable=!showUnavailable;
			
			// Re-Filter		
			sorter.setRowFilter(filter);
			updateListButtons();
			updateShowUnavailableCheckbox();
		}
	}
	
	private void updateListButtons() {
		// Enables or disables the "Show Selected" button
		// depending on whether a book is selected.
		btnShowSelected.setEnabled(tblBooks.getSelectedRowCount()>0);
	}
	
	private void updateShowUnavailableCheckbox() {
		chckbxOnlyAvailable.setSelected( showUnavailable );
	}

	@Override
	public void update(Observable o, Object arg) {
		updateListButtons();
		updateShowUnavailableCheckbox();
	}

}
