package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;

import com.qardapp.qard.R;

// Note: Twitter resolves URL so no need to launch app explicitly

public class GmailServiceManager extends ServiceManager {

	//public static String PACKAGE_NAME = "com.facebook.katana";
	public static Integer GMAIL_IMAGE = R.drawable.gmail;
	
	// Description of data (include examples)
	// Twitter uses numeric ids for accessing pages
	public GmailServiceManager(Activity activity, String data) {
		super(activity, GMAIL_IMAGE, data );
	}

	@Override
	public void switchToServiceApp() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {data});
		emailIntent.setType("message/rfc822");
		activity.startActivity(Intent.createChooser(emailIntent, "Send e-mail:"));;
	}

	@Override
	public void switchToServiceFallBack() {
		switchToServiceApp();
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