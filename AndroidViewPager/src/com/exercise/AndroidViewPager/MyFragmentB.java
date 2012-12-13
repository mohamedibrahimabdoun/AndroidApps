package com.exercise.AndroidViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MyFragmentB extends Fragment {

	TextView b_received;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myFragmentView = inflater.inflate(R.layout.fragment_b, container, false);
		
		b_received = (TextView)myFragmentView.findViewById(R.id.b_received);
		String myTag = getTag();
		
		((AndroidViewPagerActivity)getActivity()).setTabFragmentB(myTag);
		
		Toast.makeText(getActivity(), 
				"MyFragmentB.onCreateView(): " + myTag, 
				Toast.LENGTH_LONG).show();

		return myFragmentView;
	}
	
	public void b_updateText(String t){
		b_received.setText(t);
	}

}
