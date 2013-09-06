package com.qardapp.qard.settings.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.AddServiceTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.util.ImageUtil;

public class FacebookLoginActivity extends Activity {

    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private Activity activity = this;
	private ProgressDialog progDialog;
	int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.oauth_layout);
		
		type = getIntent().getIntExtra("launchType", 0);
		
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        
        Session.openActiveSession(this, true, statusCallback);
        progDialog = new ProgressDialog(FacebookLoginActivity.this);
        progDialog.setMessage("Connecting...");
        progDialog.setIndeterminate(true);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setCancelable(false);
        progDialog.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }


    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(final Session session, SessionState state, Exception exception) {
        	Log.d("here", "got here");
        	Log.d("here", String.valueOf(session.isOpened()));
        	if (session.isOpened()) {

	            Request.executeMeRequestAsync(session,
	                    new GraphUserCallback() {
                        private SharedPreferences mPrefs;

						@Override
                        public void onCompleted(GraphUser user,
                                Response response) {
                            Log.d("here", user.toString());
                            if (user != null) {
                            	String userId = null;
                                Log.d("User",user.getId());
                            
                                userId = user.getId();
                                final String name = user.getName();
                                final String uname = user.getUsername();
                                //Log.d("here", user.picture);
                        		// Shared Prefs to get username
                                mPrefs = activity.getSharedPreferences("tokens", 0);
				        		SharedPreferences.Editor editor = mPrefs.edit();
				        		editor.putString("Facebook_username",name);
				        		final String accessToken = session.getAccessToken();
				        		editor.putString("Facebook_access_token", session.getAccessToken());
				        		editor.commit();
				        		
				        		// Get picture
				        		
				        		Thread thread = new Thread(new Runnable(){
				        		    @Override
				        		    public void run() {
				        		        ImageUtil.getFBProfilePic(activity, uname, 0);
				        		    }
				        		});

				        		thread.start(); 

                                
                                AddServiceTask task = new AddServiceTask(FacebookLoginActivity.this, Services.FACEBOOK.id, userId, session.getAccessToken());
                                task.execute();
	    						ContentResolver res = getContentResolver();
	    						ContentValues values = new ContentValues();
	    						values.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, 0);
	    						values.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, Services.FACEBOOK.id);
	    						values.put(FriendsDatabaseHelper.COLUMN_FS_DATA, userId);
	    						
	    						// Delete existing entry and replace
	    						String where = FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=? AND " + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
	    						String[] args = new String[] { "0", String.valueOf(Services.FACEBOOK.id)};
	    						res.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+Services.FACEBOOK.id), where, args);
	    						res.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+Services.FACEBOOK.id), values);
				        		runOnUiThread(new Runnable() {
				        		    public void run() {
				        		    	if (type == 0) {
						                    Toast.makeText(activity, "Added " + Services.FACEBOOK.name + " information!", Toast.LENGTH_LONG).show();
				        		    	} else {
						                    Toast.makeText(activity, "Added credentials!", Toast.LENGTH_LONG).show();				        		    		
				        		    	}
				        		    }
				        		});
				        		if (progDialog != null)
				        		{
				        			if (progDialog.isShowing())
				        			{
	    						    progDialog.cancel();
				        			}
				        		}
				        		Intent intent1 = new Intent(FacebookLoginActivity.this, MainActivity.class);
				        		FacebookLoginActivity.this.startActivity(intent1);
	    						activity.finish();
                            }
                        }
	            });

            } 
        	

        }
    }
}