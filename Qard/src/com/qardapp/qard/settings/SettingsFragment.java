package com.qardapp.qard.settings;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.settings.LoginDialog.LoginDialogListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SettingsFragment extends Fragment implements LoginDialogListener{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.settings_layout1,
				container, false);

		Button b1 = (Button)rootView.findViewById(R.id.facebookconnect);
		Button b2 = (Button)rootView.findViewById(R.id.twitterconnect);
		Button b3 = (Button)rootView.findViewById(R.id.flickrconnect);

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launch(Services.FACEBOOK.id);
				
			}
		});
		
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showEditDialog(Services.TWITTER.id);
				
			}
		});
		
		b3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showEditDialog(Services.FLICKR.id);
				
			}
		});
			
		return rootView;
	}
	
	public void launch( int serviceID) {
		Intent intent = null;
		if (serviceID == Services.FACEBOOK.id) {
		    intent = new Intent(this.getActivity(),FacebookLoginActivity.class);
		} 
		startActivityForResult(intent,0);	
	}
	
    private void showEditDialog(int serviceId) {
    	
        FragmentManager fm = getFragmentManager();        
        LoginDialog loginDialog = (LoginDialog) fm.findFragmentByTag("login_fragment");
       		
    	Log.d("logindialog","here");
        loginDialog = new LoginDialog();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Bundle bund = new Bundle();
        bund.putInt("serviceType",serviceId);
        loginDialog.setArguments(bund);
        //loginDialog.show(transaction, "login_fragment");
        transaction.add(loginDialog,"login_fragment").commit();
    }

    @Override
    public void onFinishEditDialog(String inputText, int id) {
         Log.d("Text",inputText);
//         if (dialog != null) {
//             dialog.dismiss();
//             fm.beginTransaction().detach(dialog);
//             fm.beginTransaction().remove(dialog).commit();
//         }
    }
	
}





