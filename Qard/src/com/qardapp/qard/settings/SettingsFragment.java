package com.qardapp.qard.settings;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.QardLoginActivity;
import com.qardapp.qard.comm.server.ServerHelper;
import com.qardapp.qard.comm.server.UpdateUserTask;


public class SettingsFragment extends BaseFragment{
	
	// Later will put this in the global application class
	//OAuthService linkedinService = null;
	protected View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.settings_layout1,
				container, false);

		Button b1 = (Button)rootView.findViewById(R.id.facebookconnect);
		Button b2 = (Button)rootView.findViewById(R.id.twitterconnect);
		Button b3 = (Button)rootView.findViewById(R.id.flickrconnect);
		Button b4 = (Button)rootView.findViewById(R.id.linkedinconnect);
		Button b5 = (Button)rootView.findViewById(R.id.foursquareconnect);
		Button b6 = (Button)rootView.findViewById(R.id.emailconnect);
		Button b7 = (Button)rootView.findViewById(R.id.instagramconnect);
		Button b8 = (Button)rootView.findViewById(R.id.contactsync);
		Button b9 = (Button)rootView.findViewById(R.id.googleplusconnect);
		Button b10 = (Button)rootView.findViewById(R.id.phoneconnect);
		Button b11 = (Button)rootView.findViewById(R.id.whatsappconnect);

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launch(Services.FACEBOOK.id);
				
			}
		});
//		
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID", Services.TWITTER.id);
				startActivity(intent);	
			}
		});
//		
		b3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID", Services.FLICKR.id);
				startActivity(intent);
			}
		});
//		
		
		b4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID", Services.LINKEDIN.id);
				startActivity(intent);			
			}
		});
		
		b5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID", Services.FOURSQUARE.id);
				startActivity(intent);			
			}
		});
		
		b6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),AccountManagerInfoActivity.class);
				intent.putExtra("serviceID", Services.GMAIL.id);
				startActivity(intent);			
			}
		});
		
		b11.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),AccountManagerInfoActivity.class);
				intent.putExtra("serviceID", Services.WHATSAPP.id);
				startActivity(intent);			
			}
		});		
		
		b7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),OAuthActivity.class);
				intent.putExtra("serviceID",Services.INSTAGRAM.id);
				startActivity(intent);			
			}
		});
		
		b8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            //intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	            //startActivityForResult(intent, 1);
                Intent intent = new Intent(getActivity(), SyncContactsActivity.class);
                startActivity(intent);		
			}
		});
		
		b9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GooglePlusAuthActivity.class);
                startActivity(intent);		
			}
		});
		
		b10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                showEditDialog(Services.PHONE.id);		
			}
		});
		
		Button login = (Button) rootView.findViewById(R.id.settings_login_btn);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),QardLoginActivity.class);
				startActivityForResult(intent, 21);
			}
		});
		
		Button updateAcc = (Button) rootView.findViewById(R.id.settings_update_acc);
		updateAcc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UpdateUserTask task = new UpdateUserTask(getActivity(),
						((EditText)getActivity().findViewById(R.id.settings_first_name)).getText().toString(),
						((EditText)getActivity().findViewById(R.id.settings_last_name)).getText().toString(),
						((EditText)getActivity().findViewById(R.id.settings_username)).getText().toString(),
						((EditText)getActivity().findViewById(R.id.settings_password)).getText().toString());
				task.execute();
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

	@Override
	public void updateViews() {
		TextView current_id_field = (TextView) getView().findViewById(R.id.settings_current_id);
		String user_id = ServerHelper.getUserId(getActivity());
		if (user_id != null) {
			current_id_field.setText("(DEBUG) Current qard id = " + user_id);
		} else {
			current_id_field.setText("(DEBUG) Current qard id not found, restart?");
		}
	}
	
    private void showEditDialog(int serviceId) {
    	
        FragmentManager fm = getFragmentManager();        
        PopupDialog popupDialog = (PopupDialog) fm.findFragmentByTag("login_fragment");
       		
        popupDialog = new PopupDialog();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Bundle bund = new Bundle();
        bund.putInt("serviceType",serviceId);
        popupDialog.setArguments(bund);
        //loginDialog.show(transaction, "login_fragment");
        transaction.add(popupDialog,"login_fragment").commit();
    }
		
}





