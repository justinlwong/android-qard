package com.qardapp.qard.profile;

import com.qardapp.qard.QRCodeDisplayActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.qrcode.QRCodeManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TabWidget;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_layout,
				container, false);
		//GridView gridView = (GridView) rootView.findViewById(R.id.profile_gridview);
		//gridView.setAdapter(new ProfileServicesAdapter());
		ContentResolver res = getActivity().getContentResolver();
		Cursor cursor = res.query(FriendsProvider.MY_URI, null, null, null, null);
		
		cursor.moveToFirst();
		
		TextView nameView = (TextView) rootView.findViewById(R.id.profile_name);
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
		nameView.setText(first_name + " " + last_name);
		
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
		
		final Button qrButton = (Button) rootView.findViewById(R.id.profile_qr_code_button);
		final Button meButton = (Button) rootView.findViewById(R.id.profile_me_button);
		
		qrButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				qrButton.setBackgroundColor(0xFF66FFFF); 
				meButton.setBackgroundColor(0xFFFFFFFF); 
				FragmentManager fragmentManager = getChildFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.profile_main_fragment, new ProfileQRCodeFragment());
				fragmentTransaction.commit();
			}
		});
		

		meButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				qrButton.setBackgroundColor(0xFFFFFFFF); 
				meButton.setBackgroundColor(0xFF66FFFF); 
				FragmentManager fragmentManager = getChildFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.profile_main_fragment, new ProfileGridFragment());
				fragmentTransaction.commit();
			}
		});
		qrButton.setBackgroundColor(0xFF66FFFF); 
		meButton.setBackgroundColor(0xFFFFFFFF); 
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.profile_main_fragment, new ProfileQRCodeFragment());
		fragmentTransaction.commit();
		return rootView;
	}
}
