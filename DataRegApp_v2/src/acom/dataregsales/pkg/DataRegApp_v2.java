package acom.dataregsales.pkg;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.ContentResolver;
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

public class DataRegApp_v2 extends Activity {

	private static final String SOAP_ACTION = "SaveData";
	private static final String METHOD_NAME = "SaveData";
	private static final String NAMESPACE = "http://tempuri.org/";
	// private static final String URL =
	// "http://172.26.1.61:7001/Data_Registration-Data_Reg-context-root/DataRegistrationWebService";
	private static final String URL = "http://172.17.10.25:7001/Data_Registration-Data_Reg-context-root/DataRegistrationWebService";
	// private static final String URL =
	// "http://196.29.166.42:7001/Data_Registration-Data_Reg-context-root/DataRegistrationWebService";
	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final int CAMERA_PIC_REQUEST2 = 1338;

	private PopupWindow mpopup;

	ArrayAdapter<String> adapter1;
	Spinner s1;
	int flag = 0;
	EditText name, msisdn;
	EditText address, serail;
	long IDTYPE;
	String DATE, IMSI;
	Button Capture_Front_btn, Capture_Back_btn, Save_btn, Scan_btn,
			Capture_btn, btnShowPopUp;
	CheckBox agree_cb;
	ImageView image_front, image_back;
	Bitmap btm_imgfront, btm_imgback;
	byte[] myarry1, myarry2;
	String[] idtype_array = { "Nationality ID", "Personal ID",
			"National Number", "Passport Number", "Driving licence",
			"Lawyer ID", "Others" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_data_reg);

		setAutoOrientaionEnable(null, null);
		final Intent CamIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		Capture_btn = (Button) findViewById(R.id.capture);
		Save_btn = (Button) findViewById(R.id.save);
		image_front = (ImageView) findViewById(R.id.imagefront);
		image_back = (ImageView) findViewById(R.id.imageback);
		agree_cb = (CheckBox) findViewById(R.id.checkBox1);

		s1 = (Spinner) findViewById(R.id.spinner1);
		adapter1 = new ArrayAdapter<String>(getBaseContext(),
				android.R.layout.simple_spinner_item, idtype_array);

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

