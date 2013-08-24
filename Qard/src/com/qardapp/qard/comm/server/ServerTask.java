package com.qardapp.qard.comm.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;


public abstract class ServerTask extends AsyncTask<String, Void, String>{

	private Context context;
	protected String url;
	private int type = FriendsDatabaseHelper.QUEUED_NONE;
	protected int queued_id = -1;
	
	public ServerTask(Context context, String url) {
		this(context, url, FriendsDatabaseHelper.QUEUED_NONE, -1);
	}
	public ServerTask(Context context, String url, int type) {
		this(context, url, type, -1);
	}

	public ServerTask(Context context, String url, int type, int queued_id) {
		super();
		this.context = context;
		this.url = ServerHelper.SERVER_URL + url;
		this.type = type;
		this.queued_id = queued_id;
	}
	
	private String retrieveNewUser() {
		ServerHelper.resetUser(context);
		ServerHelper.getNewUserToken(context);
		return ServerHelper.getAccessToken(context);
	}
	
	protected void addToQueue(String data) {
		if (queued_id == -1 && type != FriendsDatabaseHelper.QUEUED_NONE) {
			ContentResolver resolver = context.getContentResolver();
			ContentValues values = new ContentValues();
			values.put(FriendsDatabaseHelper.COLUMN_QM_TYPE, type);	
			values.put(FriendsDatabaseHelper.COLUMN_QM_DATA, data);	
			values.put(FriendsDatabaseHelper.COLUMN_QM_TIME, new Date().getTime());	
			Uri uri = resolver.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "queue" ), values);
			queued_id = Integer.parseInt(uri.getLastPathSegment());
		}
	}
	
	protected String retrieveQueue() {
		ContentResolver resolver = context.getContentResolver();
		String where = FriendsDatabaseHelper.TABLE_QUEUED_MSG + "."+FriendsDatabaseHelper.COLUMN_QM_ID + "=?";
		String[] args = new String[] {queued_id+""};
		Cursor cursor = resolver.query(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "queue") , null, where, args, null);
		if (cursor.moveToFirst())
			return cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_QM_DATA));
		return "";
	}
	
	protected void removeFromQueue() {
		if (queued_id != -1) {
			ContentResolver resolver = context.getContentResolver();
			String where = FriendsDatabaseHelper.TABLE_QUEUED_MSG + "."+FriendsDatabaseHelper.COLUMN_QM_ID + "=?";
			String[] args = new String[] {queued_id+""};
			resolver.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "queue" ), where, args);
		}
	}
	
	protected void handleResponse(HttpResponse response) throws Exception  {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), "UTF-8"));
		String json = reader.readLine();
		if (json.equals("Unauthorized") || json.equals("error: relation \"oauth_accesstokens\" does not exist")) {
			retrieveNewUser();
			return;
		}
		JSONTokener tokener = new JSONTokener(json);
		JSONObject finalResult = new JSONObject(tokener);
		if (finalResult.has("error")) {
			onErrorResponse(finalResult);
			return;
		}
		removeFromQueue();
		onServerResponse(finalResult);
	}
	
	private HttpClient getClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 5000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 8000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		httpClient.setParams(httpParameters);
		return httpClient;
	}
	
	private String getToken() {
		String token = ServerHelper.getAccessToken(context);
		if (token == null) {
			token = retrieveNewUser();
			if (token == null)
				return null;
		}
		return token;
	}
	
	protected void makeQueuedPost() throws Exception {
		String token = getToken();
		if (token == null) {
				return;
		}
		if (queued_id == -1)
			return;
		String data = retrieveQueue();
		
		JSONObject holder = new JSONObject(data);
		holder.put("access_token", token);
		
		HttpClient httpClient = getClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setEntity(new StringEntity(holder.toString()));
		HttpResponse response = httpClient.execute(httpPost);
		handleResponse(response);
	}
	
	protected void makePost(JSONObject holder) throws Exception {
		String token = getToken();
		if (token == null) {
				return;
		}
		addToQueue(holder.toString());
		
		holder.put("access_token", token);
		
		HttpClient httpClient = getClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setEntity(new StringEntity(holder.toString()));
		HttpResponse response = httpClient.execute(httpPost);
		handleResponse(response);
	}
	
	protected void makeQueuedGet() throws Exception {
		if (queued_id == -1)
			return;
		String data = retrieveQueue();
		makeGetBase(data);
	}
	
	// Queue up
	protected void makeGet(List<NameValuePair> params) throws Exception {
		String paramString = "";
		if (params != null)
		    paramString = "&" + URLEncodedUtils.format(params, "utf-8");
		addToQueue(paramString);
		makeGetBase(paramString);
	}
	
	// No queuing
	protected void makeGetBase(String paramString) throws Exception {
		String token = getToken();
		if (token == null) {
				return;
		}
		HttpClient httpClient = getClient();
		HttpGet httpGet = new HttpGet(url + "?access_token="+token + paramString);
		httpGet.setHeader("Accept", "application/json");
		HttpResponse response = httpClient.execute(httpGet);
		handleResponse(response);
		
	}
	
	protected abstract void onServerResponse(JSONObject response) ;
	
	protected void onErrorResponse(JSONObject response) throws JSONException {
		Log.d("server", response.getString("error"));
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (context instanceof MainActivity)
			((MainActivity) context).refreshFragments();
	}

}
