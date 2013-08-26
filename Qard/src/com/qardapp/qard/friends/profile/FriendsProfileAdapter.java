package com.qardapp.qard.friends.profile;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.friends.profile.services.ServiceManager;

public class FriendsProfileAdapter extends BaseAdapter{

	 static class ViewHolder {
	    	ImageView image;
	    	ServiceManager manager;
	    }

	private Context context;
	private String dataList[] = new String[Services.values().length+1];
	private BaseFragment bf;
	 
	public FriendsProfileAdapter(Context context, Cursor cursor, BaseFragment bf) {
		super();
		this.context = context;
		this.bf = bf;
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
				for (Services ser : Services.values()) {
					if (ser.id == serviceId)
						holder.manager = ser.getManager((Activity) context, bf);
				}
		        holder.manager.addData(data);
			}
        return convertView;
	}

	
}
