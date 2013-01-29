package mtn.sales.salescarepkg;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.widget.Toast;

public class PurchaseWebService {

	//public SoapObject response = null;
	private String IMSI = null;
	private String MSGSTR = null;
	public String getIMSI() {
		return IMSI;
	}


	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}


	public String getMSGSTR() {
		return MSGSTR;
	}


	public void setMSGSTR(String mSGSTR) {
		MSGSTR = mSGSTR;
	}

	public SoapObject CallPurchaseElec(String URL, String NAMESPACE,
			String METHOD_NAME, String SOAP_ACTION) {
		SoapObject response = null;
		try {
			// o=new Object();
			
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("msisdn", IMSI);
			request.addProperty("txt", MSGSTR);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			// response = (Object) envelope.bodyIn;
			response = (SoapObject) envelope.bodyIn;
			//response = (SoapObject) envelope.getResponse();
		} catch (Exception E) {
			E.printStackTrace();
			System.out.print("ERROR" + E.getClass().getName() + E.getMessage());
			Toast.makeText(null, "ERROR" + E.getClass().getName()
					+ "here exp frm ws" + E.getMessage(), Toast.LENGTH_LONG);
		}
		return response;
	}
	
	public SoapObject GetPurElecRep(String p_URL, String p_NAMESPACE,
			String p_METHOD_NAME, String P_SOAP_ACTION) {
		SoapObject soap = null;
		try {
			SoapObject request = new SoapObject(p_NAMESPACE, p_METHOD_NAME);
			/*request.addProperty("sender", IMSI);
			request.addProperty("fromdate", DATE);
			request.addProperty("top", TOP);*/
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(p_URL);
			androidHttpTransport.call(P_SOAP_ACTION, envelope);
			soap = (SoapObject) envelope.bodyIn;
		} catch (Exception E) {
			// E.getMessage();
			Toast.makeText(null, "Error" + E.getClass().getName() + "GetRep"
					+ E.getMessage(), Toast.LENGTH_LONG);
		}
		return soap;
	}

}
