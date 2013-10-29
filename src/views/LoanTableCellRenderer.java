package views;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import domain.Loan;
import domain.Library;

public class LoanTableCellRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 1L;
	private Library library;
	
	public LoanTableCellRenderer ( Library library ) {
		this.library = library;
	}
	
	@Override
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected,	boolean hasFocus, int row, int column) {
		Component cellRenderer = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column);
		
		// Loan loan = library.getLoans().get(table.convertRowIndexToModel(row));
		
		Color fgColor = Color.BLACK,
			  bgColor = Color.WHITE;
		
		if (value!= null) {
			if( isSelected ) {
				fgColor = Color.WHITE;
				bgColor = Color.GRAY;
			}
			cellRenderer.setBackground(bgColor);
			cellRenderer.setForeground(fgColor);
		}
		return cellRenderer;
	}

}
