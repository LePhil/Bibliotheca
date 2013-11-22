package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import domain.Book;
import domain.Copy;
import domain.CopyList;

public class CopyTableModel extends AbstractTableModel implements
		Observer {

	private static final long serialVersionUID = 1L;

	private String[] columns = {
		"InvetoryNr",
		"Title",
		"Author"
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
		default:
			return null;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
