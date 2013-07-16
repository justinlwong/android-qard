package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.qardapp.qard.Services;
import com.qardapp.qard.settings.services.AccountChecker;
import com.qardapp.qard.settings.services.GooglePlusAuthActivity;
import com.qardapp.qard.settings.services.OAuthActivity;

public class DefaultServiceManager extends ServiceManager {

	private int serviceID;
	Services service;
	String url;

	public DefaultServiceManager(Activity activity, int serviceImage, int serviceId) {
		super(activity, serviceImage);
		this.serviceID = serviceId;
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
        } else if (serviceID == Services.YOUTUBE.id)
        {
        	service = Services.YOUTUBE;
            url = "http://www.youtube.com/user/"+data;
        } else if (serviceID == Services.FOURSQUARE.id)
        {
        	service = Services.FOURSQUARE;
            url = "http://m.foursquare.com/user?uid="+data;
        } else if (serviceID == Services.TWITTER.id)
        {
        	service = Services.TWITTER;
            url = "https://twitter.com/"+data;
        } else if (serviceID == Services.TUMBLR.id)
        {
        	service = Services.TUMBLR;
            url = "http://www.tumblr.com/open/app?referrer=mobile_banner&app_args=blog%3FblogName%3D"+data+"%26page%3Dblog";
        } else if (serviceID == Services.GOOGLEPLUS.id)
        {
        	service = Services.GOOGLEPLUS;
            url = "https://plus.google.com/" + data;
        } else if (serviceID == Services.BLOGGER.id)
        {
        	service = Services.BLOGGER;
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
	
	@Override
	public void startLoginIntent() {
		if (serviceID == Services.TWITTER.id) {
			// try account check first
			boolean check = new AccountChecker(activity).getAccountInfo(Services.TWITTER.id);
			if (check == false)
			{
			    Intent intent = new Intent(activity,OAuthActivity.class);
			    intent.putExtra("serviceID", Services.TWITTER.id);
			    activity.startActivity(intent);
			}			
		} else if (serviceID == Services.GOOGLEPLUS.id) {
            Intent intent = new Intent(activity, GooglePlusAuthActivity.class);
            activity.startActivity(intent);	
		} else {
			Intent intent = new Intent(activity,OAuthActivity.class);
			intent.putExtra("serviceID", serviceID);
			activity.startActivity(intent);	
		}
	}

}
