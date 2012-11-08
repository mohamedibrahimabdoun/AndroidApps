package vtu.wsdroid.pkg;

import android.app.Activity;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TabHost;

public class VTUActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost

		Intent TransferIntent = new Intent(this, TransferActivity.class);
		tabHost.addTab(tabHost.newTabSpec("Transfer")
				.setIndicator("VTU Transfer").setContent(TransferIntent));

		Intent ReportIntent = new Intent(this, ReportActivity.class);
		tabHost.addTab(tabHost.newTabSpec("Statistic Report")
				.setIndicator("Report").setContent(ReportIntent));

		// Set tabs Colors #FECA08
		tabHost.setBackgroundColor(Color.BLUE);
		tabHost.getTabWidget().setBackgroundColor(Color.WHITE);

	}

	public static void setAutoOrientaionEnable(ContentResolver resolver,
			Boolean enabled) {
		Settings.System.putInt(resolver,
				Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
		// Settings.System.putInt(android.provider.Settings.System.putInt(getContentResover()
		// , android.provider.Settings.System.USER_ROTATION, user_rotation));
	}
}