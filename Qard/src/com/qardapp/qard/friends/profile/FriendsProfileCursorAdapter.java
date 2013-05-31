package com.qardapp.qard.friends.profile;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.friends.profile.services.FacebookServiceManager;
import com.qardapp.qard.friends.profile.services.ServiceManager;
import com.qardapp.qard.friends.profile.services.TwitterServiceManager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsProfileCursorAdapter extends CursorAdapter{

	 static class ViewHolder {
	    	ImageView image;
	    }

	public FriendsProfileCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		int serviceId = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_SERVICE_ID));
		
		for (Services service : Services.values()) {
			if (service.id == serviceId)
				holder.image.setImageResource(service.imageId);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root) {
		ViewHolder holder = new ViewHolder();
		View view = LayoutInflater.from(context).inflate(R.layout.profile_service_item, null);
		holder.image = (ImageView) view.findViewById(R.id.profile_service_item_image);    	view.setTag(holder);
		int serviceId = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_SERVICE_ID));
		
		for (Services service : Services.values()) {
			if (service.id == serviceId)
				holder.image.setImageResource(service.imageId);
		}
		
		// Set up service managers depending on service type
		ServiceManager sMgr = null;	
		String data = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));		
        if (serviceId == Services.FACEBOOK.id)
		{
			sMgr = new FacebookServiceManager((Activity) context,data);					
		} else if (serviceId == Services.TWITTER.id) {
			sMgr = new TwitterServiceManager((Activity) context,data);		
	    }
        
        final ServiceManager mgr = sMgr;
        
        // Switch to service activity on user click
        holder.image.setOnClickListener(new ImageView.OnClickListener() {
			@Override
			public void onClick(View v) {
				mgr.switchToService();				
			}
		});
		
        return view;
	}

	
}
