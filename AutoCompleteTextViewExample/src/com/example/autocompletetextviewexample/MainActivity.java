package com.example.autocompletetextviewexample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class MainActivity extends Activity {

	
	 String[] devplatforms =
         {
         "C",
         "C++",
         "Java",
         "C#.NET",
         "iPhone",
         "Android",
         "ASP.NET",
         "PHP",
         "Python",
         };
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ArrayList<MyClass> lst= new  ArrayList<MyClass>() ;
        for (int i = 0; i < 10; i++) {
        	lst.add(new MyClass(i,"Name"+i ,"92290267"+i ) );
			
		}
        
        ArrayAdapter<MyClass> adapter =
                new ArrayAdapter<MyClass>( this,android.R.layout.simple_list_item_1,lst);
         
         
          AutoCompleteTextView actvDev = (AutoCompleteTextView)findViewById(R.id.actvDev);
          actvDev.setThreshold(1);
          actvDev.setAdapter(adapter);
    }

  
}
