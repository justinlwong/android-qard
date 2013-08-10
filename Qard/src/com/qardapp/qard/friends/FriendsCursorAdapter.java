package com.qardapp.qard.friends;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.qardapp.qard.R;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.util.ImageUtil;

public class FriendsCursorAdapter extends CursorAdapter implements Filterable{
	
    AlphabetIndexer mAlphabetIndexer;

	 static class ViewHolder {
	    	TextView name;
	    	TextView phone;
	    	ImageView profilePic;
	    	LinearLayout statusLayout;
	    }

	public FriendsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
//		String phone = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
		int id = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));
		String full_name;
		
		if (first_name.length() > 14) {
			// Add .. to show that the name was too long and got cut off
			full_name = first_name.substring(0,12) + "..";
		} else {
			full_name = first_name;
		}
		
		if (last_name.length() > 0)
		{
			full_name += " " + last_name.charAt(0) + ".";
		}
		
    	holder.name.setText(full_name);
//    	if (phone != null)
//    		holder.phone.setText(PhoneNumberUtils.formatNumber(phone));
    	
    	// Display Profile Pic
    	String profilePicFile = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_PROFILE_PIC_LOC));
    	holder.profilePic.setImageBitmap(ImageUtil.getProfilePic(context, id, profilePicFile));

//    	holder.statusLayout.removeAllViews();
//    	if (cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_CONFIRMED)) == 0 &&
//    			cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_HIDE_CONFIRMED)) == 0) {
//    		ImageView status = new ImageView(context);
//    		status.setImageResource(R.drawable.icon_exclamation_red);
//    		status.setScaleType(ScaleType.CENTER_INSIDE);
//    		holder.statusLayout.addView(status);
//    	}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root) {
		ViewHolder holder = new ViewHolder();
		View view = LayoutInflater.from(context).inflate(R.layout.friends_item, null);
    	holder.name = (TextView) view.findViewById(R.id.friends_name);
    	//holder.phone = (TextView) view.findViewById(R.id.friends_phone);
    	holder.profilePic = (ImageView) view.findViewById(R.id.friends_profile_pic);
    	//holder.statusLayout = (LinearLayout) view.findViewById(R.id.friends_status_icons);
    	view.setTag(holder);
		int id = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));

		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
		String full_name = first_name;
		if (last_name.length() > 0)
		{
			full_name += " " + last_name.charAt(0) + ".";
		}
    	holder.name.setText(full_name);
//    	String phone = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
//    	if (phone != null)
//    		holder.phone.setText(PhoneNumberUtils.formatNumber(phone));
    	// Display Profile Pic
    	String profilePicFile = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_PROFILE_PIC_LOC));
    	holder.profilePic.setImageBitmap(ImageUtil.getProfilePic(context, id, profilePicFile));
    	
//    	if (cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_CONFIRMED)) == 0 &&
//    			cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_HIDE_CONFIRMED)) == 0) {
//    		ImageView status = new ImageView(context);
//    		status.setImageResource(R.drawable.icon_exclamation_red);
//    		status.setScaleType(ScaleType.CENTER_INSIDE);
//
//    		holder.statusLayout.addView(status);
//    	}
        return view;
	}
	
}
