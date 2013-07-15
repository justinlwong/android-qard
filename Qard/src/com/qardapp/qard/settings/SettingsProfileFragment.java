package com.qardapp.qard.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;

public class SettingsProfileFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		// New layout
		View rootView = inflater.inflate(R.layout.settings_profile_layout1,
				container, false);
		ListView listView = (ListView) rootView.findViewById(R.id.settings_service_list);
		SettingsProfileAdapter adapter = new SettingsProfileAdapter(getActivity());
		listView.setAdapter(adapter);
		
		return rootView;
	}
	

	@Override
	public void updateViews() {
	}

}
