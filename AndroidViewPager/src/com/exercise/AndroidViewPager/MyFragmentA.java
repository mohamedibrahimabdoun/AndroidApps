package com.exercise.AndroidViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyFragmentA extends Fragment {
	
	EditText A_input;
	Button A_enter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myFragmentView = inflater.inflate(R.layout.fragment_a, container, false);
		
		A_input = (EditText)myFragmentView.findViewById(R.id.a_input);
		A_enter = (Button)myFragmentView.findViewById(R.id.a_enter);
		A_enter.setOnClickListener(A_enterOnClickListener);
		
		return myFragmentView;
	}
	
	OnClickListener A_enterOnClickListener
	= new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			
			String textPassToB = A_input.getText().toString();
			
			String TabOfFragmentB = ((AndroidViewPagerActivity)getActivity()).getTabFragmentB();
			
			MyFragmentB fragmentB = (MyFragmentB)getActivity()
					.getSupportFragmentManager()
					.findFragmentByTag(TabOfFragmentB);

			fragmentB.b_updateText(textPassToB);
			
			Toast.makeText(getActivity(), 
					"text sent to Fragment B:\n " + TabOfFragmentB, 
					Toast.LENGTH_LONG).show();
		}};

}
