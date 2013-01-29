package android.care;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ActReportActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String METHOD_NAME = "";

	private static final String SOAP_ACTION = "";

	private static final String NAMESPACE = "http://mypac/";

	//private static final String URL = "http://10.1.4.140:8080/SalesApp/saleswsService";
	private static final String URL ="http://196.29.166.42:7001/SalesApp/saleswsService";

	ActWebService vtu_wsr;
	SoapObject sp;
	GridView gridView;
	String IMSI;
	String[] l = { "Jan", "Feb", "Mar", "April", "May", "June", "July", "Augs",
			"Sept", "Oct", "Nov", "Dec" };
	RadioButton daily,weekly,monthly;
	RadioGroup rg;
	Button show;
	String FROMDATE = "SYSDATE-30";
	List<String> al = new ArrayList<String>();
	ArrayAdapter<String> adapter ;
	String DAY="SYSDATE-1";
	String WEEK="SYSDATE-7";
	String MONTH="SYSDATE-30";
	String imsi = "634021428166261";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_reportgrid);

		// ======================= IMEI
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		IMSI = telephonyManager.getSubscriberId();
		
		show=(Button)findViewById(R.id.show);
	//	rg=(RadioGroup)findViewById(R.id.radioGroup1);
		daily=(RadioButton)findViewById(R.id.daily);
		weekly=(RadioButton)findViewById(R.id.weekly);
	//	monthly=(RadioButton)findViewById(R.id.monthly);
		gridView = (GridView) findViewById(R.id.grid);
		
	/*	rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
				/*if(daily.getId()==checkedId)
				{
					FROMDATE=DAY;
				}
				 
				if(weekly.getId()==checkedId)
					{
						FROMDATE=WEEK;
					}
					else 
						if(monthly.getId()==checkedId)
						{
							FROMDATE=MONTH;
						}
			}
		});*/
		
		
		/*daily.setOnCheckedChangeListener(listener)(true)
		{
			weekly.setChecked(false);
			monthly.setChecked(false);
		}*/
		
		
		show.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
	   
		
	/*	vtu_wsr = new ActWebService();
		String DESC = "DESC";*/
		//vtu_wsr.setTOP(DESC);
		//vtu_wsr.setIMSI(imsi);
		/*if (daily.isChecked())
		{
			FROMDATE=DAY;
		} else if (weekly.isChecked())
		{
			FROMDATE=WEEK;
		}
		else if (monthly.isChecked())
		{
			FROMDATE=MONTH;
		}*/
		
		//vtu_wsr.setDATE(FROMDATE);
		//sp = vtu_wsr.GetRep(URL, NAMESPACE, METHOD_NAME, SOAP_ACTION);
		/*int counter_b = sp.getPropertyCount();
		al.add("Number");
		al.add("Credit");			
		al.add("SendAmnt");
		al.add("ReceiveAmnt");
		for (int i = 0; i < counter_b; i++) {
			SoapObject pii = (SoapObject) sp.getProperty(i);
			String CREDIT = pii.getProperty(0).toString();
			String MSISDN = pii.getProperty(1).toString();
			String RECV = pii.getProperty(2).toString();
			String SEN = pii.getProperty(3).toString();*/
			/*
			 * MyReport rep=new MyReport(); rep.setMSISDN(MSISDN);
			 * rep.setCREDIT(CREDIT); rep.setREC(RECV); rep.setSEN(SEN);
			 */
		/*	al.add(MSISDN);
			al.add(CREDIT);			
			al.add(RECV);
			al.add(SEN);
		}*/
		
		/* adapter = new ArrayAdapter<String>(getBaseContext(),
				android.R.layout.simple_list_item_1, al);
		// (this,android.R.layout.browser_link_context_header,al);
    	gridView.setAdapter(adapter);*/
    	
    	/* ===========Intent to open New window ============
		Intent launch = new Intent(getBaseContext(),
				VTUDroidActivity.class);
		launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(launch);*/
		
		
			}
		});
		// adapter.clear();

	}
}