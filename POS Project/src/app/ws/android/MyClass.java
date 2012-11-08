package app.ws.android;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.widget.Toast;

public class MyClass {

	public Object result = null;
	public String POSTYPE = null;
	private String BRAND = null;
	
	private String lat = null;
	private String lng = null;
	private double LAT = 0;
	private double LNG = 0;
	
	private String IMEI = null;
	private String DATE = null;
	private String POSNAME = null;
	private String OWNER = null;
	private String MOBILE = null;
	private String ADDRESS = null;
	private String STOCK = null;	
	
	public String getPOSTYPE() {
		return POSTYPE;
	}

	public void setPOSTYPE(String pOSTYPE) {
		POSTYPE = pOSTYPE;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}

	public String getPOSNAME() {
		return POSNAME;
	}

	public void setPOSNAME(String pOSNAME) {
		POSNAME = pOSNAME;
	}

	public String getOWNER() {
		return OWNER;
	}

	public void setOWNER(String oWNER) {
		OWNER = oWNER;
	}

	public String getMOBILE() {
		return MOBILE;
	}

	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}

	public String getBRAND() {
		return BRAND;
	}

	public void setBRAND(String bRAND) {
		BRAND = bRAND;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getSTOCK() {
		return STOCK;
	}

	public void setSTOCK(String sTOCK) {
		STOCK = sTOCK;
	}

	public double getLAT() {
		return LAT;
	}

	public void setLAT(double lAT) {
		LAT = lAT;
	}

	public double getLNG() {
		return LNG;
	}

	public void setLNG(double lNG) {
		LNG = lNG;
	}


	public SoapObject callWebService(String p_URL, String p_NAMESPACE,
			String p_METHOD_NAME, String P_SOAP_ACTION) {
		SoapObject checkcount_t = null;
		try {
			SoapObject request_type = new SoapObject(p_NAMESPACE, p_METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request_type);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(p_URL);
			androidHttpTransport.call(P_SOAP_ACTION, envelope);
			checkcount_t = (SoapObject) envelope.bodyIn;
		} catch (Exception E) {
			E.getMessage();
		}
		return checkcount_t;
	}

	public Object SendWebService(String URL, String NAMESPACE,
			String METHOD_NAME, String SOAP_ACTION) {

		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("POSNAME", POSNAME);
			request.addProperty("OWNER", OWNER);
			request.addProperty("MOBILE", MOBILE);
			request.addProperty("ADDRESS", ADDRESS);

			request.addProperty("IMEI", IMEI);
			request.addProperty("DATE", DATE);

			request.addProperty("BRAND", BRAND);
			request.addProperty("POTYPES", POSTYPE);
			request.addProperty("STOCK", STOCK);

			lat = Double.toString(LAT);
			lng = Double.toString(LNG);

			request.addProperty("LAT", lat);
			request.addProperty("LNG", lng);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			result = envelope.getResponse();
	
		} catch (Exception E) {
			E.printStackTrace();
			System.out.print("ERROR" + E.getClass().getName() + E.getMessage());
			Toast.makeText(null,
					"ERROR" + E.getClass().getName() + ":" + E.getMessage(),
					Toast.LENGTH_LONG);
		}
		return result;

	}

	public String CreateTable(String Table_Name, HashMap hm) {

		StringBuffer sb = new StringBuffer();
		sb.append("create table if not exists " + Table_Name + " (");

		Set s = hm.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			sb.append(" " + me.getKey() + " " + me.getValue() + ",");
		}
		sb.append(")");
		String SQL = sb.toString();
		String R_SQL = SQL.replace(",)", ");");
		return R_SQL;

	}
}