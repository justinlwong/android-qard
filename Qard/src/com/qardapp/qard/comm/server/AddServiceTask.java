package com.qardapp.qard.comm.server;

import org.json.JSONObject;

import com.qardapp.qard.database.FriendsDatabaseHelper;

import android.content.Context;

public class AddServiceTask extends ServerTask{

	private Context context;
	private static String ADD_SERVICE_URL = "/user/user_services";
	
	private int service_id;
	private String data;
	private String token;
	
	public AddServiceTask(Context context, int service_id, String data) {
		this(context, service_id, data, null);
	}
	
	public AddServiceTask(Context context, int service_id, String data, String token) {
		super(context, ADD_SERVICE_URL, FriendsDatabaseHelper.QUEUED_UPDATE_SERVICES);
		this.context = context;
		this.service_id = service_id;
		this.data = data;
		this.token = token;
	}
	
	public AddServiceTask(Context context, int queued_id) {
		super(context, ADD_SERVICE_URL, FriendsDatabaseHelper.QUEUED_UPDATE_SERVICES, queued_id);
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			if (queued_id != -1) {
				makeQueuedPost();
				return null;
			}
			JSONObject holder = new JSONObject();
			holder.put("service_id", service_id);
			holder.put("data", data);
			if (token != null) {
				holder.put("token", token);
				holder.put("client_id", ServerHelper.CLIENT_ID);
				holder.put("client_secret", ServerHelper.CLIENT_SEC);
			}
			makePost(holder);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onServerResponse(JSONObject response) {
		try {
			if (token != null && response.has("user_id")) {
				String new_id = response.getString("user_id");
				String access_token = response.getString("access_token");
				ServerHelper.setNewUser(context, new_id, access_token);
			}
		} catch (Exception e) {
			
		}
	}

}
