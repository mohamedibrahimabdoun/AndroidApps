package app.ws.android;

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

public class CheckConnService extends Service {
	BroadcastReceiver broadcastReceiver;
	private NotificationManager NM;
	private int NOTIFICATION = 1;
	APOSActivity obj;
	PosinfoDataSource posinfods;

	ProgressBar progressBar;
	private int progress = 0;
	Notification notification;
	Thread download;
	int c_count;
	int total_del_number = 0;
	private static final String SOAP_ACTION = "SendPosinfo";

	private static final String METHOD_NAME = "SendPosinfo";

	private static final String NAMESPACE = "http://poswebservice/";

	String ID, POSNAME, POSTYPE, DATE, IMEI, STOCK, BRAND, LAT, LNG, MOBILE,
			ADDRESS, OWNER;
	private static final String URL = "http://196.29.166.42:7001/POSWSProject-POSWebService-context-root/POSService";

	// private static final String URL =
	// "http://172.17.10.25:7001/POSWSProject-POSWebService-context-root/POSService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

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
						arrstr[0] = "SELECT * FROM POSINFO";
						posinfods = new PosinfoDataSource(context, arrstr);
						int cnt = 0;
						cnt = posinfods.getCountRow("POSINFO");
						Cursor c = posinfods.getAllPosinfo("POSINFO");
						c_count = cnt;
						if (cnt > 0) {
							Toast.makeText(context,
									" Start syncing(" + cnt + ")rows",
									Toast.LENGTH_SHORT).show();

							try {
								Intent in = new Intent(getApplicationContext(),
										CheckConnService.class);
								PendingIntent pendingIntent = PendingIntent
										.getActivity(getApplicationContext(),
												0, in, 0);
								notification = new Notification(
										R.drawable.logo,
										"Sending Pending Data",
										System.currentTimeMillis());
								notification.flags = notification.flags
										| Notification.FLAG_AUTO_CANCEL;
								notification.contentView = new RemoteViews(
										getApplicationContext()
												.getPackageName(),
										R.layout.download_progress);
								notification.contentIntent = pendingIntent;
								notification.contentView.setImageViewResource(
										R.id.status_icon,
										R.drawable.ic_menu_save);
								notification.contentView
										.setTextViewText(R.id.status_text,
												"Sending in progress");
							} catch (Exception e) {
								Toast.makeText(
										context,
										"Contact System Development Notification Error  message( "
												+ e.getMessage() + " ) Stack ("
												+ e.getStackTrace() + " )",
										Toast.LENGTH_LONG).show();
							}
							// /////////////////////////////////////////////////////////

							int i = 0;
							MyClass mclass = new MyClass();
							Object o;
							c.moveToFirst();
							do {
								// Toast.makeText(context,
								// "With in Cursor " + c.getCount(),
								// Toast.LENGTH_SHORT).show();

								ID = c.getString(c.getColumnIndex("ID"));
								POSNAME = c.getString(c
										.getColumnIndex("POSNAME"));
								POSTYPE = c.getString(c
										.getColumnIndex("POSTYPE"));
								OWNER = c.getString(c.getColumnIndex("OWNER"));
								MOBILE = c
										.getString(c.getColumnIndex("MOBILE"));
								ADDRESS = c.getString(c
										.getColumnIndex("ADDRESS"));
								BRAND = c.getString(c.getColumnIndex("BRAND"));
								STOCK = c.getString(c.getColumnIndex("STOCK"));
								DATE = c.getString(c.getColumnIndex("DATE"));
								IMEI = c.getString(c.getColumnIndex("IMEI"));
								LAT = c.getString(c.getColumnIndex("LAT"));
								LNG = c.getString(c.getColumnIndex("LNG"));

								/*
								 * Log.d("MyCursor ID: ", ID);
								 * Log.d("MyCursor POSNAME: ", POSNAME);
								 * Log.d("MyCursor POSTYPE: ", POSTYPE);
								 * Log.d("MyCursor OWNER: ", OWNER);
								 * Log.d("MyCursor MOBILE: ", MOBILE);
								 * Log.d("MyCursor ADDRESS: ", ADDRESS);
								 * Log.d("MyCursor BRAND: ", BRAND);
								 */

								MyClass obj = new MyClass();
								obj.setPOSNAME(POSNAME);
								obj.setPOSTYPE(POSTYPE);
								obj.setOWNER(OWNER);
								obj.setADDRESS(ADDRESS);
								obj.setMOBILE(MOBILE);
								obj.setBRAND(BRAND);
								obj.setSTOCK(STOCK);
								obj.setDATE(DATE);
								obj.setIMEI(IMEI);
								obj.setLAT(Double.parseDouble(LAT));
								obj.setLNG(Double.parseDouble(LNG));

								Object Send_result = obj.SendWebService(URL,
										NAMESPACE, METHOD_NAME, SOAP_ACTION);
								Log.d("Send_result before if  ",
										Send_result.toString());
								String s = "Data Saved";

								if (Send_result.toString().equals(s.toString())) {

									Log.d("Equal Send_result: ",
											Send_result.toString());

									// =============== Delete all Rows if the
									// data sync to web services
									int id_number = posinfods.DeleteRow(
											"POSINFO", "ID",
											Integer.parseInt(ID));
									Log.d("id_number",
											Integer.toString(id_number));
									total_del_number = total_del_number
											+ id_number;

								} else {
									Log.d("Not Equal Send_result: ",
											Send_result.toString());
								}

								obj = null;
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

							Toast.makeText(context,
									" (" + total_del_number + ") Record Sent",
									Toast.LENGTH_SHORT).show();

							c.close();
							notification.contentView.setProgressBar(
									R.id.status_progress, c_count, progress,
									false);
							// inform the progress bar of updates in progress
							NM.notify(42, notification);
							showNotification();
						}

					} else {
						Toast.makeText(context, "Connection Down",
								Toast.LENGTH_SHORT).show();

					}
				} catch (Exception e) {
					Toast.makeText(
							context,
							"Contact System Development Immediately message( "
									+ e.getMessage() + " ) Stack ("
									+ e.getStackTrace() + " )",
							Toast.LENGTH_LONG).show();
					Log.d("Error", e.getMessage() + "Get Class" + e.getClass());
				}

			}

		};

		registerReceiver(broadcastReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

		Toast.makeText(this, "Service Staring", Toast.LENGTH_SHORT).show();

		NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		NM.cancel(42);
	}

	private void showNotification() {

		download.run();
		NM.notify(42, notification);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		NM.cancel(NOTIFICATION);

		unregisterReceiver(broadcastReceiver);
		posinfods = null;
		// Tell the user we stopped.
		Toast.makeText(
				this,
				"Your Data has Been Lost ,Please Contact System Development to Restore it..",
				Toast.LENGTH_LONG).show();
	}
}