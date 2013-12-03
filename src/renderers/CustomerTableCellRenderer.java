package renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import sun.swing.DefaultLookup;

public class CustomerTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public CustomerTableCellRenderer () {}
	
	@Override
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected,	boolean hasFocus, int row, int column) {
		Component cellRenderer = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column);
		Color fgColor = Color.BLACK,
			  bgColor = Color.WHITE;
			
		Color alternateColor = DefaultLookup.getColor(this, ui, "Table.alternateRowColor");
		
		if (value!= null) {
			
			if ( row % 2 != 0 ) {
				bgColor = alternateColor;
			}
			
			if( isSelected ) {
				fgColor = Color.WHITE;
				bgColor = bgColor.darker();
			}
			
			cellRenderer.setBackground(bgColor);
			cellRenderer.setForeground(fgColor);
		}
		return cellRenderer;
	}
}
