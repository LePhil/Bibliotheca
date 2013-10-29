package views;

import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import domain.Library;

public class LibraryTab extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Library library;
	
	LibraryTab( Library library ) {
		this.setLibrary(library);
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}
}
