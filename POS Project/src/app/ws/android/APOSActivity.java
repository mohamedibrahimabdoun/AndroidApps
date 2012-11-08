package app.ws.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class APOSActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String SOAP_ACTION = "SendPosinfo";

	private static final String METHOD_NAME = "SendPosinfo";

	private static final String METHOD_NAME_bd = "getBrandsFindAll";

	private static final String SOAP_ACTION_bd = "getBrandsFindAll";

	private static final String SOAP_ACTION_pt = "getAllPosTypes";

	private static final String METHOD_NAME_pt = "getAllPosTypes";

	private static final String NAMESPACE = "http://poswebservice/";

	private static final String URL="http://196.29.166.42:7001/POSWSProject-POSWebService-context-root/POSService";

	//private static final String URL = "http://172.17.10.25:7001/POSWSProject-POSWebService-context-root/POSService";

	int counter_t, counter_b;
	SoapObject request_type = null;
	SoapObject request_brand = null;
	SoapObject result_type = null;
	SoapObject result_brand = null;
	SoapObject checkcount_t;
	SoapObject checkcount_b;
	SoapSerializationEnvelope envelope = null;
	static ArrayAdapter<String> adapter1;
	static ArrayAdapter<String> adapter2;
	String[] str_brand;
	String[] str_ptype;
	POSInfoType[] postype;
	POSInfoBrand[] posbrand;

	Button Send;
	EditText posname, owner;
	EditText mobile, address;
	Spinner s1, s2;
	ToggleButton stock_toggle;
	String DATE, IMEI, BRAND, POSTYPE;
	String POSNAME, OWNER, ADDRESS, MOBILE;
	double LAT, LNG;
	String lat, lng;
	String STOCK;
	public PosinfoDataSource Posinfo_ds;
	HashMap Data_hmPosinfo;
	MyClass obj;
	Object Send_result;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		STOCK = "0";
		obj = new MyClass();

		try {

			startService(new Intent(APOSActivity.this, CheckConnService.class));

			// ===================== Check GPS =================
			LocationManager loc_mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			LocationListener loc_lisn = new MyLocationListener();
			loc_mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,loc_lisn);
			// ===================== Check GPS =================
			if(!loc_mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) 
			{
				NoGpsAlertMessage();
			}

			// ===================== Check Internet =================
			ConnectivityManager con_mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo myInfo = con_mgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (con_mgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED) 
			{	
				NoInternetAlertMsg();

			} else {
				// =================creating Offline Tables On SQLite================
				HashMap Structure_hmBrand = new HashMap();
				HashMap Data_hmBrand = new HashMap();

				Structure_hmBrand.put("_id", "integer primary key");
				Structure_hmBrand.put("BRANDDESC", "varchar2");
				String SQlBRANDs = obj.CreateTable("BRAND", Structure_hmBrand);

				HashMap Structure_hmPosType = new HashMap();
				HashMap Data_hmPosType = new HashMap();

				Structure_hmPosType.put("_id", "integer primary key");
				Structure_hmPosType.put("POSTDESC", "varchar2");
				String SQlPOstyeps = obj.CreateTable("POSTYPE",
						Structure_hmPosType);

				HashMap Structure_PosInfo = new HashMap();
				Data_hmPosinfo = new HashMap();

				Structure_PosInfo.put("ID", "integer primary key autoincrement");
				Structure_PosInfo.put("POSNAME", "varchar2");
				Structure_PosInfo.put("POSTYPE", "varchar2");
				Structure_PosInfo.put("OWNER", "varchar2");
				Structure_PosInfo.put("MOBILE", "varchar2");
				Structure_PosInfo.put("ADDRESS", "varchar2");
				Structure_PosInfo.put("BRAND", "varchar2");
				Structure_PosInfo.put("STOCK", "varchar2");
				Structure_PosInfo.put("DATE", "varchar2");
				Structure_PosInfo.put("IMEI", "varchar2");
				Structure_PosInfo.put("LAT", "varchar2");
				Structure_PosInfo.put("LNG", "varchar2");
				String SQlPOSINFO = obj.CreateTable("POSINFO",Structure_PosInfo);

				try {
					String[] Commands = new String[3];
					Commands[0] = SQlBRANDs;
					Commands[1] = SQlPOstyeps;
					Commands[2] = SQlPOSINFO;
					Posinfo_ds = new PosinfoDataSource(this, Commands);
					Posinfo_ds.open();

				} catch (Exception e) {
					// e.printStackTrace();
					Toast.makeText(
							getBaseContext(),
							e.getMessage() + "Error o creating Offline Save"
									+ e.getClass().getName(), Toast.LENGTH_LONG)
							.show();
				} // =================End Of Creating Tables On SQLite================

				// ===================== call Web service To Populate POSTYPES
				Cursor c_BRAND = Posinfo_ds.getAllPosinfo("BRAND");
				Cursor c_POSTYPE = Posinfo_ds.getAllPosinfo("POSTYPE");
				if (c_BRAND.getCount() > 0 && c_POSTYPE.getCount() > 0) {

					startManagingCursor(c_POSTYPE);
					String[] postype_col = new String[] { "POSTDESC" };
					int[] postype_to = new int[] { android.R.id.text1 };

					SimpleCursorAdapter sca_postype = new SimpleCursorAdapter(
							this, android.R.layout.simple_spinner_item,
							c_POSTYPE, postype_col, postype_to);
					sca_postype
							.setDropDownViewResource(android.R.layout.simple_spinner_item);
					s1 = (Spinner) this.findViewById(R.id.spinner1);
					s1.setAdapter(sca_postype);

					// =====================Fill the BRAND Adapter======================
					startManagingCursor(c_BRAND);
					String[] brand_col = new String[] { "BRANDDESC" };
					int[] brand_to = new int[] { android.R.id.text1 };

					SimpleCursorAdapter sca_brand = new SimpleCursorAdapter(
							this, android.R.layout.simple_spinner_item,
							c_BRAND, brand_col, brand_to);
					sca_brand
							.setDropDownViewResource(android.R.layout.simple_spinner_item);
					s2 = (Spinner) this.findViewById(R.id.spinner2);
					s2.setAdapter(sca_brand);

				} else {
					// ======start=========Populate SQLite from Web service in case POSTYPE & BRAND are Empty======
					try {

						checkcount_t = obj.callWebService(URL, NAMESPACE,
								METHOD_NAME_pt, SOAP_ACTION_pt);
						counter_t = checkcount_t.getPropertyCount();
						str_ptype = new String[counter_t];
						postype = new POSInfoType[checkcount_t
								.getPropertyCount()];
						Cursor cpostype = Posinfo_ds.getAllPosinfo("POSTYPE");

						// ===================== call Web service To Populate BRAND Spinners

						checkcount_b = obj.callWebService(URL, NAMESPACE,
								METHOD_NAME_bd, SOAP_ACTION_bd);
						counter_b = checkcount_b.getPropertyCount();
						str_brand = new String[counter_b];
						posbrand = new POSInfoBrand[checkcount_b
								.getPropertyCount()];
						Cursor cbrand = Posinfo_ds.getAllPosinfo("BRAND");

						// ===================== call Web service To Populate POSTYPE Spinners
						for (int i = 0; i < postype.length; i++) {
							SoapObject pii = (SoapObject) checkcount_t
									.getProperty(i);
							POSInfoType ptype = new POSInfoType();
							ptype.id = Integer.parseInt(pii.getProperty(0)
									.toString());
							ptype.typedesc = pii.getProperty(1).toString();
							postype[i] = ptype;
							str_ptype[i] = ptype.typedesc;

					   // =========== Create The Off line table of SQLite
							Data_hmPosType.put("_id", ptype.id);
							Data_hmPosType.put("POSTDESC", ptype.typedesc);
							String msg = Posinfo_ds.InsertRow("POSTYPE",
									Data_hmPosType);

						}
						// ===================== call Web service To Populate BRAND Spinners
						for (int i = 0; i < posbrand.length; i++) {
							SoapObject pii = (SoapObject) checkcount_b
									.getProperty(i);
							POSInfoBrand brand = new POSInfoBrand();
							brand.branddesc = pii.getProperty(0).toString();
							brand.id = Integer.parseInt(pii.getProperty(1)
									.toString());
							posbrand[i] = brand;
							str_brand[i] = brand.branddesc;

							// =================== Populate the offline table of SQLite
							Data_hmBrand.put("_id", brand.id);
							Data_hmBrand.put("BRANDDESC", brand.branddesc);
							String showmsg = Posinfo_ds.InsertRow("BRAND",
									Data_hmBrand);

						}

						Log.d("cbrand", Integer.toString(cbrand.getCount()));
						Log.d("cpostypes",Integer.toString(cpostype.getCount()));
						Log.d("android.R.id.text1",Integer.toString(android.R.id.text1));

						// ===================== Fill the POSTYPE Adapter

						startManagingCursor(cpostype);
						String[] postype_col = new String[] { "POSTDESC" };
						int[] postype_to = new int[] { android.R.id.text1 };

						SimpleCursorAdapter sca_postype = new SimpleCursorAdapter(
								this, android.R.layout.simple_spinner_item,
								cpostype, postype_col, postype_to);
						sca_postype
								.setDropDownViewResource(android.R.layout.simple_spinner_item);
						s1 = (Spinner) this.findViewById(R.id.spinner1);
						s1.setAdapter(sca_postype);

						// =====================Fill the BRAND Adapter
						startManagingCursor(cbrand);
						String[] brand_col = new String[] { "BRANDDESC" };
						int[] brand_to = new int[] { android.R.id.text1 };

						SimpleCursorAdapter sca_brand = new SimpleCursorAdapter(
								this, android.R.layout.simple_spinner_item,
								cbrand, brand_col, brand_to);
						sca_brand
								.setDropDownViewResource(android.R.layout.simple_spinner_item);
						s2 = (Spinner) this.findViewById(R.id.spinner2);
						s2.setAdapter(sca_brand);

					} catch (Exception e) {

						Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
					}
					// ======End=========Populate SQLite from Web service incase
					// POSTYPE & BRAND are Empty======
				}
				s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						POSTYPE = Long.toString(parent.getSelectedItemId());
						//Toast.makeText(getBaseContext(), "s1 :" + POSTYPE,Toast.LENGTH_SHORT).show();
					}

					public void onNothingSelected(AdapterView<?> parent) {
						//Toast.makeText(getBaseContext(),"please select one of them ",Toast.LENGTH_SHORT).show();
					}
				});

				s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						BRAND = Long.toString(parent.getSelectedItemPosition());
						//Toast.makeText(getBaseContext(), "s2 :" + BRAND,Toast.LENGTH_SHORT).show();

					}

					public void onNothingSelected(AdapterView<?> parent) {
						//Toast.makeText(getBaseContext(),"please select one of them ",Toast.LENGTH_SHORT).show();
					}
				});

				// =================== Toggle Button for STOCK ===================
				stock_toggle = (ToggleButton) findViewById(R.id.toggle);
				stock_toggle.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (((ToggleButton) v).isChecked()) {
							STOCK = "1";
						} else
							STOCK = "0";
					}
				});

				// =================================================================

				Send = (Button) findViewById(R.id.send);
				posname = (EditText) findViewById(R.id.posname);
				owner = (EditText) findViewById(R.id.contact);
				mobile = (EditText) findViewById(R.id.mobile);
				address = (EditText) findViewById(R.id.address);

				// ====================== OnClick Send Button ========================

				Send.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						// ======================= DATE ===============================

						try {
							SimpleDateFormat s = new SimpleDateFormat("yyyyMMddhhmmss");
							DATE = s.format(new Date());

						// ======================= IMEI ===============================
							TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
							IMEI = telephonyManager.getDeviceId();

							POSNAME = posname.getText().toString();
							OWNER = owner.getText().toString();
							MOBILE = mobile.getText().toString();
							ADDRESS = address.getText().toString();
					
						//============= Setting data to save in SQLite ==============
							
							if (LAT != 0.0 && LNG != 0.0) {
								Data_hmPosinfo.put("POSTYPE", POSTYPE);
								Data_hmPosinfo.put("POSNAME", POSNAME);
								Data_hmPosinfo.put("OWNER", OWNER);
								Data_hmPosinfo.put("MOBILE", MOBILE);
								Data_hmPosinfo.put("ADDRESS", ADDRESS);
								Data_hmPosinfo.put("BRAND", BRAND);
								Data_hmPosinfo.put("STOCK", STOCK);
								Data_hmPosinfo.put("DATE", DATE);
								Data_hmPosinfo.put("IMEI", IMEI);
								Data_hmPosinfo.put("LAT", LAT);
								Data_hmPosinfo.put("LNG", LNG);

								String msg = Posinfo_ds.InsertRow("POSINFO",Data_hmPosinfo);
								Toast.makeText(getBaseContext(),"Data Saved Locally to send later",
										Toast.LENGTH_LONG).show();
								// ===========Intent to open New window  ============
								Intent launch = new Intent(getBaseContext(),APOSActivity.class);
								launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(launch);

							} else {
								// ==========================
								NoGpsCordAlertDailog();
							}

							// ================================================
						} catch (Exception e) 
						{
							Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
						}

					}// ===============
				}); // ====== end of OnClick ===========================
			}
		} catch (Exception e) 
		{
			// ======= Fill Spinner from SQLite Table =========
			Toast.makeText(getApplicationContext(),e.getMessage()+"Internet Connection interrupted",Toast.LENGTH_LONG).show();
		}
		
	} // ===> End of OnCreate()
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("onResume", "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		onDestroy();
		Log.i("onPause", "onPause");
	}

	@Override
	protected void onDestroy() {
		// Posinfo_ds.close();
		super.onDestroy();
		finish();
	}

	private void NoGpsAlertMessage() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				APOSActivity.this);
		builder.setMessage(
				"Your GPS seems to be disabled, Do you want to enable it")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {

								startActivity(new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}

	private void NoInternetAlertMsg() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				APOSActivity.this);

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

	private void NoGpsCordAlertDailog() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				APOSActivity.this);
		builder.setMessage(
				"Data has not been saved,The System can't found the GPS Cordinates!")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								// ================ ==============
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}

	public class MyLocationListener implements LocationListener

	{
		public void onLocationChanged(Location loc) {
			try {
				LAT = loc.getLatitude();
				LNG = loc.getLongitude();

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
			}

		}

		public void onProviderDisabled(String provider) {

			Toast.makeText(getApplicationContext(), "Gps Disabled",
					Toast.LENGTH_SHORT).show();
		}

		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Enabled",
					Toast.LENGTH_SHORT).show();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	} // =====> End of Class MyLocationListener

} // ===> End of Activity Class
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       