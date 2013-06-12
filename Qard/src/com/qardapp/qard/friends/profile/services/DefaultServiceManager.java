package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;

public class DefaultServiceManager extends ServiceManager {

	public static String PACKAGE_NAME = "com.facebook.katana";
	private int serviceID;
	//public static String PHONE_URI_PREFIX = "tel:";
	//public static Integer SERVICE_IMAGE = R.drawable.phone;
	
	// Description of data (include examples)
	// data - phone number (eg. 4161234567)
	public DefaultServiceManager(Activity activity, int serviceImage, int serviceId, String data) {
		super(activity, serviceImage, data);
		this.serviceID = serviceId;
	}

	@Override
	public void switchToServiceApp() {
		Intent intent = new Intent(activity,ServiceWebDisplayActivity.class);
		intent.putExtra("serviceID", serviceID);
		activity.startActivityForResult(intent, 100);	
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
