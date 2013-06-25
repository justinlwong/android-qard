package com.qardapp.qard.friends;

import com.qardapp.qard.R;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.friends.profile.FriendsProfileCursorAdapter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;


public class FriendProfileFragment extends Fragment{
	
	private long friend_id;
	private FriendsProfileCursorAdapter adapter;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.friends_profile,
				container, false);
		GridView gridView = (GridView) rootView.findViewById(R.id.friend_profile_gridview);
		adapter = new FriendsProfileCursorAdapter(
				this.getActivity(),
				null,
				FriendsProfileCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		gridView.setAdapter(adapter);
		
		return rootView;
	}
	
	public void setId (long id) {
		friend_id = id;
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (friend_id != -1) {
			Cursor cursor = null;
			ContentResolver res = getActivity().getContentResolver();
			cursor = res.query(
					Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/"
							+ friend_id), null, null, null, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				TextView view = (TextView) getView().findViewById(R.id.friend_profile_name);

				String first_name = cursor
						.getString(cursor
								.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
				String last_name = cursor
						.getString(cursor
								.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
				view.setText(first_name + " " + last_name);
			}
			adapter.changeCursor(cursor);
		}
		
	}
}
