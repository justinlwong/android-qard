package com.qardapp.qard.comm.server;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.profile.ProfileFragment;

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
				

		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (context instanceof MainActivity) {
			//View v = ((MainActivity) context).findViewById(R.id.qrcode_image);
			Fragment frag = ((MainActivity) context).getSupportFragmentManager().findFragmentByTag(MainActivity.FRAGNAME_PROFILE);
			if (frag !=null)
				((ProfileFragment)frag).updateViews();
		}
		
	}

}
