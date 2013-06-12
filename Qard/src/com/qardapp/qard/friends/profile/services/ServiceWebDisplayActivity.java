package com.qardapp.qard.friends.profile.services;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.Foursquare2Api;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.builder.api.TwitterApi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;

public class ServiceWebDisplayActivity extends Activity {
	
	private WebView mWebView;
	private ServiceWebDisplayActivity activity;
	private int serviceID;
	private String data;
	String url;
	Services service;
	private SharedPreferences mPrefs;
	
	private class DisplayWebView extends AsyncTask<Void, Void, String> {
		

		protected String doInBackground(Void... arg0) {
			return url;
	    }
		
		@Override  
		protected void onPostExecute(String authURL) { 
			mWebView = (WebView)findViewById(R.id.webview);
		    mWebView.setWebViewClient(new WebViewClient() {
		    	
		        @Override
		        public void onPageStarted(WebView view, String url, Bitmap favicon) {
		            Log.d("here", url);
		        }
		    		    	
		    });

		    mWebView.getSettings().setJavaScriptEnabled(true);
		    if (serviceID == Services.LINKEDIN.id) {
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
        mPrefs = getSharedPreferences("tokens", 0);
        
        if (serviceID == Services.TWITTER.id)
        {
        } else if (serviceID == Services.LINKEDIN.id) {
        	service = Services.LINKEDIN;
            url = mPrefs.getString(service.name+"_profileurl","-1");
        
        } else if (serviceID == Services.FLICKR.id)
        {
        	service = Services.FLICKR;
            url = "http://m.flickr.com/#/photos/"+mPrefs.getString(service.name+"_data","-1");        	
        } else if (serviceID == Services.FOURSQUARE.id)
        {
        } else if (serviceID == Services.INSTAGRAM.id)
        {
        	service = Services.INSTAGRAM;
            url = "https://instagram.com/"+mPrefs.getString(service.name+"_username","-1");
        }
        
        // Check shared preferences for token
               
        new DisplayWebView().execute();
    }
}
