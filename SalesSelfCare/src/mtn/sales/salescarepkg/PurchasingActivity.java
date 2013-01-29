package mtn.sales.salescarepkg;

import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PurchasingActivity extends Activity {
	private static final String METHOD_NAME = "purchase_elec";

	private static final String SOAP_ACTION = "purchase_elec";

	private static final String NAMESPACE = "http://mypac/";

	private static final String URL = "http://10.1.4.140:8080/SalesApp/saleswsService";
	//private static final String URL ="http://172.26.1.61:7001/SalesApp/saleswsService";
	//private static final String URL ="http://196.29.166.42:7001/SalesApp/saleswsService";
	String IMSI;
	String MSGSTR;
	String sender_imsi;
	int Counter;
	EditText MeterNo;
	EditText Receiver;
	EditText Amount;
	Button Purchase;
	String meterno_text, amount_text, receiver_text;
	TextView result_tv;
	SoapObject so;
	PurchaseWebService ws_obj;
	String[] trader_str;
	MyTraders[] mytrader;
    Context context=this;
    SoapObject Purchase_result;
    String ResultCode;
    String MSG;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchase);

		ws_obj = new PurchaseWebService();
		result_tv = (TextView) findViewById(R.id.result);
		// ======================= IMEI
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		IMSI = telephonyManager.getSubscriberId();

		// ===================== call Web service To Purchase
		// "634021450155813" "634021423765711";
		String imsi = "634021450155813";
		Purchase = (Button) findViewById(R.id.purchase);
		MeterNo = (EditText) findViewById(R.id.meter);
		// Receiver = (EditText) findViewById(R.id.msisdn);
		Amount = (EditText) findViewById(R.id.amount);
		// ======================= Concat MSGSTR

	
		// ====================== OnClick Purchase Button
		Purchase.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					meterno_text = MeterNo.getText().toString();
					// receiver_text = Receiver.getText().toString();
					amount_text = Amount.getText().toString();
					sender_imsi = "998316000"; // 998316000  04111111110
			
					if (meterno_text.length()>0 && amount_text.length()>0 )
					 {						
					MSGSTR = meterno_text + "*" + amount_text;
					ws_obj.setIMSI(sender_imsi);
					ws_obj.setMSGSTR(MSGSTR);

				    Purchase_result = ws_obj.CallPurchaseElec(URL,
							NAMESPACE, METHOD_NAME, SOAP_ACTION);
				    
				    int counter_b = Purchase_result.getPropertyCount();			
					for (int i = 0; i < counter_b; i++) {
					SoapObject pii = (SoapObject) Purchase_result.getProperty(i);
					MSG= pii.getProperty(0).toString();
					  ResultCode = pii.getProperty(1).toString();
					}
					Toast.makeText(getBaseContext(),
							"" + Purchase_result.toString(), +Toast.LENGTH_LONG).show();
					
					Toast.makeText(getBaseContext(),
							"" + MSG+""+ResultCode , +Toast.LENGTH_LONG).show();
					
				    final AlertDialog.Builder builder = new AlertDialog.Builder(PurchasingActivity.this);
					builder.setMessage(""+MSG).setPositiveButton("Send via SMS",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									System.out.println("OK CLICKED");	
									//ShowSMSDailog(Purchase_result);
									if (ResultCode=="505000000")
									{
									ShowSMSDailog(MSG);
									}
									else {
										//builder.setCancelable(true);
										//HideButton();
										/*Intent launch = new Intent(getBaseContext(),
												PurchasingActivity.class);
										launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(launch);*/
										//((View) builder).setVisibility(View.);
										Toast.makeText(getBaseContext(),
												 "Sorry you can't send Failed SMS" , +Toast.LENGTH_LONG).show();
									}
									
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
					//ShowDailog(Purchase_result.toString());
					// ===========Intent to open New window ============
					/*Intent launch = new Intent(getBaseContext(),PurchasingActivity.class);
					launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(launch);*/			
					  } else { Toast.makeText(getBaseContext(),
					 "Please enter Meter No and Amount" , +Toast.LENGTH_LONG).show(); }
					 

				} catch (Exception e) {
					Toast.makeText(getBaseContext(),
							e.getMessage() + "Error" + e.getClass().getName(),
							Toast.LENGTH_LONG).show();
					 ShowDailog("Internet Connection interrupted: "+e.getMessage()+e.getClass().getName());
				}// try catch Block

			}// ===============
		}); // ====== end of OnClick ===========================

	} // -------- End of onCreate

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent mainmenu = new Intent(this, MainPageActivity.class);
		startActivity(mainmenu);	
	}
	
	public void ShowDailog(String result)
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
	
	public void ShowSMSDailog(final String result)
	{
		/* ========================== */
		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.sendsmsdailog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);
        final TextView tvsms=(TextView) findViewById(R.id.textView);
		final EditText userInputPhone = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInputPhone);
		final EditText userInputSMS = (EditText) promptsView
		.findViewById(R.id.editTextDialogUserInputSMS);
		userInputSMS.setText(result);
		// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("Send",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				// get user input and set it to result
				//result.setText(userInput.getText());
			    	try {
			    	
			        String phoneNo=userInputPhone.getText().toString();
			    	//String sms=Purchase_result.toString();
			    	String sms= userInputSMS.getText().toString();
			    	if (phoneNo.length() > 0 && result.toString().length() > 0) 
			    	{
			    	sendSMS(phoneNo,result);
			    	Toast.makeText(getBaseContext(),
							"Message Sent", Toast.LENGTH_SHORT)
							.show();
			    	}
			    	else 
			    	{
			    		userInputPhone.setError("Enter the Phone No!");
			    		Toast.makeText(getBaseContext(),
								"Enter the Phone No", Toast.LENGTH_SHORT)
								.show();
			    	}
			    	
			    	} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"SMS faild, please try again later!",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}

			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
			    	Intent launch = new Intent(getBaseContext(),
							PurchasingActivity.class);
					launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(launch);
			    	dialog.cancel();
			    }
			  });

		// create alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.setTitle("Send via SMS");
		alert.setIcon(R.drawable.ic);
		alert.show();

		/* ========================== */
		

	}
	private void sendSMS(String phoneNo, String message) {
		// TODO Auto-generated method stub
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				PurchasingActivity.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNo, null, message, pi, null);
	}

	private void CreateMenu(Menu menu) {
		menu.setQwertyMode(true);
		MenuItem mnu1 = menu.add(0, 0, 0, "Main Menu");
		{
			mnu1.setAlphabeticShortcut('a');
			//mnu1.setIcon(R.drawable.icon);
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