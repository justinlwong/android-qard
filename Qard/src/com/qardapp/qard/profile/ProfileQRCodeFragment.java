package com.qardapp.qard.profile;

import com.qardapp.qard.R;
import com.qardapp.qard.qrcode.QRCodeManager;

import android.support.v4.app.Fragment;
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
		QRCodeManager.genQRCode ("hello", image); 
		return rootView;
	}
}
