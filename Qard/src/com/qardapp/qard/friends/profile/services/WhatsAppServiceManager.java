package com.qardapp.qard.friends.profile.services;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.qardapp.qard.R;

public class WhatsAppServiceManager extends ServiceManager {

	public static String PACKAGE_NAME = "com.whatsapp";
	//public static String FB_FALLBACK_URL_PREFIX = "https://www.facebook.com/";
	public static Integer WHATSAPP_IMAGE = R.drawable.whatsapp;
	private SharedPreferences mPrefs;
	
	public WhatsAppServiceManager(Activity activity, String data) {
		super(activity, WHATSAPP_IMAGE, data );
	}

	@Override
	public void switchToServiceApp() {
		Log.d("here","switching");
        mPrefs = activity.getSharedPreferences("tokens", 0);
		if (!mPrefs.getString("WhatsApp_data","-1").equals(data))
		{
			Cursor c = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
			        new String[] { ContactsContract.Contacts.Data._ID }, ContactsContract.Data.DATA1 + "=?",
			        new String[] { data + "@s.whatsapp.net" }, null);
			c.moveToFirst();
			//Log.d("here",c.getString(0));
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + c.getString(0)));
		    activity.startActivity(i);
		} else {
			Toast.makeText(activity, "Cannot start WhatsApp conversation with yourself", Toast.LENGTH_SHORT).show();			
		}
	}

	@Override
	public void switchToServiceFallBack() {
		Toast.makeText(activity, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
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
	
}
