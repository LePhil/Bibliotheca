package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import views.Messages;

import domain.Book;
import domain.Library;

public class BookTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;
	Library library;
	private String[] columns = {
		Messages.getString("BookMasterTable.ColumnHeader.Available"),
		Messages.getString("BookMasterTable.ColumnHeader.Title"),
		Messages.getString("BookMasterTable.ColumnHeader.Author"),
		Messages.getString("BookMasterTable.ColumnHeader.Publisher")
	};
	
	public BookTableModel(Library library){
		this.library = library;
		this.library.addObserver(this);
	}
	
	public void propagateUpdate(int pos) {
		System.out.println("it's happening!");
		fireTableDataChanged();
	}
	
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		//TODO return true if we have a CellEditor
		return false;
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
	
	
	@Override
	public int getRowCount() {
		return library.getBooks().size();
	}

	@Override
	public int getColumnCount() {
		return this.columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Book book = library.getBooks().get(rowIndex);
		int copies;
		
		switch (columnIndex) {
		case 0:
			copies = library.getCopiesOfBook(book).size();
			if ( copies == 0 ) {
				return "bald";	//TODO find datum
			} else {
				return String.valueOf(copies);
			}
		case 1:
			return book.getName();
		case 2:
			return book.getAuthor();
		case 3:
			return book.getPublisher();
		default:
			return null;
		}
	}
	
	@Override
	public Class getColumnClass (int columnIndex) {
		switch (columnIndex) {
		case 0:
		case 1:
		case 2:
		case 3:
			return String.class;
		default:
			return Object.class;
		}
	}
	
	//public int getBookIndex(Book book) {
	//	return library.getBooks().get(book);
	//}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("UPDATE IN BOOKTABLEMODEL CALLED");
		//int bookPos = library.getBooks().getEditedBookPos();
		
	}

	
}
