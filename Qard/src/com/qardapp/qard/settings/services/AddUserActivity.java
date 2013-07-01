package com.qardapp.qard.settings.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Foursquare2Api;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.qardapp.qard.Services;

public class AddUserActivity extends Activity{
	
	OAuthService mService = null;
	private SharedPreferences mPrefs;
	Services service;
	String accessToken;
	String accessSecret;
	private int serviceID;
	
	private class AddUserTask extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... arg0) {
			return null;

	    }
		
		@Override  
		protected void onPostExecute(String authURL) { 
			Thread t1 = new Thread() {
				public void run() {
					
					String urlStr = service.addUser;

				    OAuthRequest request = new OAuthRequest(Verb.POST,urlStr);
			        if (serviceID == Services.TWITTER.id)
			        {    
					    request.addBodyParameter("user_id", "28384755");
					    request.addBodyParameter("follow","true");
			        } else if (serviceID == Services.FOURSQUARE.id)
			        {    
			        	urlStr += "27695493/request?oauth_token="+accessToken + "&v=20130610";
			        	request = new OAuthRequest(Verb.POST,urlStr);

			        }else if (serviceID == Services.LINKEDIN.id)
			        {
			        	try {

				            JSONObject jsonbody = new JSONObject();
	
				            JSONObject info = new JSONObject();
				            info.put("_path","/people/email="+"gg_kiks@hotmail.com");
				            info.put("first-name","Gavin");
				            info.put("last-name","Guo");
				            JSONObject person = new JSONObject();	
				            person.put("person",info);
				            JSONArray arr = new JSONArray();
				            arr.put(person);
				            JSONObject values = new JSONObject();
				            values.put("values", arr);
				            JSONObject recipients = new JSONObject();
				            recipients.put("recipients",values);
				            JSONObject subject = new JSONObject();
				            subject.put("subject", "Invitation to connect.");
				            JSONObject body = new JSONObject();
				            body.put("body","Let's be friends!");
				            
				            JSONObject connect_type = new JSONObject();
				            connect_type.put("connect-type", "friend");
				            JSONObject invitation = new JSONObject();
				            invitation.put("invitation-request",connect_type);
				            
				            jsonbody.put("item-content", invitation);
				            jsonbody.put("body","Add me as a friend!");
				            jsonbody.put("subject","Join my network on LinkedIn.");
				            jsonbody.put("recipients",values);
				            
				            String message = jsonbody.toString();
				            
				            Log.d("here",message);
				            request.addHeader("Content-Type", "application/json");
				            request.addPayload(message);

						    Log.d("here",request.toString());
						    
				            
			        	} catch (JSONException e) {
			        		
			        	}
			            

			        }

				    Token t = new Token(accessToken,accessSecret);
				    mService.signRequest(t, request);
				    
				    Response response = null;
				    try {
				        response = request.send();
			    		Log.d("response",response.getBody());

				    }
				    catch ( Exception e ) {
				    	e.printStackTrace();
				    }
				    
		  		    finish();

				}
		    };
		    t1.start();
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Determine service
        Bundle extras = getIntent().getExtras();
        serviceID = extras.getInt("serviceID");
                
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
        } else if (serviceID == Services.FOURSQUARE.id)
        {       
	        service = Services.FOURSQUARE;
	        mService = new ServiceBuilder()
			.provider(Foursquare2Api.class)
			.apiKey(service.apiKey)
			.apiSecret(service.apiSecret)
			.callback(service.callbackURL)
			.build();
        }
        
        // Check shared preferences for token
        mPrefs = getSharedPreferences("tokens", 0);
        accessToken = mPrefs.getString(service.name+"_access_token","-1");
        accessSecret = mPrefs.getString(service.name+"_access_secret", "-1");
              
        new AddUserTask().execute();
    }
    
    
}