		// Capture_Back_btn = (Button) findViewById(R.id.backbutton);
		Scan_btn = (Button) findViewById(R.id.scan);
		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.address);
		msisdn = (EditText) findViewById(R.id.msisdn);
		serail = (EditText) findViewById(R.id.serail);
		

		// ====================== OnClick CAPTURE Button
		// ========================

		Capture_btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// TODO Auto-generated method stub
				if (flag == 0) {

					try {
						startActivityForResult(CamIntent, CAMERA_PIC_REQUEST);
					} catch (Exception E) {
						Toast.makeText(
								getBaseContext(),
								"pic1 :ERROR" + E.getClass().getName() + ":"
										+ E.getMessage(), Toast.LENGTH_LONG)
								.show();
					}

				} else if (flag == 1) {
					try {
						startActivityForResult(CamIntent, CAMERA_PIC_REQUEST2);
					} catch (Exception E) {
						Toast.makeText(
								getBaseContext(),
								"pic2 :ERROR" + E.getClass().getName() + ":"
										+ E.getMessage(), Toast.LENGTH_LONG)
								.show();
					}
				}

			}
		});

		agree_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
				View popUpView = getLayoutInflater().inflate(R.layout.popup,
						null);
				mpopup = new PopupWindow(popUpView, LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT, true);
				mpopup.setAnimationStyle(android.R.style.Animation_Toast);
				mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0); // Displaying
				Button btnOk = (Button) popUpView.findViewById(R.id.btnOk);
				btnOk.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mpopup.dismiss(); // dismissing the popup
					}
				});
				Button btnCancel = (Button) popUpView
						.findViewById(R.id.btnCancel);
				btnCancel.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent launch = new Intent(getBaseContext(),
								DataRegApp_v2.class);
						launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(launch);
						mpopup.dismiss(); // dismissing the popup
					}
				});
			}

		});
		/*
		 * Save_btn.setOnClickListener(new View.OnClickListener() { public void
		 * onClick(View v) {
		 * 
		 * // =========== TelePhoney Manager Info ==============
		 * TelephonyManager telephonyManager = (TelephonyManager)
		 * getSystemService(TELEPHONY_SERVICE); IMSI =
		 * telephonyManager.getSubscriberId(); //
		 * Toast.makeText(getApplicationContext(), // IMSI,
		 * Toast.LENGTH_SHORT).show(); // telephonyManager.getSimSerialNumber();
		 * 
		 * String NAME = name.getText().toString(); String MSISDN =
		 * msisdn.getText().toString(); String SERAIL =
		 * serail.getText().toString(); String ADDRESS =
		 * address.getText().toString(); String imageName = IMSI + "_" + MSISDN;
		 * String imgPath = "/export/home/weblogic/imgs/" + imageName + ".jpg";
		 * 
		 * if (MSISDN.length() > 0 && NAME.length() > 0 && ADDRESS.length() > 0
		 * && ADDRESS.length() > 0 && MSISDN.length() > 0) { try { SoapObject
		 * request = new SoapObject(NAMESPACE, METHOD_NAME);
		 * SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
		 * SoapEnvelope.VER11); // Newly Added for Serialization new
		 * MarshalBase64().register(envelope); envelope.encodingStyle =
		 * SoapEnvelope.ENC;
		 * 
		 * request.addProperty("MSISDN", MSISDN); request.addProperty("NAME",
		 * NAME); request.addProperty("ADDRESS", ADDRESS);
		 * request.addProperty("SERAIL", SERAIL);
		 * request.addProperty("IMAGENAME", imageName);
		 * request.addProperty("IMAGEPATH", imgPath);
		 * request.addProperty("IDTYPE", IDTYPE); request.addProperty("IMSI",
		 * IMSI); request.addProperty("myarry", myarry1); //
		 * request.addProperty("myarry", myarry2);
		 * 
		 * envelope.setOutputSoapObject(request); HttpTransportSE
		 * androidHttpTransport = new HttpTransportSE( URL);
		 * androidHttpTransport.call(SOAP_ACTION, envelope); Object result =
		 * envelope.getResponse(); Toast.makeText(getBaseContext(), " " +
		 * result, Toast.LENGTH_LONG).show(); } catch (Exception E) {
		 * E.printStackTrace(); Toast.makeText( getBaseContext(),
		 * "Save_btn : ERROR" + E.getClass().getName() + ":" + E.getMessage(),
		 * Toast.LENGTH_LONG).show(); // firstname.setText(E.getMessage()); }
		 * 
		 * Intent launch = new Intent(getBaseContext(), DataRegApp_v2.class);
		 * launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * startActivity(launch);
		 * 
		 * }
		 * 
		 * else { Toast.makeText(getBaseContext(),
		 * "Please Enter all the inforamtion", Toast.LENGTH_LONG).show(); }
		 * 
		 * }
		 * 
		 * }); // ====== end OnClick Save ===========
		 */

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_OK) {

			// Toast.makeText(getApplicationContext(),
			// " Picture hasn't been taken", +Toast.LENGTH_SHORT).show();

			Log.d("DataReg", Integer.toString(requestCode));
		}

		else if (requestCode == CAMERA_PIC_REQUEST) {
			btm_imgfront = (Bitmap) data.getExtras().get("data");
			image_front.setImageBitmap(btm_imgfront);

			MyCustom obj = new MyCustom();

			myarry1 = obj.ConvertToByte(btm_imgfront);

			Toast.makeText(getApplicationContext(), " Picture Taken ",
					+Toast.LENGTH_SHORT).show();
			flag = 1;

		} else if (requestCode == CAMERA_PIC_REQUEST2) {

			btm_imgback = (Bitmap) data.getExtras().get("data");
			image_back.setImageBitmap(btm_imgback);
			MyCustom obj2 = new MyCustom();
			myarry2 = obj2.ConvertToByte(btm_imgback);
			flag = 0;
		}
	}

	public static void setAutoOrientaionEnable(ContentResolver resolver,
			Boolean enabled) {
		Settings.System.putInt(resolver,
				Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
		// Settings.System.putInt(android.provider.Settings.System.putInt(getContentResover()
		// , android.provider.Settings.System.USER_ROTATION, user_rotation));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_data_reg_app_v2, menu);
		return true;
	}
}
