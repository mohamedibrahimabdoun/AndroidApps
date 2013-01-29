package mtn.sales.salescarepkg;

import java.util.Locale;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivateSIMActivity extends Activity {
	private static final String METHOD_NAME = "activate_sim";

	private static final String SOAP_ACTION = "activate_sim";

	private static final String NAMESPACE = "http://mypac/";
	//private static final String URL = "http://10.1.4.140:8080/SalesApp/saleswsService";
	//private static final String URL="http://196.29.166.42:7001/SalesApp/saleswsService";
	private static final String URL ="http://196.29.166.42:7001/SalesApp/saleswsService";

	String IMSI;
	String MSGSTR;
	String sender_text;
	int Counter;
	EditText SUBNO,PUK;
	Button Activate;
	String subno_text, puk_text, receiver_text;
	TextView result_tv;
	SoapObject so;
	ActWebService ws_obj;
	String[] trader_str;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activate_sim);
		try {
		ws_obj = new ActWebService();
		//result_tv = (TextView) findViewById(R.id.result);

		//=============================================
		 //Locale locale = Locale.getDefault(); // current locale
         //Locale locale = getResources().getConfiguration().locale; 
         
         Locale locale = Locale.getDefault();
         // Locale locale = new Locale("ar");
         //Locale locale = new Locale("en_US");
         Locale.setDefault(locale);
         Configuration config = new Configuration();
         config.locale = locale;
         getApplicationContext().getResources().updateConfiguration(config, null);
         /*Toast.makeText(getBaseContext(),
					 "" +locale.getLanguage(), +Toast.LENGTH_LONG).show();*/
         //Log.d(logtag, "lang = " + locale.getLanguage());
		
		//=============================================
		
		
		// ======================= IMEI
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		IMSI = telephonyManager.getSubscriberId();

		// ===================== call Web service To Purchase
		// "634021450155813" "634021423765711";
		Activate = (Button) findViewById(R.id.activate);
		SUBNO = (EditText) findViewById(R.id.subno);
		PUK = (EditText) findViewById(R.id.puk);

	
		// ====================== OnClick Purchase Button
		Activate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					
					subno_text = SUBNO.getText().toString();
					puk_text = PUK.getText().toString();
					sender_text = "998316000"; // 998316000
					
	        if (subno_text.length()>0 && puk_text.length()>0 ){
					
					ws_obj.setIMSI(sender_text);
					ws_obj.setSUBNO(subno_text);
					ws_obj.setPUK(puk_text);
					//URL=getResources().getString(R.string.URL);
					Object Activation_result = ws_obj.CallSIMActivate(URL,
							NAMESPACE, METHOD_NAME, SOAP_ACTION);
					
					ShowDailog(Activation_result);
					
					finish();
					 } else { Toast.makeText(getBaseContext(),
					 "Please Enter Subno and PUK or CardNo" , +Toast.LENGTH_LONG).show(); }
					 

				} catch (Exception e) {
					Toast.makeText(getBaseContext(),
							e.getMessage() + "Error" + e.getClass().getName(),
							Toast.LENGTH_LONG).show();
				}// try catch Block

			}// ===============
		}); // ====== end of OnClick ===========================

		}catch (Exception e) {
			Toast.makeText(getBaseContext(), "Errot @ get" + e.getMessage() ,Toast.LENGTH_SHORT).show();
		}
	
	} // -------- End of onCreate

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//Intent intt = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
		Intent mainmenu = new Intent(this, MainPageActivity.class);
		startActivity(mainmenu);
		
	}
	public void ShowDailog(Object result)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(""+result).setPositiveButton("OK ",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						System.out.println("OK CLICKED");

					}
				});
		builder.setNegativeButton("Reset",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						//listDialog.cancel();
						Intent launch = new Intent(getBaseContext(),
								PurchasingActivity.class);
						launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(launch);
					}
				});
		AlertDialog alert = builder.create();
		alert.setTitle("Service Reply ");
		alert.setIcon(R.drawable.ic);
		alert.show();
	}
	
	private void CreateMenu(Menu menu) {
		menu.setQwertyMode(true);
		MenuItem mnu1 = menu.add(0, 0, 0, "Main Menu");
		{
			mnu1.setAlphabeticShortcut('a');
			mnu1.setIcon(R.drawable.icon);
		}
		MenuItem mnu2 = menu.add(0, 1, 1, "Exit");
		{
			mnu2.setAlphabeticShortcut('b');
			//mnu2.setIcon(R.drawable.ic_launcher);
		}
	}

	private boolean MenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			/*Toast.makeText(this, "You clicked on Item 1", Toast.LENGTH_LONG)
					.show();*/
			Intent mainmenu = new Intent(this, MainPageActivity.class);
			startActivity(mainmenu);

			return true;
		case 1:
			/*Toast.makeText(this, "You clicked on Item 2", Toast.LENGTH_LONG)
					.show();*/
			 finish();
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