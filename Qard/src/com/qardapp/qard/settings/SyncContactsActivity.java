package com.qardapp.qard.settings;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import android.util.Log;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class SyncContactsActivity extends Activity {
	
	Activity a;
	
    public void updateDatabase(ContentResolver cr, String first, String last, String phone, String email)
    {
    	String lastname = "";
    	if (last == null) {
    	    lastname = "";
    	}
    	else {
    		lastname = last;
    	}
    	// Adding Contact
		ContentValues values = new ContentValues();
		values.put(FriendsDatabaseHelper.COLUMN_TITLE, "");
		values.put(FriendsDatabaseHelper.COLUMN_FIRST_NAME, first);
		values.put(FriendsDatabaseHelper.COLUMN_LAST_NAME, lastname);
		values.put(FriendsDatabaseHelper.COLUMN_PROFILE_PIC_LOC, "");
		values.put(FriendsDatabaseHelper.COLUMN_DATE_ADDED, new Date().getTime());
		
		// Delete existing entry and replace
		String fsprefix = FriendsDatabaseHelper.TABLE_FRIEND_SERVICES + ".";
		String fprefix = FriendsDatabaseHelper.TABLE_FRIENDS + ".";
		String where = fprefix+FriendsDatabaseHelper.COLUMN_FIRST_NAME+ "=? AND " + fprefix+FriendsDatabaseHelper.COLUMN_LAST_NAME + "=? AND " + fsprefix+FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
		String[] args = new String[] { first, lastname, String.valueOf(Services.PHONE.id)};
		Cursor c = cr.query(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/service_data"), new String[]{fsprefix+FriendsDatabaseHelper.COLUMN_FS_DATA}, where, args, null);
		
		if (c.moveToFirst())
		{
			String phoneMatch = c.getString(c.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));  
			Log.d("phone",phoneMatch);
			// Duplicate so don't add this
			if (phoneMatch.compareTo(phone) == 0)
			{
				return;
			}

		}
		
		c.close();
		
		Uri uri = cr.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0"), values);
		int user_id = Integer.parseInt(uri.getLastPathSegment());
		//Log.d("here",String.valueOf(user_id));
		
		// Use ID to add services
		ContentValues pvalues = new ContentValues();
		ContentValues evalues = new ContentValues();
		pvalues.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, user_id);
		pvalues.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, Services.PHONE.id);
		pvalues.put(FriendsDatabaseHelper.COLUMN_FS_DATA, phone);
		cr.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+Services.PHONE.id), pvalues);
		
		if (email != null)
		{
			evalues.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, user_id);
			evalues.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, Services.GMAIL.id);
			evalues.put(FriendsDatabaseHelper.COLUMN_FS_DATA, email);		
			cr.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+Services.GMAIL.id), evalues);
		}
		
    }
	
	private class RetrieveContacts extends AsyncTask<Void, Void, Void> {
		
		ProgressDialog progDialog;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDialog = new ProgressDialog(SyncContactsActivity.this);
            progDialog.setMessage("Syncing...");
            progDialog.setIndeterminate(true);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setCancelable(false);
            progDialog.show();
        }
		
	     protected Void doInBackground(Void... v) {
	         ContentResolver cr = getContentResolver();
	         //Cursor r, d = null;
	         Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
	                 null, null, null);
	         
	         if (cur.getCount() > 0) {
	             while (cur.moveToNext()) {
	                 String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                     String hasPhone = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                     if (hasPhone.equalsIgnoreCase("1")) 
                     {
                    	 
                    	  String cNumber = null, cEmail = null;
                    	  String first = null;
                    	  String last = null;
                    	  
                    	  String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = " + id; 
                    	  String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE };
                    	 
                          Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
                                 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                          Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null, 
                                  ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = "+ id,null, null);
                          Cursor nameCur = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

                          
                          if (phones.moveToFirst())
                          {
                              cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));                       	  
                          }

                          if (emails.moveToFirst()) {
                              cEmail = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));                        	  
                          }
                          
                          while (nameCur.moveToNext()) {                       
                        	    first = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                        	    last = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                          }
                          
                          if (cEmail != null)
                          {
    		                  Log.d("here",first + " " + cNumber + " " + cEmail);                      	  
                          }
                          
                          // Update Database
                          updateDatabase(cr, first, last, cNumber, cEmail);
                          
                          phones.close();
                          emails.close();
                          nameCur.close();
