package app.ws.android;

public class POSInfoBrand {

	public int id;
	public String branddesc;
		public POSInfoBrand() 
		{
		
		}
		
		void setBrandId(int id)
		{
			this.id=id;
		}
		
		void setBrandDesc(String brand)
		{
			this.branddesc=brand;
		}
		
		String getBranadDesc()
		{
			return branddesc; 
		}
		
		public int getBrandId()
		{
			return id;
		}
		
		public String getPOSBRANDID(int position,POSInfoBrand[] arr)
		{
			String x= Integer.toString(arr[position].getBrandId()) ;
			return x;
			}


}
