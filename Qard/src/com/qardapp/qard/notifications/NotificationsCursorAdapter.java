package com.qardapp.qard.notifications;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AlphabetIndexer;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qardapp.qard.R;
import com.qardapp.qard.comm.server.AddFriendTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.util.ImageUtil;

public class NotificationsCursorAdapter extends CursorAdapter{
    
	 static class ViewHolder {
	    	TextView name;
	    	Button addButton;
	    	int id;
	    }

	public NotificationsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

	    //defaultPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_default);
	}

	@Override
	public void bindView(View convertView, final Context context, final Cursor cursor) {
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
//		String phone = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
		holder.id = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_USER_ID));
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
		
    	holder.name.setText(full_name+ " wants to be your friend.");
    	
		holder.addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AddFriendTask task = new AddFriendTask(context, holder.id+"");
				task.execute();
				//holder.name.setText("Confirming...");
				//((RelativeLayout)holder.addButton.getParent()).removeView(holder.addButton);
			}
		});
    	
    	
//    	if (phone != null)
//    		holder.phone.setText(PhoneNumberUtils.formatNumber(phone));
    	
    	// Display Profile Pic
    	//Log.d("id",full_name +" "+id);


	}

	@Override
	public View newView(final Context context, final Cursor cursor, ViewGroup root) {
		final ViewHolder holder = new ViewHolder();
		View view = LayoutInflater.from(context).inflate(R.layout.notifications_item, null);
    	holder.name = (TextView) view.findViewById(R.id.friends_name);
    	Log.d("status", String.valueOf(cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_CONFIRMED))));
    	//holder.phone = (TextView) view.findViewById(R.id.friends_phone);
    	//holder.statusLayout = (LinearLayout) view.findViewById(R.id.friends_status_icons);
    	view.setTag(holder);
		//int id = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));

		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
		String full_name = first_name;
		if (last_name.length() > 0)
		{
			full_name += " " + last_name.charAt(0) + ".";
		}
    	holder.name.setText(full_name + " wants to be your friend.");
    	holder.id = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_USER_ID));
    	   	
    	holder.addButton = (Button)view.findViewById(R.id.add_button);

		holder.addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AddFriendTask task = new AddFriendTask(context, holder.id+"");
				task.execute();
				//holder.name.setText("Confirming...");
				//((RelativeLayout)holder.addButton.getParent()).removeView(holder.addButton);
			}
		});

        return view;
	}
	
}