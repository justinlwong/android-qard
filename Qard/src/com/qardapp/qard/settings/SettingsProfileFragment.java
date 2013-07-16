package com.qardapp.qard.settings;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.settings.services.AccountChecker;
import com.qardapp.qard.settings.services.SyncContactsActivity;
import android.widget.ListView;
import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;

public class SettingsProfileFragment extends BaseFragment {

	private SettingsProfileAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		// New layout
		View rootView = inflater.inflate(R.layout.settings_profile_layout1,
				container, false);
		ListView listView = (ListView) rootView.findViewById(R.id.settings_service_list);
		adapter = new SettingsProfileAdapter(getActivity(), null, this );
		listView.setAdapter(adapter);
		
		return rootView;
	}
	

	@Override
	public void updateViews() {
		ContentResolver res = getActivity().getContentResolver();
		Cursor cursor = res.query(FriendsProvider.MY_URI, null, null, null, null);
		adapter.changeCursor(cursor);
	}

}
