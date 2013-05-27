package com.qardapp.qard;

import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.friends.FriendsCursorAdapter;
import com.qardapp.qard.friends.profile.FriendsProfileCursorAdapter;
import com.qardapp.qard.profile.ProfileServicesAdapter;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.view.Menu;
import android.widget.GridView;
import android.widget.TextView;

public class FriendProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_profile);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_profile, menu);
		return true;
	}

}
