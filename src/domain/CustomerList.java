package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CustomerList extends Observable implements Observer {
	private List<Customer> customers;

	private int editedCustomerPos;
	private int addCustomerIndex;
	private int removeCustomerIndex;
	private int latestCustomer;
	
	public CustomerList() {
		customers = new ArrayList<Customer>();
		editedCustomerPos = -1;
		addCustomerIndex = -1;
		removeCustomerIndex = -1;
		latestCustomer = 0;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		editedCustomerPos = customers.indexOf( o );
		addCustomerIndex = -1;
		removeCustomerIndex = -1;
		
		setChanged();
		notifyObservers( o );
	}
	
	private void doNotify() {
		setChanged();
		notifyObservers();
	}
	
	public int getLatestCustomerNo() {
		return this.latestCustomer;
	}
	
	public boolean removeCustomer( Customer customer ) {
		editedCustomerPos = -1;
		addCustomerIndex = -1;
		removeCustomerIndex = customers.indexOf( customer );
		
		customer.deleteObserver(this);
		boolean result = this.customers.remove( customer );
		
		doNotify();
		return result;
	}
	
	public Customer createAndAddCustomer(String name, String surname) {
		Customer c = new Customer(++latestCustomer, name, surname);
		customers.add( c );
		c.addObserver(this);
		
		editedCustomerPos = -1;
		addCustomerIndex = customers.indexOf( c );
		removeCustomerIndex = -1;
		
		doNotify();
		return c;
	}
	
	public Customer addCustomer( Customer customer ) {
		customers.add( customer );
		customer.setCustomerNo( ++latestCustomer );
		customer.addObserver(this);

		editedCustomerPos = -1;
		addCustomerIndex = customers.indexOf( customer );
		removeCustomerIndex = -1;
		
		doNotify();
		return customers.get( addCustomerIndex );
	}
	
	/**
	 * Returns the customer based on the ID. If the customer is
	 * nonexistent, return null.
	 * @param ID
	 * @return Customer|null
	 * @author PCHR
	 */
	public Customer getByID( int ID ) {
		if ( ID > latestCustomer || ID < 0 ) {
			return null;
		}
		for (Customer customer : customers) {
			if ( customer.getCustomerNo() == ID ) {
				return customer;
			}
		}
		return null;
	}
	
	public int getEditedCustomerPos() {
		return this.editedCustomerPos;
	}
	
	public int getRemovedCustomerIndex() {
		return this.removeCustomerIndex;
	}
	
	public int getAddedCustomerIndex() {
		return this.addCustomerIndex;
	}
	
	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
}
