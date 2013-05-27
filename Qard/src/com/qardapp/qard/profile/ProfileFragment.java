package com.qardapp.qard.profile;

import com.qardapp.qard.QRCodeDisplayActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.qrcode.QRCodeManager;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

public class ProfileFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_layout,
				container, false);
		GridView gridView = (GridView) rootView.findViewById(R.id.profile_gridview);
		gridView.setAdapter(new ProfileServicesAdapter());
		
		Button scan = (Button) rootView.findViewById(R.id.profile_scan);
		scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				QRCodeManager.scanQRCode(ProfileFragment.this.getActivity());
			}
		});
		
		
		Button send = (Button) rootView.findViewById(R.id.profile_send);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), QRCodeDisplayActivity.class);
				Bundle bundle = new Bundle();
				//bundle.putString("msg", "Test");
				intent.putExtra("msg", "HelloTesting123456");
				startActivity(intent);
			}
		});
		return rootView;
	}
}
