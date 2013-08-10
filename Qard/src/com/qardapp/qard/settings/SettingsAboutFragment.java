package com.qardapp.qard.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.comm.server.QardLoginActivity;

public class SettingsAboutFragment extends BaseFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.settings_about_layout,
				container, false);
		
		Button login = (Button) rootView.findViewById(R.id.nexcircle_url_btn);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(getActivity(),QardLoginActivity.class);
				//startActivityForResult(intent, 21);
				Uri uri = Uri.parse("http://www.nexcircle.com");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		return rootView;
	}
		


	@Override
	public void updateViews() {
		// TODO Auto-generated method stub
		
	}
}
