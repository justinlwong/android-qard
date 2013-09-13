package com.qardapp.qard.settings.services;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.friends.profile.FriendProfileFragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SendInviteActivity{
	public static void sendInvite(Context context){
		ContentResolver cr = context.getContentResolver();
		String fprefix = FriendsDatabaseHelper.TABLE_FRIENDS + ".";
		String fsprefix = FriendsDatabaseHelper.TABLE_FRIEND_SERVICES + ".";
		//String where = fprefix+FriendsDatabaseHelper.COLUMN_USER_ID+ "=? AND " + fsprefix+FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
		String where = fprefix + FriendsDatabaseHelper.COLUMN_USER_ID + " IS NULL";
		//String[] args = new String[] {user_id, String.valueOf(Services.PHONE.id)};
		String[] args = new String[] {};
		Cursor c = cr.query(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, ""), new String[]{fsprefix+FriendsDatabaseHelper.COLUMN_FS_DATA}, where, args, null);
		
		if (c.moveToFirst())
		{
			while (c.isAfterLast() == false)
			{
				String phone_no = c.getString(c.getColumnIndex(fsprefix+FriendsDatabaseHelper.COLUMN_FS_DATA));  
				Log.d("invite",phone_no);
				/*
				setContentView(R.layout.friends_profile);
				Button mButton=(Button)findViewById(R.id.send_btn);
				mButton.setTextColor(Color.parseColor("#FF0000")); // custom color
				//mButton.setTextColor(Color.RED); // use default color
				mButton.setBackgroundResource(R.drawable.button_shape);
				
				Button login = (Button) rootView.findViewById(R.id.login_btn);
				login.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),FacebookLoginActivity.class);
						intent.putExtra("launchType", 1);
						getActivity().startActivityForResult(intent,0);
					}
				});
				*/
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
