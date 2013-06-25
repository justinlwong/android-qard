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

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

public class AddFriendTask extends AsyncTask<String, Void, String>{

	private Context context;
	private static String ADD_FRIEND_URL = ServerHelper.SERVER_URL + "/user/friend";
	
	private String friend_id;
	private String first_name;
	private String last_name;
	private String number;
	
	public AddFriendTask(Context context, String friend_id, String first_name, String last_name, String number) {
		super();
		this.context = context;
		this.friend_id = friend_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.number = number;
	}
	
	
	
	@Override
	protected String doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(ADD_FRIEND_URL);
        
		JSONObject holder = new JSONObject();
		try {
			holder.put("friend_id", friend_id);
			holder.put("access_token", ServerHelper.getAccessToken(context));
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(new StringEntity(holder.toString()));
			HttpResponse response = httpClient.execute(httpPost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();
			if (json.equals("Unauthorized")) {
				ServerHelper.resetUser(context);
				return null;
			}
			JSONTokener tokener = new JSONTokener(json);
			JSONObject finalResult = new JSONObject(tokener);
			
			ContentResolver resolver = context.getContentResolver();
			String where = FriendsDatabaseHelper.TABLE_FRIENDS + "."+FriendsDatabaseHelper.COLUMN_USER_ID + "=?";
			String[] args = new String[] {friend_id};
			Cursor cur = resolver.query(FriendsProvider.CONTENT_URI, null, where, args, null);
			long local_friend_id = 0;
			if (cur.getCount() == 0 ) {
				ContentValues values = new ContentValues();
				values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, first_name);
				values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, last_name);
				values.put(FriendsDatabaseHelper.COLUMN_USER_ID, friend_id);	
				values.put(FriendsDatabaseHelper.COLUMN_CONFIRMED, true);	
				Uri uri = resolver.insert(FriendsProvider.CONTENT_URI, values);
				local_friend_id = Long.parseLong(uri.getLastPathSegment());
			} else {
				cur.moveToFirst();
				local_friend_id = cur.getLong(cur.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));
				ContentValues values = new ContentValues();
				values.put(FriendsDatabaseHelper.COLUMN_CONFIRMED, true);	
				resolver.update(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/" + local_friend_id), values, null, null);
			}
			cur.close();
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
