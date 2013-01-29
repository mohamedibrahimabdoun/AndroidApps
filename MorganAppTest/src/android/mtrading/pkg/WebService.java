package android.mtrading.pkg;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebService {

	
	public SoapObject GetCategory(String URL, String NAMESPACE,
			String METHOD_NAME, String SOAP_ACTION) {
		SoapObject sop = null;
		try {
			SoapObject request_type = new SoapObject(NAMESPACE, METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request_type);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			sop = (SoapObject) envelope.bodyIn;
		} catch (Exception E) {
			E.getMessage();
		}
		return sop;
	}
	
		
}
