package android.salescare.pkg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
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

	private static final String METHOD_NAME_IdTypes = "getTypesFindAll";

	private static final String SOAP_ACTION_IdTypes = "getTypesFindAll";

	private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL="http://172.17.10.25:7001/Data_Registration-Data_Reg-context-root/DataRegistrationWebService";
	//private static final String URL = "http://172.26.1.61:7001/SalesCare-Sales_CareProject-context-root/DataRegistrationService";
   //private static final String URL = "http://196.29.166.42:7001/SalesCare-Sales_CareProject-context-root/DataRegistrationService";

	private static final int CAMERA_PIC_REQUEST_Front = 1337;
	private static final int CAMERA_PIC_REQUEST_Back = 1338;
	private PopupWindow agree_popup;
	ArrayAdapter<String> adapter1;
	Spinner s1;
	int flag = 0;
	int counter;
	EditText name, msisdn, address, serail;
	long IDTYPE;
	String[] IDTypes_str;
	IDTypes[] IDTypes_arr;
	HashMap Reg_Data;
	SoapObject checkcount;
	String DATE, IMSI, ADDRESS, NAME, MSISDN, SERAIL, FrontImageName, IdType,
			BackImageName;
	Button Save_btn, Scan_btn, Capture_btn, btnShowPopUp;
	CheckBox agree_checkbox;
	ImageView FrontImage, BackImage;
	Bitmap btm_FrontImage, btm_BackImage;
	byte[] FrontImage_byt, BackImage_byt;
	String[] idtype_array = { "Nationality ID", "Personal ID",
			"National Number", "Passport Number", "Driving licence",
			"Lawyer ID", "Others" };
	ConnectivityManager con_mgr;
	TextView name_tv, msisdn_tv, serail_tv, idtype_tv, address_tv;
	ImageView f_imgview, b_imgview;
	Intent NewIntent;
	Intent ScanIntent;
	String contents;
	ImageDbAdapter imgAdapter;
	NetworkInfo myInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_registration_layout);

		DataRegWebServices obj = new DataRegWebServices();
		final Intent CamIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		NewIntent = new Intent(getBaseContext(), DataRegistrationActivity.class);
		NewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(NewIntent);
		Scan_btn = (Button) findViewById(R.id.scan);
		Capture_btn = (Button) findViewById(R.id.capture);
		Save_btn = (Button) findViewById(R.id.save);
		FrontImage = (ImageView) findViewById(R.id.frontimage);
		BackImage = (ImageView) findViewById(R.id.backimage);
		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.address);
		msisdn = (EditText) findViewById(R.id.msisdn);
		serail = (EditText) findViewById(R.id.serail);
		s1 = (Spinner) findViewById(R.id.spinner1);
		try {
			// startService(new Intent(APOSActivity.this,
			// CheckConnService.class));
			startService(new Intent(DataRegistrationActivity.this,
					CheckingConnService.class));

			// ===================== Check Internet =================
			con_mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		     myInfo = con_mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (con_mgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED) {
				Intent intt = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);

				ComponentName cn = new ComponentName("com.android.phone",
						"com.android.phone.Settings");
				intt.setComponent(cn);

				ShowDailog(
						"Internet seems to be disabled,do you want to enable it?",
						intt);

			} else {

				// ================= Creating SQLite Tables
				HashMap Types_Structure = new HashMap();
				HashMap Types_Data = new HashMap();

				Types_Structure.put("_id", "integer primary key");
				Types_Structure.put("TYPE_NAME", "varchar2");
				Types_Structure.put("ID_SIDES", "integer");
				String TYPES_SQL = obj.CreateTable("TYPES", Types_Structure);

				// ArrayList<HashMap <String ,Object>>
				// Registration_Structure=new ArrayList<HashMap <String
				// ,Object>>();
				// ArrayList<HashMap <String ,Object>> Reg_Data=new
				// ArrayList<HashMap <String ,Object>>();
				HashMap Registration_Structure = new HashMap();
				Reg_Data = new HashMap();
				// Registration_Structure.add(object)
				Registration_Structure.put("ID",
						"integer primary key autoincrement");
				Registration_Structure.put("DATE", "varchar2");
				Registration_Structure.put("NAME", "varchar2");
				Registration_Structure.put("MSISDN", "varchar2");
				Registration_Structure.put("SERAIL", "varchar2");
				Registration_Structure.put("ADDRESS", "varchar2");
				Registration_Structure.put("IDTYPES", "varchar2");
				Registration_Structure.put("IMSI", "varchar2");
				Registration_Structure.put("FRONTIMAGE_NAME", "varchar2");
				Registration_Structure.put("BACKIMAGE_NAME", "varchar2");
				Registration_Structure.put("FRONT_IMAGE", "blob");
				Registration_Structure.put("BACK_IMAGE", "blob");
				String OLDREGISTRATION_SQL = obj.CreateTable(
						"OLD_REGISTRATION", Registration_Structure);

				try {
					String[] Commands = new String[2];
					Commands[0] = TYPES_SQL;
					Commands[1] = OLDREGISTRATION_SQL;

					imgAdapter = new ImageDbAdapter(this, Commands);
					imgAdapter.open();

				} catch (Exception e) {
					// e.printStackTrace();
					Toast.makeText(
							getBaseContext(),
							e.getMessage()
									+ "Error on creating Offline DataBase"
									+ e.getClass().getName(), Toast.LENGTH_LONG)
							.show();
				} // =================End Of Creating Tables On SQLite

				// ================= call Web service To Populate IDTYPES
				// Spinner

				Cursor Types_Cur = imgAdapter.getAllData("TYPES");
				if (Types_Cur.getCount() > 0) {
					startManagingCursor(Types_Cur);
					String[] Types_col = new String[] { "TYPE_NAME" };
					int[] Types_to = new int[] { android.R.id.text1 };
					SimpleCursorAdapter sca_Types = new SimpleCursorAdapter(
							this, android.R.layout.simple_spinner_item,
							Types_Cur, Types_col, Types_to);
					sca_Types
							.setDropDownViewResource(android.R.layout.simple_spinner_item);
					s1 = (Spinner) this.findViewById(R.id.spinner1);
					s1.setAdapter(sca_Types);
				} else {
					// ======start=========Populate SQLite from Web service in
					// case POSTYPE & BRAND are Empty======
					try {
						checkcount = obj.GetIDTypes(URL, NAMESPACE,
								METHOD_NAME_IdTypes, SOAP_ACTION_IdTypes);
						counter = checkcount.getPropertyCount();
						IDTypes_str = new String[counter];
						IDTypes_arr = new IDTypes[checkcount.getPropertyCount()];
						Cursor curtypes_temp = imgAdapter.getAllData("TYPES");

						// ===================== call Web service To Populate
						// POSTYPE Spinners
						for (int i = 0; i < IDTypes_arr.length; i++) {
							SoapObject pii = (SoapObject) checkcount
									.getProperty(i);
							IDTypes idtypes = new IDTypes();
							idtypes.Id = Integer.parseInt(pii.getProperty(1)
									.toString());
							idtypes.TYPEName = pii.getProperty(2).toString();
							IDTypes_arr[i] = idtypes;
							IDTypes_str[i] = idtypes.TYPEName;

							// =========== Create The Off line table of SQLite
							Reg_Data.put("_id", idtypes.Id);
							Reg_Data.put("TYPE_NAME", idtypes.TYPEName);
							String msg = imgAdapter
									.InsertRow("TYPES", Reg_Data);
						}
						// ===================== Fill the POSTYPE Adapter
						startManagingCursor(curtypes_temp);
						String[] Idtypes_col = new String[] { "TYPE_NAME" };
						int[] Idtypes_to = new int[] { android.R.id.text1 };
						SimpleCursorAdapter sca = new SimpleCursorAdapter(this,
								android.R.layout.simple_spinner_item,
								curtypes_temp, Idtypes_col, Idtypes_to);
						sca.setDropDownViewResource(android.R.layout.simple_spinner_item);
						s1 = (Spinner) this.findViewById(R.id.spinner1);
						s1.setAdapter(sca);

					} catch (Exception e) {

						Toast.makeText(getApplicationContext(), e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				}// ==== insert error
					// adapter1 = new
					// ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,
					// idtype_array);
				// s1.setAdapter(adapter1);

				s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						IdType = Long.toString(parent.getSelectedItemId());
						// Object msg=
						// parent.getItemAtPosition(Integer.parseInt(IdType));
						// Double.parseDouble(LAT)
						// BRAND =
						// Long.toString(parent.getSelectedItemPosition());
						// Toast.makeText(getApplicationContext(),IdType+"",Toast.LENGTH_SHORT).show();
					}

					public void onNothingSelected(AdapterView<?> parent) {
						Toast.makeText(getApplicationContext(),
								"please select one of them ",
								Toast.LENGTH_SHORT).show();
					}
				});

				// ====================== OnClick CAPTURE Button
				Capture_btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (flag == 0) {

							try {
								startActivityForResult(CamIntent,
										CAMERA_PIC_REQUEST_Front);
							} catch (Exception E) {
								Toast.makeText(
										getBaseContext(),
										"Front Image Error"
												+ E.getClass().getName() + ":"
												+ E.getMessage(),
										Toast.LENGTH_LONG).show();
							}

						} else if (flag == 1) {
							try {
								startActivityForResult(CamIntent,
										CAMERA_PIC_REQUEST_Back);
							} catch (Exception E) {
								Toast.makeText(
										getBaseContext(),
										"Back Image Error"
												+ E.getClass().getName() + ":"
												+ E.getMessage(),
										Toast.LENGTH_LONG).show();
							}
						}
					}
				});
				agree_checkbox = (CheckBox) findViewById(R.id.checkBox);

				/*
				 * msisdn_tv = (TextView) findViewById(R.id.msisdntv); serail_tv
				 * = (TextView) findViewById(R.id.serailtv); idtype_tv =
				 * (TextView) findViewById(R.id.idtypestv); address_tv =
				 * (TextView) findViewById(R.id.addresstv); f_imgview =
				 * (ImageView) findViewById(R.id.fimage); b_imgview =
				 * (ImageView) findViewById(R.id.bimage);
				 */

				agree_checkbox
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub

								View popUpView = getLayoutInflater().inflate(
										R.layout.popup, null);

								agree_popup = new PopupWindow(popUpView,
										LayoutParams.FILL_PARENT,
										LayoutParams.WRAP_CONTENT, true);

								agree_popup
										.setAnimationStyle(android.R.style.Animation_Toast);
								agree_popup.showAtLocation(popUpView,
										Gravity.CENTER, 0, 0); // Displaying
								// agree_popup.isClippingEnabled();
								Button btnOk = (Button) popUpView
										.findViewById(R.id.btnOk);
								((TextView) agree_popup.getContentView()
										.findViewById(R.id.nametv))
										.setText(name.getText().toString());
								((TextView) agree_popup.getContentView()
										.findViewById(R.id.msisdntv))
										.setText(msisdn.getText().toString());
								((TextView) agree_popup.getContentView()
										.findViewById(R.id.serailtv))
										.setText(serail.getText().toString());
								/*
								 * ((TextView) agree_popup.getContentView()
								 * .findViewById(R.id.idtypestv))
								 * .setText(Long.toString(IDTYPE));
								 */
								((TextView) agree_popup.getContentView()
										.findViewById(R.id.addresstv))
										.setText(address.getText().toString());

								((ImageView) agree_popup.getContentView()
										.findViewById(R.id.fimage))
										.setImageBitmap(btm_FrontImage);

								((ImageView) agree_popup.getContentView()
										.findViewById(R.id.bimage))
										.setImageBitmap(btm_BackImage);

								btnOk.setOnClickListener(new OnClickListener() {
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										agree_popup.dismiss(); // dismissing
																// the popup
										/*
										 * Toast.makeText(getBaseContext(),
										 * " Check Error:",
										 * Toast.LENGTH_LONG).show();
										 */
									}
								});
								Button btnCancel = (Button) popUpView
										.findViewById(R.id.btnCancel);
								btnCancel
										.setOnClickListener(new OnClickListener() {
											public void onClick(View arg0) {
												// TODO Auto-generated
												// method stub
												Intent launch = new Intent(
														getBaseContext(),
														DataRegistrationActivity.class);
												launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivity(launch);
												agree_popup.dismiss(); // dismissing
																		// the
																		// popup
											}
										});

								/*
								 * if (agree_checkbox.isChecked()=false) {
								 * ShowDailog
								 * ("Sorry Please Agree All terms first",null);
								 * /*final AlertDialog.Builder builder = new
								 * AlertDialog.Builder(
								 * DataRegistrationActivity.this);
								 * 
								 * builder.setMessage(msg) .setCancelable(false)
								 * .setPositiveButton("Yes", new
								 * DialogInterface.OnClickListener() { public
								 * void onClick(final DialogInterface dialog,
								 * final int id) { if (intent != null)
								 * startActivity(intent);
								 * 
								 * } });
								 * 
								 * final AlertDialog alert = builder.create();
								 * alert.show(); }
								 */

							}
						});

				try {
					// Button scanner = (Button) findViewById(R.id.scanner);
					Scan_btn.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent ScanIntent = new Intent(
									"com.google.zxing.client.android.SCAN");
							ScanIntent.putExtra("SCAN_MODE", "ONE_D_MODE");
							startActivityForResult(ScanIntent, 0);
						}
					});

				} catch (ActivityNotFoundException anfe) {
					Log.e("onCreate", "Scanner Not Found", anfe);

				}

				Save_btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						SimpleDateFormat s = new SimpleDateFormat(
								"yyyyMMddhhmmss");
						DATE = s.format(new Date());

						// =========== TelePhoney Manager Info
						// ==============
						TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
						IMSI = telephonyManager.getSubscriberId();
						// telephonyManager.getSimSerialNumber();
						NAME = name.getText().toString();
						MSISDN = msisdn.getText().toString();
						SERAIL = serail.getText().toString();
						// contents= serail.getText().toString();

						serail.setText(contents);
						ADDRESS = address.getText().toString();
						FrontImageName = IMSI + "_" + MSISDN;
						BackImageName = IMSI + "_" + MSISDN;

						/*
						 * if (MSISDN.length() > 0 && NAME.length() > 0 &&
						 * ADDRESS.length() > 0 &&
						 * serail.getText().toString().length() > 0 &&
						 * MSISDN.length() > 0) {
						 */
						/*
						 * Toast.makeText(getBaseContext(), "serail " + SERAIL,
						 * Toast.LENGTH_LONG) .show();
						 */
						// ======================= web services
						/*
						 * Object response_msg; NewIntent = new
						 * Intent(getBaseContext(),
						 * DataRegistrationActivity.class); NewIntent.addFlags
						 * (Intent.FLAG_ACTIVITY_CLEAR_TOP);
						 */
						// ConnectivityManager con_mgr =
						// (ConnectivityManager)
						// getSystemService(CONNECTIVITY_SERVICE);
						NetworkInfo myInfo = con_mgr
								.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
						// if (con_mgr.getNetworkInfo(0).getState() ==
						// NetworkInfo.State.CONNECTED) {
						// *------------------ */

						if (!agree_checkbox.isChecked()) {
							ShowDailog("Sorry, Please Agree All terms first",
									null);
						} else {
							try {
								/*
								 * MyCustom cus = new MyCustom(); FrontImage_byt
								 * = cus .ConvertToByte(btm_FrontImage);
								 */

								/*
								 * byte[] image =
								 * Utilities.getBytes(BitmapFactory
								 * .decodeResource( getResources(),
								 * R.drawable.sumire02));
								 */
								imgAdapter.insert(DATE, NAME, SERAIL, MSISDN,
										IMSI, ADDRESS, IdType, FrontImageName,
										BackImageName, FrontImage_byt,
										BackImage_byt);

								Toast.makeText(getBaseContext(),
										"Data Saved to send later",
										Toast.LENGTH_LONG).show();
								/*Toast.makeText(
										getBaseContext(),
										"" + FrontImage_byt + ""
												+ BackImage_byt,
										Toast.LENGTH_SHORT).show();*/

								/*
								 * DataRegWebServices reg = new
								 * DataRegWebServices(); reg.setNAME(NAME);
								 * reg.setMSISDN(MSISDN);
								 * reg.setADDRESS(ADDRESS);
								 * reg.setSERAIL(SERAIL); reg.setIMSI(IMSI);
								 * reg.setIdType(IdType);
								 * reg.setFrontImageName(FrontImageName);
								 * reg.setBackImageName(BackImageName);
								 * reg.setFrontImage(FrontImage_byt);
								 * reg.setBackImage(BackImage_byt);
								 * 
								 * 
								 * Object Send_result = reg .SaveData(URL,
								 * NAMESPACE, METHOD_NAME, SOAP_ACTION);
								 * ShowDailog(Send_result.toString(),
								 * NewIntent);
								 */

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ShowDailog("Internet Connection interrupted: "
										+ e.getMessage()
										+ e.getClass().getName(), NewIntent);
							}

							// }

							// else if(con_mgr.getNetworkInfo(0).getState()
							// == NetworkInfo.State.DISCONNECTED) {
							// *------------------ */
							/*
							 * Toast.makeText(getBaseContext(),
							 * "Diconnected Data Saved to send later",
							 * Toast.LENGTH_LONG).show();
							 */

							try {
								/*
								 * DataRegWebServices obj = new
								 * DataRegWebServices(); Reg_Data.put("NAME",
								 * NAME); Reg_Data.put("MSISDN", MSISDN);
								 * Reg_Data.put("ADDRESS", ADDRESS);
								 * Reg_Data.put("SERAIL", SERAIL);
								 * Reg_Data.put("IMSI", IMSI);
								 * Reg_Data.put("DATE", DATE);
								 * Reg_Data.put("IDTYPES", "IdType");
								 * Reg_Data.put("FRONTIMAGE_NAME",
								 * FrontImageName);
								 * Reg_Data.put("BACKIMAGE_NAME",
								 * BackImageName); Reg_Data.put("FRONT_IMAGE",
								 * FrontImage_byt); Reg_Data.put("BACK_IMAGE",
								 * BackImage_byt); //
								 * Reg_Data.put("FRONT_IMAGE",
								 * MyCustom.getBytes(btm_FrontImage)); //
								 * Reg_Data
								 * .put("BACK_IMAGE",MyCustom.getBytes(btm_BackImage
								 * )); String msg = ds.InsertRow("REGISTRATION",
								 * Reg_Data);
								 */

								/*
								 * Toast.makeText(getBaseContext(),
								 * "Data Saved to send later",
								 * Toast.LENGTH_SHORT).show();
								 */

								// Toast.makeText(getBaseContext(),
								// "Data Saved Locally ",
								// Toast.LENGTH_LONG).show();
								// ShowDailog("Data Saved to send later",NewIntent);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								/*
								 * Toast.makeText( getBaseContext(), "Error " +
								 * e.getMessage() + "" + e.getClass().getName(),
								 * Toast.LENGTH_LONG).show();
								 */
								ShowDailog("Internet Connection interrupted: "
										+ e.getMessage()
										+ e.getClass().getName(), NewIntent);
							}
						}// End of Else for Check I agree all terms
							// }// ===== End of if connection
						/*
						 * } else { Toast.makeText(getBaseContext(),
						 * "Please enter all inforamtion",
						 * +Toast.LENGTH_LONG).show(); }
						 */

					} // ======end OnClick Save
				}); // ====== end OnClick Save ===========

			} // ======== End of the if the first check of conn
		} catch (Exception e) {
			// TODO Auto-generated catch block

			Toast.makeText(
					getBaseContext(),
					"Internet Connection interrupted:" + e.getMessage() + ""
							+ e.getClass().getName(), Toast.LENGTH_LONG).show();

		}

	} // =============End of On Create

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ds.close();
		imgAdapter.close();

		// System.runFinalizersOnExit(true);
		// finish();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	/*
	 * protected void onStop() { // TODO Auto-generated method stub
	 * super.onStop(); //System.runFinalizersOnExit(true);
	 * //android.os.Process.killProcess(android.os.Process.myPid()); }
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAMERA_PIC_REQUEST_Front) {
			if (resultCode == RESULT_OK) {
				btm_FrontImage = (Bitmap) data.getExtras().get("data");
				FrontImage.setImageBitmap(btm_FrontImage);
				MyCustom obj = new MyCustom();
				// btm_FrontImage
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				btm_FrontImage.compress(CompressFormat.PNG, 0, bos);
				FrontImage_byt = bos.toByteArray();
				// FrontImage_byt = obj.ConvertToByte(btm_FrontImage);
				Toast.makeText(getApplicationContext(),
						" Front Picture Taken ", +Toast.LENGTH_SHORT).show();
				flag = 1;
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(),
						"Take Front picture is canceled", +Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "No picture taken",
						+Toast.LENGTH_SHORT).show();
			}
		}

		if (requestCode == CAMERA_PIC_REQUEST_Back) {
			if (resultCode == RESULT_OK) {
				btm_BackImage = (Bitmap) data.getExtras().get("data");
				BackImage.setImageBitmap(btm_BackImage);
				MyCustom obj2 = new MyCustom();
				BackImage_byt = obj2.ConvertToByte(btm_BackImage);
				Toast.makeText(getApplicationContext(), "Back Image Taken ",
						+Toast.LENGTH_SHORT).show();
				flag = 0;
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(),
						"Take Back picture is canceled", +Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "No picture taken",
						+Toast.LENGTH_SHORT).show();
			}
		}

		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				contents = data.getStringExtra("SCAN_RESULT");
				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
				serail.setText(contents);

				Toast toast = Toast.makeText(this, "Content:" + contents
						+ " Format:" + format, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Toast toast = Toast.makeText(this, "Scan was Cancelled!",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 24, 104);
				toast.show();
			}
		}
	}

	public static void setAutoOrientaionEnable(ContentResolver resolver,
			Boolean enabled) {
		Settings.System.putInt(resolver,
				Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
		// Settings.System.putInt(android.provider.Settings.System.putInt(getContentResover()
		// , android.provider.Settings.System.USER_ROTATION, user_rotation));
	}

	/*
	 * @Override public void onConfigurationChanged(Configuration newConfig) {
	 * Configuration c = getResources().getConfiguration();
	 * 
	 * if (c.orientation == Configuration.ORIENTATION_PORTRAIT) { // portrait
	 * setContentView(R.layout.data_registration_layout);
	 * 
	 * } else if (c.orientation == Configuration.ORIENTATION_LANDSCAPE) { //
	 * landscape setContentView(R.layout.data_registration_layout);
	 * 
	 * } }
	 */

	private void ShowDailog(String msg, final Intent intent) {

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				DataRegistrationActivity.this);

		builder.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								if (intent != null)

									startActivity(intent);

							}
						});

		final AlertDialog alert = builder.create();
		alert.setTitle("  ");
		alert.setIcon(R.drawable.logonotify);
		alert.show();
	}

	private void CreateMenu(Menu menu) {
		menu.setQwertyMode(true);
		MenuItem mnu1 = menu.add(0, 0, 0, "Main Menu");
		{
			mnu1.setAlphabeticShortcut('a');
			mnu1.setIcon(R.drawable.icon);
		}
		MenuItem mnu2 = menu.add(0, 1, 1, "Settings");
		{
			mnu2.setAlphabeticShortcut('b');
			// mnu2.setIcon(R.drawable.icon);
		}
		MenuItem mnu3 = menu.add(0, 2, 2, "Exit");
		{
			mnu3.setAlphabeticShortcut('c');
			// mnu3.setIcon(R.drawable.icon);
			// onDestroy();
		}
		/*
		 * MenuItem mnu4 = menu.add(0, 3, 3, "Settings"); {
		 * mnu4.setAlphabeticShortcut('d'); } menu.add(0, 3, 3, "Item 5");
		 * menu.add(0, 3, 3, "Item 6"); menu.add(0, 3, 3, "Item 7");
		 */
	}

	private boolean MenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			/*
			 * Toast.makeText(this, "You clicked on Item 1", Toast.LENGTH_LONG)
			 * .show();
			 */
			Intent mainmenu = new Intent(this, MainPageActivity.class);
			startActivity(mainmenu);

			return true;
		case 1:
			/*
			 * Intent exit = new Intent(this, DataRegistrationActivity.class);
			 * startActivity(exit);
			 */
			// finish();
			return true;
		case 2:
			/*
			 * Intent exit = new Intent(this, Sales_CareActivity.class);
			 * startActivity(exit);
			 */
			finish();
			// finish();
			// System.runFinalizersOnExit(true);
			// android.os.Process.killProcess(android.os.Process.myPid());
			onDestroy();
			return true;

		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.data_registration_layout, menu);
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuChoice(item);
	}
}
// http://w2davids.wordpress.com/android-screen-orientation-rotation-changes/