package com.qardapp.qard.comm.server;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class GetFriendsTask extends ServerTask{

	private Context context;
	private static String FRIENDS_INFO_URL = "/user/friends";
	
	public GetFriendsTask(Context context) {
		super(context, FRIENDS_INFO_URL);
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			makeGet(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onServerResponse(JSONObject response) {
		try {
			int count = response.getInt("num_rows");
			if (count > 0) {
				JSONArray friends = response.getJSONArray("friends");
				for (int x = 0; x < friends.length(); x++) {
					JSONObject obj = friends.getJSONObject(x);
					final String user_id = obj.getString("friend_id");
					String first_name = obj.getString("first_name");
					String last_name = obj.getString("last_name");
					
					
					ContentResolver resolver = context.getContentResolver();
					String where = FriendsDatabaseHelper.COLUMN_USER_ID + "=?";
					String[] args = new String[] {user_id};
					Cursor cur = resolver.query(FriendsProvider.CONTENT_URI, null, where, args, null);
					final int local_id;

					if (cur.getCount() == 0 ) {

						ContentValues values = new ContentValues();
						values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, first_name);
						values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, last_name);
						if (obj.has("new_friend_id") && !(obj.isNull("new_friend_id")))
							values.put(FriendsDatabaseHelper.COLUMN_USER_ID, obj.getInt("new_friend_id"));
						else
							values.put(FriendsDatabaseHelper.COLUMN_USER_ID, user_id);	
						Uri uri = resolver.insert(FriendsProvider.CONTENT_URI, values);
						local_id = Integer.parseInt(uri.getLastPathSegment());
					} else {
						cur.moveToFirst();
						local_id = cur.getInt(cur.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));
						// Update names
						ContentValues values = new ContentValues();
						values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, first_name);
						values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, last_name);
						if (obj.has("new_friend_id") && !(obj.isNull("new_friend_id")))
							values.put(FriendsDatabaseHelper.COLUMN_USER_ID, obj.getInt("new_friend_id"));
						else
							values.put(FriendsDatabaseHelper.COLUMN_USER_ID, user_id);	
						resolver.update(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, ""+ local_id), values, null, null);
					}
				}
			}
			Log.e("Server", response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
