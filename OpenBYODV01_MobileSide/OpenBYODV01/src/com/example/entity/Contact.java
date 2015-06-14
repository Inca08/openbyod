package com.example.entity;

public class Contact {
	
	private String name;
	private int contactNumber;
	private int id;
	
	public Contact(String name, int contactNumber) {
		super();
		this.name = name;
		this.contactNumber = contactNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(int contactNumber) {
		this.contactNumber = contactNumber;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Contact(String name, int contactNumber, int id) {
		super();
		this.name = name;
		this.contactNumber = contactNumber;
		this.id = id;
	}
	
	

}
