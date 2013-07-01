package com.qardapp.qard.settings.services;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.qardapp.qard.comm.server.AddServiceTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class UpdateDatabase {
	
	public static void updateDatabase(String data, int serviceId, Activity a)
	{
    	AddServiceTask task = new AddServiceTask(a, serviceId, data);
        task.execute();
        
		ContentResolver res = a.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, 0);
		values.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, serviceId);
		values.put(FriendsDatabaseHelper.COLUMN_FS_DATA, data);
		
		// Delete existing entry and replace
		String where = FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=? AND " + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
		String[] args = new String[] { "0", String.valueOf(serviceId)};
		res.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+serviceId), where, args);
		res.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+serviceId), values);
	}

}
