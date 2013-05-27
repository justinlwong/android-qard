package com.qardapp.qard.services.connect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.DialogError;

public class FacebookConnect extends ServiceConnect{
	
	public FacebookConnect(Activity activity) {
		super(activity);
	}
	

	// Note: Will need to convert this to an async operation if need to know when data is retrieved
	public void getUserInfo() {

	    if (Session.getActiveSession() == null
	            || Session.getActiveSession().isClosed()) {
	        Session.openActiveSession(activity, true, new StatusCallback() {
	
	            @Override
	            public void call(Session session, SessionState state,
	                    Exception exception) {
	                Log.d("State",state.toString());
	
	                if (session.isOpened()) {
	                    Log.d("Token", session.getAccessToken());
	                    Request.executeMeRequestAsync(session,
	                            new GraphUserCallback() {
	                                @Override
	                                public void onCompleted(GraphUser user,
	                                        Response response) {
	                                    if (user != null) {
	                                        Log.d("User", user.toString());
	
	                                    }
	                                    if (response != null) {
	                                        Log.d("Response",response.toString());
	                                    }
	                                }
	                            });
	                }
	                if (exception != null) {
	                    Log.d("Status","Some thing bad happened!");
	                    exception.printStackTrace();
	                }
	            }
	        });
	    }
	}
	


}
