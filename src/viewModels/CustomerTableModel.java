package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import views.Messages;
import domain.Customer;
import domain.CustomerList;
import domain.Library;

public class CustomerTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;
	Library library;
	CustomerList customers;
	
	private String[] columns = {
		Messages.getString("CustomerMasterTable.ColumnHeader.CustomerNo"),
		Messages.getString("CustomerMasterTable.ColumnHeader.Name"),
		Messages.getString("CustomerMasterTable.ColumnHeader.Address")
	};
	
	public CustomerTableModel( Library library ) {
		this.library = library;
		//this.library.addObserver(this);
		this.customers = library.getCustomerList();
		
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
		System.out.println( "CustomerTableModel: getRowCount = "+customers.getCustomers().size() );
		return customers.getCustomers().size();
	}
	@Override
	public int getColumnCount() {
		return this.columns.length;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Customer customer = customers.getCustomers().get(rowIndex);
		String returnString;
		
		switch (columnIndex) {
		case 0:
			returnString = ""+customer.getCustomerNo();
			break;
		case 1:
			returnString = customer.getName()+" "+customer.getSurname();
			break;
		case 2:
			returnString = customer.getStreet()+" "+customer.getZip()+" "+customer.getCity();
			break;
		default:
			returnString = "";
			break;
		}
		
		return returnString;
	}
	
	@Override
	public Class getColumnClass (int columnIndex) {
		switch (columnIndex) {
		case 0:
		case 1:
		case 2:
			return String.class;
		default:
			return Object.class;
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("UPDATE IN CUSTOMERTABLEMODEL!");
		
		int pos = customers.getEditedCustomerPos();
			
		if ( pos >= 0 ) {
			System.out.println("Customer was edited: "+pos);
			// Customer was edited
			fireTableRowsUpdated( pos, pos );
		} else {
			pos = customers.getRemovedCustomerIndex();
			System.out.println("pos: "+pos);
			if ( pos >= 0 ) {
				System.out.println("Customer was removed: "+pos);
				// Customer was removed
				fireTableRowsDeleted( pos, pos );
			} else {
				pos = customers.getAddedCustomerIndex();
				
				if ( pos >= 0 ) {
					System.out.println("Customer was added: "+pos);
					// Customer was added
					fireTableRowsInserted( pos, pos );
					fireTableDataChanged();
				} else {
					// Nothing of our concern. Maybe a TODO?
				}
			}
		}
	}
	
	public Customer getCustomer(Object identifier) {
		return this.customers.getCustomers().get((Integer) identifier);
	}
}
