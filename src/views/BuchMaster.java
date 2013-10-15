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
import javax.swing.AbstractListModel;

public class BuchMaster {

	private JFrame frmTodoTitle;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BuchMaster window = new BuchMaster();
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
	public BuchMaster() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTodoTitle = new JFrame();
		frmTodoTitle.setTitle("TODO TITLE - BookMaster");
		frmTodoTitle.setBounds(100, 100, 600, 400);
		frmTodoTitle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setToolTipText("TODO TOOLTIP");
		frmTodoTitle.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel booksTab = new JPanel();
		tabbedPane.addTab("Bücher", null, booksTab, null);
		booksTab.setLayout(new BoxLayout(booksTab, BoxLayout.Y_AXIS));
		
		JPanel invStatsPanel = new JPanel();
		invStatsPanel.setBorder(new TitledBorder(null, "Inventar-Statistik", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		booksTab.add(invStatsPanel);
		invStatsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Anzahl Bücher: 842");
		invStatsPanel.add(lblNewLabel);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		invStatsPanel.add(horizontalStrut);
		
		JLabel lblNewLabel_1 = new JLabel("Anzahl Exemplare: 2200");
		invStatsPanel.add(lblNewLabel_1);
		
		JPanel booksInvPanel = new JPanel();
		booksInvPanel.setBorder(new TitledBorder(null, "B\u00FCcher-Inventar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		booksTab.add(booksInvPanel);
		GridBagLayout gbl_booksInvPanel = new GridBagLayout();
		gbl_booksInvPanel.columnWidths = new int[]{0, 0};
		gbl_booksInvPanel.rowHeights = new int[]{0, 0, 0};
		gbl_booksInvPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_booksInvPanel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		booksInvPanel.setLayout(gbl_booksInvPanel);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		booksInvPanel.add(panel, gbc_panel);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel label = new JLabel("Ausgewählt: 1");
		panel.add(label);
		
		JButton btnSelektierteAnzeigen = new JButton("Selektierte Anzeigen");
		panel.add(btnSelektierteAnzeigen);
		
		JButton btnNeuesBuchHinzufgen = new JButton("Neues Buch hinzufügen");
		panel.add(btnNeuesBuchHinzufgen);
		
		JPanel loansTab = new JPanel();
		tabbedPane.addTab("Ausleihen", null, loansTab, null);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0};
		gridBagLayout.rowHeights = new int[]{0};
		gridBagLayout.columnWeights = new double[]{Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{Double.MIN_VALUE};
		loansTab.setLayout(gridBagLayout);
	}
}
