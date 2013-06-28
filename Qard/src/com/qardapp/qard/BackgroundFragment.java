package com.qardapp.qard;

import android.os.Bundle;
import android.support.v4.app.Fragment;


public class BackgroundFragment extends Fragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
}
