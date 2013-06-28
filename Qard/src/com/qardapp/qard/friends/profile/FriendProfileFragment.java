package com.qardapp.qard.friends.profile;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.AddFriendTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;


public class FriendProfileFragment extends BaseFragment{
	
	private long friend_id;
	private long friend_qard_id;
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
	public void updateViews() {
		if (friend_id != -1) {
			Cursor cursor = null;
			ContentResolver res = getActivity().getContentResolver();
			cursor = res.query(
					Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/"
							+ friend_id), null, null, null, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				TextView view = (TextView) getView().findViewById(R.id.friend_profile_name);
				friend_qard_id = cursor
						.getLong(cursor
								.getColumnIndex(FriendsDatabaseHelper.COLUMN_USER_ID));
				String first_name = cursor
						.getString(cursor
								.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
				String last_name = cursor
						.getString(cursor
								.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
				view.setText(first_name + " " + last_name);
	    		LinearLayout statusView = (LinearLayout) getView().findViewById(R.id.friend_profile_status_view);
	    		statusView.removeAllViews();
		    	if (cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_CONFIRMED)) == 0) {
		    		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    		LinearLayout v = (LinearLayout)inflater.inflate(R.layout.friends_profile_status, statusView);

		    		ImageView statusIcon = (ImageView) v.findViewById(R.id.friends_profile_status_image);
		    		statusIcon.setImageResource(R.drawable.icon_exclamation_red);
		    		
		    		Button button = (Button) v.findViewById(R.id.friends_profile_status_ok);
		    		button.setText("Click to confirm friendship");
		    		button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							AddFriendTask task = new AddFriendTask(getActivity(), friend_qard_id+"");
							task.execute();
						}
					});
		    		ImageButton cancelButton = (ImageButton) v.findViewById(R.id.friends_profile_status_cancel);
		    		cancelButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ContentValues values = new ContentValues();
							values.put(FriendsDatabaseHelper.COLUMN_HIDE_CONFIRMED, true);
							ContentResolver res = getActivity().getContentResolver();
							res.update(
									Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/"
											+ friend_id), values, null, null);
						}
					});
		    		//statusView.addView(v);
		    	}
				
				do {
					int val = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_SERVICE_ID));
					if (val == Services.PHONE.id) {
						String number = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
						if (number != null) {
							TextView phoneView = (TextView) getView().findViewById(R.id.friend_profile_phone);
							phoneView.setText(PhoneNumberUtils.formatNumber(number));
						}
					}
				} while (cursor.moveToNext());
				
			}
			adapter.changeCursor(cursor);
		}
		
	}
}
