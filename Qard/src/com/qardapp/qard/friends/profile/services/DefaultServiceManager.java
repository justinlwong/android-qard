package com.qardapp.qard.friends.profile.services;

import com.qardapp.qard.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class DefaultServiceManager extends ServiceManager {

	public static String PACKAGE_NAME = "com.facebook.katana";
	private int serviceID;
	Services service;
	String url;
	//public static String PHONE_URI_PREFIX = "tel:";
	//public static Integer SERVICE_IMAGE = R.drawable.phone;
	private SharedPreferences mPrefs;
	Activity a;
	
	// Description of data (include examples)
	// data - phone number (eg. 4161234567)
	public DefaultServiceManager(Activity activity, int serviceImage, int serviceId, String data) {
		super(activity, serviceImage, data);
		this.serviceID = serviceId;
		this.a = activity;
	}

	@Override
	public void switchToServiceApp() {
        //mPrefs = a.getSharedPreferences("tokens", 0);
        if (serviceID == Services.TWITTER.id)
        {
        } else if (serviceID == Services.LINKEDIN.id) {
        	service = Services.LINKEDIN;
            url = data;
        
        } else if (serviceID == Services.FLICKR.id)
        {
        	service = Services.FLICKR;
            url = "http://m.flickr.com/#/photos/"+data;        	
        } else if (serviceID == Services.FOURSQUARE.id)
        {
        } else if (serviceID == Services.INSTAGRAM.id)
        {
        	service = Services.INSTAGRAM;
            url = "https://instagram.com/"+data;
        }
        
		//String uri = "fb://profile/" + data;
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
		//Intent intent = new Intent(activity,ServiceWebDisplayActivity.class);
		//intent.putExtra("serviceID", serviceID);
		//activity.startActivityForResult(intent, 100);	
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
