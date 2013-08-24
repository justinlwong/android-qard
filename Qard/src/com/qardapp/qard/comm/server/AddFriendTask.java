package com.qardapp.qard.comm.server;

import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class AddFriendTask extends ServerTask{

	private Context context;
	private static String ADD_FRIEND_URL = "/user/friend";
	
	private String friend_id;
	private String first_name;
	private String last_name;
	private String number;
	private int local_friend_id;
	
	public AddFriendTask(Context context, String friend_id, String first_name, String last_name, String number) {
		super(context, ADD_FRIEND_URL, FriendsDatabaseHelper.QUEUED_ADD_FRIEND);
		this.context = context;
		this.friend_id = friend_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.number = number;
	}
	
	public AddFriendTask(Context context, String friend_id) {
		super(context, ADD_FRIEND_URL, FriendsDatabaseHelper.QUEUED_ADD_FRIEND);
		this.context = context;
		this.friend_id = friend_id;
	}
	
	public AddFriendTask(Context context, int queue_id) {
		super(context, ADD_FRIEND_URL, FriendsDatabaseHelper.QUEUED_ADD_FRIEND, queue_id);
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			if (queued_id != -1) {
				makeQueuedPost();
				return null;
			}
			// Don't add self
			if (ServerHelper.getUserId(context) == friend_id) {
				return null;
			}
			ContentResolver resolver = context.getContentResolver();
			String where = FriendsDatabaseHelper.TABLE_FRIENDS + "."+FriendsDatabaseHelper.COLUMN_USER_ID + "=?";
			String[] args = new String[] {friend_id};
			Cursor cur = resolver.query(FriendsProvider.CONTENT_URI, null, where, args, null);
			local_friend_id = 0;
			if (cur.getCount() == 0 ) {
				ContentValues values = new ContentValues();
				if (first_name !=null)
					values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, first_name);
				if (last_name != null)
					values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, last_name);
				values.put(FriendsDatabaseHelper.COLUMN_USER_ID, friend_id);	
				values.put(FriendsDatabaseHelper.COLUMN_CONFIRMED, true);
				Uri uri = resolver.insert(FriendsProvider.CONTENT_URI, values);
				local_friend_id = Integer.parseInt(uri.getLastPathSegment());
			} else {
				cur.moveToFirst();
				local_friend_id = cur.getInt(cur.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));
				ContentValues values = new ContentValues();
				values.put(FriendsDatabaseHelper.COLUMN_CONFIRMED, true);
				resolver.update(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/" + local_friend_id), values, null, null);
			}
			// Add their number
			if (number != null) {
				ContentValues values = new ContentValues();
				values.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, local_friend_id);
				values.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, Services.PHONE.id);
				values.put(FriendsDatabaseHelper.COLUMN_FS_DATA, number);
				where = FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=? AND " + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
				args = new String[] { local_friend_id +"", String.valueOf(Services.PHONE.id)};
				resolver.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, local_friend_id + "/service/"+Services.PHONE.id), where, args);
				resolver.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, local_friend_id + "/service/"+Services.PHONE.id), values);
			}
			
			JSONObject holder = new JSONObject();
			holder.put("friend_id", friend_id);
			makePost(holder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onServerResponse(JSONObject response) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
