package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BookList extends Observable implements Observer {
	private List<Book> books;
	
	private int editedBookPos;
	private int addBookIndex;
	private int removeBookIndex;
	
	public BookList() {
		books = new ArrayList<Book>();
		editedBookPos = -1;
		addBookIndex = -1;
		removeBookIndex = -1;
	}
	
	@Override
	public void update(Observable book, Object arg) {
		editedBookPos = books.indexOf(book);
		addBookIndex = -1;
		removeBookIndex = -1;
		
		setChanged();
		notifyObservers( book );
	}
	private void doNotify() {
		setChanged();
		notifyObservers();
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
	public List<Book> getBooks() {
		return books;
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
}
