package com.qardapp.qard.settings;

import java.util.List;

import org.scribe.oauth.OAuthService;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.QardLoginActivity;
import com.qardapp.qard.comm.server.ServerHelper;
import com.qardapp.qard.comm.server.UpdateUserTask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Photo;
import android.provider.ContactsContract.Profile;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SettingsFragment extends Fragment{
	
	// Later will put this in the global application class
	//OAuthService linkedinService = null;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.settings_layout1,
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

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launch(Services.FACEBOOK.id);
				
			}
		});
//		
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID", Services.TWITTER.id);
				startActivityForResult(intent, 100);	
			}
		});
//		
		b3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID", Services.FLICKR.id);
				startActivityForResult(intent, 100);
			}
		});
//		
		
		b4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID", Services.LINKEDIN.id);
				startActivityForResult(intent, 100);			
			}
		});
		
		b5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID", Services.FOURSQUARE.id);
				startActivityForResult(intent, 100);			
			}
		});
		
		b6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),AuthActivity.class);
				startActivityForResult(intent, 100);			
			}
		});
		
		b7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID",Services.INSTAGRAM.id);
				startActivityForResult(intent, 100);			
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
                Intent intent = new Intent(getActivity(), GooglePlusAuthActivity.class);
                startActivity(intent);		
			}
		});
		
		Button login = (Button) rootView.findViewById(R.id.settings_login_btn);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),QardLoginActivity.class);
				startActivityForResult(intent, 21);
			}
		});
		
		Button updateAcc = (Button) rootView.findViewById(R.id.settings_update_acc);
		updateAcc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UpdateUserTask task = new UpdateUserTask(getActivity(),
						((EditText)getActivity().findViewById(R.id.settings_first_name)).getText().toString(),
						((EditText)getActivity().findViewById(R.id.settings_last_name)).getText().toString(),
						((EditText)getActivity().findViewById(R.id.settings_username)).getText().toString(),
						((EditText)getActivity().findViewById(R.id.settings_password)).getText().toString());
				task.execute();
			}
		});
		
		TextView current_id_field = (TextView) rootView.findViewById(R.id.settings_current_id);
		String user_id = ServerHelper.getUserId(getActivity());
		if (user_id != null) {
			current_id_field.setText("(DEBUG) Current qard id = " + user_id);
		} else {
			current_id_field.setText("(DEBUG) Current qard id not found, restart?");
		}
		
		return rootView;
	}
	
	public void launch( int serviceID) {
		Intent intent = null;
		if (serviceID == Services.FACEBOOK.id) {
		    intent = new Intent(this.getActivity(),FacebookLoginActivity.class);
		} 
		startActivityForResult(intent,0);	
	}
		
}





