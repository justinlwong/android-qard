package com.qardapp.qard.comm.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class NewUserLoader extends
		AsyncTaskLoader<ArrayList<ServerNotifications>> {

	private Context context;
	private static String NEW_USER_URL = ServerHelper.SERVER_URL + "/new_user";

	public NewUserLoader(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public ArrayList<ServerNotifications> loadInBackground() {

		HttpClient httpClient = new DefaultHttpClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(NEW_USER_URL);

		JSONObject holder = new JSONObject();
		try {
			holder.put("first_name", "First");
			holder.put("last_name", "Last");
			holder.put("client_id", "Android01Lo9");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(new StringEntity(holder.toString()));
			HttpResponse response = httpClient.execute(httpPost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();
			JSONTokener tokener = new JSONTokener(json);
			JSONObject finalResult = new JSONObject(tokener);
			String id = finalResult.getString("id");
			String token = finalResult.getString("access_token");
			ServerHelper.setNewUser(context, id, token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<ServerNotifications>();
	}

	@Override
	protected void onStartLoading() {
		String token = ServerHelper.getAccessToken(context);
		if (token == null) {
			forceLoad();
		}
	}
}
