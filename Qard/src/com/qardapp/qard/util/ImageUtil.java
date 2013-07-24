package com.qardapp.qard.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.qardapp.qard.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

public class ImageUtil {
	
	
	private static Bitmap DEFAULT_PROFILE_PIC;
	
	public static void getFBProfilePic(Context context, String uname, int id) {
    	Log.d("here",uname);
    	URL image_value;
		try {
			image_value = new URL("https://graph.facebook.com/"+uname+"/picture?width=250&height=250" );
	    	Log.d("here", image_value.toString());
	    	//OAuthRequest request = new OAuthRequest(Verb.GET,"http://graph.facebook.com/"+uname+"?fields=picture")
			//URL image_value = new URL("http://graph.facebook.com/" + uname+ "/picture?type=large" );
			BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inScaled = false;
		    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		    options.inDither = false;
	    	Bitmap profPict = BitmapFactory.decodeStream(image_value.openConnection().getInputStream(), null, options);
			Log.d("here","gotprofilepic");
	        ImageUtil.createProfilePic(context, id, profPict);
	        Log.d("here", "added profile picture");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Bitmap getProfilePic (Context context, int id, String  profilePicFile) {
		
	    String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			if (context.getExternalFilesDir(null) != null) {
				File file = new File(context.getExternalFilesDir(null).toString() + "/profile/" + id + ".png");
			    if (file.exists()) {
		    		Log.d("test", "Using saved image");
		
			    	BitmapFactory.Options options = new BitmapFactory.Options();
				    options.inScaled = false;
				    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				    options.inDither = false;
					return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			    }
			}
		}
	    if (profilePicFile == null || profilePicFile.isEmpty()) {
			return createProfilePic (context, id, null);
		} else {
			return createProfilePic (context, id, null);
		}
	}
	
	public static Bitmap createProfilePic (Context context,int id, Bitmap bitmap) {
		if (bitmap == null) {
			if (DEFAULT_PROFILE_PIC == null)
				DEFAULT_PROFILE_PIC = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_default);
			bitmap = DEFAULT_PROFILE_PIC;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inScaled = false;
	    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    options.inDither = false;
	    
	    int size = bitmap.getHeight() < bitmap.getWidth() ? bitmap.getHeight(): bitmap.getWidth();
	    
		Bitmap output = Bitmap.createBitmap(size,
				size, Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    
		Bitmap crop = BitmapFactory.decodeResource(context.getResources(), R.drawable.circle_crop, options);
/*
	    Bitmap scaledCrop = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	    Log.d("size", size + " " + crop.getWidth());
	    float ratioX = size / (float) crop.getWidth();
	    float ratioY = size / (float) crop.getHeight();
	    float middleX = size / 2.0f;
	    float middleY = size / 2.0f;
	    Matrix scaleMatrix = new Matrix();
	    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

	    Canvas canvas1 = new Canvas(scaledCrop);
	    canvas1.setMatrix(scaleMatrix);
	    canvas1.drawBitmap(crop, middleX- crop.getWidth() /2, middleY- crop.getHeight() /2, new Paint(Paint.FILTER_BITMAP_FLAG));*/
		crop =  Bitmap.createScaledBitmap(crop, size, size, true);

	    Paint paint = new Paint();
	    final Rect rect = new Rect((bitmap.getWidth() - size) /2, (bitmap.getHeight() - size) /2, size, size);
	    final Rect finalRect = new Rect(0, 0, size, size);

	    paint.setAntiAlias(true);
	    paint.setFilterBitmap(true);
	    paint.setDither(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(0xff000000);
	    canvas.drawCircle(size / 2, size / 2,
	            bitmap.getWidth() / 2 - 2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setFilterBitmap(true);
	    paint.setDither(true);
	    canvas.drawBitmap(crop, rect, finalRect, paint);

	    
	    boolean mExternalStorageAvailable = false;
	    boolean mExternalStorageWriteable = false;
	    String state = Environment.getExternalStorageState();

	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        // We can read and write the media
	        mExternalStorageAvailable = mExternalStorageWriteable = true;
	    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        // We can only read the media
	        mExternalStorageAvailable = true;
	        mExternalStorageWriteable = false;
	    } else {
	        // Something else is wrong. It may be one of many other states, but all we need
	        //  to know is we can neither read nor write
	        mExternalStorageAvailable = mExternalStorageWriteable = false;
	    }
	    
	    if (mExternalStorageWriteable) {
	    	try {
	    		Log.d("test", "Saving to file...");
	    	File dir = new File(context.getExternalFilesDir(null).toString() + "/profile/");
	    	dir.mkdirs();
	    	File f = new File(dir, id + ".png");
	    	FileOutputStream out = new FileOutputStream(f);
	    	output.compress(Bitmap.CompressFormat.PNG, 90, out);
	    	Log.d("test", "Saved to file");
	    	} catch (Exception e) {
				// TODO: handle exception
			}
	    }
	    
	    return output;
	}
}
