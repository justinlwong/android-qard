package com.qardapp.qard.comm;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

public class QardMessage {
	public static int ID = 1;
	public static int FIRST_NAME = 2;
	public static int LAST_NAME = 3;
	public static int PHONE = 4;
	public static String pattern = "Q/(.*)/(.*)/(.*)/(.*)";
	
	public static String getMessage (Context context) {
		ContentResolver res = context.getContentResolver();
		String where = FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID +"=?";
		String args[] = {Services.PHONE.id + ""};
		Cursor cursor = res.query(FriendsProvider.MY_URI, null, where, args, null);
		cursor.moveToFirst();
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.app_package_name), Context.MODE_PRIVATE);
		String user_id = pref.getString("user_id", "noid");
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
    	String phone = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
		return encodeMessage (user_id, first_name, last_name, phone);
	}
	
	
	public static String encodeMessage (String id, String first, String last, String phone) {
		return "Q/" + id +"/" + first + "/" + last + "/"+ phone;
	}
	
	public static ArrayList<String> decodeMessage (String msg) {
		Pattern pat = Pattern.compile(pattern);
		Matcher m = pat.matcher(msg);
		if (m.matches()) {
			ArrayList<String> values = new ArrayList<String>();
			values.add(0, msg);
			values.add(ID, m.group(ID));
			values.add(FIRST_NAME, m.group(FIRST_NAME));
			values.add(LAST_NAME, m.group(LAST_NAME));
			values.add(PHONE, m.group(PHONE));
			return values;
		}
		return null;
	}
}
