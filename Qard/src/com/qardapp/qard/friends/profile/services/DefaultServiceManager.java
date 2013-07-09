package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.qardapp.qard.Services;

public class DefaultServiceManager extends ServiceManager {

	private int serviceID;
	Services service;
	String url;

	Activity a;
	
	public DefaultServiceManager(Activity activity, int serviceImage, int serviceId, String data) {
		super(activity, serviceImage, data);
		this.serviceID = serviceId;
		this.a = activity;
	}

	@Override
	public void switchToServiceApp() {

        if (serviceID == Services.LINKEDIN.id) {
        	service = Services.LINKEDIN;
            url = data;
        
        } else if (serviceID == Services.FLICKR.id)
        {
        	service = Services.FLICKR;
            url = "http://m.flickr.com/#/photos/"+data;        	
        } else if (serviceID == Services.INSTAGRAM.id)
        {
        	service = Services.INSTAGRAM;
            url = "https://instagram.com/"+data;
        }else if (serviceID == Services.TUMBLR.id)
        {
        	service = Services.TUMBLR;
            url = data;
        }
        
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));
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
