package com.salescare.pkg;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class Sales_CareActivity extends Activity {

	ImageView press_img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_salescare);

		press_img=(ImageView) findViewById(R.id.pressimgview);
		press_img.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent mainmenu = new Intent(getBaseContext(),
						MainPage_SalesCareActivity.class);
				startActivity(mainmenu);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainactivity_mtnsalescare, menu);
		return true;
	}
}