package app.ws.android;

import android.R.integer;

public class Posinfo

 {
      private integer p_id;
	  private String p_name;
	  private String p_mobile;
	  private String p_owner;
	  private String p_address;
	  private String p_imei;
	  private String p_date;
	  private String p_lat;
	  private String p_lng;
	  private String p_stock;
	  private String p_postypeid;
	  private String p_brandid;
	  

	  public void setId(integer id) {
			this.p_id = id;
		}

		public integer getId() {
			return p_id;
		}
	 public void setPosname(String posname)
	  {
		  this.p_name=posname;
	  }
    
	 public void setMobile(String mobile)
	  {
		  this.p_mobile=mobile;
	  }
	
	 public void setOwner(String owner)
	 {
		 this.p_owner=owner;
	 }
	 public void setAddress(String address)
	 {
		 this.p_address=address;
	 }
	 
	 public String getPosname()
	 {
		 return p_name;
	 }
	 
	 public String getMobile()
	 {
		 return p_mobile;
	 }
	 public String getOwner()
	 {
		 return p_owner;
	 }
	 public String getAddress()
	 {
		 return p_address;
	 }

	public void setImei(String p_imei) {
		this.p_imei = p_imei;
	}

	public String getImei() {
		return p_imei;
	}

	public void setDate(String p_date) {
		this.p_date = p_date;
	}

	public String getDate() {
		return p_date;
	}

	public void setLat(String p_lat) {
		this.p_lat = p_lat;
	}

	public String getLat() {
		return p_lat;
	}

	public void setLng(String p_lng) {
		this.p_lng = p_lng;
	}

	public String getLng() {
		return p_lng;
	}

	public void setStock(String p_stock) {
		this.p_stock = p_stock;
	}

	public String getStock() {
		return p_stock;
	}

	public void setPostypeid(String p_postypeid) {
		this.p_postypeid = p_postypeid;
	}

	public String getPostypeid() {
		return p_postypeid;
	}

	public void setBrandid(String p_brandid) {
		this.p_brandid = p_brandid;
	}

	public String geBrandid() {
		return p_brandid;
	}
	
}

