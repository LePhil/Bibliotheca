package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Library extends Observable implements Observer {

	private List<Loan> loans;
	
	private CustomerList customerList;
	private BookList bookList;
	private CopyList copies;
	
	private int addLoanIndex;
	private int removeLoanIndex;
	private int editedLoanIndex;

	public Library() {
		copies = new CopyList();

		setBookList( new BookList() );
		setCustomerList( new CustomerList() );
		
		loans = new ArrayList<Loan>();
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

	public Copy createAndAddCopy(Book title) {
		Copy c = new Copy(title);
		copies.addCopy( c );
		doNotify();
		return c;
	}
	
	public void removeCopy(Copy copy){
		copies.removeCopy( copy );
	}
	
	public void returnLoan(Loan loan){
		for(Loan l : this.getLoans()){
			if(loan.equals(l)){
				l.returnCopy();
			}
		}
		doNotify();
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
		for ( Copy c : copies.getCopyList() ) {
			if ( c.getTitle().equals(book) ) {
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
		for ( Copy c : copies.getCopyList() ) {
			if ( isLent == isCopyLent( c ) ) {
				retCopies.add( c );
			}
		}
		return retCopies;
	}

	public List<Copy> getCopies() {
		return copies.getCopyList();
	}
	
	public CopyList getCopyList() {
		return copies;
	}

	public List<Loan> getLoans() {
		return loans;
	}

	public List<Customer> getCustomers() {
		return customerList.getCustomers();
	}
	
	private void doNotify() {
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers( o );
	}
	
	//LOANS
	public int getEditedLoanPos() {
		return editedLoanIndex;
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
	// used to fill the list by the application from XML
	public Customer createAndAddCustomer(String name, String surname) {
		return customerList.createAndAddCustomer( name, surname );
	}

	public CustomerList getCustomerList() {
		return customerList;
	}

	public void setCustomerList(CustomerList customerList) {
		this.customerList = customerList;
	}

	public BookList getBookList() {
		return bookList;
	}

	public void setBookList(BookList bookList) {
		this.bookList = bookList;
	}
}