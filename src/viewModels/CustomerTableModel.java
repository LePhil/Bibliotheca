package viewModels;

import i18n.Messages;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import domain.Customer;
import domain.CustomerList;

public class CustomerTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;
	CustomerList customers;
	
	private String[] columns = {
		Messages.getString("CustomerMasterTable.ColumnHeader.CustomerNo"),
		Messages.getString("CustomerMasterTable.ColumnHeader.Name"),
		Messages.getString("CustomerMasterTable.ColumnHeader.Address")
	};
	
	public CustomerTableModel( CustomerList customers) {
		this.customers = customers;
		
		this.customers.addObserver( this );
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
		return customers.getCustomers().size();
	}
	@Override
	public int getColumnCount() {
		return this.columns.length;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Customer customer = customers.getCustomers().get(rowIndex);
		
		switch (columnIndex) {
		case 0:
			return customer.getCustomerNo();
		case 1:
			return customer.getName()+" "+customer.getSurname();
		case 2:
			return customer.getStreet()+" "+customer.getZip()+" "+customer.getCity();
		default:
			return "";
		}
	}
	
	@Override
	public Class<?> getColumnClass (int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Integer.class;
		case 1:
		case 2:
			return String.class;
		default:
			return Object.class;
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		int pos = customers.getEditedCustomerPos();
			
		if ( pos >= 0 ) {
			// Customer was edited
			fireTableRowsUpdated( pos, pos );
		} else {
			pos = customers.getRemovedCustomerIndex();

			if ( pos >= 0 ) {
				// Customer was removed
				fireTableRowsDeleted( pos, pos );
			} else {
				pos = customers.getAddedCustomerIndex();
				
				if ( pos >= 0 ) {
					// Customer was added
					fireTableRowsInserted( pos, pos );
				} else {
					fireTableDataChanged();
				}
			}
		}
	}
	
	public Customer getCustomer(Object identifier) {
		return this.customers.getCustomers().get((Integer) identifier);
	}
}