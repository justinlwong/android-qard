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

import android.content.Context;
import android.os.AsyncTask;

public class AddServiceTask extends AsyncTask<String, Void, String>{

	private Context context;
	private static String ADD_SERVICE_URL = ServerHelper.SERVER_URL + "/user/user_services";
	
	private int service_id;
	private String data;
	private String token;
	
	public AddServiceTask(Context context, int service_id, String data) {
		this(context, service_id, data, null);
	}
	
	public AddServiceTask(Context context, int service_id, String data, String token) {
		super();
		this.context = context;
		this.service_id = service_id;
		this.data = data;
		this.token = token;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(ADD_SERVICE_URL);
        
		JSONObject holder = new JSONObject();
		try {
			holder.put("service_id", service_id);
			holder.put("data", data);
			holder.put("access_token", ServerHelper.getAccessToken(context));
			if (token != null) {
				holder.put("token", token);
				holder.put("client_id", ServerHelper.CLIENT_ID);
				holder.put("client_secret", ServerHelper.CLIENT_SEC);
			}
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
			if (token != null && finalResult.has("user_id")) {
				String new_id = finalResult.getString("user_id");
				String access_token = finalResult.getString("access_token");
				ServerHelper.setNewUser(context, new_id, access_token);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
