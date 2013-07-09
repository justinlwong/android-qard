package com.qardapp.qard.friends.profile;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.friends.profile.services.DefaultServiceManager;
import com.qardapp.qard.friends.profile.services.FacebookServiceManager;
import com.qardapp.qard.friends.profile.services.FoursquareServiceManager;
import com.qardapp.qard.friends.profile.services.GmailServiceManager;
import com.qardapp.qard.friends.profile.services.GooglePlusServiceManager;
import com.qardapp.qard.friends.profile.services.PhoneServiceManager;
import com.qardapp.qard.friends.profile.services.ServiceManager;
import com.qardapp.qard.friends.profile.services.TumblrServiceManager;
import com.qardapp.qard.friends.profile.services.TwitterServiceManager;
import com.qardapp.qard.friends.profile.services.WhatsAppServiceManager;

public class FriendsProfileAdapter extends BaseAdapter{

	 static class ViewHolder {
	    	ImageView image;
	    	ServiceManager manager;
	    }

	private Context context;
	private String dataList[] = new String[Services.values().length+1];
	 
	public FriendsProfileAdapter(Context context, Cursor cursor) {
		super();
		this.context = context;
		changeCursor(cursor);
	}
	
	public void changeCursor (Cursor cursor) {
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					int index = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_SERVICE_ID));
					dataList[index] = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		notifyDataSetChanged();
	}
/*
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
		final ViewHolder holder = new ViewHolder();
		View view = LayoutInflater.from(context).inflate(R.layout.profile_service_item, null);
		holder.image = (ImageView) view.findViewById(R.id.profile_service_item_image);    	view.setTag(holder);
		int serviceId = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_SERVICE_ID));
		
		for (Services service : Services.values()) {
			if (service.id == serviceId)
				holder.image.setImageResource(service.imageId);
		}
		
		// Set up service managers depending on service type	
		String data = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));		
        if (serviceId == Services.FACEBOOK.id)
		{
        	holder.manager = new FacebookServiceManager((Activity) context,data);					
		} else if (serviceId == Services.TWITTER.id) {
			holder.manager = new TwitterServiceManager((Activity) context,data);		
		} else if (serviceId == Services.FLICKR.id) {
			holder.manager = new DefaultServiceManager((Activity) context, Services.FLICKR.imageId, Services.FLICKR.id, data);		
		} else if (serviceId == Services.PHONE.id) {
			holder.manager = new PhoneServiceManager((Activity) context,data);	
        } else if (serviceId == Services.INSTAGRAM.id) {
        	holder.manager = new DefaultServiceManager((Activity) context, Services.INSTAGRAM.imageId, Services.INSTAGRAM.id, data);	
        } else if (serviceId == Services.LINKEDIN.id) {
        	holder.manager = new DefaultServiceManager((Activity) context, Services.LINKEDIN.imageId, Services.LINKEDIN.id, data);	
        } else if (serviceId == Services.FOURSQUARE.id) {
        	holder.manager = new FoursquareServiceManager((Activity) context,data);		
        } else if (serviceId == Services.GMAIL.id) {
        	holder.manager = new GmailServiceManager((Activity) context, data);	        			
        } else if (serviceId == Services.GOOGLEPLUS.id) {
        	holder.manager = new GooglePlusServiceManager((Activity) context, data);	        			
        }
        
	        holder.image.setOnClickListener(new ImageView.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (holder.manager != null)
						holder.manager.switchToService();				
				}
			});
        return view;
	}
*/
	
	@Override
	public int getCount() {
		return Services.values().length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.profile_service_item, null);
			holder.image = (ImageView) convertView.findViewById(R.id.profile_service_item_image);
			holder.image.setOnClickListener(new ImageView.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (holder.manager != null)
						holder.manager.switchToService();				
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			int serviceId = -1;
			String data = null;
			for (Services service : Services.values()) {
				if (service.priority == position+1) {
					serviceId = service.id;
					data = dataList[serviceId];	
					if (data == null)
						holder.image.setImageResource(service.grayImageId);
					else
						holder.image.setImageResource(service.imageId);
					break;
				}
			}
        	holder.manager = null;
			if (data != null) {
				// Set up service managers depending on service type		
		        if (serviceId == Services.FACEBOOK.id)
				{
		        	holder.manager = new FacebookServiceManager((Activity) context,data);					
				} else if (serviceId == Services.TWITTER.id) {
					holder.manager = new TwitterServiceManager((Activity) context,data);		
				} else if (serviceId == Services.FLICKR.id) {
					holder.manager = new DefaultServiceManager((Activity) context, Services.FLICKR.imageId, Services.FLICKR.id, data);		
				} else if (serviceId == Services.PHONE.id) {
					holder.manager = new PhoneServiceManager((Activity) context,data);	
		        } else if (serviceId == Services.INSTAGRAM.id) {
		        	holder.manager = new DefaultServiceManager((Activity) context, Services.INSTAGRAM.imageId, Services.INSTAGRAM.id, data);	
		        } else if (serviceId == Services.LINKEDIN.id) {
		        	holder.manager = new DefaultServiceManager((Activity) context, Services.LINKEDIN.imageId, Services.LINKEDIN.id, data);	
		        } else if (serviceId == Services.FOURSQUARE.id) {
		        	holder.manager = new FoursquareServiceManager((Activity) context,data);		
		        } else if (serviceId == Services.EMAIL.id) {
		        	holder.manager = new GmailServiceManager((Activity) context, data);	        			
		        } else if (serviceId == Services.GOOGLEPLUS.id) {
		        	holder.manager = new GooglePlusServiceManager((Activity) context, data);	        			
		        } else if (serviceId == Services.WHATSAPP.id) {
					holder.manager = new WhatsAppServiceManager((Activity) context, data);	        			
		        } else if (serviceId == Services.TUMBLR.id) {
					holder.manager = new TumblrServiceManager((Activity) context, data);	        			
		        }
			}
        return convertView;
	}

	
}
