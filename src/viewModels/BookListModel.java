package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import domain.Book;
import domain.Library;

public class BookListModel extends AbstractListModel implements Observer {

	private static final long serialVersionUID = 1L;
	Library bookList;
	
	public BookListModel(Library bookList) {
		this.bookList = bookList;
		bookList.addObserver( this );
	}
	
	public void propagateUpdate(int pos) {
		fireContentsChanged(this, pos, pos);
	}

	@Override
	public void update(Observable obj, Object arg1) {
		//TODO fix this mess.
		/*
		if (obj instanceof Book){
			int pos = bookList.getBooks().indexOf((Book)obj);
			fireContentsChanged(this, pos, pos);						
		}
		*/
		if (obj instanceof Library){
			fireContentsChanged(this, 0, bookList.getBooks().size());
		}
	}

	@Override
	public Object getElementAt(int index) {
		return bookList.getBooks().get(index);
	}

	@Override
	public int getSize() {
		return bookList.getBooks().size();
	}
}
