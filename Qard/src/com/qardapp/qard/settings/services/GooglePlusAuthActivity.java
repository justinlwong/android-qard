package com.qardapp.qard.settings.services;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.AddServiceTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class GooglePlusAuthActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener {
    private static final String TAG = "ExampleActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    static final int REQUEST_AUTHORIZATION = 2;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
    static final Object[] SCOPES = new String[] { Scopes.PLUS_PROFILE };
    Activity a;
	private SharedPreferences mPrefs;
	private ProgressDialog progDialog;
	    
    private Void getToken() {
    	    	
    	String accessToken = null;
    	Log.d("here","attempt");
    	try {
    	  accessToken = GoogleAuthUtil.getToken(a,
    	    mPlusClient.getAccountName(),
    	    "oauth2:" + TextUtils.join(" ", SCOPES));
    	  Log.d("here",accessToken);
    	  
  		  SharedPreferences.Editor editor = mPrefs.edit();
  		  editor.putString(Services.GOOGLEPLUS.name+"_access_token", accessToken);

  		  
  		
    	} catch (IOException transientEx) {
    		Log.d("here","server error");
    	  // network or server error, the call is expected to succeed if you try again later.
    	  // Don't attempt to call again immediately - the request is likely to
    	  // fail, you'll hit quotas or back-off.
    	  return null;
    	} catch (UserRecoverableAuthException e) {
    		   Log.d("here","auth exception");
    	       startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
    	} catch (GoogleAuthException authEx) {
    	  // Failure. The call is not expected to ever succeed so it should not be
    	  // retried.
    		   Log.d("here","gauth exception");
    	  return null;
    	} catch (Exception e) {
    	  throw new RuntimeException(e);
    	}
		return null;    	
    }
    
	private class getAccessToken extends AsyncTask<Void, Void, Void> {
		
        
		protected Void doInBackground(Void... arg0) {
	    	return getToken();
		}
		
		@Override  
		protected void onPostExecute(Void v) { 

		    mConnectionProgressDialog.dismiss();
		    a.finish();
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a = this;
        
        progDialog = new ProgressDialog(GooglePlusAuthActivity.this);
        progDialog.setMessage("Connecting...");
        progDialog.setIndeterminate(true);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setCancelable(false);
        progDialog.show();
        
        // Check shared preferences for token
        mPrefs = getSharedPreferences("tokens", 0);
        
        mPlusClient = new PlusClient.Builder(this, this, this)
                .setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();
        // Progress bar to be displayed if the connection failure is not resolved.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mPlusClient.isConnected())
            mPlusClient.connect();
        else {    		  
		    // Do API call to get user info
		    Person p = mPlusClient.getCurrentPerson();
		    Log.d("here",p.getId());
		    UpdateDatabase.updateDatabase(p.getId(),Services.GOOGLEPLUS.id,this);
        	new getAccessToken().execute();
        }
        //finish();
    }

	@Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (SendIntentException e) {
                mPlusClient.connect();
            }
        }
        // Save the result and resolve the connection failure upon a user click.
        mConnectionResult = result;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        } else if (requestCode == REQUEST_AUTHORIZATION){
        	// Attempt again
        	new getAccessToken().execute();
        	
        }
       
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    	Log.d("here","connected");
	    // Do API call to get user info
	    Person p = mPlusClient.getCurrentPerson();
	    Log.d("here",p.getId());
	    UpdateDatabase.updateDatabase(p.getId(),Services.GOOGLEPLUS.id, this);
	    
        String accountName = mPlusClient.getAccountName();
        Toast.makeText(this, "Added Google Plus information!", Toast.LENGTH_LONG).show();
        new getAccessToken().execute();
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "disconnected");
    }
}
