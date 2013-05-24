package com.qardapp.qard.profile;


import com.qardapp.qard.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
 
public class ProfileServicesAdapter extends BaseAdapter {

	
    // Keep all Images in array
    public Integer[] mThumbIds = { R.drawable.phone,
            R.drawable.facebook, R.drawable.twitter,
            R.drawable.foursquare, R.drawable.gmail,
            R.drawable.flickr, R.drawable.gplus,
            R.drawable.instagram, R.drawable.line,
            R.drawable.linkedin, R.drawable.pinterest,
            R.drawable.tumblr, R.drawable.whatsapp,
            
    };

    @Override
    public int getCount() {
        return mThumbIds.length;
    }
    
    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	if (convertView == null) {
    		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_service_item, null);
    		holder = new ViewHolder();
    		holder.image = (ImageView) convertView.findViewById(R.id.profile_service_item_image);
    		convertView.setTag(holder);
    	} else {
			holder = (ViewHolder) convertView.getTag();
		}
    	holder.image.setImageResource(mThumbIds[position]);
    	//holder.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    	//holder.image.setLayoutParams(new GridView.LayoutParams(200, 200));
        return convertView;
    }
    
    static class ViewHolder {
    	ImageView image;
    }
 
}
