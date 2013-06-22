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
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.EditText;

public class LoginTaskLoader extends AsyncTaskLoader<String>{

	private Context context;
	private static String LOGIN_URL = ServerHelper.SERVER_URL + "/rlogin";
	
	private EditText username;
	private EditText password;

	
	public LoginTaskLoader(Context context, EditText username, EditText password) {
		super(context);
		this.context = context;
		this.username = username;
		this.password = password;
	}

	@Override
	protected void onStartLoading() {
	  if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty())
		  return;
	  forceLoad();
	}
	
	@Override
	public String loadInBackground() {
		if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty())
			  return null;
		HttpClient httpClient = new DefaultHttpClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(LOGIN_URL);
        
		JSONObject holder = new JSONObject();
		try {
			holder.put("username", username.getText().toString());
			holder.put("password", password.getText().toString());
			holder.put("client_id", ServerHelper.CLIENT_ID);
			holder.put("client_secret", ServerHelper.CLIENT_SEC);
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
			String token = finalResult.getString("access_token");
			ServerHelper.setNewUser(context, id, token);
			return token;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
