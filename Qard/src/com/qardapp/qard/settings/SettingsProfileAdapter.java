package com.qardapp.qard.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

public class SettingsProfileAdapter extends BaseAdapter{

	private Activity activty;
	
	private ArrayList<Services> serviceList;
	
	public SettingsProfileAdapter(Activity activity) {
		super();
		this.activty = activity;
		serviceList = new ArrayList<Services>();
		for (Services ser : Services.values()) {
			serviceList.add(ser);
		}
		Collections.sort(serviceList, new Comparator<Services>() {

			@Override
			public int compare(Services lhs, Services rhs) {
				return lhs.priority - rhs.priority;
			}
		});
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
		holder.image.setImageResource(serviceList.get(position).imageId);
		holder.button.setTag(serviceList.get(position));
		
		// Add conditional text here
		holder.button.setText("Sign in to " + serviceList.get(position).name);
		holder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Services)v.getTag()).getManager(activty).startLoginIntent();
			}
		});
		return convertView;
	}
	
    static class ViewHolder {
    	ImageView image;
    	Button button;
    }
	

}
