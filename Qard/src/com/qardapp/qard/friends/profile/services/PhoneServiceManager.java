package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.settings.services.PopupDialog;

public class PhoneServiceManager extends ServiceManager {

	public static String PACKAGE_NAME = "com.facebook.katana";
	public static String PHONE_URI_PREFIX = "tel:";
	public static Integer PHONE_IMAGE = R.drawable.phone;
	private BaseFragment bf;
	
	// Description of data (include examples)
	// data - phone number (eg. 4161234567)
	public PhoneServiceManager(Activity activity) {
		super(activity, PHONE_IMAGE);
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
	
	
	@Override
	public void startLoginIntent() {
        showEditDialog(Services.PHONE.id);	
	}
	
	public void getFragment(BaseFragment bf) {
		this.bf = bf;
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
