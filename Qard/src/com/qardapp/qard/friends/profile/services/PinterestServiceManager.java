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
import com.qardapp.qard.settings.services.FacebookLoginActivity;
import com.qardapp.qard.settings.services.PopupDialog;


public class PinterestServiceManager extends ServiceManager {

	public static String PACKAGE_NAME = "com.pinterest";
	public static String PINTEREST_FALLBACK_URL_PREFIX = "https://www.pinterest.com/";
	public static Integer PINTEREST_IMAGE = R.drawable.pinterest;
	private BaseFragment bf;
	
	// Description of data (include examples)
	// data - FB user id (eg. raymond.lam.73)
	public PinterestServiceManager(Activity activity, BaseFragment bf) {
		super(activity, PINTEREST_IMAGE);
		this.bf = bf;
	}

	@Override
	public void switchToServiceApp() {
		String uri = "pinterest://pinterest.com/" + data;
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		activity.startActivity(intent);
	}

	@Override
	public void switchToServiceFallBack() {
		Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(PINTEREST_FALLBACK_URL_PREFIX + data));
		activity.startActivity(intent);
	}


	@Override
	public boolean isAppInstalled() {
		try {
			activity.getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public void startLoginIntent() {
        showEditDialog(Services.PINTEREST.id);	
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