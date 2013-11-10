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
		fireTableDataChanged();
	}
	
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
		// PCHR: We decided not to make the cells editable, as that interferes with the normal clicks on the cells.
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
				// library.getLentCopiesOfBook(book) <-- sowas... TODO PCHR
				return "bald";	//TODO PCHR find datum
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

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("UPDATE IN BOOKTABLEMODEL CALLED");
		int pos = library.getEditedBookPos();
		
		if ( pos >= 0 ) {
			// edit happened, redraw edited book
			fireTableRowsUpdated(pos, pos);
		} else {
			pos = library.getRemovedBookIndex();
			
			if (pos>=0){
				//remove happend
				fireTableRowsDeleted(pos, pos);
			}else{
				pos = library.getInsertedBookIndex();
				if (pos >= 0){
					//insert happend
					fireTableRowsInserted(pos, pos);
				}else{
					fireTableDataChanged();
				}
			}
		}
		
	}

	public Book getBook(Object identifier) {
		return this.library.getBooks().get((Integer) identifier);
	}
}
