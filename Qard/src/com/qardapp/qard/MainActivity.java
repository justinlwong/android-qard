package com.qardapp.qard;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.qardapp.qard.comm.server.FriendsInfoLoader;
import com.qardapp.qard.comm.server.NewUserTask;
import com.qardapp.qard.comm.server.ServerNotifications;
import com.qardapp.qard.friends.FriendsFragment;
import com.qardapp.qard.profile.ProfileFragment;
import com.qardapp.qard.qrcode.QRCodeManager;
import com.qardapp.qard.settings.SettingsFragment;

public class MainActivity extends SherlockFragmentActivity implements LoaderCallbacks<ArrayList<ServerNotifications>> {

	public String qrcode = "";

	public static int FRAG_PROFILE = 0;
	public static int FRAG_FRIENDS = 1;
	public static int FRAG_SETTINGS = 2;
	
	public static String FRAGNAME_PROFILE = "FRAGNAME_PROFILE";
	public static String FRAGNAME_FRIENDS = "FRAGNAME_FRIENDS";
	public static String FRAGNAME_SETTINGS = "FRAGNAME_SETTINGS";
	public static String FRAGNAME_FRIENDS_PROFILE = "FRAGNAME_FRIENDS_PROFILE";
	public static String FRAGNAME_SETTINGS_PROFILE = "FRAGNAME_SETTINGS_PROFILE";
	public static String FRAGNAME_SETTINGS_ACCOUNT = "FRAGNAME_SETTINGS_ACCOUNT";
	
	public static int REFRESH_LOADER_ID = 0;
	public static int NEW_USER_LOADER_ID = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		// !! NOTE: Reset database on app update for testing 
		// Token Setup
		//ServerHelper.resetUser(this);
		//this.deleteDatabase("qard.db");
		
		// !! TEST: GET FACEBOOK USER INFO
		//FacebookConnect fc = new FacebookConnect(this);
		//fc.getUserInfo();
		
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.menu_buttons_extra, null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(v);
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);


		((ImageView) v.findViewById(R.id.menu_back)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//MainActivity.this.switchFragments(FRAG_FRIENDS);
				onBackPressed();
			}
		});
		((ImageView) v.findViewById(R.id.menu_me)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.switchFragments(FRAG_PROFILE);
			}
		});
		((ImageView) v.findViewById(R.id.menu_friends)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.switchFragments(FRAG_FRIENDS);
			}
		});
		((ImageView) v.findViewById(R.id.menu_search)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment fragment = MainActivity.this.switchFragments(FRAG_FRIENDS);
				((FriendsFragment) fragment).openSearchKeyboard();
			}
		});
		((ImageView) v.findViewById(R.id.menu_camera)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				QRCodeManager.scanQRCode(MainActivity.this);
			}
		});
		((ImageView) v.findViewById(R.id.menu_refresh)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getSupportLoaderManager().restartLoader(REFRESH_LOADER_ID, null, MainActivity.this);
			}
		});
		

		SharedPreferences pref = this.getSharedPreferences(getString(R.string.app_package_name), Context.MODE_PRIVATE);
		String token = pref.getString("access_token", null);
		if (token != null)
		Log.e("Hi", token);
		if (token == null) {
			NewUserTask task = new NewUserTask(this);
			task.execute();

		} else {
			getSupportLoaderManager().initLoader(REFRESH_LOADER_ID, null, this);
		}
		switchFragments(FRAG_PROFILE);
		
		Bundle widgetResponse = getIntent().getExtras();
		//String widgetAction = widgetResponse.getString("widgetAction");
		
		if (widgetResponse == null){
			return;
		}
		String widgetAction = widgetResponse.getString("widgetAction");
		if (widgetAction.equals("Scan")){
			QRCodeManager.scanQRCode(MainActivity.this);
		}
		if (widgetAction.equals("QR")){
			MainActivity.this.switchFragments(FRAG_PROFILE);
		}
			
	}

	public Fragment switchFragments (int id) {
		if (id == FRAG_PROFILE) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGNAME_PROFILE);
			if (fragment == null) {
				Log.e("Hi", "Created new profile frag");
				fragment =  new ProfileFragment();
			}
			// Don't do anything if already visible
			if (fragment.isVisible())
				return fragment;
			FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
			trans.replace(R.id.main_container, fragment, FRAGNAME_PROFILE);
			trans.addToBackStack(null);
			trans.commit();
			return fragment;
		}
		else if (id == FRAG_FRIENDS) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGNAME_FRIENDS);
			if (fragment == null) {
				Log.e("Hi", "Created new friends frag");
				fragment =  new FriendsFragment();
			}
			// Don't do anything if already visible
			if (fragment.isVisible())
				return fragment;
			FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
			trans.replace(R.id.main_container, fragment, FRAGNAME_FRIENDS);
			trans.addToBackStack(null);
			trans.commit();
			// Close keyboard
			FrameLayout layout = (FrameLayout) MainActivity.this
					.findViewById(R.id.main_container);
			layout.requestFocus();
			return fragment;
		}
		else if (id == FRAG_SETTINGS) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGNAME_SETTINGS);
			if (fragment == null) {
				Log.e("Hi", "Created new settings frag");
				fragment =  new SettingsFragment();
			}
			// Don't do anything if already visible
			if (fragment.isVisible())
				return fragment;
			FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
			trans.replace(R.id.main_container, fragment, FRAGNAME_SETTINGS);
			trans.addToBackStack(null);
			trans.commit();
			return fragment;
		}
		return null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			switchFragments(FRAG_SETTINGS);
			break;
		}
		return true;
	}

	public void refreshFragments() {
		Fragment frag = getSupportFragmentManager().findFragmentByTag(FRAGNAME_PROFILE);
		if (frag != null && frag.isVisible())
			((BaseFragment) frag).updateViews();
		frag = getSupportFragmentManager().findFragmentByTag(FRAGNAME_FRIENDS);
		if (frag != null && frag.isVisible())
			((BaseFragment) frag).updateViews();
		frag = getSupportFragmentManager().findFragmentByTag(FRAGNAME_FRIENDS_PROFILE);
		if (frag != null && frag.isVisible())
			((BaseFragment) frag).updateViews();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{  
		qrcode = QRCodeManager.checkScanActivityResult(this, requestCode, resultCode, data);
		if (qrcode != null) {
			Toast.makeText(this, "Scan Result = " + qrcode, Toast.LENGTH_SHORT).show();
			refreshFragments();
		}
	}


	@Override
	public Loader<ArrayList<ServerNotifications>> onCreateLoader(int id,
			Bundle arg1) {
		if (id == REFRESH_LOADER_ID)
			return new FriendsInfoLoader(this);
		//if (id == NEW_USER_LOADER_ID)
			//return new NewUserLoader(this);
		return null;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<ServerNotifications>> loader,
			ArrayList<ServerNotifications> result) {
		if (loader.getId() == NEW_USER_LOADER_ID) {
			SharedPreferences pref = this.getSharedPreferences(getString(R.string.app_package_name), Context.MODE_PRIVATE);
			String user_id = pref.getString("user_id", null);
			View qr_code_image = findViewById(R.id.profile_qr_code);
			if (qr_code_image != null) {
				QRCodeManager.genQRCode (user_id, (ImageView)qr_code_image); 
			}
		}
		if (loader.getId() == REFRESH_LOADER_ID) {
			refreshFragments();
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<ServerNotifications>> loader) {
		// TODO Auto-generated method stub
		
	}

}
