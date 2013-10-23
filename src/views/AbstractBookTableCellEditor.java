	package views;

	import java.awt.Component;
import java.util.EventObject;

	import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import domain.Book;
import domain.Library;

public abstract class AbstractBookTableCellEditor extends DefaultCellEditor {

			private int editRow;
			private JTable table;
			private Library library;
			
			public AbstractBookTableCellEditor (JTextField tf, Library library) {
				super(tf);
				this.library = library;
			}

			@Override
			public boolean isCellEditable(EventObject anEvent) {
				return true;
			}

			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
				this.editRow=row;
				this.table=table;
				return super.getTableCellEditorComponent(table, value, isSelected, row, column);
			}

			@Override
			public boolean stopCellEditing() {
				super.stopCellEditing();
				if (table==null) {
					return true; // sometimes called on row selection
				}
				Book book = library.getBooks().get(table.convertRowIndexToModel(editRow));
				return setBookValue(book); //false would disallow leaving the field
			}

			public abstract boolean setBookValue(Book book);
				
	}

