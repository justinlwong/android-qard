package com.qardapp.qard.settings;

import java.util.Arrays;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.qrcode.QRCodeManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.android.Facebook;
import com.facebook.widget.LoginButton;
import com.facebook.Session;
import com.facebook.SessionState;

public class SettingsFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.settings_layout2,
				container, false);
		//LinearLayout listView = (LinearLayout) rootView.findViewById(R.id.settings_linearlayout);
		//TextView text = (TextView)rootView.findViewById(R.id.qrcode_text);
		//text.setText(((MainActivity)getActivity()).qrcode);
		//listView.setAdapter(new FriendsCursorAdapter());
		//ImageView image = (ImageView)rootView.findViewById(R.id.qrcode_image);
		
		//QRCodeManager.genQRCode("TESTING", image, 15);
		LoginButton authButton = (LoginButton) rootView.findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("basic_info"));
		authButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickLogin();
			}
		});
				
		return rootView;
	}
	
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	
	private void onClickLogin() {
	    Session session = Session.getActiveSession();
	    if (!session.isOpened() && !session.isClosed()) {
	        session.openForRead(new Session.OpenRequest(this)
	            .setPermissions(Arrays.asList("basic_info"))
	            .setCallback(statusCallback));
	    } else {
	        Session.openActiveSession(getActivity(), this, true, statusCallback);
	    }
	}
		
	private class SessionStatusCallback implements Session.StatusCallback {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	            // Respond to session state changes, ex: updating the view
	    }
	}
	
}





