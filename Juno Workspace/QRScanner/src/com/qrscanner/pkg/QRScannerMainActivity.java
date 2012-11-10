package com.qrscanner.pkg;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QRScannerMainActivity extends Activity {

	Button capture_btn;
	Button captureOneD_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrscanner_main);

		captureOneD_btn = (Button) findViewById(R.id.button2);

		captureOneD_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, 0);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_qrscanner_main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String content = data.getStringExtra("SCAN_RESULT");
				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				// String type =data.getStringExtra("ENCODE_TYPE");
				String text = "content:" + content + "format:" + format;// +"type:"+type;
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
						.show();
			} else if (resultCode == RESULT_CANCELED) {

			}
		}
	}

}
