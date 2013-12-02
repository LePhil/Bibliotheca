package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class LoanList extends Observable {
	private List<Loan> loanList = new ArrayList<Loan>();
	private int editedLoanIndex;
	private int addLoanIndex;
	private int removeLoanIndex;

	public void addLoan(Loan loan) {
		doNotify();
		loanList.add( loan );

	}

	public Loan getLoanAt(int index) {
		if(index >= loanList.size()){
			index = loanList.size() - 1;
		}
		return loanList.get(index);
	}

	public void removeLoanAt(int index) {
		loanList.remove(index);
	}

	public boolean removeLoan(Loan loan) {
		return loanList.remove(loan);
	}

	public int getLength() {
		return loanList.size();
	}

	private void doNotify() {
		setChanged();
		notifyObservers();
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
		doNotify();
		this.loanList = loanList;
	}

}
