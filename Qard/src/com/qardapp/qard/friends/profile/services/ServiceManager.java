package com.qardapp.qard.friends.profile.services;

import android.app.Activity;

public abstract class ServiceManager {

	protected String data;
	protected Activity activity;
	
	public Integer imageID;
	
	public ServiceManager(Activity activity, Integer imageID) {
		this.activity = activity;
		this.imageID = imageID;
	}
	
	public void switchToService() {
		if (isAppInstalled())
			switchToServiceApp();
		else
			switchToServiceFallBack();
	}
	
	public void addData(String data)
	{
		this.data = data;
	}
	
	public abstract void switchToServiceApp();
	public abstract void switchToServiceFallBack();
	public abstract boolean isAppInstalled();
	public abstract void startLoginIntent();
}
