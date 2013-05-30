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

import android.util.Log;

public class TwitterApiRequest {

	public static String retrieveUserInfo(String text)
	{
        String userId = null;
        String screenName = text.replace(" ","");
        
        try {

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
            } else {
            	return null;
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
		return userId;
	
	}
	
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
}
