package com.qardapp.qard.profile;

import com.qardapp.qard.QRCodeDisplayActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.QardMessage;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.qrcode.QRCodeManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberUtils;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

public class ProfileFragment extends Fragment{

	private String lastUserId = "noid";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_layout,
				container, false);
		
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
				//Bundle bundle = new Bundle();
				//bundle.putString("msg", "Test");
				intent.putExtra("msg", "HelloTesting123456");
				startActivity(intent);
			}
		});
		
		return rootView;
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
		ContentResolver res = getActivity().getContentResolver();
		Cursor cursor = res.query(FriendsProvider.MY_URI, null, null, null, null);
		
		cursor.moveToFirst();
		
		TextView nameView = (TextView) getView().findViewById(R.id.profile_name);
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
		nameView.setText(first_name + " " + last_name);
		String number = null;
		do {
			int val = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_SERVICE_ID));
			if (val == Services.PHONE.id) {
				number = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
				if (number != null) {
					TextView phoneView = (TextView) getView().findViewById(R.id.profile_phone);
					phoneView.setText(PhoneNumberUtils.formatNumber(number));
				}
			}
		} while (cursor.moveToNext()) ;
		
		if (number == null)
			number = "";
		SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.app_package_name), Context.MODE_PRIVATE);
		String user_id = pref.getString("user_id", "noid");
		// Don't regenerate everytime
		if (!(lastUserId.equals(user_id))) {
			ImageView image = (ImageView) getView().findViewById(R.id.profile_qr_code);
			String msg = QardMessage.encodeMessage(user_id, first_name, last_name, number);
			QRCodeManager.genQRCode (msg, image); 
		}
	}

}
