package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import views.Messages;

import domain.Book;
import domain.Copy;
import domain.CopyList;

public class CopyTableModel extends AbstractTableModel implements
		Observer {

	private static final long serialVersionUID = 1L;

	private String[] columns = {
		Messages.getString( "BookMasterLoanTable.ColumnHeader.CopyID" ),
		Messages.getString( "BookDetail.lblTitle.text" ),
		Messages.getString( "BookDetail.lblAuthor.text" ),
		Messages.getString( "BookMasterLoanTable.ColumnHeader.Status" ),
		Messages.getString( "CopyTable.ColHeader.Condition" )
	};

	private CopyList copyList;

	public CopyTableModel(CopyList copyList) {
		this.copyList = copyList;
		copyList.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return copyList.getLength();
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
		Copy copy = copyList.getCopyAt(rowIndex);
		Book book = copy.getTitle();
		
		switch (columnIndex) {
		case 0:
			return copy.getInventoryNumber();
		case 1:
			return book.getName();
		case 2:
			return book.getAuthor();
		case 3:
			return "TODO - status"; // TODO status. (ausgeliehen or whatever)
		case 4:
			return conditionToString( copy.getCondition() ); // TODO: make editable!!!
		default:
			return null;
		}
	}
	
	private String conditionToString( Copy.Condition condition ) {
		switch (condition) {
		case NEW:
			return Messages.getString( "Copy.Condition.New" );
		case DAMAGED:
			return Messages.getString( "Copy.Condition.Damaged" );
		case GOOD:
			return Messages.getString( "Copy.Condition.Good" );
		case LOST:
			return Messages.getString( "Copy.Condition.Lost" );
		case WASTE:
			return Messages.getString( "Copy.Condition.Waste" );
		default:
			return "";
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		fireTableDataChanged();
	}

}
