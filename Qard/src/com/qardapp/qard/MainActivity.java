package com.qardapp.qard;

import java.util.ArrayList;
import java.util.Locale;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.qardapp.qard.comm.server.FriendsInfoLoader;
import com.qardapp.qard.comm.server.NewUserLoader;
import com.qardapp.qard.comm.server.NewUserTask;
import com.qardapp.qard.comm.server.ServerHelper;
import com.qardapp.qard.comm.server.ServerNotifications;
import com.qardapp.qard.friends.FriendsFragment;
import com.qardapp.qard.profile.ProfileFragment;
import com.qardapp.qard.qrcode.QRCodeManager;
import com.qardapp.qard.settings.SettingsFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener, LoaderCallbacks<ArrayList<ServerNotifications>> {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	//SectionsPagerAdapter mSectionsPagerAdapter;
	public String qrcode = "";
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	//ViewPager mViewPager;
	public static int FRAG_PROFILE = 0;
	public static int FRAG_FRIENDS = 1;
	public static int FRAG_SETTINGS = 2;
	
	public static int REFRESH_LOADER_ID = 0;
	public static int NEW_USER_LOADER_ID = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		//actionBar.setCustomView(view)
		actionBar.setIcon(R.drawable.profile_default);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.menu_buttons_extra, null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(v);
		

		// Consider using menu items
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		  actionBar.setDisplayHomeAsUpEnabled(false);
		    actionBar.setHomeButtonEnabled(true);
		    actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);

		// !! NOTE: Reset database on app update for testing 
		//this.deleteDatabase("qard.db");
		
		// !! TEST: GET FACEBOOK USER INFO
		//FacebookConnect fc = new FacebookConnect(this);
		//fc.getUserInfo();
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		/*mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);*/
		

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
	/*	mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
*/
		/*
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}*/
	
		ImageView menu_friends = (ImageView) v.findViewById(R.id.menu_friends);
		menu_friends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.switchFragments(FRAG_FRIENDS);

			}
		});
		//ServerHelper.resetUser(this);
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
		
	}

	public Fragment switchFragments (int id) {
		if (id == FRAG_PROFILE) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAG_PROFILE");
			if (fragment == null) {
				Log.e("Hi", "Created new profile frag");
				fragment =  new ProfileFragment();
			}
			FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
			trans.replace(R.id.main_container, fragment, "FRAG_PROFILE");
			trans.addToBackStack(null);
			trans.commit();
			return fragment;
		}
		else if (id == FRAG_FRIENDS) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAG_FRIENDS");
			if (fragment == null) {
				Log.e("Hi", "Created new friends frag");
				fragment =  new FriendsFragment();
			}
			FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
			trans.replace(R.id.main_container, fragment, "FRAG_FRIENDS");
			trans.addToBackStack(null);
			trans.commit();
			// Close search box
			FrameLayout layout = (FrameLayout) MainActivity.this
					.findViewById(R.id.main_container);
			layout.requestFocus();
			return fragment;
		}
		else if (id == FRAG_SETTINGS) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAG_SETTINGS");
			if (fragment == null) {
				Log.e("Hi", "Created new settings frag");
				fragment =  new SettingsFragment();
			}
			FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
			trans.replace(R.id.main_container, fragment, "FRAG_SETTINGS");
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
		case android.R.id.home:
			switchFragments(FRAG_PROFILE);
			break;
		case R.id.menu_search:
			switchFragments(FRAG_FRIENDS);
			SearchView v = (SearchView) findViewById(R.id.friends_search);
			v.requestFocus();
			break;
		case R.id.menu_camera:
			//mViewPager.setCurrentItem(0);
			QRCodeManager.scanQRCode(this);
			break;
		case R.id.menu_refresh:
			break;
		case R.id.action_settings:
			switchFragments(FRAG_SETTINGS);
			break;
		}
		return true;
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		//mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{  
		qrcode = QRCodeManager.checkScanActivityResult(requestCode, resultCode, data);
		if (qrcode != null) {
			Toast.makeText(this, "Scan Result = " + qrcode, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Log.d("Frag", "" +position);
			if (position == 0) {
				Fragment fragment = new ProfileFragment();
				return fragment;
			}
			else if (position == 1) {
				Fragment fragment = new FriendsFragment();
				return fragment;
			}
			else if (position == 2) {
				Fragment fragment = new SettingsFragment();
				return fragment;
			}
			else{
				return null;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_profile).toUpperCase(l);
			case 1:
				return getString(R.string.title_friends).toUpperCase(l);
			case 2:
				return getString(R.string.title_settings).toUpperCase(l);
			}
			return null;
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
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<ServerNotifications>> loader) {
		// TODO Auto-generated method stub
		
	}

}
