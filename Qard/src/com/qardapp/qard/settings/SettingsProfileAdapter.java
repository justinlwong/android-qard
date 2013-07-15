package com.qardapp.qard.settings;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;

public class SettingsProfileAdapter extends BaseAdapter{

	private Activity activty;
	
	public SettingsProfileAdapter(Activity activity) {
		super();
		this.activty = activity;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
    		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_service_item, null);
    		holder = new ViewHolder();
    		holder.image = (ImageView) convertView.findViewById(R.id.settings_service_item_image);
    		holder.button = (Button) convertView.findViewById(R.id.settings_service_item_button);
    		convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int serviceId = -1;
		Services service = Services.values()[0];
		for (Services ser : Services.values()) {
			if (ser.priority == position+1) {
				service = ser;
				serviceId = ser.id;
				holder.image.setImageResource(ser.imageId);
				break;
			}
		}
		holder.button.setText("Sign in to " + service.name);
		
		return convertView;
	}
	
    static class ViewHolder {
    	ImageView image;
    	Button button;
    	Services service;
    	boolean active;
    }
	

}
