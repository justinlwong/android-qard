package com.qardapp.qard.comm.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.util.ImageUtil;

public class FriendsInfoLoader extends AsyncTaskLoader<ArrayList<ServerNotifications>> {

	private Context context;
	private static String FRIENDS_INFO_URL = ServerHelper.SERVER_URL + "/user/info";
	
	public FriendsInfoLoader(Context context) {
		super(context);
		this.context = context;
	}
	
	@Override
	public ArrayList<ServerNotifications> loadInBackground() {
		HttpClient httpClient = new DefaultHttpClient();
		String token = ServerHelper.getAccessToken(context);
		if (token == null)
			return null;
		// Creating HTTP Post
		HttpGet httpGet = new HttpGet(FRIENDS_INFO_URL + "?access_token="+token);

		JSONObject holder = new JSONObject();
		try {
			
			httpGet.setHeader("Accept", "application/json");
			HttpResponse response = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();
			if (json.equals("Unauthorized") || json.equals("error: relation \"oauth_accesstokens\" does not exist")) {
				ServerHelper.resetUser(context);
				return null;
			}
			JSONTokener tokener = new JSONTokener(json);
			JSONObject finalResult = new JSONObject(tokener);
			if (finalResult.has("error")) {
				Log.d("server", finalResult.getString("error"));
				return null;
			}
			int count = finalResult.getInt("num_rows");
			if (count > 0) {
				JSONArray info = finalResult.getJSONArray("info");
				for (int x = 0; x < info.length(); x++) {
					JSONObject obj = info.getJSONObject(x);
					String service_id = obj.getString("service_id");
					final String user_id = obj.getString("friend_id");
					final String data = obj.getString("service_data");

					
					ContentResolver resolver = context.getContentResolver();
					String where = FriendsDatabaseHelper.COLUMN_USER_ID + "=?";
					String[] args = new String[] {user_id};
					Cursor cur = resolver.query(FriendsProvider.CONTENT_URI, null, where, args, null);
					final int friend_id;
					String first_name = obj.getString("first_name");
					String last_name = obj.getString("last_name");
					if (cur.getCount() == 0 ) {

						ContentValues values = new ContentValues();
						values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, first_name);
						values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, last_name);
						values.put(FriendsDatabaseHelper.COLUMN_USER_ID, user_id);	
						Uri uri = resolver.insert(FriendsProvider.CONTENT_URI, values);
						friend_id = Integer.parseInt(uri.getLastPathSegment());
					} else {
						cur.moveToFirst();
						friend_id = cur.getInt(cur.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));
						// Update names
						ContentValues values = new ContentValues();
						values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, first_name);
						values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, last_name);
						if (obj.has("old_friend_id") && (!obj.isNull("old_friend_id")))
							values.put(FriendsDatabaseHelper.COLUMN_USER_ID, obj.getInt("old_friend_id"));
						resolver.update(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/" + friend_id), values, null, null);
					}
					// If service is facebook, grab picture
					if (Integer.parseInt(service_id) == Services.FACEBOOK.id)
					{
		        		Thread thread = new Thread(new Runnable(){
		        		    @Override
		        		    public void run() {
		        		        ImageUtil.getFBProfilePic(context, data, friend_id);
		        		    }
		        		});
		        		thread.start();
					}
					cur.close();
					if (service_id != null && !(service_id.equals("null"))) {
						ContentValues values = new ContentValues();
						values.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, friend_id);
						values.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, service_id);
						values.put(FriendsDatabaseHelper.COLUMN_FS_DATA, data);	
						where = FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=? AND " + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
						args = new String[] { friend_id +"", service_id};
						resolver.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/"+ friend_id+"/service/"+service_id), where, args);
						resolver.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/"+ friend_id+"/service/"+service_id), values);
					}
				}
			}
			Log.e("HI", finalResult.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<ServerNotifications>();
	}

	
	@Override
	protected void onStartLoading() {
	  /*if(dataIsReady) {
	    deliverResult(data);
	  } else {*/
	    forceLoad();
	  //}
	}
}