/*
		                 long rawid = 0;
		                 d = cr.query(RawContacts.CONTENT_URI,
		                         new String[]{RawContacts._ID},
		                         RawContacts.CONTACT_ID + "=?",
		                         new String[]{String.valueOf(id)}, null);
		                 
		                 if (d.moveToFirst()){
		               	   do{
		               	      rawid = d.getInt(d.getColumnIndex(RawContacts._ID));
		               	   }while(d.moveToNext());
		               	}
		               	d.close();
		               	Log.d("here",String.valueOf(rawid));
		                 
		                 Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawid);
		                 Uri entityUri = Uri.withAppendedPath(rawContactUri, Entity.CONTENT_DIRECTORY);
		                 r = cr.query(entityUri,
		                          new String[]{RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE, Entity.DATA1},
		                          null, null, null);
		                 //Log.d("here",r.toString());
		                 while (r.moveToNext()) {
		                     String sourceId = r.getString(0);
		                     //Log.d("here",sourceId);
		                     if (!r.isNull(1)) {
		                         String mimeType = r.getString(2);
		                         String rdata = r.getString(3);
		                         Log.d("here",mimeType + " " + rdata);
		                     }
		                 }   
		                 r.close();*/
		             }
	             }
	         }
	         
	         cur.close();
			return null;

	     }
	     
		@Override  
		protected void onPostExecute(Void v) { 
			progDialog.dismiss();
			a.finish();
		}

/*	     protected void onProgressUpdate(Integer... progress) {
	         setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(Long result) {
	         showDialog("Downloaded " + result + " bytes");
	     }*/
	 }
	
	public static int PICK_CONTACT = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauth_layout);
        
        a = this;
        
        new RetrieveContacts().execute();        

        //finish();
       
    }
    
    /*@Override 
    public void onActivityResult(int reqCode, int resultCode, Intent data){ 
        super.onActivityResult(reqCode, resultCode, data);

        switch(reqCode)
        {
           case (1):
             if (resultCode == Activity.RESULT_OK)
             {
                 Uri contactData = data.getData();
                 Cursor c = getContentResolver().query(contactData, null, null, null, null);
                  if (c.moveToFirst())
                  {
                      String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                      long rawid = 0;
                      String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                      
                      Cursor d = getContentResolver().query(RawContacts.CONTENT_URI,
                              new String[]{RawContacts._ID},
                              RawContacts.CONTACT_ID + "=?",
                              new String[]{String.valueOf(id)}, null);
                      //Log.d("here",d.getString(0));
                      
                      if (d.moveToFirst()){
                    	   do{
                    	      rawid= d.getInt(d.getColumnIndex(RawContacts._ID));
                    	      //Log.d("here",rawid);
                    	      // do what ever you want here
                    	   }while(d.moveToNext());
                    	}
                    	d.close();
git                       
                      Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawid);
                      Uri entityUri = Uri.withAppendedPath(rawContactUri, Entity.CONTENT_DIRECTORY);
                      Cursor r = getContentResolver().query(entityUri,
                               new String[]{RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE, Entity.DATA1},
                               null, null, null);
                      Log.d("here",r.toString());
                      try {
                          while (r.moveToNext()) {
                              String sourceId = r.getString(0);
                              Log.d("here",sourceId);
                              if (!r.isNull(1)) {
                                  String mimeType = r.getString(2);
                                  String rdata = r.getString(3);
                                  Log.d("here",mimeType + " " + rdata);
                              }
                          }
                      } finally {
                          r.close();
                      }

                      if (hasPhone.equalsIgnoreCase("1")) 
                      {
                          Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
                                 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                          phones.moveToFirst();
                          String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                           Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();

                          String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                          Log.d("here",nameContact+ " "+ cNumber);
                      }
                 }
           }
        }
    }*/


}
