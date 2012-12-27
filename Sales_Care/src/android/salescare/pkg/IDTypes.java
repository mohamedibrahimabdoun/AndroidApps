package android.salescare.pkg;

public class IDTypes {
	public int Id;
	public String TYPEName;
	public String IDSides;
	
	public IDTypes()
	{
		
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getTYPEName() {
		return TYPEName;
	}

	public void setTYPEName(String name) {
		TYPEName = name;
	}

	public String getIDSides() {
		return IDSides;
	}

	public void setIDSides(String msisdn) {
		this.IDSides = msisdn;
	}
	
	public String getTypesID(int position,IDTypes[] arr)
	{
		String x= Integer.toString(arr[position].getId()) ;
		return x;
		}

}
