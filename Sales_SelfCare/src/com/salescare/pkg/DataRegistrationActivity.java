package com.salescare.pkg;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


public class DataRegistrationActivity extends Activity {

	private static final String SOAP_ACTION = "SaveData";
	private static final String METHOD_NAME = "SaveData";
	private static final String NAMESPACE = "http://tempuri.org/";
  //private static final String URL = "http://172.17.10.25:7001/Data_Registration-Data_Reg-context-root/DataRegistrationWebService";
  //private static final String URL = "http://172.26.1.61:7001/SalesCare-Sales_CareProject-context-root/DataRegistrationService";
	private static final String URL = "http://172.26.1.61:7001/SalesCare-Sales_CareProject-context-root/DataRegistrationService";
  //"http://196.29.166.42:7001  http://196.29.166.42
	private static final int CAMERA_PIC_REQUEST_Front = 1337;
	private static final int CAMERA_PIC_REQUEST_Back = 1338;
	private PopupWindow agree_popup;
	ArrayAdapter<String> adapter1;
	Spinner s1;
	int flag = 0;
	EditText name, msisdn,address, serail;
	long IDTYPE;
	String DATE, IMSI,ADDRESS,NAME,MSISDN,SERAIL,FrontImageName,BackImageName;
	Button Save_btn, Scan_btn,Capture_btn, btnShowPopUp;
	CheckBox agree_checkbox;
	ImageView FrontImage, BackImage;
	Bitmap btm_FrontImage, btm_BackImage;
	byte[] FrontImage_byt, BackImage_byt;
	String[] idtype_array = { "Nationality ID", "Personal ID",
			"National Number", "Passport Number", "Driving licence",
			"Lawyer ID", "Others" };
	DataRegWebServices Data_Regws_obj;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_registration_layout);
		
	    Data_Regws_obj=new DataRegWebServices();
		final Intent CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	  //Scan_btn = (Button) findViewById(R.id.scan);
		Capture_btn=(Button) findViewById (R.id.capture);
		Save_btn = (Button) findViewById(R.id.save);
		FrontImage = (ImageView) findViewById(R.id.frontimage);
		BackImage = (ImageView) findViewById(R.id.backimage);
		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.address);
		msisdn = (EditText) findViewById(R.id.msisdn);
		serail = (EditText) findViewById(R.id.serail);	
		agree_checkbox = (CheckBox) findViewById(R.id.checkBox1);
		s1 = (Spinner) findViewById(R.id.spinner1);
				
		adapter1 = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item, idtype_array);
		s1.setAdapter(adapter1);
		s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				IDTYPE = (long) parent.getSelectedItemPosition();
			}
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(getApplicationContext(),
						"please select one of them ", Toast.LENGTH_SHORT)
						.show();
			}
		});	

		// ====================== OnClick CAPTURE Button
		Capture_btn.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag == 0) {

					try {
						startActivityForResult(CamIntent, CAMERA_PIC_REQUEST_Front);
					} catch (Exception E) {
						Toast.makeText(
								getBaseContext(),
								"Front Image Error" + E.getClass().getName() + ":"
										+ E.getMessage(), Toast.LENGTH_LONG)
								.show();
					}

				} else if (flag == 1) {
					try {
						startActivityForResult(CamIntent, CAMERA_PIC_REQUEST_Back);
					} catch (Exception E) {
						Toast.makeText(
								getBaseContext(),
								"Back Image Error" + E.getClass().getName() + ":"
										+ E.getMessage(), Toast.LENGTH_LONG)
								.show();
					}
				}

			}
		});

		agree_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
				View popUpView = getLayoutInflater().inflate(R.layout.popup,null);
				agree_popup = new PopupWindow(popUpView, LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, true);
				agree_popup.setAnimationStyle(android.R.style.Animation_Toast);
				agree_popup.showAtLocation(popUpView, Gravity.CENTER, 0, 0); // Displaying
				Button btnOk = (Button) popUpView.findViewById(R.id.btnOk);
				
				btnOk.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						agree_popup.dismiss(); // dismissing the popup
					}
				});
				Button btnCancel = (Button) popUpView.findViewById(R.id.btnCancel);
				btnCancel.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent launch = new Intent(getBaseContext(),
								DataRegistrationActivity.class);
						launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(launch);
						agree_popup.dismiss(); // dismissing the popup
					}
				});
			}

		});

		 Save_btn.setOnClickListener(new View.OnClickListener() 
		 { public void onClick(View v) 
		 {		 
			 
		 // =========== TelePhoney Manager Info ==============
		 TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE); 
		 IMSI = telephonyManager.getSubscriberId();
	    // telephonyManager.getSimSerialNumber();
		 NAME = name.getText().toString(); 
		 MSISDN =msisdn.getText().toString(); 
		 SERAIL =serail.getText().toString(); 
		 ADDRESS = address.getText().toString(); 
		 FrontImageName = IMSI + "_" + MSISDN;
		 BackImageName = IMSI + "_" + MSISDN;		 
	     
		/* if (MSISDN.length() > 0 && NAME.length() > 0 && ADDRESS.length() > 0
		 && SERAIL.length() > 0 && MSISDN.length() > 0) 
		 { */
	   //======================= web services
		 DataRegWebServices obj=new DataRegWebServices();
		 //Data_Regws_obj.setIMSI(IMSI); obj	 
		 obj.setMSISDN(MSISDN);
		 obj.setNAME(NAME);
		 obj.setIMSI(IMSI);
		 obj.setADDRESS(ADDRESS);
		 obj.setSERAIL(SERAIL);
		 obj.setIdType(IDTYPE);
		 obj.setFrontImageName(FrontImageName);
		 obj.setBackImageName(BackImageName);
		 obj.setFrontImage(FrontImage_byt);
		 obj.setBackImage(BackImage_byt);	 
		 
	     Object response_msg;
		try {
			response_msg = obj.SaveData( URL,NAMESPACE,METHOD_NAME,SOAP_ACTION);
			 Toast.makeText(getBaseContext(), "Result"+response_msg,Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(getBaseContext(), "Result"+e.getMessage(),Toast.LENGTH_LONG).show();
		}
		 /*try {
		 SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// Newly Added for Serialization new
			new MarshalBase64().register(envelope);
			envelope.encodingStyle = SoapEnvelope.ENC;

			request.addProperty("MSISDN", MSISDN);
			request.addProperty("NAME", NAME);
			request.addProperty("ADDRESS", ADDRESS);
			request.addProperty("SERAIL", SERAIL);
			request.addProperty("FrontImageName", FrontImageName);
			request.addProperty("BackImageName", BackImageName);
			request.addProperty("idType", IDTYPE);
			request.addProperty("IMSI", IMSI);
			request.addProperty("FrontImage", FrontImage_byt);
			request.addProperty("BackImage", BackImage_byt);

			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			try {
				androidHttpTransport.call(SOAP_ACTION, envelope);
				Object result = envelope.getResponse();
				Toast.makeText(getBaseContext(),
						result.toString(), Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		
		 Intent launch = new Intent(getBaseContext(), DataRegistrationActivity.class);
		 launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(launch);
		 
		/* }else { Toast.makeText(getBaseContext(),
		 "Please Enter all the inforamtion", Toast.LENGTH_LONG).show(); 
		      }*/
		   } 
		 }); // ====== end OnClick Save ===========				 
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_OK) {
			// Toast.makeText(getApplicationContext(),
			// " Picture hasn't been taken", +Toast.LENGTH_SHORT).show();

			Log.d("DataReg", Integer.toString(requestCode));
		}

		else if (requestCode == CAMERA_PIC_REQUEST_Front) {
			btm_FrontImage = (Bitmap) data.getExtras().get("data");
			FrontImage.setImageBitmap(btm_FrontImage);
			MyCustom obj = new MyCustom();
			FrontImage_byt = obj.ConvertToByte(btm_FrontImage);
			Toast.makeText(getApplicationContext(), " Front Image Taken ",
					+Toast.LENGTH_SHORT).show();
			flag = 1;

		} else if (requestCode == CAMERA_PIC_REQUEST_Back) {

			btm_BackImage = (Bitmap) data.getExtras().get("data");
			BackImage.setImageBitmap(btm_BackImage);
			MyCustom obj2 = new MyCustom();
			BackImage_byt = obj2.ConvertToByte(btm_BackImage);
			Toast.makeText(getApplicationContext(), "Back Image Taken ",
					+Toast.LENGTH_SHORT).show();
			flag = 0;
		}
	}

	public static void setAutoOrientaionEnable(ContentResolver resolver,
			Boolean enabled) 
	{
		Settings.System.putInt(resolver,Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
		// Settings.System.putInt(android.provider.Settings.System.putInt(getContentResover()
		// , android.provider.Settings.System.USER_ROTATION, user_rotation));
	}

	
	private void NoInternetAlertMsg() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				DataRegistrationActivity.this);

		builder.setMessage(
				"Your Internet seems to be disabled, do you want to enable it")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								Intent intt = new Intent(
										Settings.ACTION_DATA_ROAMING_SETTINGS);

								ComponentName cn = new ComponentName(
										"com.android.phone",
										"com.android.phone.Settings");
								intt.setComponent(cn);
								startActivity(intt);
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainactivity_mtnsalescare, menu);
		return true;
	}
}
