package com.qardapp.qard.friends;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qardapp.qard.R;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.util.ImageUtil;

public class FriendsCursorAdapter extends CursorAdapter implements Filterable{
	
    AlphabetIndexer mAlphabetIndexer;

    private LruCache<Integer, Bitmap> imageCache;
    //private Bitmap defaultPic;
    private static int EAGER_BUFFER = 12;
    private SparseBooleanArray eagerMap;
    
	 static class ViewHolder {
	    	TextView name;
	    	TextView phone;
	    	ImageView profilePic;
	    	ProfileImageWorkerTask profileTask;
	    	LinearLayout statusLayout;
	    }

	public FriendsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		eagerMap = new SparseBooleanArray();
	    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	    final int cacheSize = maxMemory / 4;
	    Log.d("Cache", cacheSize +"");
	    imageCache = new LruCache<Integer, Bitmap>(cacheSize) {
	        @Override
	        protected int sizeOf(Integer key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	            return bitmap.getByteCount() / 1024;
	        }
	    };
	    //defaultPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_default);
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
    	//Log.d("id",full_name +" "+id);

    	Bitmap bitmap = imageCache.get(id);
    	if (bitmap == null) {
    		if (holder.profileTask == null) {
	    		holder.profileTask = new ProfileImageWorkerTask(context, holder.profilePic);
	    		holder.profileTask.execute(id);
    		} else {
    	    	if (holder.profileTask.getId() != id) {
    	    		holder.profileTask.cancel(true);
    	    		holder.profilePic.setImageBitmap(null);
    	        	holder.profileTask = new ProfileImageWorkerTask(context, holder.profilePic);
    	        	holder.profileTask.execute(id);
    	    	} 			
    		}
    	} else {
    		if (holder.profileTask != null)
    			holder.profileTask.cancel(true);
        	holder.profilePic.setImageBitmap(bitmap);
    	}
    	
    	int pos = cursor.getPosition();
    	if (pos % EAGER_BUFFER == 1) {
	    	while (cursor.moveToPrevious()) {
	    		int eager_id = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));
	    		if (imageCache.get(eager_id) == null && eagerMap.get(eager_id) == false){
	    			eagerMap.put(eager_id, true);
		    		holder.profileTask = new ProfileImageWorkerTask(context, null);
		    		holder.profileTask.execute(eager_id);
	    		}
	    		if (cursor.getPosition() == pos - EAGER_BUFFER)
	    			break;
	    	}
	    	
	    	cursor.moveToPosition(pos);
	    	while (cursor.moveToNext()) {
	    		int eager_id = cursor.getInt(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_ID));
	    		if (imageCache.get(eager_id) == null && eagerMap.get(eager_id) == false){
	    			eagerMap.put(eager_id, true);
		    		holder.profileTask = new ProfileImageWorkerTask(context, null);
		    		holder.profileTask.execute(eager_id);
	    		}
	    		if (cursor.getPosition() == pos + EAGER_BUFFER)
	    			break;
	    	}
	    	cursor.moveToPosition(pos);
    	}
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
    	//String profilePicFile = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_PROFILE_PIC_LOC));
    	Bitmap bitmap = imageCache.get(id);
    	holder.profilePic.setImageBitmap(bitmap);
    	if (bitmap == null) {
    		holder.profileTask = new ProfileImageWorkerTask(context, holder.profilePic);
    		holder.profileTask.execute(id);
    	}
    	else
    		holder.profileTask = null;
    	
    	
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
	
	class ProfileImageWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	    private final WeakReference<ImageView> imageViewReference;
	    private int id = 0;
	    private Context context;
	    
	    public ProfileImageWorkerTask(Context context, ImageView imageView) {
	        imageViewReference = new WeakReference<ImageView>(imageView);
	        this.context = context;
	    }

	    public int getId () {
	    	return id;
	    }
	    
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
	    	id = params[0];
	    	Bitmap bitmap = imageCache.get(id);
	    	if (bitmap == null) {
	    		bitmap = ImageUtil.getProfilePic(context, id);
	    		imageCache.put(id, bitmap);
	    		eagerMap.delete(id);
	    	}
	        return bitmap;
	    }

	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	    	if (isCancelled()) {
	            bitmap = null;
	            return;
	    	}
	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            if (imageView != null && !isCancelled()) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	}
	
}
