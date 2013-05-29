package com.qardapp.qard.settings;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.qrcode.QRCodeManager;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SettingsFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.settings_layout1,
				container, false);
		//LinearLayout listView = (LinearLayout) rootView.findViewById(R.id.settings_linearlayout);
		//TextView text = (TextView)rootView.findViewById(R.id.qrcode_text);
		//text.setText(((MainActivity)getActivity()).qrcode);
		//listView.setAdapter(new FriendsCursorAdapter());
		//ImageView image = (ImageView)rootView.findViewById(R.id.qrcode_image);
		
		//QRCodeManager.genQRCode("TESTING", image, 15);
		Button b1 = (Button)rootView.findViewById(R.id.facebookconnect);
		Button b2 = (Button)rootView.findViewById(R.id.twitterconnect);

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launch(Services.FACEBOOK.id);
				
			}
		});
		
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launch(Services.TWITTER.id);
				
			}
		});
			
		return rootView;
	}
	
	public void launch( int serviceID) {
		Intent intent = null;
		if (serviceID == Services.FACEBOOK.id) {
		    intent = new Intent(this.getActivity(),FacebookLoginActivity.class);
		} else if (serviceID == Services.TWITTER.id) {
		    intent = new Intent(this.getActivity(),TwitterLoginActivity.class);
		}
		startActivityForResult(intent,0);	
	}
	
}





