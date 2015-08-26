package com.qardapp.qard.settings.services;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.widget.Toast;

import com.qardapp.qard.comm.server.AddServiceTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class UpdatePhoneBook {
	
	public static Cursor checkContact(Activity activity)
	{
		Cursor c = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
		        new String[] { ContactsContract.Contacts.Data._ID }, ContactsContract.Data.DATA1 + "=?",
		        new String[] { data + "@s.whatsapp.net" }, null);
		c.moveToFirst();
		return c;
	}
	
	// Need to update every service as a raw contact entry ... need a system to do this
	
	public static void updatePhoneBook(String data, int serviceId, Activity activity)
	{
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
                .withValue(ContactsContract.Data.DATA1, data + "@s.whatsapp.net") 
                .build());
        
        ContentProviderResult[] res = null;
		try {
			res = activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
        if (res!=null && res[0]!=null) {
        	Uri newContactUri = res[0].uri;	
        	Log.d("here", "URI added contact:"+ newContactUri);
			Cursor a = checkContact(activity);
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

}
