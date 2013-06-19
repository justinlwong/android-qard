package com.qardapp.qard.profile;

import com.qardapp.qard.R;
import com.qardapp.qard.qrcode.QRCodeManager;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ProfileQRCodeFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_qr_code_layout,
				container, false);
		ImageView image = (ImageView) rootView.findViewById(R.id.profile_qr_code);
		SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.app_package_name), Context.MODE_PRIVATE);
		String user_id = pref.getString("user_id", "test");

		QRCodeManager.genQRCode (user_id, image); 
		return rootView;
	}
}
