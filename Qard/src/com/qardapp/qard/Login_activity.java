package com.qardapp.qard;

import com.qardapp.qard.settings.services.FacebookLoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

/*
 * Activity to show the splash screen
 */
public class Login_activity extends Activity {

	SharedPreferences mPrefs;
	final String welcomeScreenShownPref = "welcomeScreenShown";

	// On create, show the spash screen
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.settings_account_layout2);

	    mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	    // second argument is the default to use if the preference can't be found
	    Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
	    Button login = (Button) findViewById(R.id.login_btn);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login_activity.this, FacebookLoginActivity.class);
				SharedPreferences.Editor editor = mPrefs.edit();
		        editor.putBoolean(welcomeScreenShownPref, true);
		        editor.commit(); // Very important to save the preference
				//intent.putExtra("launchType", 1);
				//intent.putExtra("loginactivity", "login");
				Login_activity.this.startActivityForResult(intent,0);
				
			}
		});
		Button skip_login = (Button) findViewById(R.id.skip_btn);
		skip_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(Login_activity.this, MainActivity.class);
				SharedPreferences.Editor editor = mPrefs.edit();
		        editor.putBoolean(welcomeScreenShownPref, true);
		        editor.commit(); // Very important to save the preference
				//Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
				//intent2.putExtra("loginactivity", "skip");
				Login_activity.this.startActivity(intent1);
				
				
			}
		});
	    
	}
	


}

