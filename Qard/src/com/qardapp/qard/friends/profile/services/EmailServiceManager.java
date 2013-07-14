package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.settings.services.AccountChecker;

// Note: Twitter resolves URL so no need to launch app explicitly

public class EmailServiceManager extends ServiceManager {

	public static Integer EMAIL_IMAGE = R.drawable.gmail;
	
	// Description of data (include examples)
	public EmailServiceManager(Activity activity) {
		super(activity, EMAIL_IMAGE );
	}

	@Override
	public void switchToServiceApp() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {data});
		emailIntent.setType("message/rfc822");
		activity.startActivity(Intent.createChooser(emailIntent, "Send e-mail:"));

	}

	@Override
	public void switchToServiceFallBack() {
		switchToServiceApp();
	}


	@Override
	public boolean isAppInstalled() {
		try {
			//activity.getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public void startLoginIntent() {
		// try account check first
		boolean check = new AccountChecker(activity).getAccountInfo(Services.EMAIL.id);		
		if (check == false)
		{
			// Backup option would be to popup dialog to ask user to add it in
            Toast.makeText(activity, "No email account was detected on this device!", Toast.LENGTH_LONG).show();					
		}	
	}
	
	
}