package com.qardapp.qard.settings.services;

import java.util.regex.Pattern;

import com.qardapp.qard.Services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

public class AccountChecker {
	
	Activity a;
	private int serviceID;
	public static int ALL = 99;
	private boolean rval = false;
	
	public AccountChecker(Activity a)
	{
		this.a = a;
	}	
	
	public boolean getAccountInfo(int id)
	{
	    // Determine service
	    serviceID = id;
	    
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
	    		
		AccountManager am = AccountManager.get(a);
		Account[] accounts = am.getAccounts();
	
		for (Account ac : accounts) {
		    String acname = ac.name;
		    String actype = ac.type;
		    // Take your time to look at all available accounts
		    Log.d("here","Accounts : " + acname + ", " + actype);
		    
		    if (serviceID == Services.EMAIL.id || serviceID == ALL)
	        {
		    
			    if (emailPattern.matcher(acname).matches() && actype.equals("com.google")) {
			        SharedPreferences sp = a.getSharedPreferences("tokens", 0);
	        		SharedPreferences.Editor editor = sp.edit();
	        		editor.putString("E-mail_data", acname);
	        		editor.commit();
	        		
	        		// Show toast
	                Toast.makeText(a, "Added email information!", Toast.LENGTH_LONG).show();
	        		
	        		// Update database
	        		UpdateDatabase.updateDatabase(acname, Services.EMAIL.id,a);
	        		rval = true;
	
			    }
	        } else if (serviceID == Services.WHATSAPP.id || serviceID == ALL)
	        {
		    
			    if(actype.equals("com.whatsapp")){
			        String phoneNumber = ac.name;
			        Log.d("here",phoneNumber);
			        SharedPreferences sp = a.getSharedPreferences("tokens", 0);
	        		SharedPreferences.Editor editor = sp.edit();
	        		editor.putString("WhatsApp_data", phoneNumber);
	        		editor.commit();
	        		
	        		// Show toast
	                Toast.makeText(a, "Added WhatsApp information!", Toast.LENGTH_LONG).show();
	        		
	        		// Update database
	        		UpdateDatabase.updateDatabase(acname, Services.WHATSAPP.id,a);
	        		rval = true;
			    }
	        } else if (serviceID == Services.TWITTER.id || serviceID == ALL)
	        {
		    
			    if(actype.startsWith("com.twitter")){
			        String uname = ac.name;
			        Log.d("here",uname);
			        SharedPreferences sp = a.getSharedPreferences("tokens", 0);
	        		SharedPreferences.Editor editor = sp.edit();
	        		editor.putString("Twitter_data", uname);
	        		editor.commit();
	        		

	        		
	        		// Update database
	        		UpdateDatabase.updateDatabase(acname, Services.TWITTER.id,a);
	        		
	        		// get auth token as well 
	        		am.getAuthToken(ac, "com.twitter.android.oauth.token", null, a,
	        				new AccountManagerCallback<Bundle>() {

	        		    @Override
	        		    public void run(AccountManagerFuture<Bundle> arg0) {
	        		        try {
    		                     Bundle b = arg0.getResult();  
    		                     Log.e("TrendDroid", "THIS AUTHTOKEN: " + b.getString(AccountManager.KEY_AUTHTOKEN));  
    		 	        		// Show toast
    		 	                Toast.makeText(a, "Added Twitter information!", Toast.LENGTH_LONG).show();
    		                } catch (Exception e) {  
    		                     Log.e("TrendDroid", "EXCEPTION@AUTHTOKEN");  
    		                }  
	        		    }}, null);
	        	    rval = true;
			    }
			 
	        }
		}
		return rval;
	}
}
