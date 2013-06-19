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
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

public class AddFriendTask extends AsyncTask<String, Void, String>{

	private Context context;
	private static String ADD_FRIEND_URL = ServerHelper.SERVER_URL + "/user/friend";
	
	private String friend_id;
	
	public AddFriendTask(Context context, String friend_id) {
		super();
		this.context = context;
		this.friend_id = friend_id;
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
