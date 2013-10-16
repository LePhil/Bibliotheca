package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JList;

import domain.Book;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class BookMaster {

	private JFrame bookMaster;

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
	
	/**
	 * Just for testing purposes!!!
	 * TODO: remove main method
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookMaster window = new BookMaster();
					window.bookMaster.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param bookList 
	 */
	public BookMaster() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		bookMaster = new JFrame();
		bookMaster.setTitle(Messages.getString("BookMaster.frmTodoTitle.title")); //$NON-NLS-1$
		bookMaster.setBounds(100, 100, 600, 400);
		bookMaster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tbsMain = new JTabbedPane(JTabbedPane.TOP);
		tbsMain.setToolTipText(Messages.getString("BookMaster.tbsMain.toolTipText")); //$NON-NLS-1$
		bookMaster.getContentPane().add(tbsMain, BorderLayout.CENTER);
		
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
		pnlBooksInvTop.add(btnShowSelected);
		
		btnAddNewBook = new JButton(Messages.getString("BookMaster.btnAddNewBook.text")); //$NON-NLS-1$
		btnAddNewBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				BookDetail detailView = new BookDetail();
				detailView.getFrame().setVisible(true);
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
		
		lstBooks = new JList<domain.Book>();
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
	}
	
	public JFrame getBookMaster() {
		return bookMaster;
	}

	public void setBookMaster(JFrame bookMaster) {
		this.bookMaster = bookMaster;
	}

}
