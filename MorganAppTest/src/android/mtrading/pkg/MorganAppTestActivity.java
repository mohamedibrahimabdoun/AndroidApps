package android.mtrading.pkg;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MorganAppTestActivity extends Activity {
    /** Called when the activity is first created. */
	
	private static final String SOAP_ACTION = "getCategories";

	private static final String METHOD_NAME = "getCategories"; //46471 order id

	private static final String NAMESPACE = "http://market.askmorgan.net/webservice/lib/"; 

	private static final String URL="http://market.askmorgan.net/webservice/lib/marketservice.php?PHPSOAPCLIENT";
	//private static final String URL="http://market.askmorgan.net/webservice/lib/marketservice.php";
	
	SoapObject sp=null;
	WebService obj;
	TextView tv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        obj = new WebService();
        tv=(TextView) findViewById(R.id.textView1);
        
        try {
        sp = obj.GetCategory(URL, NAMESPACE, METHOD_NAME, SOAP_ACTION);
        tv.setText(""+sp);
    	Toast.makeText(getBaseContext(),
				"Categories" + sp, Toast.LENGTH_LONG)
				.show();
    	
	        } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		Toast.makeText(getBaseContext(),"Internet Connection interrupted "
						+ e.getMessage()
						+ e.getClass().getName(),Toast.LENGTH_LONG).show();
				    }
}
}