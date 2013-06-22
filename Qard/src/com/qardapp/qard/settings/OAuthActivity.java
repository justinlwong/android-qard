package com.qardapp.qard.settings;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.Foursquare2Api;
import org.scribe.builder.api.FoursquareApi;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.builder.api.TwitterApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;


import com.google.android.gms.plus.PlusClient;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.graphics.Bitmap;

public class OAuthActivity extends Activity {
	
	OAuthService mService = null;
	public Token mRequestToken;
	public WebView mWebView;
	private SharedPreferences mPrefs;
	Services service;
	int serviceID;
	String data;
	Activity activity;
	String email;
	String firstname;
	String lastname;
	String username;
	String profileURL;
	String userID;
	
    public void updateDatabase(String data)
    {
		ContentResolver res = getContentResolver();
		ContentValues values = new ContentValues();
		values.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, 0);
		values.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, serviceID);
		values.put(FriendsDatabaseHelper.COLUMN_FS_DATA, data);
		
		// Delete existing entry and replace
		String where = FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=? AND " + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
		String[] args = new String[] { "0", String.valueOf(serviceID)};
		res.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+serviceID), where, args);
		res.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+serviceID), values);
    }
	
	private class OAuthTask extends AsyncTask<Void, Void, String> {
		
		ProgressDialog progDialog;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDialog = new ProgressDialog(OAuthActivity.this);
            progDialog.setMessage("Connecting...");
            progDialog.setIndeterminate(true);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setCancelable(false);
            progDialog.show();
        }
        
		protected String doInBackground(Void... arg0) {

			// Temporary URL
			String authURL = service.authURL;

			try {
				if (serviceID == Services.FOURSQUARE.id || serviceID == Services.INSTAGRAM.id)
				{
				    authURL = mService.getAuthorizationUrl(null);	
				} else {
				    mRequestToken = mService.getRequestToken();
					authURL = mService.getAuthorizationUrl(mRequestToken);
				}
			}
			catch ( OAuthException e ) {
				e.printStackTrace();
				return null;
			}
            Log.d("here", authURL);
			return authURL;
	    }
		
		@Override  
		protected void onPostExecute(String authURL) { 
			
			mWebView = (WebView)findViewById(R.id.webview);
		    mWebView.setWebViewClient(new WebViewClient() {		    	
		        
		        @Override
		        public void onPageFinished(WebView view, String url) {
	        		if (progDialog != null) {
			            progDialog.dismiss();
					}                
		        }
		    		    	
			    @Override
			    public boolean shouldOverrideUrlLoading(WebView view, String url) {	 
			    	Log.d("urls",url);

					super.shouldOverrideUrlLoading(view, url);

					if( url.startsWith("oauth") ) {
				        mWebView.setVisibility(WebView.GONE);	
				        
				        // Restart loading animation
			            progDialog = new ProgressDialog(OAuthActivity.this);
			            progDialog.setMessage("Authorizing...");
			            progDialog.setIndeterminate(true);
			            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			            progDialog.setCancelable(false);
			            progDialog.show();		
			            
					    final String url1 = url;
				   	    Thread t1 = new Thread() {
							public void run() {
						  	    Uri uri = Uri.parse(url1);
			
							    String verifier = uri.getQueryParameter("oauth_verifier");
							    if (serviceID == Services.FOURSQUARE.id || serviceID == Services.INSTAGRAM.id )
							    {
							    	verifier = uri.getQueryParameter("code");
							    	mRequestToken = null;
							    }
							    
							    Verifier v = new Verifier(verifier);
							    Token accessToken = mService.getAccessToken(mRequestToken, v);		

							    // Do a quick query to get user information
							    
							    String urlStr = service.userQuery;
							    if (serviceID == Services.FOURSQUARE.id)
							    {
							    	urlStr += accessToken.getToken() + "&v=20130606";
							    } else if (serviceID == Services.INSTAGRAM.id) {
							    	urlStr += accessToken.getToken();
							    }
							    
							    OAuthRequest request = new OAuthRequest(Verb.GET,urlStr);

							    Token t = new Token(accessToken.getToken(),accessToken.getSecret());
							    mService.signRequest(t, request);
							    
							    Response response = null;
							    try {
							        response = request.send();
						    		Log.d("response",response.getBody());
							    	if ( response.isSuccessful() )
							    	{	
						                JSONObject mainObject = new JSONObject(response.getBody());
						                if (serviceID == Services.FLICKR.id)
						                {
						                    JSONObject user = mainObject.getJSONObject("user");
						                    data = user.getString(service.idFieldName);
						                } else if (serviceID == Services.FOURSQUARE.id)
						                {
						                    JSONObject resp = mainObject.getJSONObject("response");
						                    JSONObject user = resp.getJSONObject("user");
						                    data = user.getString(service.idFieldName);
						                }else if (serviceID == Services.INSTAGRAM.id)
						                {
						                    JSONObject resp = mainObject.getJSONObject("data");
						                    data = resp.getString(service.idFieldName);
						                    username = resp.getString("username");
						                }else if (serviceID == Services.LINKEDIN.id)
						                {
						                    data = mainObject.getString(service.idFieldName);
						                    firstname = mainObject.getString("firstName");
						                    lastname = mainObject.getString("lastName");
						                    email = mainObject.getString("emailAddress");
						                    JSONObject req = mainObject.getJSONObject("siteStandardProfileRequest");
						                    profileURL = req.getString("url");
						                } else if (serviceID == Services.TWITTER.id) {
							                data = mainObject.getString("screen_name");	
							                username = mainObject.getString("screen_name");
							                userID = mainObject.getString(service.idFieldName);
						                }
						                else {
							                data = mainObject.getString(service.idFieldName);						                	
						                }
						                updateDatabase(data);
							    	}
							    }
							    catch ( Exception e ) {
							    	e.printStackTrace();
							    }
							    
				        		// Store the tokens in preferences for further use
				        		SharedPreferences.Editor editor = mPrefs.edit();
				        		editor.putString(service.name+"_access_token", accessToken.getToken());
				        		editor.putString(service.name+"_access_secret", accessToken.getSecret());
				        		editor.putString(service.name+"_username",username);
				        		editor.putString(service.name+"_data", data);
				        		editor.putString(service.name+"_profileurl",profileURL);
				        		editor.putString(service.name+"_userid",userID);
				        		if (serviceID == Services.LINKEDIN.id)
				        		{
					        		editor.putString(service.name+"_emailAddress", email );
					        		editor.putString(service.name+"_firstName", firstname);
					        		editor.putString(service.name+"_lastName", lastname);
				        		}
				        		editor.commit();    
				        		
				                //Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
				        		
					  		    finish();

							}
					    };
					    t1.start();
					
					} else {
						
						// If not oauth link, it is an intermediate page so dismiss loading dialog here				
			            //if (progDialog != null)
			            //    progDialog.dismiss();
					}
	
				    return false;
			    }
		    });

		    mWebView.getSettings().setJavaScriptEnabled(true);
		    if (serviceID == Services.FLICKR.id) {
			    String userAgent = "User-Agent: Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
			    mWebView.getSettings().setUserAgentString(userAgent);
		    }
		    mWebView.loadUrl(authURL);
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauth_layout);
        
        activity = this;
        
        // Determine service
        Bundle extras = getIntent().getExtras();
        serviceID = extras.getInt("serviceID");
        
        // Check shared preferences for token
        mPrefs = getSharedPreferences("tokens", 0);
        Log.d("sharedpref",mPrefs.getString("LinkedIn_access_token","-1"));
        
        if (serviceID == Services.TWITTER.id)
        {
        	service = Services.TWITTER;
            mService = new ServiceBuilder()
    		.provider(TwitterApi.class)
    		.apiKey(service.apiKey)
    		.apiSecret(service.apiSecret)
    		.callback(service.callbackURL)
    		.build();
        } else if (serviceID == Services.LINKEDIN.id)
        {
        	service = Services.LINKEDIN;
            mService = new ServiceBuilder()
    		.provider(LinkedInApi.class)
    		.apiKey(service.apiKey)
    		.apiSecret(service.apiSecret)
    		.callback(service.callbackURL)
                    .scope(service.scope)
    		.build();
        } else if (serviceID == Services.FLICKR.id)
        {
        	service = Services.FLICKR;
            mService = new ServiceBuilder()
    		.provider(FlickrApi.class)
    		.apiKey(service.apiKey)
    		.apiSecret(service.apiSecret)
    		.callback(service.callbackURL)
    		.build();
        } else if (serviceID == Services.FOURSQUARE.id)
        {
        	service = Services.FOURSQUARE;
            mService = new ServiceBuilder()
    		.provider(Foursquare2Api.class)
    		.apiKey(service.apiKey)
    		.apiSecret(service.apiSecret)
    		.callback(service.callbackURL)
    		.build();
        } else if (serviceID == Services.INSTAGRAM.id)
        {
        	service = Services.INSTAGRAM;
            mService = new ServiceBuilder()
    		.provider(InstagramApi.class)
    		.apiKey(service.apiKey)
    		.apiSecret(service.apiSecret)
    		.callback(service.callbackURL)
    		.scope(service.scope)
    		.build();
        }
        

        
        new OAuthTask().execute();
    }
        
}
