package com.example.autocompletetextviewexample;

public class MyClass {
	
	private int  id;
	private String  Name;
	private String  MSISDN;
	public int getId() {
		return id;
	}
	
	
	public MyClass(int id, String name, String mSISDN) {
		super();
		this.id = id;
		Name = name;
		MSISDN = mSISDN;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getMSISDN() {
		return MSISDN;
	}
	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}
	
	
	

}
