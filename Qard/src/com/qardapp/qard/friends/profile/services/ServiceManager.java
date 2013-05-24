package com.qardapp.qard.friends.profile.services;

import android.app.Activity;

public abstract class ServiceManager {

	protected String data;
	protected Activity activity;
	
	public Integer imageID;
	
	public ServiceManager(Activity activity, Integer imageID, String data) {
		this.activity = activity;
		this.imageID = imageID;
		this.data = data;
	}
	
	public void switchToService() {
		if (isAppInstalled())
			switchToServiceApp();
		else
			switchToServiceFallBack();
	}
	
	public abstract void switchToServiceApp();
	public abstract void switchToServiceFallBack();
	public abstract boolean isAppInstalled();
}
