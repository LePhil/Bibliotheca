package renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import sun.swing.DefaultLookup;

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
		Book book = library.getBookList().getBooks().get(table.convertRowIndexToModel(row));
		int numberOfCopies = library.getCopiesOfBook(book).size();
		int numberOfLentCopies = library.getLentCopiesOfBook(book).size();
		boolean available = numberOfCopies - numberOfLentCopies > 0;
		
		Color fgColor = Color.BLACK,
			  bgColor = Color.WHITE;
			
		Color alternateColor = DefaultLookup.getColor(this, ui, "Table.alternateRowColor");
		
		if (value!= null) {
			
			if ( row % 2 != 0 ) {
				bgColor = alternateColor;
			}
			
			if( isSelected ) {
				fgColor = Color.WHITE;
				bgColor = bgColor.darker();
			}
			
			if ( !available ) {
				fgColor = Color.RED;
			}
			
			cellRenderer.setBackground(bgColor);
			cellRenderer.setForeground(fgColor);
		}
		return cellRenderer;
	}

}
