package com.qardapp.qard.profile;

import com.qardapp.qard.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ProfileFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_layout,
				container, false);
		GridView gridView = (GridView) rootView
				.findViewById(R.id.profile_gridview);
		gridView.setAdapter(new ProfileServicesAdapter());
		return rootView;
	}
}
