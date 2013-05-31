package com.qardapp.qard;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.friends.FriendsCursorAdapter;
import com.qardapp.qard.friends.profile.FriendsProfileCursorAdapter;
import com.qardapp.qard.profile.ProfileServicesAdapter;
import com.qardapp.qard.qrcode.QRCodeManager;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

public class FriendProfileActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_profile);

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
		  actionBar.setDisplayHomeAsUpEnabled(true);
		    actionBar.setHomeButtonEnabled(true);
		    actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		
		
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			String id = extra.getString("id");
			if (id != null) {
				Cursor cursor = null;
				ContentResolver res = getContentResolver();
				cursor = res.query(
						Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/"
								+ id), null, null, null, null);
				TextView view = (TextView) findViewById(R.id.friend_profile_name);
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					String first_name = cursor
							.getString(cursor
									.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
					String last_name = cursor
							.getString(cursor
									.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
					view.setText(first_name + " " + last_name);
				}
				GridView gridView = (GridView) findViewById(R.id.friend_profile_gridview);
				gridView.setAdapter(new FriendsProfileCursorAdapter(
						this,
						cursor,
						FriendsProfileCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		case R.id.menu_search:
			Intent intent = this.getIntent();
			intent.putExtra("launch", "search");
			this.setResult(RESULT_OK, intent);
			break;
		case R.id.menu_camera:
			//mViewPager.setCurrentItem(0);
			QRCodeManager.scanQRCode(this);
			break;
		case R.id.menu_refresh:
			break;
		case R.id.action_settings:
			//mViewPager.setCurrentItem(2);
			break;
		}
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		overridePendingTransition(R.animator.slide_in,  R.animator.appear);
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition( R.animator.appear, R.animator.slide_out);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
