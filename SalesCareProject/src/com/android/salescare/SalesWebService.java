package com.android.salescare;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.widget.Toast;

public class SalesWebService {

	/******** get all MSISDNs by passing IMSI ********/
	
	public String L_IMSI;
	public String getL_IMSI() {
		return L_IMSI;
	}



	public void setL_IMSI(String l_IMSI) {
		L_IMSI = l_IMSI;
	}



	public String getL_PIN() {
		return L_PIN;
	}



	public void setL_PIN(String l_PIN) {
		L_PIN = l_PIN;
	}



	public String L_PIN;
	
	
	/********   SIM Activation parameters *********/
	public  Object A_response = null;
	private String A_IMSI = null;
	private String A_SUBNO = null;
	private String A_PUK = null;
	
	/********   Data Registration parameters *********/
	public Object response;
	public String MSISDN;
	public String NAME;
	public String ADDRESS;
	public String SERAIL;
	public String IMSIs;
	public String FrontImageName;
	public String BackImageName;
	public String idType;
	public byte[] FrontImage;
	public byte[] BackImage;
	
	/********   VTU Transfer parameters *********/
	
	public Object V_response = null;
	private String V_IMSI = null;
	private String MSGSTR = null;
	private String V_MSISDN = null;
	private String DATE = null;
	private String TOP = null;
	
	/********   Electricity parameters  *********/
	
	public Object E_response = null;
	private String E_IMSI = null;
	
	
	
	public SoapObject GetMSISDN(String URL, String NAMESPACE,
			String METHOD_NAME, String SOAP_ACTION) {
		SoapObject soap = null;
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("imsi", L_IMSI);
			//request.addProperty("pin", L_PIN);
			//request.addProperty("pin", pin);	
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			soap = (SoapObject) envelope.bodyIn;
		} catch (Exception E) {
			// E.getMessage();
			Toast.makeText(null, "Error" + E.getClass().getName() + "GetMSISDN"
					+ E.getMessage(), Toast.LENGTH_LONG);
		}
		return soap;
	}
}
