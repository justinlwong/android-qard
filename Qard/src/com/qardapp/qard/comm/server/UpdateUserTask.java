package com.qardapp.qard.comm.server;

import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class UpdateUserTask extends ServerTask {

	private Context context;
	private static String UPDATE_USER_URL = "/user";
	private String first_name, last_name, username, password;

	public UpdateUserTask(Context context, String first_name, String last_name, String username, String password) {
		super(context, UPDATE_USER_URL, FriendsDatabaseHelper.QUEUED_UPDATE_PROFILE);
		this.context = context;
		this.first_name = first_name;
		this.last_name = last_name;
		this.username = username;
		this.password = password;
	}

	public UpdateUserTask(Context context, String first_name, String last_name) {
		this(context, first_name, last_name, null, null);
	}
	
	public UpdateUserTask (Context context, int queue_id) {
		super(context, UPDATE_USER_URL, FriendsDatabaseHelper.QUEUED_UPDATE_PROFILE, queue_id);
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		try {
			JSONObject holder = new JSONObject();
			holder.put("first_name", first_name);
			holder.put("last_name", last_name);
			if (username != null) {
				holder.put("username", username);
				holder.put("password", password);
			}
			makePost(holder);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		((MainActivity)context).refreshFragments();
	}

	@Override
	protected void onServerResponse(JSONObject response) {
		try {
			
			if (response.has("id")) {
				String id = response.getString("id");
				ContentValues values = new ContentValues();
				values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, first_name);
				values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, last_name);
				values.put(FriendsDatabaseHelper.COLUMN_USER_ID, id);
				ContentResolver resolver = context.getContentResolver();
				resolver.update(FriendsProvider.MY_URI, values, null, null);
				if (username != null)
					ServerHelper.setUserName(context, response.getString("username"));
			}
		} catch (Exception e) {
			
		}
	}
}
