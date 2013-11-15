package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Library extends Observable implements Observer {

	private List<Copy> copies;
	private List<Customer> customers;
	private List<Loan> loans;
	private List<Book> books;
	
	private CustomerList customerList;
	
	private int editedBookPos;
	private int addBookIndex;
	private int removeBookIndex;
	
	private int editedLoanPos;
	private int addLoanIndex;
	private int removeLoanIndex;
	
	private int editedCustomerPos;
	private int addCustomerIndex;
	private int removeCustomerIndex;
	private int latestCustomer;

	public Library() {
		copies = new ArrayList<Copy>();
		customers = new ArrayList<Customer>();
		
		//TODO: try out this way.
		setCustomerList(new CustomerList());
		
		loans = new ArrayList<Loan>();
		books = new ArrayList<Book>();
	}

	public Loan createAndAddLoan(Customer customer, Copy copy) {
		if (!isCopyLent(copy)) {
			Loan l = new Loan(customer, copy);
			loans.add(l);
			doNotify();
			return l;
		} else {
			return null;
		}
	}

	public Book createAndAddBook(String name) {
		return this.addBook( new Book( name ) );
	}
	
	public Book addBook( Book book ) {
		books.add( book );
		
		editedBookPos = -1;
		addBookIndex = books.indexOf(book);
		removeBookIndex = -1;
		
		doNotify();
		return book;
	}
	
	public boolean removeBook( Book book ) {
		book.deleteObserver(this);
		boolean succeeded = books.remove(book);
		
		editedBookPos = -1;
		addBookIndex = -1;
		removeBookIndex = books.indexOf(book);
		
		doNotify();
		return succeeded;
	}

	public Copy createAndAddCopy(Book title) {
		Copy c = new Copy(title);
		copies.add(c);
		doNotify();
		return c;
	}
	
	public void removeCopy(Copy copy){
		int index = copies.indexOf(copy);
		copies.remove(index);
	}

	public Integer getEditedBookPos() {
		return editedBookPos;
	}
	
	public Book findByBookTitle(String title) {
		for (Book b : books) {
			if (b.getName().equals(title)) {
				return b;
			}
		}
		return null;
	}

	public boolean isCopyLent(Copy copy) {
		for (Loan l : loans) {
			if (l.getCopy().equals(copy) && l.isLent()) {
				return true;
			}
		}
		return false;
	}

	public List<Copy> getCopiesOfBook(Book book) {
		List<Copy> res = new ArrayList<Copy>();
		for (Copy c : copies) {
			if (c.getTitle().equals(book)) {
				res.add(c);
			}
		}

		return res;
	}

	public List<Loan> getLentCopiesOfBook(Book book) {
		List<Loan> lentCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCopy().getTitle().equals(book) && l.isLent()) {
				lentCopies.add(l);
			}
		}
		return lentCopies;
	}

	public List<Loan> getCustomerLoans(Customer customer) {
		List<Loan> lentCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCustomer().equals(customer)) {
				lentCopies.add(l);
			}
		}
		return lentCopies;
	}
	
	public List<Loan> getOverdueLoans() {
		List<Loan> overdueLoans = new ArrayList<Loan>();
		for ( Loan l : getLoans() ) {
			if (l.isOverdue())
				overdueLoans.add(l);
		}
		return overdueLoans;
	}
	
	public List<Copy> getAvailableCopies(){
		return getCopies(false);
	}
	
	public List<Copy> getLentOutBooks(){
		return getCopies(true);
	}

	private List<Copy> getCopies(boolean isLent) {
		List<Copy> retCopies = new ArrayList<Copy>();
		for (Copy c : copies) {
			if (isLent == isCopyLent(c)) {
				retCopies.add(c);
			}
		}
		return retCopies;
	}

	public List<Copy> getCopies() {
		return copies;
	}

	public List<Loan> getLoans() {
		return loans;
	}

	public List<Book> getBooks() {
		return books;
	}

	public List<Customer> getCustomers() {
		return customers;
	}
	
	private void doNotify() {
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable book, Object arg) {
		editedBookPos = books.indexOf(book);
		addBookIndex = -1;
		removeBookIndex = -1;
		
		setChanged();
		notifyObservers( book );
	}
	
	public int getInsertedBookIndex() {
		return addBookIndex;
	}
	
	public int getRemovedBookIndex() {
		return removeBookIndex;
	}
	 
	public int getBookIndex(Book book) {
		return books.indexOf(book);
	}
	
	//LOANS
	public int getEditedLoanPos() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getInsertedLoanIndex() {
		return addLoanIndex;
	}
	
	public int getRemovedLoanIndex() {
		return removeLoanIndex;
	}
	
	public int getloansIndex(Loan loan) {
		return loans.indexOf(loan);
	}
	
	//CUSTOMER
	public int getLatestCustomerNo() {
		return customerList.getLatestCustomerNo();
	}
	
	/*
	public boolean removeCustomer( Customer customer ) {
		boolean result = this.customers.remove( customer );
		
		editedCustomerPos = -1;
		addCustomerIndex = -1;
		removeCustomerIndex = customers.indexOf( customer );
		
		doNotify();
		return result;
	}
	*/
	
	// used to fill the list by the application from XML
	public Customer createAndAddCustomer(String name, String surname) {
		//Customer c = new Customer(++latestCustomer, name, surname);
		//customers.add(c);
		
		//editedCustomerPos = -1;
		//addCustomerIndex = customers.indexOf( c );
		//removeCustomerIndex = -1;
		
		//doNotify();
		return customerList.createAndAddCustomer( name, surname );
	}
	
	/*
	public Customer addCustomer( Customer customer ) {
		customers.add( customer );
		
		//Customer addedCustomer = this.createAndAddCustomer(customer.getName(), customer.getSurname());
		//addedCustomer.setAdress(customer.getStreet(), customer.getZip(), customer.getCity() );
		
		editedCustomerPos = -1;
		addCustomerIndex = customers.indexOf( customer );
		removeCustomerIndex = -1;
		
		doNotify();
		return customers.get( addCustomerIndex );
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
	*/

	public CustomerList getCustomerList() {
		return customerList;
	}

	public void setCustomerList(CustomerList customerList) {
		this.customerList = customerList;
	}
}