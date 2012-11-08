package app.ws.android;

public class POSInfoType {

	public int id;
	public String typedesc;
		public POSInfoType()
		{
 		
		}
	
		public void setPostypeId(int id)
		{
			  this.id=id;
		}
		
		public void setPostypeDesc(String type)
		{
			this.typedesc=type;
		}
		
		String getPostypeDesc()
		{
			return typedesc; 
		}
		
		public int getId()
		{
			return id;
		}
		
		public String getPOSTYPEID(int postion,POSInfoType[] arr)
		{
			String x= Integer.toString(arr[postion].getId()) ;
			return x;
		}		

}
