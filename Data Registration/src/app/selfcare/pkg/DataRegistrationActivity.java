package app.selfcare.pkg;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class DataRegistrationActivity extends Activity {
	/** Called when the activity is first created. */
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

	ArrayAdapter<String> adapter1;
	Spinner s1;
	EditText firstname, msisdn;
	EditText secondname, thirdname, lastdname;
	long IDTYPE;
	String DATE, IMSI;
	Button Capture_btn, Save_btn, SaveOffline;
	ImageView imageview, imageview2;
	Bitmap btm_image, btm_image2;
	byte[] myarry, myarry2;
	String[] idtype_array = { "Nationality ID", "Personal ID",
			"Passport number", "Driving licence" };

	private ImageView mMain;
	private ImageDbAdapter mDbAdapter;
	int flag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		flag = 0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final Intent CamIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		imageview = (ImageView) findViewById(R.id.idimage);
		imageview2 = (ImageView) findViewById(R.id.idimage2);

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		try {

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

			Capture_btn = (Button) findViewById(R.id.capture);
			SaveOffline = (Button) findViewById(R.id.saveOffline);
			Save_btn = (Button) findViewById(R.id.save);

			firstname = (EditText) findViewById(R.id.fname);
			secondname = (EditText) findViewById(R.id.sname);
			thirdname = (EditText) findViewById(R.id.tname);
			lastdname = (EditText) findViewById(R.id.lname);
			msisdn = (EditText) findViewById(R.id.msisdn);

			// ====================== OnClick CAPTURE Button
			// ========================
			Capture_btn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					// TODO Auto-generated method stub
					if (flag == 0) {

						try {
							startActivityForResult(CamIntent,
									CAMERA_PIC_REQUEST);
						} catch (Exception E) {
							Toast.makeText(
									getBaseContext(),
									"pic1 :ERROR" + E.getClass().getName()
											+ ":" + E.getMessage(),
									Toast.LENGTH_LONG).show();
						}

					} else if (flag == 1) {
						try {
							startActivityForResult(CamIntent,
									CAMERA_PIC_REQUEST2);
						} catch (Exception E) {
							Toast.makeText(
									getBaseContext(),
									"pic2 :ERROR" + E.getClass().getName()
											+ ":" + E.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					}

				}
			});
			// ================ OnClick Send Button ========================
			Save_btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					// =========== TelePhoney Manager Info ==============
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					IMSI = telephonyManager.getSubscriberId();
					// Toast.makeText(getApplicationContext(),
					// IMSI, Toast.LENGTH_SHORT).show();
					// telephonyManager.getSimSerialNumber();

					String FIRSTNAME = firstname.getText().toString();
					String SECONDNAME = secondname.getText().toString();
					String THIRDNAME = thirdname.getText().toString();
					String LASTNAME = lastdname.getText().toString();
					String MSISDN = msisdn.getText().toString();
					String imageName = IMSI + "_" + MSISDN;
					String imgPath = "/export/home/weblogic/imgs/" + imageName
							+ ".jpg";
					String vname = FIRSTNAME + " " + SECONDNAME + " "
							+ THIRDNAME + " " + LASTNAME;
					if (FIRSTNAME.length() > 0 && SECONDNAME.length() > 0
							&& THIRDNAME.length() > 0 && LASTNAME.length() > 0
							&& MSISDN.length() > 0) {
						try {
							SoapObject request = new SoapObject(NAMESPACE,
									METHOD_NAME);
							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
									SoapEnvelope.VER11);
							// Newly Added for Serialization
							new MarshalBase64().register(envelope);
							envelope.encodingStyle = SoapEnvelope.ENC;

							request.addProperty("FISRTNAME", FIRSTNAME);
							request.addProperty("SECONDNAME", SECONDNAME);
							request.addProperty("THIRDNAME", THIRDNAME);
							request.addProperty("LASTNAME", LASTNAME);
							request.addProperty("NAME", vname);
							request.addProperty("IMAGENAME", imageName);
							request.addProperty("IMAGEPATH", imgPath);
							request.addProperty("IDTYPE", IDTYPE);
							request.addProperty("MSISDN", MSISDN);
							request.addProperty("IMSI", IMSI);
							request.addProperty("myarry", myarry);

							envelope.setOutputSoapObject(request);
							HttpTransportSE androidHttpTransport = new HttpTransportSE(
									URL);
							androidHttpTransport.call(SOAP_ACTION, envelope);
							Object result = envelope.getResponse();

							Toast.makeText(getBaseContext(), " " + "Result",
									Toast.LENGTH_LONG).show();
						} catch (Exception E) {
							E.printStackTrace();
							Toast.makeText(
									getBaseContext(),
									"Save_btn : ERROR" + E.getClass().getName()
											+ ":" + E.getMessage(),
									Toast.LENGTH_LONG).show();
							firstname.setText(E.getMessage());
						}

						Intent launch = new Intent(getBaseContext(),
								DataRegistrationActivity.class);
						launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(launch);

					}

					else {
						Toast.makeText(getBaseContext(),
								"Please Enter all the inforamtion",
								Toast.LENGTH_LONG).show();
					}

				}

			}); // ====== end OnClick Save ===========

			SaveOffline.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					mDbAdapter.open();
					long l = mDbAdapter.insert(myarry);

					Toast.makeText(getApplicationContext(),
							" Insert Result  is :" + l, +Toast.LENGTH_SHORT)
							.show();

				}
			});
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	} // ===> End of OnCreate()

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_CANCELED) {

			Toast.makeText(getApplicationContext(),
					" Picture hasn't been taken", +Toast.LENGTH_SHORT).show();
		}

		else if (requestCode == CAMERA_PIC_REQUEST) {
			btm_image = (Bitmap) data.getExtras().get("data");
			imageview.setImageBitmap(btm_image);

			MyCustom obj = new MyCustom();

			myarry = obj.ConvertToByte(btm_image);

			Toast.makeText(getApplicationContext(), " Picture Taken ",
					+Toast.LENGTH_SHORT).show();
			flag = 1;

		} else if (requestCode == CAMERA_PIC_REQUEST2) {

			btm_image2 = (Bitmap) data.getExtras().get("data");
			imageview2.setImageBitmap(btm_image2);
			MyCustom obj2 = new MyCustom();
			myarry2 = obj2.ConvertToByte(btm_image2);
			flag = 0;
		}
		/*
		 * else if (requestCode == RESULT_CANCELED) { //if(requestCode ==
		 * RESULT_OK) {
		 * 
		 * Toast.makeText(getApplicationContext(), " Picture hasn't been taken",
		 * +Toast.LENGTH_SHORT).show(); //}
		 * 
		 * //Toast.makeText(getApplicationContext(), " Other Error ",
		 * //+Toast.LENGTH_SHORT).show(); }
		 * 
		 * else { Toast.makeText(getApplicationContext(), " Other Error ",
		 * +Toast.LENGTH_SHORT).show(); }
		 */

	}

	public static void setAutoOrientaionEnable(ContentResolver resolver,
			Boolean enabled) {
		Settings.System.putInt(resolver,
				Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
		// Settings.System.putInt(android.provider.Settings.System.putInt(getContentResover()
		// , android.provider.Settings.System.USER_ROTATION, user_rotation));
	}
} // ====> End of Big Class