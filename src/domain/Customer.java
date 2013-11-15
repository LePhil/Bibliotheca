package domain;

import java.util.Observable;

public class Customer extends Observable {
	
	private int customerNo;
	private String name, surname, street, city;
	private int zip;

	public Customer( int customerNo, String name, String surname ) {
		this.customerNo = customerNo;
		this.name = name;
		this.surname = surname;
	}
	public Customer( String name, String surname ) {
		this.customerNo = -1;
		this.name = name;
		this.surname = surname;
	}
	
	public void setAdress(String street, int zip, String city) {
		this.street = street;
		this.zip = zip;
		this.city = city;
		
		doNotify();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		doNotify();
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
		doNotify();
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
		doNotify();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
		doNotify();
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
		doNotify();
	}
	
	public int getCustomerNo() {
		return this.customerNo;
	}
	public void setCustomerNo( int customerNo ) {
		this.customerNo = customerNo;
		doNotify();
	}
	
	private void doNotify() {
		setChanged();
		notifyObservers();
	}
	
	@Override
	public String toString() {
		return name + " " + surname + " , " + street + " , " + zip + " " + city;
	}

}
