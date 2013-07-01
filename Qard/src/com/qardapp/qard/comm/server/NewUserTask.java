package com.qardapp.qard.comm.server;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.qrcode.QRCodeManager;
import com.qardapp.qard.settings.services.UpdateDatabase;

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
