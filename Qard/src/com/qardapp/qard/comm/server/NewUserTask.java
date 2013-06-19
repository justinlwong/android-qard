package com.qardapp.qard.comm.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.qardapp.qard.R;
import com.qardapp.qard.qrcode.QRCodeManager;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.ImageView;

public class NewUserTask extends
		AsyncTask<String, Void, String> {

	private Context context;
	private static String NEW_USER_URL = ServerHelper.SERVER_URL + "/new_user";

	public NewUserTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		if (ServerHelper.getAccessToken(context) != null) {
			return null;
		}
		
		ServerHelper.getNewUserToken(context);
		if (context instanceof Activity) {
			View v = ((Activity) context).findViewById(R.id.qrcode_image);
			if (v !=null)
				QRCodeManager.genQRCode(ServerHelper.getUserId(context), (ImageView)v);
		}
		return null;
	}

}
