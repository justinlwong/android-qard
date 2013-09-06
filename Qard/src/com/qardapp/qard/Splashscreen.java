package com.qardapp.qard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/*
 * Activity to show the splash screen
 */
public class Splashscreen extends Activity {

private static final int SPLASH_DISPLAY_TIME = 1000;

	// On create, show the spash screen
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.settings_account_layout2);
	
	    new Handler().postDelayed(new Runnable() {
	
	        public void run() {
	
	            Intent mainIntent = new Intent(Splashscreen.this,
	                    MainActivity.class);
	            Splashscreen.this.startActivity(mainIntent);
	
	            Splashscreen.this.finish();
	            overridePendingTransition(R.layout.mainfadein,
	                    R.layout.splashfadeout);
	        }
	    	}, SPLASH_DISPLAY_TIME);
	    
	    
	}
	


}

