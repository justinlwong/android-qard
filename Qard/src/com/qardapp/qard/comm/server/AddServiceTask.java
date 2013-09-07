package com.qardapp.qard.comm.server;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.util.ImageUtil;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class AddServiceTask extends ServerTask{

	private Context context;
	private static String ADD_SERVICE_URL = "/user/user_services";
	
	private int service_id;
	private String data;
	private String token;
	
	public AddServiceTask(Context context, int service_id, String data) {
		this(context, service_id, data, null);
	}
	
	public AddServiceTask(Context context, int service_id, String data, String token) {
		super(context, ADD_SERVICE_URL, FriendsDatabaseHelper.QUEUED_UPDATE_SERVICES);
		this.context = context;
		this.service_id = service_id;
		this.data = data;
		this.token = token;
	}
	
	public AddServiceTask(Context context, int queued_id) {
		super(context, ADD_SERVICE_URL, FriendsDatabaseHelper.QUEUED_UPDATE_SERVICES, queued_id);
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			if (queued_id != -1) {
				makeQueuedPost();
				return null;
			}
			JSONObject holder = new JSONObject();
			holder.put("service_id", service_id);
			holder.put("data", data);
			if (token != null) {
				holder.put("token", token);
				holder.put("client_id", ServerHelper.CLIENT_ID);
				holder.put("client_secret", ServerHelper.CLIENT_SEC);
			}
			makePost(holder);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onServerResponse(JSONObject response) {
		try {
			if (token != null && response.has("user_id")) {
				String new_id = response.getString("user_id");
				String access_token = response.getString("access_token");
				JSONArray services = response.getJSONArray("services");
				ContentResolver resolver = context.getContentResolver();
				for (int i = 0; i < services.length(); i++) {
					JSONObject service = services.getJSONObject(i);
					
					ContentValues values = new ContentValues();
					values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, service.getString("first_name"));
					values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, service.getString("last_name"));
					values.put(FriendsDatabaseHelper.COLUMN_USER_ID, service.getInt("id"));	
					resolver.update(FriendsProvider.MY_URI, values, null, null);

					values = new ContentValues();
					values.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, 0);
					values.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, service.getInt("service_id"));
					values.put(FriendsDatabaseHelper.COLUMN_FS_DATA, service.getString("data"));	
					String where = FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=? AND " + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
					String[] args = new String[] { 0 +"", "" + service.getInt("service_id")};
					resolver.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "0/service/"+service_id), where, args);
					resolver.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "0/service/"+service_id), values);
					
					// If service is facebook, grab picture
					if (service.getInt("service_id") == Services.FACEBOOK.id)
					{
		        		Thread thread = new Thread(new Runnable(){
		        		    @Override
		        		    public void run() {
		        		        ImageUtil.getFBProfilePic(context, data, 0);
		        		    }
		        		});
		        		thread.start();
					}

				}
				ServerHelper.setNewUser(context, new_id, access_token);
			}
		} catch (Exception e) {
			
		}
	}

}
