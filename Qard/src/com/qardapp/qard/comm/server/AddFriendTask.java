package com.qardapp.qard.comm.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class AddFriendTask extends ServerTask{

	private Context context;
	private static String ADD_FRIEND_URL = ServerHelper.SERVER_URL + "/user/friend";
	
	private String friend_id;
	private String first_name;
	private String last_name;
	private String number;
	private int local_friend_id;
	
	public AddFriendTask(Context context, String friend_id, String first_name, String last_name, String number) {
		super(context, ADD_FRIEND_URL);
		this.context = context;
		this.friend_id = friend_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.number = number;
	}
	
	public AddFriendTask(Context context, String friend_id) {
		super(context, ADD_FRIEND_URL);
		this.context = context;
		this.friend_id = friend_id;
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		try {
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
				values.put(FriendsDatabaseHelper.COLUMN_FRIEND_SERVER_QUEUED, true);
				Uri uri = resolver.insert(FriendsProvider.CONTENT_URI, values);
				local_friend_id = Integer.parseInt(uri.getLastPathSegment());
			} else {
				cur.moveToFirst();
				local_friend_id = cur.getInt(cur.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));
				ContentValues values = new ContentValues();
				values.put(FriendsDatabaseHelper.COLUMN_CONFIRMED, true);
				values.put(FriendsDatabaseHelper.COLUMN_FRIEND_SERVER_QUEUED, true);
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
				resolver.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/" + local_friend_id + "/service/"+Services.PHONE.id), where, args);
				resolver.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/"+ local_friend_id + "/service/"+Services.PHONE.id), values);
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
			if (response.has("id")) {
				ContentResolver resolver = context.getContentResolver();
				ContentValues values = new ContentValues();
				values.put(FriendsDatabaseHelper.COLUMN_FRIEND_SERVER_QUEUED, false);	
				resolver.update(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/" + local_friend_id), values, null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
