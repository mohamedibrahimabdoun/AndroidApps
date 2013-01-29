package android.care;

public class IDTypes {
	public int Id;
	public String TYPEName;
	public int IDSides;
	
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


	
	public int getIDSides() {
		return IDSides;
	}

	public void setIDSides(int iDSides) {
		IDSides = iDSides;
	}

	public String getTypesID(int position,IDTypes[] arr)
	{
		String x= Integer.toString(arr[position].getId()) ;
		return x;
		}

}
