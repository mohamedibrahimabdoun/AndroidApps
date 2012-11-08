package popup.pkg;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class PopUpWindowActivity extends Activity {
    /** Called when the activity is first created. */
	private Button btnShowPopUp;
	private PopupWindow mpopup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		btnShowPopUp = (Button) findViewById(R.id.btnShowPopUp);
		btnShowPopUp.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				View popUpView = getLayoutInflater().inflate(R.layout.popup,
						null); // inflating popup layout
				mpopup = new PopupWindow(popUpView, LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT, true); // Creation of popup
				mpopup.setAnimationStyle(android.R.style.Animation_Toast);
				mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0); // Displaying
																		// popup

				Button btnOk = (Button) popUpView.findViewById(R.id.btnOk);
				btnOk.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						mpopup.dismiss(); // dismissing the popup
					}
				});

				Button btnCancel = (Button) popUpView
						.findViewById(R.id.btnCancel);
				btnCancel.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						mpopup.dismiss(); // dismissing the popup
					}
				});
			}
		});
    }
}