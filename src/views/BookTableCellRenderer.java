package views;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import domain.Book;
import domain.Library;

public class BookTableCellRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 1L;
	private Library library;
	
	public BookTableCellRenderer ( Library library ) {
		this.library = library;
	}
	
	@Override
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected,	boolean hasFocus, int row, int column) {
		Component cellRenderer = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column);
		Book book = library.getBooks().get(table.convertRowIndexToModel(row));
		Boolean available = library.getCopiesOfBook(book).size() != 1;
		Color fgColor = Color.BLACK,
			  bgColor = Color.WHITE;
		
		if (value!= null) {
			if( isSelected ) {
				fgColor = Color.WHITE;
				bgColor = Color.GRAY;
				
				if(!available) {
					bgColor = Color.GRAY.brighter();
				}
			}
			// Show red text if no copies are available
			if(!available) {
				fgColor = Color.RED.darker();
			}
			cellRenderer.setBackground(bgColor);
			cellRenderer.setForeground(fgColor);
		}
		return cellRenderer;
	}

}