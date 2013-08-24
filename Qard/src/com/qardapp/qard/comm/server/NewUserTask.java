package com.qardapp.qard.comm.server;

import android.content.Context;
import android.os.AsyncTask;

import com.qardapp.qard.MainActivity;

public class NewUserTask extends
		AsyncTask<String, Void, String> {

	private Context context;

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
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (context instanceof MainActivity) {
			((MainActivity) context).refreshFragments();
		}	
	}
}
