package viewModels;

import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import views.Messages;

import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Loan;

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
		String returnString;
		
		switch (columnIndex) {
		case 0:
			List<Copy> copies = library.getCopiesOfBook(book);;
			List<Loan> lentCopies = library.getLentCopiesOfBook(book);
			int availCopies = copies.size()-lentCopies.size();
			Calendar pickupCal, earliestReturnCal = null;
			
			// Show available of total copies for this book. (e.g. 2/3)
			returnString = availCopies+"/"+copies.size();
			
			// if there are no more copies available, show the expected date when one will be available again.
			if ( availCopies == 0 ) {
				for (Loan loan : lentCopies) {
					pickupCal = loan.getPickupDate();
					
					if ( earliestReturnCal == null ||
							pickupCal.before(earliestReturnCal)) {
						earliestReturnCal = (Calendar) pickupCal.clone();
					}
				}
				earliestReturnCal.add( Calendar.DAY_OF_MONTH, 30 );
				returnString += " - "+earliestReturnCal.getTime().toString();
			}
			break;
		case 1:
			returnString = book.getName();
			break;
		case 2:
			returnString = book.getAuthor();
			break;
		case 3:
			returnString = book.getPublisher();
			break;
		default:
			returnString = "";
		}
		
		return returnString;
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
