package act.pkg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.ksoap2.serialization.SoapObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivateSIMActivity extends Activity {
	private static final String METHOD_NAME = "activate_sim";

	private static final String SOAP_ACTION = "activate_sim";

	private static final String NAMESPACE = "http://mypac/";

	private static final String URL = "http://10.1.4.140:8080/SalesApp/saleswsService";
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

		ws_obj = new ActWebService();
		result_tv = (TextView) findViewById(R.id.result);

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
					// if (pin_text.length()==4 && receiver_text.length()==9 ){
					subno_text = SUBNO.getText().toString();
					puk_text = PUK.getText().toString();
					sender_text = "998316000"; // 998316000

					ws_obj.setIMSI(sender_text);
					ws_obj.setSUBNO(subno_text);
					ws_obj.setPUK(puk_text);
					Object Activation_result = ws_obj.CallSIMActivate(URL,
							NAMESPACE, METHOD_NAME, SOAP_ACTION);
					
					ShowDailog(Activation_result.toString());
					
					/*
					 * } else { Toast.makeText(getBaseContext(),
					 * "Incorrect MSISDN or PIN" , +Toast.LENGTH_LONG).show(); }
					 */

				} catch (Exception e) {
					Toast.makeText(getBaseContext(),
							e.getMessage() + "Error" + e.getClass().getName(),
							Toast.LENGTH_LONG).show();
				}// try catch Block

			}// ===============
		}); // ====== end of OnClick ===========================

	} // -------- End of onCreate

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
								ActActivity.class);
						launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(launch);
					}
				});
		AlertDialog alert = builder.create();
		alert.setTitle("Service Reply ");
		alert.setIcon(R.drawable.ic);
		alert.show();
	}
}