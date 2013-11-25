package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import views.Messages;

import domain.Book;
import domain.Copy;
import domain.Loan;
import domain.LoanList;

public class CustomerLoanTableModel extends AbstractTableModel implements
		Observer {

	private static final long serialVersionUID = 1L;

	private String[] columns = {
		Messages.getString( "BookMasterLoanTable.ColumnHeader.CopyID" ),
		Messages.getString( "BookMasterLoanTable.ColumnHeader.Status" ),
		Messages.getString( "BookDetail.lblTitle.text" ),
		Messages.getString( "BookDetail.lblAuthor.text" )
	};

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
		Loan loan = loanList.getLoanAt(rowIndex);
		Copy copy = loan.getCopy();
		Book book = copy.getTitle();
		switch (columnIndex) {
		case 0:
			return copy.getInventoryNumber();
		case 1:
			return loan.isOverdue() ? 0 : loan.isLent() ? 1 : 2;
		case 2:
			return book.getName();
		case 3:
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
