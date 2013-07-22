package com.qardapp.qard.settings.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.AddServiceTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class FacebookLoginActivity extends Activity {

    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private Activity activity = this;
	private ProgressDialog progDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.oauth_layout);
		
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        
//        progDialog = new ProgressDialog(FacebookLoginActivity.this);
//        progDialog.setMessage("Connecting...");
//        progDialog.setIndeterminate(true);
//        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progDialog.setCancelable(false);
//        progDialog.show();

//        Session session = new Session(this);
//        Session.setActiveSession(session);  
        Session.openActiveSession(this, true, statusCallback);
        progDialog = new ProgressDialog(FacebookLoginActivity.this);
        progDialog.setMessage("Connecting...");
        progDialog.setIndeterminate(true);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setCancelable(false);
        progDialog.show();
//	    if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
//	        session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
//	    }
//        Log.d("here","aftergetactive");
//        if (session == null) {
//        	Log.d("here","sessionnull");
//            if (savedInstanceState != null) {
//                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
//            }
//            if (session == null) {
//                session = new Session(this);
//            }
//            Session.setActiveSession(session);
//            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
//                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
//            }
//        } else {
//            if (!session.isOpened() && !session.isClosed()) {
//                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
//            } else {
//                Session.openActiveSession(this, true, statusCallback);
//            }
//    		runOnUiThread(new Runnable() {
//    		    public void run() {
//	                Toast.makeText(activity, "Added " + Services.FACEBOOK.name + " information!", Toast.LENGTH_LONG).show();
//    		    }
//    		});
    		//finish();
//        }

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Session.getActiveSession().addCallback(statusCallback);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Session.getActiveSession().removeCallback(statusCallback);
//    }
//    
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        if(progDialog != null)
//            progDialog.dismiss();
//        progDialog = null;
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
//    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Session session = Session.getActiveSession();
//        Session.saveSession(session, outState);
//    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
        	Log.d("here", "got here");
        	if (session.isOpened()) {

	            Request.executeMeRequestAsync(session,
	                    new GraphUserCallback() {
                        private SharedPreferences mPrefs;

						@Override
                        public void onCompleted(GraphUser user,
                                Response response) {

                            if (user != null) {
                            	String userId = null;
                                Log.d("User",user.getId());
                                userId = user.getId();
                                String uname = user.getName();
                        		// Shared Prefs to get username
                                mPrefs = activity.getSharedPreferences("tokens", 0);
				        		SharedPreferences.Editor editor = mPrefs.edit();
				        		editor.putString("Facebook_username",uname);
				        		editor.commit();
                                
                                AddServiceTask task = new AddServiceTask(FacebookLoginActivity.this, Services.FACEBOOK.id, userId);
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
						                Toast.makeText(activity, "Added " + Services.FACEBOOK.name + " information!", Toast.LENGTH_LONG).show();
				        		    }
				        		});
				        		if (progDialog != null)
				        		{
				        			if (progDialog.isShowing())
				        			{
	    						    progDialog.cancel();
				        			}
				        		}
	    						activity.finish();
                            }
                        }
	            });

            }
        	

        }
    }
}