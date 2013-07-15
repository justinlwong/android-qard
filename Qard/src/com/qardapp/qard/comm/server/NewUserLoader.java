package com.qardapp.qard.comm.server;

import java.util.ArrayList;

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
		if (ServerHelper.getAccessToken(context) != null) {
			return null;
		}
		
		ServerHelper.getNewUserToken(context);
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
