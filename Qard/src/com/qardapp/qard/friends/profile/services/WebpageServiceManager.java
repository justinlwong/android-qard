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
import com.qardapp.qard.settings.services.DisplayNotePopup;
import com.qardapp.qard.settings.services.PopupDialog;

public class WebpageServiceManager extends ServiceManager {

	public static Integer WEBPAGE_IMAGE = R.drawable.service_nexcircle;
	private BaseFragment bf;
	
	// Description of data (include examples)
	// data - phone number (eg. 4161234567)
	public WebpageServiceManager(Activity activity, BaseFragment bf) {
		super(activity, WEBPAGE_IMAGE);
		this.bf = bf;
	}

	@Override
	public void switchToServiceApp() {
        showNote();
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
        showEditDialog(Services.WEBPAGE.id);	
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
    
    private void showNote()
    {
        FragmentManager fm = bf.getFragmentManager();        
        DisplayNotePopup displayPopup = (DisplayNotePopup) fm.findFragmentByTag("displaynote_fragment");
       		
        displayPopup = new DisplayNotePopup();
        FragmentTransaction transaction = bf.getChildFragmentManager().beginTransaction();
        Bundle bund = new Bundle();
        bund.putString("data",data);
        displayPopup.setArguments(bund);
        //loginDialog.show(transaction, "login_fragment");
        transaction.add(displayPopup,"displaynote_fragment").commit();    	
    }

}