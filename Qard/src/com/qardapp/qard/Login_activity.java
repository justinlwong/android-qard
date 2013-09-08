package com.qardapp.qard;

import com.facebook.Session;
import com.qardapp.qard.settings.services.FacebookLoginActivity;
import com.qardapp.qard.util.ImageUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/*
 * Activity to show the splash screen
 */
public class Login_activity extends Activity {

	SharedPreferences mPrefs;
	final String welcomeScreenShownPref = "welcomeScreenShown";
	static final int LOGIN_REQUEST = 1;

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
				intent.putExtra("launchType", 1);
				//intent.putExtra("loginactivity", "login");
				Login_activity.this.startActivityForResult(intent,LOGIN_REQUEST);
				
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
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == LOGIN_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // When facebook login finishes we come here and end this activity
            	Log.d("login", "finished login");
            	setResult(Activity.RESULT_OK);
            	
            	this.finish();
            }
        }        
    }
    


}

