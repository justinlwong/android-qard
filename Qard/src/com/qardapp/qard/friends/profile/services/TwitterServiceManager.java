package com.qardapp.qard.friends.profile.services;

import com.qardapp.qard.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

// Note: Twitter resolves URL so no need to launch app explicitly

public class TwitterServiceManager extends ServiceManager {

	//public static String PACKAGE_NAME = "com.facebook.katana";
	public static String TWITTER_FALLBACK_URL_PREFIX = "https://twitter.com/";
	public static Integer TWITTER_IMAGE = R.drawable.twitter;
	
	// Description of data (include examples)
	// Twitter uses numeric ids for accessing pages
	public TwitterServiceManager(Activity activity, String data) {
		super(activity, TWITTER_IMAGE, data );
		Log.d("twitterdata",data);
	}

	@Override
	public void switchToServiceApp() {
		//String uri = "fb://profile/" + data;
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_FALLBACK_URL_PREFIX + data));
		activity.startActivity(intent);
	}

	@Override
	public void switchToServiceFallBack() {
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_FALLBACK_URL_PREFIX + data));
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