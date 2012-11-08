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

public class CheckingService extends Service {
	BroadcastReceiver broadcastReceiver;
	private NotificationManager NM;
	private int NOTIFICATION = 1;
	//PosinfoDataSource posinfods;
	ProgressBar progressBar;
	private int progress = 0;
	Notification notification;
	Thread download;
	int c_count;
	int total_del_number = 0;
	private static final String SOAP_ACTION = "";

	private static final String METHOD_NAME = "";

	private static final String NAMESPACE = "";

	private static final String URL = "";
	
	public String ID,MSISDN, NAME,DATE, IMSI,ADDRESS,SERAIL ,IDTYPE,
		 FrontImgName,BackImgName;
	public byte[] FrontImageByte;
	public byte[] BackImageByte;

	@Override
	public void onCreate() 
	{
		super.onCreate();
						
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}


