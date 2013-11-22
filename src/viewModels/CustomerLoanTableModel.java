package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import domain.Book;
import domain.Copy;
import domain.LoanList;

public class CustomerLoanTableModel extends AbstractTableModel implements
		Observer {

	private static final long serialVersionUID = 1L;

	private String[] columns = { "Exemplar-ID", "Titel", "Author" };

	private LoanList loanList;

	public CustomerLoanTableModel(LoanList loanList) {
		this.loanList = loanList;
		loanList.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return loanList.getLength();
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Copy copy = loanList.getLoanAt(rowIndex).getCopy();
		Book book = copy.getTitle();
		switch (columnIndex) {
		case 0:
			return copy.getInventoryNumber();
		case 1:
			return book.getName();
		case 2:
			return book.getAuthor();
		default:
			return null;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		fireTableDataChanged();
	}

}
