package com.qardapp.qard.settings.services;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class SendInviteActivity{
	public static void sendInvite(ContentResolver cr, Context context){
		//String user_id = "";
		cr = context.getContentResolver();
		//String fsprefix = FriendsDatabaseHelper.TABLE_FRIEND_SERVICES + ".";
		String fprefix = FriendsDatabaseHelper.TABLE_FRIENDS + ".";
		//String where = fprefix+FriendsDatabaseHelper.COLUMN_USER_ID+ "=? AND " + fsprefix+FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
		String where = fprefix + FriendsDatabaseHelper.COLUMN_USER_ID+ "=?";
		//String[] args = new String[] {user_id, String.valueOf(Services.PHONE.id)};
		//String[] args = new String[] {null};
		//Cursor c = cr.query(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, ""), new String[]{fprefix+FriendsDatabaseHelper.COLUMN_USER_ID}, where, args, null);
		Cursor c = cr.query(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, ""), null, where, null, null);
		Log.d("test","count:"+c.getColumnCount());
		if (c.moveToFirst())
		{
			while (c.isAfterLast() == false)
			{
				
				/*
				if (c.isNull(c.getColumnIndex(FriendsDatabaseHelper.COLUMN_USER_ID))){
					Log.d("testg", "testcase"+i);
				}
				else{
					Log.d("testg1", "testg1"+i);
				}
				i++;
				*/
				String user_id = c.getString(c.getColumnIndex(FriendsDatabaseHelper.COLUMN_USER_ID));  
				Log.d("invite",user_id);
				// Duplicate so don't add this
				
				if (user_id == null)
				{
					Log.d("null",user_id);
					return;
				} else {
					//Log.d("norepeat",phone + " " + phoneMatch);
					Log.e("errorhere",user_id);
				}
				
				c.moveToNext();
			}

		}

		/*
		cr= context.getContentResolver();
		String where = FriendsDatabaseHelper.COLUMN_SERVICE_ID +"=?";
		String args[] = {Services.PHONE.id + ""};
		Cursor cursor = cr.query(FriendsProvider.MY_URI, null, where, args, null);
		cursor.moveToFirst();
		while (cursor.moveToNext()){
			SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.app_package_name), Context.MODE_PRIVATE);
			String user_id = pref.getString("user_id", "noid");
			String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
			//if (user_id.equals("noid")){
				Log.d("invite", phone);
			//}
			 */
		
		//}
		//cursor.moveToFirst();
		//SharedPreferences pref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.app_package_name), Context.MODE_PRIVATE);
		//String user_id = pref.getString("user_id", "noid");
	}
}
