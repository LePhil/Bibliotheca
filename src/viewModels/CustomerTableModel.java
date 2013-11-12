package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import views.Messages;
import domain.Library;

public class CustomerTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;
	Library library;
	private String[] columns = {
		Messages.getString("CustomerMasterTable.ColumnHeader.CustomerNo"),
		Messages.getString("CustomerMasterTable.ColumnHeader.Name"),
		Messages.getString("CustomerMasterTable.ColumnHeader.Address")
	};
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
