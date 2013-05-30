package com.qardapp.qard.settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import com.facebook.android.Util;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.net.Uri;
import android.os.Bundle;


public class TwitterLoginActivity extends Activity {
	
	private String userId;
	
    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return sb.toString();
}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		final Button   mButton;
		final EditText mEdit;
		final Button uButton;
		final TextView serviceTitle;
		final TextView status; 
		
//		ConfigurationBuilder cb = new ConfigurationBuilder();
//		cb.setDebugEnabled(true)
//		.setOAuthConsumerKey("Your consumer key")
//		.setOAuthConsumerSecret("Your consumer secret key")
//		.setOAuthAccessToken("Access token")
//		.setOAuthAccessTokenSecret("Yout acccess token secret");
//		TwitterFactory tf = new TwitterFactory(cb.build());
//		Twitter twitter = tf.getInstance();
		
//		AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,editPinCode.getText().toString());
//		oHelper.storeAccessToken(accessToken);
//
//		Log.i("Access Token:", accessToken.getToken());
//
//		Log.i("Access Secret:", accessToken.getTokenSecret());
//
//		long userID = accessToken.getUserId();
//
//		User user = twitter.showUser(userID);
//
//		user.getName();
	
		// Set generic login layout title with title of service
		
		serviceTitle = (TextView)findViewById(R.id.serviceTitle);
		serviceTitle.setText("Twitter");
		status = (TextView)findViewById(R.id.status);
		status.setText("Submit username:");
	    mButton = (Button)findViewById(R.id.submitButton);
	    mEdit   = (EditText)findViewById(R.id.userName);
	    uButton = (Button)findViewById(R.id.updateButton);

	    mButton.setOnClickListener(new View.OnClickListener()
	        {
	            public void onClick(View view)
	            {
	                try {

	                    Thread thread = new Thread(new Runnable(){
	                        @Override
	                        public void run() {
	                            try {
	            	                Log.d("EditText", mEdit.getText().toString());
	            	                String screenName = mEdit.getText().toString();
	            	                screenName = screenName.replace(" ","");
	            	                
	            	                String url = "https://api.twitter.com/1/users/show.json?screen_name="+screenName+"&include_entities=true";

	            	                HttpClient httpclient = new DefaultHttpClient();

	            	                // Prepare a request object
	            	                HttpGet httpget = new HttpGet(url); 

	            	                // Execute the request
	            	                HttpResponse response;
	        	                    response = httpclient.execute(httpget);
	        	                    
	        	                    // Get hold of the response entity
	        	                    HttpEntity entity = response.getEntity();
	        	                    // If the response does not enclose an entity, there is no need
	        	                    // to worry about connection release

	        	                    if (entity != null) {

	        	                        // A Simple JSON Response Read
	        	                        InputStream instream = entity.getContent();
	        	                        String result = convertStreamToString(instream);
	        	                        // now you have the string representation of the HTML request
	        	                        instream.close();
	        	                        JSONObject mainObject = new JSONObject(result);
	        	                        userId = mainObject.getString("id");
	        	                    }
	        	                    

	                            } catch (Exception e) {
	                                e.printStackTrace();
	                            }
	                        }
	                    });

	                    thread.start(); 

	                } catch (Exception e) {
	                	e.printStackTrace();
	                }
	                
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
	                // Make Update button visible
	                uButton.setVisibility(View.VISIBLE);
	                mButton.setVisibility(View.INVISIBLE);
	                mEdit.setVisibility(View.INVISIBLE);
	                status.setText("Twitter Profile Info added");
	                uButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
			                uButton.setVisibility(View.INVISIBLE);
			                mButton.setVisibility(View.VISIBLE);
			                mEdit.setVisibility(View.VISIBLE);
							
						}
					});
	            }
	        });		

    }
    
}