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

public class BookMaster {

	private JFrame frmTodoTitle;
	private JTabbedPane tbsMain;
	private JPanel tabBooks;
	private JPanel pnlInventoryStats;
	private JLabel lblNrOfBooks;
	private Component horizontalStrut;
	private JLabel lblNrOfCopies;
	private JPanel pnlBookInventory;
	private GridBagLayout gbl_pnlBookInventory;
	private JPanel pnlBooksInvTop;
	private GridBagConstraints gbc_pnlBooksInvTop;
	private JLabel lblChosen;
	private Component horizontalStrut_1;
	private JButton btnShowSelected;
	private JButton btnAddNewBook;
	private JPanel pnlBooksInvBottom;
	private GridBagConstraints gbc_pnlBooksInvBottom;
	private GridBagLayout gbl_pnlBooksInvBottom;
	private JList<domain.Book> lstBooks;
	private GridBagConstraints gbc_lstBooks;
	private JPanel tabLoans;
	private GridBagLayout gbl_tabLoans;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookMaster window = new BookMaster();
					window.frmTodoTitle.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BookMaster() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTodoTitle = new JFrame();
		frmTodoTitle.setTitle(Messages.getString("BookMaster.frmTodoTitle.title")); //$NON-NLS-1$
		frmTodoTitle.setBounds(100, 100, 600, 400);
		frmTodoTitle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tbsMain = new JTabbedPane(JTabbedPane.TOP);
		tbsMain.setToolTipText(Messages.getString("BookMaster.tbsMain.toolTipText")); //$NON-NLS-1$
		frmTodoTitle.getContentPane().add(tbsMain, BorderLayout.CENTER);
		
		tabBooks = new JPanel();
		tbsMain.addTab("Bücher", null, tabBooks, null);
		tabBooks.setLayout(new BoxLayout(tabBooks, BoxLayout.Y_AXIS));
		
		pnlInventoryStats = new JPanel();
		pnlInventoryStats.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlInventoryStats.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		tabBooks.add(pnlInventoryStats);
		pnlInventoryStats.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		lblNrOfBooks = new JLabel(Messages.getString("BookMaster.lblNrOfBooks.text")); //$NON-NLS-1$
		pnlInventoryStats.add(lblNrOfBooks);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		pnlInventoryStats.add(horizontalStrut);
		
		lblNrOfCopies = new JLabel(Messages.getString("BookMaster.lblNrOfCopies.text")); //$NON-NLS-1$
		pnlInventoryStats.add(lblNrOfCopies);
		
		pnlBookInventory = new JPanel();
		pnlBookInventory.setBorder(new TitledBorder(null, Messages.getString("BookMaster.pnlBookInventory.borderTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		tabBooks.add(pnlBookInventory);
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
		
		tabLoans = new JPanel();
		tbsMain.addTab("Ausleihen", null, tabLoans, null);
		gbl_tabLoans = new GridBagLayout();
		gbl_tabLoans.columnWidths = new int[]{0};
		gbl_tabLoans.rowHeights = new int[]{0};
		gbl_tabLoans.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_tabLoans.rowWeights = new double[]{Double.MIN_VALUE};
		tabLoans.setLayout(gbl_tabLoans);
	}
}
