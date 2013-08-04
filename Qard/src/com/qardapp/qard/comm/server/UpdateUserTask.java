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
import android.os.AsyncTask;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class UpdateUserTask extends
		AsyncTask<String, Void, String> {

	private Context context;
	private static String UPDATE_USER_URL = ServerHelper.SERVER_URL + "/user";
	private String first_name, last_name, username, password;

	public UpdateUserTask(Context context, String first_name, String last_name, String username, String password) {
		super();
		this.context = context;
		this.first_name = first_name;
		this.last_name = last_name;
		this.username = username;
		this.password = password;
	}

	public UpdateUserTask(Context context, String first_name, String last_name) {
		this(context, first_name, last_name, null, null);
	}
	
	@Override
	protected String doInBackground(String... params) {
		if (ServerHelper.getAccessToken(context) == null) {
			return null;
		}
		HttpClient httpClient = new DefaultHttpClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(UPDATE_USER_URL);
        
		JSONObject holder = new JSONObject();
		try {
			holder.put("first_name", first_name);
			holder.put("last_name", last_name);
			if (username != null) {
				holder.put("username", username);
				holder.put("password", password);
			}
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
			String id = finalResult.getString("id");
			if (id != null) {
				ContentValues values = new ContentValues();
				values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, first_name);
				values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, last_name);
				values.put(FriendsDatabaseHelper.COLUMN_USER_ID, id);
				ContentResolver resolver = context.getContentResolver();
				resolver.update(FriendsProvider.MY_URI, values, null, null);
				if (username != null)
					ServerHelper.setUserName(context, finalResult.getString("username"));
			}
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
}
