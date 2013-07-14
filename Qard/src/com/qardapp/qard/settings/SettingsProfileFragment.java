package com.qardapp.qard.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.settings.services.AccountChecker;
import com.qardapp.qard.settings.services.FacebookLoginActivity;
import com.qardapp.qard.settings.services.GooglePlusAuthActivity;
import com.qardapp.qard.settings.services.OAuthActivity;
import com.qardapp.qard.settings.services.PopupDialog;
import com.qardapp.qard.settings.services.SyncContactsActivity;

public class SettingsProfileFragment extends BaseFragment {

	// Later will put this in the global application class
	//OAuthService linkedinService = null;
	protected View rootView;
	private AccountChecker a;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		a = new AccountChecker(getActivity());
		
		rootView = inflater.inflate(R.layout.settings_profile_layout,
				container, false);

		Button b1 = (Button)rootView.findViewById(R.id.facebookconnect);
		Button b2 = (Button)rootView.findViewById(R.id.twitterconnect);
		Button b3 = (Button)rootView.findViewById(R.id.flickrconnect);
		Button b4 = (Button)rootView.findViewById(R.id.linkedinconnect);
		Button b5 = (Button)rootView.findViewById(R.id.foursquareconnect);
		Button b6 = (Button)rootView.findViewById(R.id.emailconnect);
		Button b7 = (Button)rootView.findViewById(R.id.instagramconnect);
		Button b8 = (Button)rootView.findViewById(R.id.contactsync);
		Button b9 = (Button)rootView.findViewById(R.id.googleplusconnect);
		Button b10 = (Button)rootView.findViewById(R.id.phoneconnect);
		Button b11 = (Button)rootView.findViewById(R.id.whatsappconnect);
		Button b12 = (Button)rootView.findViewById(R.id.tumblrconnect);
		Button b13 = (Button)rootView.findViewById(R.id.youtubeconnect);
		
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.FACEBOOK.getManager(getActivity()).startLoginIntent();
				
			}
		});
//		
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.TWITTER.getManager(getActivity()).startLoginIntent();
			}
		});
//		
		b3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.FLICKR.getManager(getActivity()).startLoginIntent();
			}
		});
//		
		
		b4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.LINKEDIN.getManager(getActivity()).startLoginIntent();			
			}
		});
		
		b5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.FOURSQUARE.getManager(getActivity()).startLoginIntent();		
			}
		});
		
		b6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.EMAIL.getManager(getActivity()).startLoginIntent();					
			}
		});
		
		b11.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.WHATSAPP.getManager(getActivity()).startLoginIntent();
			}
		});		
		
		b7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.INSTAGRAM.getManager(getActivity()).startLoginIntent();		
			}
		});
		
		b12.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.TUMBLR.getManager(getActivity()).startLoginIntent();		
			}
		});
		
		b13.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.YOUTUBE.getManager(getActivity()).startLoginIntent();		
			}
		});
		
		b8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            //intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	            //startActivityForResult(intent, 1);
                Intent intent = new Intent(getActivity(), SyncContactsActivity.class);
                startActivity(intent);		
			}
		});
		
		b9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.GOOGLEPLUS.getManager(getActivity()).startLoginIntent();
			}
		});
		
		b10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.PHONE.getManager(getActivity()).startLoginIntent();	
			}
		});
		return rootView;
	}
	

	@Override
	public void updateViews() {
	}
	

}
