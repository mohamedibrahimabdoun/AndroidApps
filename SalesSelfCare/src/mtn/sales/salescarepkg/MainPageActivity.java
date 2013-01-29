package mtn.sales.salescarepkg;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainPageActivity extends Activity {

	ImageView press_img1;
	Button datareg_btn;
	Button vtu_btn;
	Button elec_btn;
	Button exit_btn;
	Button act_btn;
	int flag = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity_layout);

		// startService(new Intent(MainPage_SalesCareActivity.this,
		// CheckingConnService.class));
		datareg_btn = (Button) findViewById(R.id.datareg);
		vtu_btn = (Button) findViewById(R.id.vtu);
		elec_btn = (Button) findViewById(R.id.elec);
		act_btn = (Button) findViewById(R.id.act);
		exit_btn = (Button) findViewById(R.id.exit);
		// vtu_btn.setVisibility(1); // setVisibility(View.GONE);
		//vtu_btn.setVisibility(View.GONE);
		datareg_btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent data_registration = new Intent(getBaseContext(),
						DataRegistrationActivity.class);
				startActivity(data_registration);
				finish();
			}
		});

		if (flag == 1) {
			vtu_btn.setVisibility(View.GONE);
			
		} else {
			vtu_btn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					Intent vtutransfer = new Intent(getBaseContext(),
							VTUActivity.class);
					startActivity(vtutransfer);
					finish();
				}
			});
		}

		elec_btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent electricity = new Intent(getBaseContext(),
						PurchasingActivity.class);
				startActivity(electricity);
				finish();

			}
		});

		act_btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent data_registration = new Intent(getBaseContext(),
						ActivateSIMActivity.class);
				startActivity(data_registration);
				finish();
			}
		});

		exit_btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// Process.killProcess( Process.myPid() );
				// MainPageActivity.this.finish();
				// finish();
				/*
				 * Intent exit = null;
				 * exit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 * exit.putExtra("EXIT", true);
				 * 
				 * if (getIntent().getBooleanExtra("EXIT", false)) {
				 * 
				 * 
				 * Intent exit = new Intent(getBaseContext(),
				 * SalesSelfCareActivity.class); startActivity(exit);}
				 */
				finish();
			}
		});

	}

	public static void setAutoOrientaionEnable(ContentResolver resolver,
			Boolean enabled) {
		Settings.System.putInt(resolver,
				Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
		// Settings.System.putInt(android.provider.Settings.System.putInt(getContentResover()
		// , android.provider.Settings.System.USER_ROTATION, user_rotation));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.mainactivity_mtnsalescare, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
