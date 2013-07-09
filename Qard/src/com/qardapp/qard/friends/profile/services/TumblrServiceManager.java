package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.qardapp.qard.R;

// Note: Twitter resolves URL so no need to launch app explicitly

public class TumblrServiceManager extends ServiceManager {

	public static Integer TUMBLR_IMAGE = R.drawable.tumblr;
	
	// Description of data (include examples)
	public TumblrServiceManager(Activity activity, String data) {
		super(activity, TUMBLR_IMAGE, data );
	}

	@Override
	public void switchToServiceApp() {
		String url = "http://www.tumblr.com/open/app?referrer=mobile_banner&app_args=blog%3FblogName%3D"+data+"%26page%3Dblog";
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}

	@Override
	public void switchToServiceFallBack() {
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(data));
		activity.startActivity(intent);
	}


	@Override
	public boolean isAppInstalled() {
		try {
			//activity.getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}