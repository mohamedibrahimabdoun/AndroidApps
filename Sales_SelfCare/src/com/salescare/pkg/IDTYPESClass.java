package com.salescare.pkg;

public class IDTYPESClass {
	public int Id;
	public String Name;
	public String msisdn;
	
	public IDTYPESClass()
	{
		
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	public String getTypesID(int position,IDTYPESClass[] arr)
	{
		String x= Integer.toString(arr[position].getId()) ;
		return x;
		}

}
