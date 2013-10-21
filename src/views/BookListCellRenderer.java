package views;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import domain.Book;


public class BookListCellRenderer implements ListCellRenderer {
	
	JLabel renderLabel = new JLabel();
	
	public BookListCellRenderer () {
		renderLabel.setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object obj,
			int row, boolean isSelected, boolean hasFocus) {

		Book book = (Book) obj;
		renderLabel.setText(book.toString());
		
		
		return renderLabel;
	}

}
