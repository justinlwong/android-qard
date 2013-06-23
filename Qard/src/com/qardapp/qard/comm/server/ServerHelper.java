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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.qardapp.qard.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class ServerHelper extends Fragment{

	//public static String SERVER_URL = "https://qard-server.herokuapp.com";
	public static String SERVER_URL = "http://10.0.1.14:5000";
	private static String NEW_USER_URL = ServerHelper.SERVER_URL + "/new_user";
	public  static String CLIENT_ID = "Android01Lo9";
	public static String CLIENT_SEC = "GooAndroid1";
	
	// Returns the access token or null
	public static String getAccessToken(Context context) {
		return context.getSharedPreferences(
				context.getString(R.string.app_package_name),
				Context.MODE_PRIVATE).getString("access_token", null);
	}
	
	public static String getUserId (Context context) {
		return context.getSharedPreferences(
				context.getString(R.string.app_package_name),
				Context.MODE_PRIVATE).getString("user_id", null);
	}
	
	public static String getUserName (Context context) {
		return context.getSharedPreferences(
				context.getString(R.string.app_package_name),
				Context.MODE_PRIVATE).getString("username", null);
	}
	
	public static boolean setNewUser (Context context, String user_id, String token) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				context.getString(R.string.app_package_name),
				Context.MODE_PRIVATE).edit();
		editor.putString("user_id", user_id);
		editor.putString("access_token", token);
		return editor.commit();
	}
	
	public static boolean setUserName (Context context, String username) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				context.getString(R.string.app_package_name),
				Context.MODE_PRIVATE).edit();
		editor.putString("username", username);
		return editor.commit();
	}
	
	public static boolean resetUser (Context context) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				context.getString(R.string.app_package_name),
				Context.MODE_PRIVATE).edit();
		editor.remove("user_id");
		editor.remove("access_token");
		return editor.commit();
	}
	
	
	
	public static ServerHelper getInstance (SherlockFragmentActivity activity) {
		Fragment frag = activity.getSupportFragmentManager().findFragmentByTag("bg");
		if (frag != null)
			return (ServerHelper) frag;
		else {
			ServerHelper helper = new ServerHelper();
			FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
			transaction.add(helper, "bg");
			transaction.commit();
			return helper;
		}
	}
	
	public static void getNewUserToken(Context context) {
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
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	public void createNewUser(){
		NewUserTask task = new NewUserTask(getActivity());
		task.execute();
	}
}
