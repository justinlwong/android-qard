package com.qardapp.qard.settings;

import java.util.regex.Pattern;

import org.scribe.oauth.OAuthService;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.AddServiceTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
 
public class AuthActivity extends Activity {
 
    private static final int AUTHORIZATION_CODE = 1993;
	private static final int ACCOUNT_CODE = 1601;
	OAuthService mService = null;
 
	private AuthPreferences authPreferences;
	private AccountManager accountManager;
	private Account userAccount;
 
	/**
	 * change this depending on the scope needed for the things you do in
	 * doCoolAuthenticatedStuff()
	 */
	private final String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
	private int serviceID;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		accountManager = AccountManager.get(this);
		serviceID = Services.GMAIL.id;
 
		authPreferences = new AuthPreferences(this);
		if (authPreferences.getUser() != null
				&& authPreferences.getToken() != null) {
			doCoolAuthenticatedStuff();
		} else {
			chooseAccount();
		}
	}
 
	private void doCoolAuthenticatedStuff() {
		// TODO: insert cool stuff with authPreferences.getToken()
		/*Log.d("here", "authenticated!");
   	    Thread t1 = new Thread() {
			public void run() {
			    OAuthRequest request = new OAuthRequest(Verb.GET,"https://www.googleapis.com/oauth2/v1/userinfo?access_token=");
			    //Token t = new Token(token);
			    //mService.signRequest(t, request);
			    Log.d("here","signed request");
			    
			    Response response = null;
			    try {
			        response = request.send();
		    		Log.d("response",response.getBody());
		
			    }
			    catch ( Exception e ) {
			    	e.printStackTrace();
			    }
			}
		};
		t1.start();*/
		finish();
		
	}
 
	private void chooseAccount() {
		// use https://github.com/frakbot/Android-AccountChooser for
		// compatibility with older devices
		Intent intent = AccountManager.newChooseAccountIntent(null, null,
				new String[] { "com.google" }, false, null, null, null, null);
		startActivityForResult(intent, ACCOUNT_CODE);
	}
 //new String[] { "com.google" }
	private void requestToken() {
		userAccount = null;
		String user = authPreferences.getUser();
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		for (Account account : accountManager.getAccounts()) {
		    if (emailPattern.matcher(account.name).matches()) {
		        SharedPreferences sp = getSharedPreferences("tokens", 0);
        		SharedPreferences.Editor editor = sp.edit();
        		editor.putString("Gmail_data", account.name);
        		editor.commit();
        		
        		// Show toast
                Toast.makeText(this, "Added email information!", Toast.LENGTH_LONG).show();
        		
        		// Update database
        		UpdateDatabase.updateDatabase(account.name, serviceID,this);

		    }			
			if (account.name.equals(user)) {
				userAccount = account;
				break;
			}
		}
		
		AccountManager am = AccountManager.get(this);
		Account[] accounts = am.getAccounts();

		for (Account ac : accounts) {
		    String acname = ac.name;
		    String actype = ac.type;
		    // Take your time to look at all available accounts
		    Log.d("here","Accounts : " + acname + ", " + actype);
		    if(actype.equals("com.whatsapp")){
		        String phoneNumber = ac.name;
		        Log.d("here",phoneNumber);
		    }
		}
 
		accountManager.getAuthToken(userAccount, "oauth2:" + SCOPE, null, this,
				new OnTokenAcquired(), null);
	}
 
	/**
	 * call this method if your token expired, or you want to request a new
	 * token for whatever reason. call requestToken() again afterwards in order
	 * to get a new token.
	 */
	private void invalidateToken() {
		AccountManager accountManager = AccountManager.get(this);
		accountManager.invalidateAuthToken("com.google",
				authPreferences.getToken());
 
		authPreferences.setToken(null);
	}
 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
 
		if (resultCode == RESULT_OK) {
			if (requestCode == AUTHORIZATION_CODE) {
				requestToken();
			} else if (requestCode == ACCOUNT_CODE) {
				String accountName = data
						.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				authPreferences.setUser(accountName);
 
				// invalidate old tokens which might be cached. we want a fresh
				// one, which is guaranteed to work
				invalidateToken();
 
				requestToken();
			}
		}
	}
 
	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
 
		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			try {
				Bundle bundle = result.getResult();
				Log.d("here",bundle.toString());
 
				Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
				if (launch != null) {
					startActivityForResult(launch, AUTHORIZATION_CODE);
				} else {
					String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
					authPreferences.setToken(token);
 
					doCoolAuthenticatedStuff();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	

}