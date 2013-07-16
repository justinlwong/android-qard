package com.qardapp.qard.settings;


import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.settings.services.SyncContactsActivity;


public class SettingsFragment extends BaseFragment{
	
	// Later will put this in the global application class
	//OAuthService linkedinService = null;
	protected View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.settings_layout,
				container, false);
		
		Button profile = (Button) rootView.findViewById(R.id.settings_profile_btn);
		profile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager manager = SettingsFragment.this.getFragmentManager();
				SettingsProfileFragment fragment = (SettingsProfileFragment) manager.findFragmentByTag(MainActivity.FRAGNAME_SETTINGS_PROFILE);
				if (fragment == null)
					fragment = new SettingsProfileFragment();
				FragmentTransaction transaction = SettingsFragment.this.getFragmentManager().beginTransaction();
				transaction.replace(R.id.main_container, fragment, MainActivity.FRAGNAME_SETTINGS_PROFILE);
				transaction.addToBackStack(null);
				// Commit the transaction
				transaction.commit();
			}
		});
		
		Button account = (Button) rootView.findViewById(R.id.settings_account_btn);
		account.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager manager = SettingsFragment.this.getFragmentManager();
				SettingsAccountFragment fragment = (SettingsAccountFragment) manager.findFragmentByTag(MainActivity.FRAGNAME_SETTINGS_ACCOUNT);
				if (fragment == null)
					fragment = new SettingsAccountFragment();
				FragmentTransaction transaction = SettingsFragment.this.getFragmentManager().beginTransaction();
				transaction.replace(R.id.main_container, fragment, MainActivity.FRAGNAME_SETTINGS_ACCOUNT);
				transaction.addToBackStack(null);
				// Commit the transaction
				transaction.commit();
			}
		});
		
		Button syncContacts = (Button) rootView.findViewById(R.id.settings_sync_btn);
		syncContacts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            //intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	            //startActivityForResult(intent, 1);
                Intent intent = new Intent(getActivity(), SyncContactsActivity.class);
                startActivity(intent);		
			}
		});
		return rootView;
	}

	@Override
	public void updateViews() {

	}
		
}





