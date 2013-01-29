package com.salescare.pkg;

import org.ksoap2.serialization.SoapObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

public class CheckingConnService extends Service {
	BroadcastReceiver broadcastReceiver;
	private NotificationManager NM;
	private int NOTIFICATION = 1;
	Data_RegDataSource reg_ds;
	ProgressBar progressBar;
	private int progress = 0;
	Notification notification;
	Thread download;
	int c_count;
	int total_del_number = 0;
	private static final String SOAP_ACTION = "SaveData";

	private static final String METHOD_NAME = "SaveData";

	private static final String NAMESPACE = "http://tempuri.org/";

	//private static final String URL = "http://196.29.166.42:7001/SalesCare-Sales_CareProject-context-root/DataRegistrationService";
    private static final String URL ="http://172.26.1.61:7001/SalesCare-Sales_CareProject-context-root/DataRegistrationService";
	// private static final String URL =
	// "http://172.26.1.61:7001/SalesCare-Sales_CareProject-context-root/DataRegistrationService";

	public String ID, DATE, NAME, MSISDN, IMSI, ADDRESS, SERAIL, IDTYPE,
			FrontImgName, BackImgName;
	public byte[] FrontImageByte;
	public byte[] BackImageByte;

	@Override
	public void onCreate() {
		super.onCreate();
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				try {
					ConnectivityManager con_mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
					// NetworkInfo myInfo
					// =con_mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
					if (con_mgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED) {
						Toast.makeText(context, "Connection Up",
								Toast.LENGTH_SHORT).show();
			String[] arrstr = new String[1];
			arrstr[0] = "SELECT * FROM REGISTRATION";
			reg_ds = new Data_RegDataSource(context, arrstr);
			int cnt = 0;
			cnt = reg_ds.getCountRow("REGISTRATION");
			Cursor c = reg_ds.getAllData("REGISTRATION");
			c_count = cnt;
			if (cnt > 0) {
				Toast.makeText(context,
						" Start Syncing(" + cnt + ")rows",
						Toast.LENGTH_SHORT).show();

			Intent in = new Intent(getApplicationContext(),CheckingConnService.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,in, 0);
			notification = new Notification(R.drawable.logo,"Sending Pending Data",
			System.currentTimeMillis());
			notification.flags = notification.flags| Notification.FLAG_AUTO_CANCEL;
			notification.contentView = new RemoteViews(getApplicationContext().getPackageName(),R.layout.download_progress);
		    notification.contentIntent = pendingIntent;
			notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.ic_menu_save);
			notification.contentView.setTextViewText(R.id.status_text, "Sending in progress");
                     // ------------------------- Progress 
			int i = 0;
			Object o;
			c.moveToFirst();
			do {
			    ID = c.getString(c.getColumnIndex("ID"));
				NAME = c.getString(c.getColumnIndex("NAME"));
				MSISDN = c.getString(c.getColumnIndex("MSISDN"));
				SERAIL = c.getString(c.getColumnIndex("SERAIL"));
				ADDRESS = c.getString(c.getColumnIndex("ADDRESS"));
				IDTYPE = c.getString(c.getColumnIndex("IDTYPE"));
				DATE = c.getString(c.getColumnIndex("DATE"));
				FrontImgName = c.getString(c.getColumnIndex("FrontImgName"));
				BackImgName = c.getString(c.getColumnIndex("BackImgName"));
				FrontImageByte=c.getBlob(c.getColumnIndex("FrontImageByte"));
				BackImageByte=c.getBlob(c.getColumnIndex("BackImageByte"));

	     		/*     Log.d("MyCursor ID: ", ID);
					 * Log.d("MyCursor POSNAME: ", POSNAME);
					 * Log.d("MyCursor POSTYPE: ", POSTYPE);*/

				DataRegWebServices reg = new DataRegWebServices();
			    reg.setNAME(NAME);
			    reg.setMSISDN(MSISDN);
			    reg.setADDRESS(ADDRESS);
			    reg.setSERAIL(SERAIL);
			    reg.setIMSI(IMSI);
			    reg.setIdType(IDTYPE);
			    reg.setFrontImageName(FrontImgName);
			    reg.setBackImageName(BackImgName);
			    reg.setFrontImage(FrontImageByte);
			    reg.setBackImage(BackImageByte);
				
				Object Send_result = reg.SaveData(URL,NAMESPACE, METHOD_NAME, SOAP_ACTION);
				Log.d("Send_result before if  ",Send_result.toString());
				String s = "Data Saved";

				if (Send_result.toString().equals(s.toString())) 
				{

					Log.d("Equal Send_result: ",Send_result.toString());

	// =============== Delete all Rows if the data sync to web services
				int id_number = reg_ds.DeleteRow("REGISTRATION", "ID",Integer.parseInt(ID));
				Log.d("id_number",Integer.toString(id_number));
				total_del_number = total_del_number+ id_number;
					} else {
						Log.d("Not Equal Send_result: ",
								Send_result.toString());
							}

						reg = null;
						i = i++;
						progress++;

						download = new Thread() {
							@Override
							public void run() {
								for (int i = 1; i < c_count; i++) {
									notification.contentView
											.setProgressBar(
													R.id.status_progress,
													c_count, progress,
													false);
									NM.notify(42, notification);
									try {
										Thread.sleep(150);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								NM.cancel(42);
							}
						};

						download.run();
						NM.notify(42, notification);

					} while (c.moveToNext());

					Toast.makeText(context," (" + total_del_number + ") Record Sent",
							Toast.LENGTH_SHORT).show();

					c.close();
					notification.contentView.setProgressBar(R.id.status_progress, c_count, progress,false);
					// inform the progress bar of updates in progress
					NM.notify(42, notification);
					showNotification();
				}

					} else {
						Toast.makeText(context, "Connection Down",
								Toast.LENGTH_SHORT).show();

					}
				} catch (Exception e) {
					// Toast.makeText(context, e.getStackTrace()+"Here",
					// Toast.LENGTH_SHORT).show();
					Log.d("Error", e.getMessage() + "Get Class" + e.getClass());
				}

			}

			private void showNotification() {
				// TODO Auto-generated method stub
				download.run();
				NM.notify(42, notification);
			}

		};

		registerReceiver(broadcastReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

		Toast.makeText(this, "Service Staring", Toast.LENGTH_SHORT).show();

		NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		NM.cancel(42);

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
