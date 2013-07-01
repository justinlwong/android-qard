package com.qardapp.qard.friends.profile.services;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
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
	
	public Cursor checkContact()
	{
		Cursor c = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
		        new String[] { ContactsContract.Contacts.Data._ID }, ContactsContract.Data.DATA1 + "=?",
		        new String[] { data + "@s.whatsapp.net" }, null);
		c.moveToFirst();
		return c;
	}

	@Override
	public void switchToServiceApp() {
		Log.d("here","switching");
		//data = "14162624142";
        mPrefs = activity.getSharedPreferences("tokens", 0);
		if (!mPrefs.getString("WhatsApp_data","-1").equals(data))
		{
			Cursor c = checkContact();

			//Log.d("here",c.getString(0));
			if (c.getCount() != 0)
			{
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + c.getString(0)));
                //i.putExtra("finishActivityOnSaveComplete", true);
			    activity.startActivity(i);				
			} else {
				
				// Try to add new contact
				
				ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		        int rawContactInsertIndex = ops.size();
		        
		        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
		                .withValue(RawContacts.ACCOUNT_TYPE, null)
		                .withValue(RawContacts.ACCOUNT_NAME, null)
		                .build());
		 
		        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
		                .withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.whatsapp.profile")
		                .withValue(ContactsContract.Data.DATA1, data + "@s.whatsapp.net") // Name of the person
		                .build());
		        
		        ContentProviderResult[] res = null;
				try {
					res = activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OperationApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            if (res!=null && res[0]!=null) {
	            	Uri newContactUri = res[0].uri;	
	            	//02-20 22:21:09 URI added contact:content://com.android.contacts/raw_contacts/612
	            	Log.d("here", "URI added contact:"+ newContactUri);
	    			Cursor a = checkContact();
	    			if (!a.isNull(0))
	    			{
	    				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + a.getString(0)));
	                    //i.putExtra("finishActivityOnSaveComplete", true);
	    			    activity.startActivity(i);		
	    			} else {
						Toast.makeText(activity, "Invalid Contact", Toast.LENGTH_SHORT).show();		    				
	    			}
	            }
	            else { 
	            	Log.e("here", "Contact not added.");
					Toast.makeText(activity, "Invalid Contact", Toast.LENGTH_SHORT).show();	
	            }
		        
			}

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
		Log.d("here","is app installed");
		try {
			activity.getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
