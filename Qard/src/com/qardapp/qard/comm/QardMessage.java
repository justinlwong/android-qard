package com.qardapp.qard.comm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.ServerHelper;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class QardMessage {
	public static int ID = 1;
	public static int FIRST_NAME = 2;
	public static int LAST_NAME = 3;
	public static int PHONE = 4;
	public static String pattern = "Q/(.*)/(.*)/(.*)/(.*)";
	
	public static String getMessage (Context context) {
		ContentResolver res = context.getContentResolver();
		String where = FriendsDatabaseHelper.COLUMN_SERVICE_ID +"=?";
		String args[] = {Services.PHONE.id + ""};
		Cursor cursor = res.query(FriendsProvider.MY_URI, null, where, args, null);
		cursor.moveToFirst();
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.app_package_name), Context.MODE_PRIVATE);
		String user_id = pref.getString("user_id", "noid");
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
    	String phone = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
    	if (phone == null)
    		phone = "";
		return encodeMessage (user_id, first_name, last_name, phone);
	}
	
	
	public static String encodeMessage (String id, String first, String last, String phone) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("f", first));
		params.add(new BasicNameValuePair("l", last));
		if (phone == null)
			params.add(new BasicNameValuePair("p", phone));
		String url = ServerHelper.FRONT_SERVER_URL + "/user/" + id + "?" + URLEncodedUtils.format(params, "utf-8");;
		return url;
	}
	
	public static ArrayList<String> decodeMessage (String msg) {
		Uri url = Uri.parse(msg);
		ArrayList<String> values = new ArrayList<String>();
		values.add(0, msg);
		values.add(ID, url.getLastPathSegment());
		values.add(FIRST_NAME, url.getQueryParameter("f"));
		values.add(LAST_NAME, url.getQueryParameter("l"));
		values.add(PHONE, url.getQueryParameter("p"));
		return values;
	}
}
