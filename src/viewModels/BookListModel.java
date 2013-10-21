package viewModels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import domain.Book;
import domain.Library;

public class BookListModel extends AbstractListModel implements Observer {

	private static final long serialVersionUID = 1L;
	Library library;
	
	public BookListModel(Library library) {
		this.library = library;
		library.addObserver( this );
	}
	
	public void propagateUpdate(int pos) {
		fireContentsChanged(this, pos, pos);
	}

	@Override
	public void update(Observable obj, Object arg1) {
		System.out.println("UPDATE ON BOOKLISTMODEL");
		//TODO fix this mess.
		//if (obj instanceof Book){
			//int pos = bookList.getBooks().indexOf((Book)obj);
			//fireContentsChanged(this, pos, pos);						
		//}
		if (obj instanceof Library){
			fireContentsChanged(this, 0, library.getBooks().size());
		}
	}

	@Override
	public Object getElementAt(int index) {
		return library.getBooks().get(index);
	}

	@Override
	public int getSize() {
		return library.getBooks().size();
	}
}
