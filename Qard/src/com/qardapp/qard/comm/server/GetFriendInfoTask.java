package com.qardapp.qard.comm.server;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.util.ImageUtil;

public class GetFriendInfoTask extends ServerTask{

	private Context context;
	private static String FRIENDS_INFO_URL = "/user/friend";
	private int local_id;
	private int user_id;
	
	public GetFriendInfoTask(Context context, int local_id) {
		super(context, FRIENDS_INFO_URL);
		this.context = context;
		this.local_id = local_id;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			ContentResolver resolver = context.getContentResolver();
			Cursor cur = resolver.query(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, ""+ local_id), null, null, null, null);
			cur.moveToFirst();
			user_id = cur.getInt(cur.getColumnIndex(FriendsDatabaseHelper.COLUMN_USER_ID));
			url = url + "/" + user_id;
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
				JSONArray friends = response.getJSONArray("friend");
				for (int x = 0; x < friends.length(); x++) {
					JSONObject obj = friends.getJSONObject(x);
					final String user_id = obj.getString("friend_id");
					String first_name = obj.getString("first_name");
					String last_name = obj.getString("last_name");
					String service_id = obj.getString("service_id");
					final String data = obj.getString("service_data");
					
					ContentResolver resolver = context.getContentResolver();
					String where = FriendsDatabaseHelper.COLUMN_USER_ID + "=?";
					String[] args = new String[] {user_id};
					Cursor cur = resolver.query(FriendsProvider.CONTENT_URI, null, where, args, null);
					final int local_id;

					if (cur.getCount() == 0 ) {

						return;
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
					// If service is facebook, grab picture
					if (service_id != null && !(service_id.equals("null")) && Integer.parseInt(service_id) == Services.FACEBOOK.id)
					{
		        		Thread thread = new Thread(new Runnable(){
		        		    @Override
		        		    public void run() {
		        		        ImageUtil.getFBProfilePic(context, data, local_id);
		        		    }
		        		});
		        		thread.start();
					}
					cur.close();
					if (service_id != null && !(service_id.equals("null"))) {
						ContentValues values = new ContentValues();
						values.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, local_id);
						values.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, service_id);
						values.put(FriendsDatabaseHelper.COLUMN_FS_DATA, data);	
						where = FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=? AND " + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
						args = new String[] { local_id +"", service_id};
						resolver.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, ""+ local_id+"/service/"+service_id), where, args);
						resolver.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, ""+ local_id+"/service/"+service_id), values);
					}
				}
			}
			Log.e("Server", response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
