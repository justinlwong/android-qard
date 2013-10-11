package com.qardapp.qard.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.settings.services.UpdateDatabase;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;


public class SettingsProfileAdapter extends BaseAdapter{

	private Activity activity;
	private BaseFragment bf;
	private String dataList[] = new String[Services.values().length+1];
	private String userList[] = new String[Services.values().length+1];

	private ArrayList<Services> serviceList;
	private ArrayList<Drawable> drawableList;
	private Drawable lightBackground, darkBackground;
	private SharedPreferences mPrefs;
	
	public SettingsProfileAdapter(Activity activity, Cursor cursor, BaseFragment bf) {
		super();
		this.activity = activity;
		this.bf = bf;
		serviceList = new ArrayList<Services>();
		drawableList = new ArrayList<Drawable>();
		lightBackground = activity.getResources().getDrawable(R.drawable.settings_service_item_button_background);
		darkBackground = activity.getResources().getDrawable(R.drawable.settings_service_item_button_background_dark);

		for (Services ser : Services.values()) {
			serviceList.add(ser);
		}
		Collections.sort(serviceList, new Comparator<Services>() {

			@Override
			public int compare(Services lhs, Services rhs) {
				return lhs.priority - rhs.priority;
			}
		});
		for (Services ser : serviceList) {
			drawableList.add(activity.getResources().getDrawable(ser.imageId));
		}
		changeCursor(cursor);
		
		// Shared Prefs to get username
        mPrefs = activity.getSharedPreferences("tokens", 0);
	}
	
	@Override
	public int getCount() {
		return Services.values().length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
    		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_service_item, null);
    		holder = new ViewHolder();
    		holder.image = (ImageView) convertView.findViewById(R.id.settings_service_item_image);
    		holder.button = (Button) convertView.findViewById(R.id.settings_service_item_button);
    		convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.image.setImageDrawable(drawableList.get(position));
		holder.button.setTag(serviceList.get(position));
		
		// Add conditional text here
		if (dataList[position] == null) {
			holder.button.setText("Enable " + serviceList.get(position).name);
			holder.button.setTextColor(Color.WHITE);
			holder.button.setBackground(darkBackground.getConstantState().newDrawable());
		}
		else {
			holder.button.setText(userList[position]);
			holder.button.setTextColor(Color.DKGRAY);
			holder.button.setBackground(lightBackground.getConstantState().newDrawable());
		}
		holder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Services)v.getTag()).getManager(activity, bf).startLoginIntent();
			}
		});
		
		holder.button.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				PopupMenu mypopupmenu = new PopupMenu(activity, v);
				MenuInflater inflater = mypopupmenu.getMenuInflater();
				inflater.inflate(R.layout.stock_item_menu, mypopupmenu.getMenu());
				mypopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId())
						{
						case R.id.edit_item:
							((Services)holder.button.getTag()).getManager(activity, bf).startLoginIntent();	
							return true;
						case R.id.remove_item:
							Log.d("here", "removing");
							UpdateDatabase.updateDatabase(null, serviceList.get(position).id, activity);
			                // Refresh settings page when the service call is not an activity (eg. PopupDialog)
			                if (activity instanceof MainActivity) {
			                	((MainActivity)activity).refreshFragments();
			                }
			
						default:
						}
						return false;
					}
				});
				mypopupmenu.show();
				return false;
			}
		});
		
		return convertView;
	}
	
	public void changeCursor (Cursor cursor) {
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					int index = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_SERVICE_PRIORITY));
					dataList[index-1] = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FS_DATA));
					// need to change this, since loading the profile won't already have this stored in preferences
					// maybe on user sign in, do a quick set of api requests and then save the preferences?
					userList[index-1] = mPrefs.getString(serviceList.get(index-1).name+"_username","-1");
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		notifyDataSetChanged();
	}
	
    static class ViewHolder {
    	ImageView image;
    	Button button;
    }
	

}
