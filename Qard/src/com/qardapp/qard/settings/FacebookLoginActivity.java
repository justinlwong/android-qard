package com.qardapp.qard.settings;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphUser;

public class FacebookLoginActivity extends Activity {
    private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";

    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout2);
        buttonLoginLogout = (Button)findViewById(R.id.authButton);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        updateView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            Request.executeMeRequestAsync(session,
                    new GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user,
                                Response response) {

                            if (user != null) {
                            	String userId = null;
                                Log.d("User",user.getId());
                                userId = user.getId();

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
	    						
	                            buttonLoginLogout.setText("Added Info!");
	                            buttonLoginLogout.setOnClickListener(new OnClickListener() {
	                                public void onClick(View view) { onClickLogout(); }
	                            });
                            }
                        }
                    });

        } else {    	
            buttonLoginLogout.setText("Add Facebook Info");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogin(); }
            });
        }
    }

    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
}