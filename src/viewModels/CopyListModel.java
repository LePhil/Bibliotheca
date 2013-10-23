package viewModels;

import java.util.List;

import javax.swing.AbstractListModel;

import domain.Copy;

public class CopyListModel extends AbstractListModel<Copy> {

	private static final long serialVersionUID = -7422713137199201598L;

	private List<Copy> copies;

	public CopyListModel(List<Copy> copies) {
		this.copies = copies;
	}

	@Override
	public int getSize() {
		if (copies == null) {
			return 0;
		}
		return copies.size();
	}

	@Override
	public Copy getElementAt(int index) {
		if (copies == null) {
			return null;
		}
		return copies.get(index);
	}

}
