package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.settings.services.FacebookLoginActivity;
import com.qardapp.qard.settings.services.OAuthActivity;


public class FacebookServiceManager extends ServiceManager {

	public static String PACKAGE_NAME = "com.facebook.katana";
	public static String FB_FALLBACK_URL_PREFIX = "https://www.facebook.com/";
	public static Integer FB_IMAGE = R.drawable.facebook;
	
	// Description of data (include examples)
	// data - FB user id (eg. raymond.lam.73)
	public FacebookServiceManager(Activity activity) {
		super(activity, FB_IMAGE);
	}

	@Override
	public void switchToServiceApp() {
		String uri = "fb://profile/" + data;
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		activity.startActivity(intent);
	}

	@Override
	public void switchToServiceFallBack() {
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(FB_FALLBACK_URL_PREFIX + data));
		activity.startActivity(intent);
	}


	@Override
	public boolean isAppInstalled() {
		try {
			activity.getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public void startLoginIntent() {
		Intent intent = new Intent(activity,FacebookLoginActivity.class);
		activity.startActivityForResult(intent,0);
	}
	
}
