package vtu.wsdroid.pkg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
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

public class TransferActivity extends Activity {
	private static final String METHOD_NAME = "callsp";

	private static final String SOAP_ACTION = "callsp";

	private static final String METHOD_NAME_get_rec = "GetRec";

	private static final String SOAP_ACTION_get_rec = "GetRec";

	private static final String NAMESPACE = "http://mypac/";

	private static final String URL = "http://10.1.4.140:8080/VTUWS/androidwsService";

	String IMSI;
	String MSGSTR;
	String sender_imsi;
	int Counter;
	EditText PIN;
	EditText Receiver;
	EditText Amount;
	Button Transfer;
	Spinner s1;
	static ArrayAdapter<String> adapter1;
	String pin_text, amount_text, receiver_text;
	DataSource ds;
	TextView result_tv;
	SoapObject so;
	VTUWebService ws_obj;
	String[] trader_str;
	MyTraders[] mytrader;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer);

		ws_obj = new VTUWebService();
		result_tv = (TextView) findViewById(R.id.result);

		// ======================= IMEI
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		IMSI = telephonyManager.getSubscriberId();

		// ================= Create Traders Table in SQLite ================

		HashMap Structure_Traders = new HashMap();
		HashMap Data_Traders = new HashMap();
		Structure_Traders.put("_id", "integer primary key AUTOINCREMENT");
		Structure_Traders.put("MSISDN", "varchar2");
		String SQl_cstmt = CreateTable("TRADERS", Structure_Traders);
		try {
			String[] Commands = new String[1];
			Commands[0] = SQl_cstmt;
			ds = new DataSource(this, Commands);
			ds.open();

		} catch (Exception e) {
			Toast.makeText(
					getBaseContext(),
					e.getMessage() + "Error o creating Offline Save"
							+ e.getClass().getName(), Toast.LENGTH_LONG).show();
		}
		// ================= End Of Creating Tables

		// #################################### call Web service To Populate
	
		// String sender_imsi2 = "998316000";
		// Cursor curall =null;
		Cursor curall = ds.getAllMSISDN("TRADERS");
		if (curall.getCount() > 0) {

			startManagingCursor(curall);
			String[] curall_col = new String[] { "MSISDN" };
			int[] curall_to = new int[] { android.R.id.text1 };

			SimpleCursorAdapter sca_traders = new SimpleCursorAdapter(this,
					android.R.layout.simple_spinner_item, curall, curall_col,
					curall_to);
			// ========= Spinner
			sca_traders.setDropDownViewResource(android.R.layout.simple_spinner_item);
			s1 = (Spinner) this.findViewById(R.id.spinner);
			//s1.setTextFilterEnabled(true);

			
	       // listview.setTextFilterEnabled(true);

			s1.setAdapter(sca_traders);

		} else {
			try {
				// ===================== call Web service To Populate

				// ===Call GetRec Web Services
				String imsi = "634021423765711";
				// so=new SoapObject("sender",imsi);
				VTUWebService obj;
				obj = new VTUWebService();
				obj.setIMSI(imsi);
				// so=new SoapObject("sender",imsi);
				so = obj.CallGetRec(URL, NAMESPACE, METHOD_NAME_get_rec,
						SOAP_ACTION_get_rec);
				Toast.makeText(getBaseContext(), "Result" + so,
						+Toast.LENGTH_LONG).show();
				Counter = so.getPropertyCount();
				trader_str = new String[Counter];
				mytrader = new MyTraders[Counter];
				Cursor cur_traders = ds.getAllMSISDN("TRADERS");

				Toast.makeText(getBaseContext(),
						"Result SQLite" + cur_traders.getCount(),
						+Toast.LENGTH_LONG).show();

				// ===================== call Web service To Populate TRADERS

				for (int i = 0; i < mytrader.length; i++) {
					int j = 0;
					SoapObject soap = (SoapObject) so.getProperty(i);
					MyTraders t = new MyTraders();
					// t.id = j;
					t.msisdn = soap.getProperty(0).toString();
					mytrader[i] = t;
					trader_str[i] = t.msisdn ;
					Toast.makeText(getBaseContext(),
							"Result SQLite" + trader_str[i], +Toast.LENGTH_LONG).show();
					// =========== Create The Off line table of SQLite
					//Data_Traders.put("_id", j);
					Data_Traders.put("MSISDN", t.msisdn);
					String msg = ds.InsertRow("TRADERS", Data_Traders);
					j++;
				}

				startManagingCursor(cur_traders);
				String[] cur_col = new String[] { "MSISDN" };
				int[] cur_to = new int[] { android.R.id.text1 };

				SimpleCursorAdapter sca_offline = new SimpleCursorAdapter(this,
						android.R.layout.simple_spinner_item, cur_traders,
						cur_col, cur_to);
				sca_offline
						.setDropDownViewResource(android.R.layout.simple_spinner_item);
				s1 = (Spinner) this.findViewById(R.id.spinner);
				s1.setAdapter(sca_offline);

			} catch (Exception e) {
				result_tv.setText(e.getMessage() + "Error in OnCreate"
						+ e.getClass().getName());
				// Toast.makeText(getApplicationContext(),
				// e.getMessage()+"Error in OnCreate"+
				// e.getClass().getName(),Toast.LENGTH_LONG).show();
			}
			// ======End=========Populate SQLite from Web service BRAND are
			// Empty======
		}

		/*
		 * s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		 * {
		 * 
		 * public void onItemSelected(AdapterView<?> parent, View view, int pos,
		 * long id) {
		 * 
		 * Toast.makeText(getBaseContext(), "s1 :" ,Toast.LENGTH_SHORT).show();
		 * }
		 * 
		 * public void onNothingSelected(AdapterView<?> parent) {
		 * //Toast.makeText
		 * (getBaseContext(),"please select one of them ",Toast.LENGTH_SHORT
		 * ).show(); } });
		 */

		Transfer = (Button) findViewById(R.id.transfer);
		PIN = (EditText) findViewById(R.id.pin);
		Receiver = (EditText) findViewById(R.id.msisdn);
		Amount = (EditText) findViewById(R.id.amount);

		// ======================= MSG STR

		pin_text = PIN.getText().toString();
		receiver_text = Receiver.getText().toString();
		amount_text = Amount.getText().toString();
		sender_imsi = "998316000";
	//	ds.getFiterable("TRADERS", receiver_text);
		//ds.getAllRec("TRADERS", receiver_text);
		ds.getAllMSISDN("TRADERS");
		// ====================== OnClick Transfer Button
		Transfer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					// if (pin_text.length()==4 && receiver_text.length()==9 ){
					// *626*0000*928038956*1#
					MSGSTR = "*626*" + pin_text + "*" + receiver_text + "*"
							+ amount_text + "#";

					ws_obj.setMSGSTR(MSGSTR);
					ws_obj.setIMSI(sender_imsi);
					Object transfer_result = ws_obj.CallTransfer(URL,
							NAMESPACE, METHOD_NAME, SOAP_ACTION);
					// result_tv.setText("The MSG:"+transfer_result.toString());
					// result_tv.setText(transfer_result.toString());
					// result_tv.setTextColor(339900);
					Toast.makeText(getBaseContext(),
							"" + transfer_result.toString(), +Toast.LENGTH_LONG)
							.show();
					// ===========Intent to open New window ============
					Intent launch = new Intent(getBaseContext(),
							VTUActivity.class);
					launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(launch);

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

	// Create Table Method , will use it in SQLite
	public String CreateTable(String Table_Name, HashMap hm) {

		StringBuffer sb = new StringBuffer();
		sb.append("create table if not exists " + Table_Name + " (");
		Set s = hm.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			sb.append(" " + me.getKey() + " " + me.getValue() + ",");
		}
		sb.append(")");
		String SQL = sb.toString();
		String R_SQL = SQL.replace(",)", ");");
		return R_SQL;
	}
}