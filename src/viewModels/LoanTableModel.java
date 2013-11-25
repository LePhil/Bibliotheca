package viewModels;


import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;


import views.Messages;

import domain.Loan;
import domain.Library;

public class LoanTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;
	Library library;
    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yy" ); //TODO READ local instead of guessing it :P

	private String[] columns = {
		Messages.getString("BookMasterLoanTable.ColumnHeader.Status"),
		Messages.getString("BookMasterLoanTable.ColumnHeader.CopyID"),
		Messages.getString("BookMasterLoanTable.ColumnHeader.Title"),
		Messages.getString("BookMasterLoanTable.ColumnHeader.LentUntil"),
		Messages.getString("BookMasterLoanTable.ColumnHeader.LentTo")
	};
	
	public LoanTableModel(Library library){
		this.library = library;
		this.library.addObserver(this);
	}
	
	public void propagateUpdate(int pos) {
		fireTableDataChanged();
	}
	
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
	
	
	@Override
	public int getRowCount() {
		//return library.getLentOutBooks().size();
		return library.getLoans().size();
	}

	@Override
	public int getColumnCount() {
		return this.columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Loan loan = library.getLoans().get(rowIndex);
		
		switch (columnIndex) {
		case 0: return loan.isOverdue();
		case 1:
			return loan.getCopy().getInventoryNumber();
		case 2:
			return loan.getCopy().getTitle();
		case 3:
			if ( loan.isLent() ) {
				return Messages.getString("BookMasterLoanTable.LoanDate.Until", dateFormat.format(loan.getDueDate().getTime()));
			} else {
				return dateFormat.format(loan.getReturnDate().getTime());
			}
		case 4:
			return loan.getCustomer().getName() + ", " + loan.getCustomer().getSurname();
		default:
			return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass (int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Boolean.class;
		case 1:
		case 2:
		case 3:
		case 4:
			return String.class;
		default:
			return Object.class;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("UPDATE IN LOANSTABLEMODEL CALLED"); // TODO: remove
		int pos = library.getEditedLoanPos();
		
		if ( pos >= 0 ) {
			// edit happened, redraw edited book
			fireTableRowsUpdated(pos, pos);
		} else {
			pos = library.getBookList().getRemovedBookIndex();
			
			if (pos>=0){
				//remove happend
				fireTableRowsDeleted(pos, pos);
			}else{
				pos = library.getBookList().getInsertedBookIndex();
				if (pos >= 0){
					//insert happend
					fireTableRowsInserted(pos, pos);
				}else{
					fireTableDataChanged();
				}
			}
		}
		
	}

	public Loan getLoan(Object identifier) {
		return this.library.getLoans().get((Integer) identifier);
	}
}
