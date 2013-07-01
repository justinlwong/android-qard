package com.qardapp.qard.settings;

import java.util.regex.Pattern;

import com.qardapp.qard.Services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

public class AccountManagerInfoActivity extends Activity {
	 
	/**
	 * change this depending on the scope needed for the things you do in
	 * doCoolAuthenticatedStuff()
	 */
	private int serviceID;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // Determine service
        Bundle extras = getIntent().getExtras();
        serviceID = extras.getInt("serviceID");
        
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        		
		AccountManager am = AccountManager.get(this);
		Account[] accounts = am.getAccounts();

		for (Account ac : accounts) {
		    String acname = ac.name;
		    String actype = ac.type;
		    // Take your time to look at all available accounts
		    Log.d("here","Accounts : " + acname + ", " + actype);
		    
		    if (serviceID == Services.GMAIL.id)
	        {
		    
			    if (emailPattern.matcher(acname).matches() && actype.equals("com.google")) {
			        SharedPreferences sp = getSharedPreferences("tokens", 0);
	        		SharedPreferences.Editor editor = sp.edit();
	        		editor.putString("E-mail_data", acname);
	        		editor.commit();
	        		
	        		// Show toast
	                Toast.makeText(this, "Added email information!", Toast.LENGTH_LONG).show();
	        		
	        		// Update database
	        		UpdateDatabase.updateDatabase(acname, serviceID,this);
	
			    }
	        } else if (serviceID == Services.WHATSAPP.id)
	        {
		    
			    if(actype.equals("com.whatsapp")){
			        String phoneNumber = ac.name;
			        Log.d("here",phoneNumber);
			        SharedPreferences sp = getSharedPreferences("tokens", 0);
	        		SharedPreferences.Editor editor = sp.edit();
	        		editor.putString("WhatsApp_data", phoneNumber);
	        		editor.commit();
	        		
	        		// Show toast
	                Toast.makeText(this, "Added WhatsApp information!", Toast.LENGTH_LONG).show();
	        		
	        		// Update database
	        		UpdateDatabase.updateDatabase(acname, serviceID,this);
			    }
	        }
		}
		
		finish();
 
	}
 
}
