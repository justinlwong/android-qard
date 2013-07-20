package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.settings.services.PopupDialog;

public class SkypeServiceManager extends ServiceManager {

	public static String PACKAGE_NAME = "com.facebook.katana";
	//public static String PHONE_URI_PREFIX = "tel:";
	public static Integer SKYPE_IMAGE = R.drawable.service_skype;
	private BaseFragment bf;
	
	// Description of data (include examples)
	// data - phone number (eg. 4161234567)
	public SkypeServiceManager(Activity activity, BaseFragment bf) {
		super(activity, SKYPE_IMAGE);
		this.bf = bf;
	}

	@Override
	public void switchToServiceApp() {
        Toast.makeText(activity, data, Toast.LENGTH_LONG).show();
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
        showEditDialog(Services.SKYPE.id);	
	}
	
    private void showEditDialog(int serviceId) {
        FragmentManager fm = bf.getFragmentManager();        
        PopupDialog popupDialog = (PopupDialog) fm.findFragmentByTag("login_fragment");
       		
        popupDialog = new PopupDialog();
        FragmentTransaction transaction = bf.getChildFragmentManager().beginTransaction();
        Bundle bund = new Bundle();
        bund.putInt("serviceType",serviceId);
        popupDialog.setArguments(bund);
        //loginDialog.show(transaction, "login_fragment");
        transaction.add(popupDialog,"login_fragment").commit();
    }

}