package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.qardapp.qard.R;

public class PhoneServiceManager extends ServiceManager {

	public static String PACKAGE_NAME = "com.facebook.katana";
	public static String PHONE_URI_PREFIX = "tel:";
	public static Integer PHONE_IMAGE = R.drawable.phone;
	
	// Description of data (include examples)
	// data - phone number (eg. 4161234567)
	public PhoneServiceManager(Activity activity, String data) {
		super(activity, PHONE_IMAGE, data);
	}

	@Override
	public void switchToServiceApp() {
		String uri = "tel:" + data.trim();
		// ACTION_DIAL just opens the phone app but not call
		// ACTION_CALL calls the number but requires user permissions
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
		activity.startActivity(intent);
	}

	@Override
	public void switchToServiceFallBack() {
		return;
	}

	@Override
	public boolean isAppInstalled() {
		return true;
	}

}
