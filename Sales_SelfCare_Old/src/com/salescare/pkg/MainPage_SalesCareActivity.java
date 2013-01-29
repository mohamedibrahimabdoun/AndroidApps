package com.salescare.pkg;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainPage_SalesCareActivity extends Activity {

	ImageView press_img1;
	Button datareg_btn;
	Button vtu_btn;
	Button elec_btn;
	Button exit_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity_salescare);

		datareg_btn=(Button) findViewById(R.id.datareg);
		vtu_btn=(Button) findViewById(R.id.vtu);
		elec_btn=(Button) findViewById(R.id.elec);
		exit_btn=(Button) findViewById(R.id.exit);
	
		datareg_btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent data_registration = new Intent(getBaseContext(),
						DataRegistrationActivity.class);
				startActivity(data_registration);

			}
		});
	
	
		vtu_btn.setOnClickListener(new View.OnClickListener() {

		public void onClick(View v) {

			Intent vtutransfer = new Intent(getBaseContext(),
					MainPage_SalesCareActivity.class);
			startActivity(vtutransfer);

		}
	});
	
	
		elec_btn.setOnClickListener(new View.OnClickListener() {

		public void onClick(View v) {

			Intent electricity = new Intent(getBaseContext(),
					MainPage_SalesCareActivity.class);
			startActivity(electricity);

		}
	});
	
	
	
		exit_btn.setOnClickListener(new View.OnClickListener() {

		public void onClick(View v) {

			Intent exit = new Intent(getBaseContext(),
					Sales_CareActivity.class);
			startActivity(exit);

		}
	});
	
}
	public static void setAutoOrientaionEnable(ContentResolver resolver,
			Boolean enabled) 
	{
		Settings.System.putInt(resolver,Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
		// Settings.System.putInt(android.provider.Settings.System.putInt(getContentResover()
		// , android.provider.Settings.System.USER_ROTATION, user_rotation));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainactivity_mtnsalescare, menu);
		return true;
	}
}
