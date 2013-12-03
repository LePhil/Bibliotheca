package test;

import java.util.List;

import junit.framework.TestCase;
import domain.Customer;
import domain.Library;
import domain.Loan;
import domain.Book;

public class LibraryTest extends TestCase {
	
	Library library;
	
	@Override
	protected void setUp() throws Exception {
		
		library = new Library();
		
		// Books
		Book b1 = library.getBookList().createAndAddBook("Design Pattern");
		Book b2 = library.getBookList().createAndAddBook("Refactoring");
		Book b3 = library.getBookList().createAndAddBook("Clean Code");
		
		// Books
		library.createAndAddCopy(b1);
		library.createAndAddCopy(b1);
		library.createAndAddCopy(b1);
		
		library.createAndAddCopy(b2);
		library.createAndAddCopy(b2);		
		
		library.createAndAddCopy(b3);
		
		
		// Customers
		library.createAndAddCustomer("Keller", "Peter");
		library.createAndAddCustomer("Mueller", "Fritz");
		library.createAndAddCustomer("Meier", "Martin");
		
	}

	public void testGetBooksPerTitle() {
		
		Book t = library.getBookList().findByBookTitle("Design Pattern");
		assertEquals(3, library.getCopiesOfBook(t).size());
		
		Book t2 = library.getBookList().findByBookTitle("Clean Code");
		assertEquals(1, library.getCopiesOfBook(t2).size());
		
		Book t3 = library.getBookList().findByBookTitle("noTitle");
		assertEquals(0, library.getCopiesOfBook(t3).size());
	}
	
	public void testLoans() {
		Book t = library.getBookList().findByBookTitle("Design Pattern");
		
		assertEquals(0, library.getLentCopiesOfBook(t).size());
		
		Customer c = library.getCustomers().get(0);
		
		Loan lo = library.createAndAddLoan(c, library.getCopiesOfBook(t).get(0));
		
		assertEquals(1, library.getLentCopiesOfBook(t).size());
		assertEquals(lo, library.getLentCopiesOfBook(t).get(0));
		
		Loan lo2 = library.createAndAddLoan(c, library.getCopiesOfBook(t).get(0));
		assertNull(lo2);
		
		List<Loan> lo3 = library.getCustomerLoans(c);
		assertEquals(1, lo3.size());
		
	}
	
	public void testAvailability () {
		assertEquals(library.getCopies().size(),library.getAvailableCopies().size());
		
		Book t = library.getBookList().findByBookTitle("Refactoring");
		Customer c = library.getCustomers().get(1);
		library.createAndAddLoan(c, library.getCopiesOfBook(t).get(0));
		
		assertEquals(1,library.getLentOutBooks().size());

	}
	

}
