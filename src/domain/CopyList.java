package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CopyList extends Observable implements Observer {
	private List<Copy> copyList = new ArrayList<Copy>();
	private int editedCopyIndex;
	private int addCopyIndex;
	private int removeCopyIndex;

	public void addCopy() {
		// TODO pforster
	}

	public void addCopy(Copy copy) {
		copyList.add(copy);

	}

	public Copy getCopyAt(int index) {
		if (index >= copyList.size()) {
			index = copyList.size() - 1;
		}
		return copyList.get(index);
	}

	public void removeCopyAt(int index) {
		doNotify();
		copyList.remove(index);
	}

	public boolean removeCopy(Copy copy) {
		doNotify();
		return copyList.remove(copy);
	}

	public int getLength() {
		return copyList.size();
	}

	private void doNotify() {
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO pforster

	}

	public int getEditedCopyIndex() {
		return editedCopyIndex;
	}

	public int getAddCopyIndex() {
		return addCopyIndex;
	}

	public int getRemoveCopyIndex() {
		return removeCopyIndex;
	}

	public int getCopyIndex(Copy copy) {
		return copyList.indexOf(copy);
	}

	public List<Copy> getCopyList() {
		return copyList;
	}

	public void setCopyList(List<Copy> copyList) {
		doNotify();
		this.copyList = copyList;
	}

}
