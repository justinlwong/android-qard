package com.qardapp.qard.friends;

import com.qardapp.qard.R;
import com.qardapp.qard.database.FriendsDatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsCursorAdapter extends CursorAdapter{

	 static class ViewHolder {
	    	TextView name;
	    }

	public FriendsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));

    	holder.name.setText(first_name + " " + last_name);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup root) {
		ViewHolder holder = new ViewHolder();
		View view = LayoutInflater.from(context).inflate(R.layout.friends_item, null);
    	holder.name = (TextView) view.findViewById(R.id.friends_name);
    	view.setTag(holder);
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
    	holder.name.setText(first_name + " " + last_name);
        return view;
	}

	
}
