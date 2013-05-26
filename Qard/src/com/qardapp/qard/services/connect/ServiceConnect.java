package com.qardapp.qard.services.connect;

import android.app.Activity;

public abstract class ServiceConnect {
	
	public ServiceConnect(Activity activity) {
		this.activity = activity;
	}
	
	Activity activity;

}
