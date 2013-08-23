package com.qardapp.qard.comm.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.AsyncTask;

import com.qardapp.qard.MainActivity;


public abstract class ServerTask extends AsyncTask<String, Void, String>{

	private Context context;
	protected String url;
	
	public ServerTask(Context context, String url) {
		super();
		this.context = context;
		this.url = ServerHelper.SERVER_URL + url;
	}
	
	private String retrieveNewUser() {
		ServerHelper.resetUser(context);
		ServerHelper.getNewUserToken(context);
		return ServerHelper.getAccessToken(context);
	}
	
	protected void makePost(JSONObject holder) throws Exception {
		String token = ServerHelper.getAccessToken(context);
		if (token == null) {
			token = retrieveNewUser();
			if (token == null)
				return;
		}
		holder.put("access_token", token);
		
		HttpClient httpClient = new DefaultHttpClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setEntity(new StringEntity(holder.toString()));
		HttpResponse response = httpClient.execute(httpPost);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), "UTF-8"));
		String json = reader.readLine();
		if (json.equals("Unauthorized")) {
			retrieveNewUser();
			return;
		}
		JSONTokener tokener = new JSONTokener(json);
		JSONObject finalResult = new JSONObject(tokener);
		onServerResponse(finalResult);
	}
	
	protected void makeGet(List<NameValuePair> params) throws Exception {
		String token = ServerHelper.getAccessToken(context);
		if (token == null) {
			token = retrieveNewUser();
			if (token == null)
				return;
		}
	    params.add(new BasicNameValuePair("access_token", token));

		HttpClient httpClient = new DefaultHttpClient();
	    String paramString = URLEncodedUtils.format(params, "utf-8");
		HttpPost httpGet = new HttpPost(url+"?"+paramString);
		httpGet.setHeader("Accept", "application/json");

		HttpResponse response = httpClient.execute(httpGet);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), "UTF-8"));
		String json = reader.readLine();
		if (json.equals("Unauthorized")) {
			retrieveNewUser();
			return;
		}
		JSONTokener tokener = new JSONTokener(json);
		JSONObject finalResult = new JSONObject(tokener);
		onServerResponse(finalResult);
	}
	
	protected abstract void onServerResponse(JSONObject response) ;
	
	@Override
	protected void onPostExecute(String result) {
		if (context instanceof MainActivity)
			((MainActivity) context).getSupportLoaderManager().restartLoader(MainActivity.REFRESH_LOADER_ID, null, ((MainActivity) context));
	}

}
