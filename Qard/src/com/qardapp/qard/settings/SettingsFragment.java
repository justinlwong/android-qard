package com.qardapp.qard.settings;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.qrcode.QRCodeManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.settings_layout,
				container, false);
		//LinearLayout listView = (LinearLayout) rootView.findViewById(R.id.settings_linearlayout);
		TextView text = (TextView)rootView.findViewById(R.id.qrcode_text);
		text.setText(((MainActivity)getActivity()).qrcode);
		//listView.setAdapter(new FriendsCursorAdapter());
		ImageView image = (ImageView)rootView.findViewById(R.id.qrcode_image);
		
		QRCodeManager.genQRCode("TESTING", image, 15);
		return rootView;
	}
	
}
