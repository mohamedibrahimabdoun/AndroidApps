package app2.packge;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


public class DataRegisterationActivity extends Activity {
    /** Called when the activity is first created. */	
	private static final String SOAP_ACTION = "";
	private static final String METHOD_NAME = "";
	private static final String NAMESPACE = "";
    private static final String URL ="";
    private static final int CAMERA_PIC_REQUEST=1337;
    ArrayAdapter<String> adapter1;
    Spinner s1;
    EditText firstname,msisdn;
    EditText secondname,thirdname,lastdname;
    
    String DATE,IMSI;
    Button Capture_btn,Save_btn;
    ImageView imageview;
    Bitmap btm_image;
    
   String[] idtype_array= {"Nationality ID","Personal ID","Passport number" ,"Driving licence"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Intent CamIntent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        imageview=(ImageView) findViewById(R.id.idimage);
     try
     {
    	 
        s1 = (Spinner) findViewById(R.id.spinner1);  
        adapter1 = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,idtype_array);
		
		s1.setAdapter(adapter1);
		s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				int msg =parent.getSelectedItemPosition();
				//Toast.makeText(getBaseContext(),"U have choose(" +msg+ ")No. ", Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(getApplicationContext(), "please select one of them ",
						Toast.LENGTH_SHORT).show();
			}
		});
		
		Capture_btn=(Button) findViewById(R.id.capture);
		Save_btn = (Button) findViewById(R.id.save);
		
		firstname = (EditText) findViewById(R.id.fname);
		secondname = (EditText) findViewById(R.id.sname);
		thirdname = (EditText) findViewById(R.id.tname);
		lastdname = (EditText) findViewById(R.id.lname);
		msisdn = (EditText) findViewById(R.id.msisdn);
	
	    // ====================== OnClick CAPTURE Button ========================
	Capture_btn.setOnClickListener(new View.OnClickListener() {

	  
	   	public void onClick(View v) {
	 			// TODO Auto-generated method stub
	  	try{
	   startActivityForResult(CamIntent, CAMERA_PIC_REQUEST);
	       }catch (Exception E) {Toast.makeText(getBaseContext(),"ERROR" + E.getClass().getName() + ":"
					+ E.getMessage(), Toast.LENGTH_LONG).show();}						
	    				}
	    			});  		 
		// ================ OnClick Send Button ========================
	 Save_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
	   // =========== TelePhoney Manager Info ==============					
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		IMSI = telephonyManager.getSubscriberId();
		//telephonyManager.getSimSerialNumber();

		String FNAME= firstname.getText().toString();
		String SNAME= secondname.getText().toString();
		String TNAME= thirdname.getText().toString();
		String LNAME= lastdname.getText().toString();
		String MSISDN=msisdn.getText().toString();
						
		//if (FNAME.length() > 0 && SNAME.length() > 0 && 
		// TNAME.length() > 0 && LNAME.length() > 0 && MSISDN.length() > 0
		//	&& MOBILE.length() > 0){
	try {
	    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
		request.addProperty("FIRSTNAME", FNAME);
		request.addProperty("SECONDNAME", SNAME);
		request.addProperty("THIRDNAME", TNAME);
		request.addProperty("LASTNAME", LNAME);

		request.addProperty("MSISDN", MSISDN);
		request.addProperty("IMSI", IMSI);
		//request.addProperty("ByteImage", btm_image);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		androidHttpTransport.call(SOAP_ACTION, envelope);
		Object result = envelope.getResponse();
		Toast.makeText(getBaseContext(), " " + result,Toast.LENGTH_SHORT).show();
	    } catch (Exception E) {
			E.printStackTrace();
			Toast.makeText(getBaseContext(),"ERROR" + E.getClass().getName() + ":"
				+ E.getMessage(), Toast.LENGTH_LONG).show();
					}
									
		Intent launch=new Intent(getBaseContext(),DataRegisterationActivity.class);
					launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(launch);					

			}
		}); // ====== end OnClick Save ===========

     } catch (Exception e) {
	Toast.makeText(getApplicationContext(),
			e.getMessage(), Toast.LENGTH_SHORT).show();
}
	} // ===> End of OnCreate()
  //  protected void onActivityResult(int requestCode,int resultCode,Intent data){} // startActivityForResult
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode==CAMERA_PIC_REQUEST)
    	{
    		Toast.makeText(getApplicationContext(),
    				" Picture Taken ",+ Toast.LENGTH_SHORT).show();
    		btm_image= (Bitmap) data.getExtras().get("data");	
    		imageview.setImageBitmap(btm_image);
    		
      	}	else {
      		Toast.makeText(getApplicationContext(),
				" Picture hasn't been taken",+ Toast.LENGTH_SHORT).show();}
    }

} // ====> End of Big Class