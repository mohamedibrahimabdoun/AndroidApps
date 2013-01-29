package mtn.sales.salescarepkg;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.widget.Toast;

public class VTUWebService {

	public Object response = null;
	private String IMSI = null;
	private String MSGSTR = null;
	private String MSISDN = null;
	private String DATE = null;
	private String TOP = null;

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}

	public String getTOP() {
		return TOP;
	}

	public void setTOP(String tOP) {
		TOP = tOP;
	}

	// Object o;
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

	public String getMSISDN() {
		return MSISDN;
	}

	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}

	public Object CallTransfer(String URL, String NAMESPACE,
			String METHOD_NAME, String SOAP_ACTION) {

		try {
			// o=new Object();
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("sender", IMSI);
			request.addProperty("msgstr", MSGSTR);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			// response = (Object) envelope.bodyIn;
			response = envelope.getResponse();
		} catch (Exception E) {
			E.printStackTrace();
			System.out.print("ERROR" + E.getClass().getName() + E.getMessage());
			Toast.makeText(null, "ERROR" + E.getClass().getName()
					+ "here exp frm ws" + E.getMessage(), Toast.LENGTH_LONG);
		}
		return response;

	}

	public SoapObject CallGetRec(String URL, String NAMESPACE,
			String METHOD_NAME, String SOAP_ACTION) {
		SoapObject checkcount_sp = null;
		try {
			SoapObject request_type = new SoapObject(NAMESPACE, METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			request_type.addProperty("sender", IMSI);

			envelope.setOutputSoapObject(request_type);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			checkcount_sp = (SoapObject) envelope.bodyIn;
			// checkcount_sp = (SoapObject) envelope.toString();
		} catch (Exception E) {
			E.getMessage();
			Toast.makeText(null, "ERROR GetRec" + E.getClass().getName()
					+ "exp GetRec" + E.getMessage(), Toast.LENGTH_LONG);
		}
		return checkcount_sp;
	}

	public SoapObject GetRep(String p_URL, String p_NAMESPACE,
			String p_METHOD_NAME, String P_SOAP_ACTION) {
		SoapObject soap = null;
		try {
			SoapObject request = new SoapObject(p_NAMESPACE, p_METHOD_NAME);
			request.addProperty("sender", IMSI);
			request.addProperty("fromdate", DATE);
			request.addProperty("top", TOP);
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
