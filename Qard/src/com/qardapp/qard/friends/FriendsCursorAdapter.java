package com.qardapp.qard.friends;

import java.io.File;

import com.qardapp.qard.R;
import com.qardapp.qard.database.FriendsDatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsCursorAdapter extends CursorAdapter{

	 static class ViewHolder {
	    	TextView name;
	    	TextView phone;
	    	ImageView profilePic;
	    }

	public FriendsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
		String phone = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
		
    	holder.name.setText(first_name + " " + last_name);
    	if (phone != null)
    		holder.phone.setText(PhoneNumberUtils.formatNumber(phone));
    	
    	// Display Profile Pic
    	String profilePicFile = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_PROFILE_PIC_LOC));
    	if (profilePicFile == null || profilePicFile.isEmpty())
    		holder.profilePic.setImageResource(R.drawable.profile_default);
    	else {
    		File filePath = context.getFileStreamPath(profilePicFile);
    		holder.profilePic.setImageDrawable(Drawable.createFromPath(filePath.toString()));
    	}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root) {
		ViewHolder holder = new ViewHolder();
		View view = LayoutInflater.from(context).inflate(R.layout.friends_item, null);
    	holder.name = (TextView) view.findViewById(R.id.friends_name);
    	holder.phone = (TextView) view.findViewById(R.id.friends_phone);
    	holder.profilePic = (ImageView) view.findViewById(R.id.friends_profile_pic);
    	view.setTag(holder);
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
    	holder.name.setText(first_name + " " + last_name);
    	String phone = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
    	if (phone != null)
    		holder.phone.setText(PhoneNumberUtils.formatNumber(phone));
    	// Display Profile Pic
    	String profilePicFile = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_PROFILE_PIC_LOC));
    	if (profilePicFile == null || profilePicFile.isEmpty())
    		holder.profilePic.setImageResource(R.drawable.profile_default);
    	else {
    		File filePath = context.getFileStreamPath(profilePicFile);
    		holder.profilePic.setImageDrawable(Drawable.createFromPath(filePath.toString()));
    	}
        return view;
	}

	
}
