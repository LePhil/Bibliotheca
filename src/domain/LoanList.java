package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class LoanList extends Observable implements Observer {
	private List<Loan> loanList = new ArrayList<Loan>();
	private int editedLoanIndex;
	private int addLoanIndex;
	private int removeLoanIndex;

	public void addLoan() {
		// TODO pforster
	}

	public void addLoan(Loan loan) {
		// TODO pforster

	}

	public Loan getLoanAt(int index) {
		if(index >= loanList.size()){
			index = loanList.size() - 1;
		}
		return loanList.get(index);
	}

	public void removeLoanAt(int index) {
		// TODO pforster
	}

	public boolean removeLoan(Loan loan) {
		// TODO pforster
		return false;
	}

	public int getLength() {
		return loanList.size();
	}

	private void doNotify() {
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO pforster

	}

	public int getEditedLoanIndex() {
		return editedLoanIndex;
	}

	public int getAddLoanIndex() {
		return addLoanIndex;
	}

	public int getRemoveLoanIndex() {
		return removeLoanIndex;
	}

	public int getLoanIndex(Loan loan) {
		return loanList.indexOf(loan);
	}

	public List<Loan> getLoanList() {
		return loanList;
	}

	public void setLoanList(List<Loan> loanList) {
		this.loanList = loanList;
	}

}
