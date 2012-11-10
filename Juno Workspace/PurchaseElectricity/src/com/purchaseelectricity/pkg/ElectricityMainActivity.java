package com.purchaseelectricity.pkg;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ElectricityMainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_electricity_main, menu);
        return true;
    }
}
