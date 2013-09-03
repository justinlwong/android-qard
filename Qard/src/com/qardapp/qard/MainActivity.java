package com.qardapp.qard;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.qardapp.qard.comm.QardMessage;
import com.qardapp.qard.comm.server.AddFriendTask;
import com.qardapp.qard.comm.server.AddServiceTask;
import com.qardapp.qard.comm.server.GetFriendsWithServicesTask;
import com.qardapp.qard.comm.server.NewUserTask;
import com.qardapp.qard.comm.server.ServerNotifications;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.friends.FriendsFragment;
import com.qardapp.qard.profile.ProfileFragment;
import com.qardapp.qard.qrcode.QRCodeManager;
import com.qardapp.qard.settings.SettingsFragment;
import com.qardapp.qard.settings.SettingsProfileFragment;
import com.qardapp.qard.util.ImageUtil;

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
	public static String FRAGNAME_SETTINGS_ABOUT = "FRAGNAME_SETTINGS_ABOUT";

	
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
		ImageView menuMe = (ImageView) v.findViewById(R.id.menu_me);
		menuMe.setImageBitmap(ImageUtil.getProfilePic(this, 0));
		menuMe.setOnClickListener(new OnClickListener() {
			
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
				v.startAnimation( 
					    AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_forever) );
				GetFriendsWithServicesTask task = new GetFriendsWithServicesTask(MainActivity.this);
	    		task.execute();
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
		
		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			String widgetAction = extra.getString("widgetAction");
			if (widgetAction.equals("Scan")){
				QRCodeManager.scanQRCode(MainActivity.this);
			}
			if (widgetAction.equals("QR")){
				//MainActivity.this.switchFragments(FRAG_PROFILE);
				Intent intent = new Intent(this.getApplicationContext(), QRCodeDisplayActivity.class);
				intent.putExtra("msg", QardMessage.getMessage(this.getApplicationContext()));
				startActivity(intent);
			}
			
			
			String nfc_msg = extra.getString("nfc_data");
			if (nfc_msg != null) {
				ArrayList<String> resultValues = QardMessage.decodeMessage(nfc_msg);
		    	if (resultValues != null) {
		    		AddFriendTask task = new AddFriendTask(this, resultValues.get(QardMessage.ID),
		    				resultValues.get(QardMessage.FIRST_NAME), resultValues.get(QardMessage.LAST_NAME),
		    				resultValues.get(QardMessage.PHONE));
		    		task.execute();
		    	}
			}
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
		frag = getSupportFragmentManager().findFragmentByTag(FRAGNAME_SETTINGS_PROFILE);
		if (frag != null && frag.isVisible())
			((BaseFragment) frag).updateViews();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{  
		qrcode = QRCodeManager.checkScanActivityResult(this, requestCode, resultCode, data);
		if (qrcode != null) {
			Toast.makeText(this, "Scan Result = " + qrcode, Toast.LENGTH_SHORT).show();
		}
		refreshFragments();
	}


	@Override
	public Loader<ArrayList<ServerNotifications>> onCreateLoader(int id,
			Bundle arg1) {
		//if (id == REFRESH_LOADER_ID)
		//	return new FriendsInfoLoader(this);
		//if (id == NEW_USER_LOADER_ID)
			//return new NewUserLoader(this);
		return null;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<ServerNotifications>> loader,
			ArrayList<ServerNotifications> result) {
		/*
		if (loader.getId() == NEW_USER_LOADER_ID) {
			SharedPreferences pref = this.getSharedPreferences(getString(R.string.app_package_name), Context.MODE_PRIVATE);
			String user_id = pref.getString("user_id", null);
			View qr_code_image = findViewById(R.id.profile_qr_code);
			if (qr_code_image != null) {
				QRCodeManager.genQRCode (user_id, (ImageView)qr_code_image); 
				Log.d("here", "reached here");
				
				// Update widget with qr code
//				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//				RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.id.widget_qr);
//				ComponentName thisWidget = new ComponentName(this, QardWidgetProvider.class);
//				remoteViews.setImageViewResource(R.id.widget_qr, R.id.profile_qr_code);
//				appWidgetManager.updateAppWidget(thisWidget, remoteViews);
			}			
		}
		if (loader.getId() == REFRESH_LOADER_ID) {
			refreshFragments();
			findViewById(R.id.menu_refresh).clearAnimation();
		}
		*/
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<ServerNotifications>> loader) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "queue") , null, null, null, null);
		while (cursor.moveToNext()) {
			int type = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_QM_TYPE));
			int queue_id = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_QM_ID));
			if (type == FriendsDatabaseHelper.QUEUED_ADD_FRIEND) {
				new AddFriendTask(this, queue_id).execute();
			}
			else if (type == FriendsDatabaseHelper.QUEUED_DELETE_FRIEND) {
				//new AddFriendTask(this, queue_id).execute();
			}
			else if (type == FriendsDatabaseHelper.QUEUED_UPDATE_SERVICES) {
				//new AddServiceTask(this, queue_id).execute();
			}
		}
	}

}