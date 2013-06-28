package com.qardapp.qard.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.qardapp.qard.R;

public class ProfileGridFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_grid_layout,
				container, false);
		GridView gridView = (GridView) rootView.findViewById(R.id.profile_gridview);
		gridView.setAdapter(new ProfileServicesAdapter());
		return rootView;
	}
}
