package com.qardapp.qard.settings.services;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;

public class PopupDialog extends DialogFragment implements OnEditorActionListener{

    public interface LoginDialogListener  {
        void onFinishEditDialog(String inputText, int id);
    }

    private EditText mEditText;
    private TextView mText;
    private int serviceId;
    private String userInput;
    PopupDialog p;
	private SharedPreferences mPrefs;
	private Services service;
	private String text;
    static String phNo;
    ProgressDialog progress;
    static Boolean wasMyOwnNumber;
    static Boolean workDone;
    final static int SMS_ROUNDTRIP_TIMOUT = 15000;
    View view;
    Button okB;

    public PopupDialog() {
        // Empty constructor required for DialogFragment
    }

        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        Bundle bund = this.getArguments();
        serviceId = bund.getInt("serviceType");
        
        wasMyOwnNumber = false;
        workDone = false;
        mPrefs = getActivity().getSharedPreferences("tokens", 0);        
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean("wasMyOwnNumber", false);
		editor.commit();            
        view = null;
        
        if (serviceId != Services.WEBPAGE.id)
        {
            view = inflater.inflate(R.layout.login_fragment, container);      
            mEditText = (EditText) view.findViewById(R.id.screenName1);
        } else {
        	view = inflater.inflate(R.layout.nexcircle_fragment, container);
            mEditText = (EditText) view.findViewById(R.id.screenName2);
        }


        okB = (Button)view.findViewById(R.id.okButton);
        Button cancelB = (Button)view.findViewById(R.id.cancelButton);       

        p = this;
        
        if (serviceId == Services.PHONE.id)
        {
            mText = (TextView) view.findViewById(R.id.instructions);
        	service = Services.PHONE;
        } else if (serviceId == Services.SKYPE.id)
        {
            mText = (TextView) view.findViewById(R.id.instructions);
        	service = Services.SKYPE;
        	mText.setText("Enter Skype ID");
        } else if (serviceId == Services.PINTEREST.id)
        {
            mText = (TextView) view.findViewById(R.id.instructions);
        	service = Services.PINTEREST;
        	mText.setText("Enter Pinterest username");
        } else if (serviceId == Services.WEBPAGE.id)
        {
        	service = Services.WEBPAGE;
            mPrefs = getActivity().getSharedPreferences("tokens", 0);
            // Note: This is the note data, not the username (convention from other uses of this)
    		text = mPrefs.getString(service.name+"_username", "-1");
        	//Log.d("here",text);
        	if (text != "-1")
        	{
        	    mEditText.setText(text, TextView.BufferType.EDITABLE);
        	}
        }
        
        if (serviceId != Services.WEBPAGE.id)
        {
            getDialog().setTitle("Add "+service.name+" Info");        	
        } else {
        	getDialog().setTitle("Leave a short note!");
        }


        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);
        
        okB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				update();
				
			}
		});
        
        cancelB.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				p.dismiss();
				
			}
		});

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {        	
          
            return update();
        }
        return false;
    }
    
    public boolean normalUpdate()
    {

      
        UpdateDatabase.updateDatabase(userInput, serviceId, getActivity());
        
        // Add to username preferences
        mPrefs = getActivity().getSharedPreferences("tokens", 0);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString(service.name+"_username",userInput);
		editor.commit();    
		
		getActivity().runOnUiThread(new Runnable() {
		    public void run() {
		    	if (serviceId != Services.WEBPAGE.id)
		    	{
	                Toast.makeText(getActivity(), "Added " + service.name + " information!", Toast.LENGTH_LONG).show();
		    	} else {
	                Toast.makeText(getActivity(), "Added Note!", Toast.LENGTH_LONG).show();		    		
		    	}
                // Refresh settings page when the service call is not an activity (eg. PopupDialog)
                if (getActivity() instanceof MainActivity) {
                	((MainActivity) getActivity()).refreshFragments();
                }
		    }
		});
        this.dismiss();    	
        return true;
    }
    
    public boolean update()
    {
        // Get user input
        userInput = mEditText.getText().toString();
        
        // For phone, we have to verify number through text
        if (serviceId == Services.PHONE.id)
        {
		    if (userInput.length() == 0)
		    {
		        Toast.makeText(getActivity(), "Field must not be empty.", Toast.LENGTH_LONG).show();
		        this.dismiss();
		        return false;
		    }
		    // standardize phone number
		    userInput = userInput.replaceAll("[^\\d.]", "");
            phNo = userInput;
            // Store phone number so we can check from sms receiver
            mPrefs = getActivity().getSharedPreferences("tokens", 0);
    		SharedPreferences.Editor editor = mPrefs.edit();
    		editor.putString("phoneNumber",userInput);
    		editor.commit();    
            new CheckOwnMobileNumber().execute();
        } else {
        	ViewGroup layout = (ViewGroup) mEditText.getParent();
            layout.removeView(mEditText);
            layout.removeView(mText);
  	      
		    if (userInput.length() == 0)
		    {
		        Toast.makeText(getActivity(), "Field must not be empty.", Toast.LENGTH_LONG).show();
		        this.dismiss();
		        return false;
		    }
            normalUpdate();
	        
        }

        return true;
    	
    }
    
    private class CheckOwnMobileNumber extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPostExecute(String result)
        {
            // TODO Auto-generated method stub

            if(wasMyOwnNumber)
            {
                //Toast.makeText(getApplicationContext(), "Number matched.", Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(), "Added Phone information!", Toast.LENGTH_LONG).show();
            	mText.setText("Verified!");
            	// Set flag back to false
        		SharedPreferences.Editor editor = mPrefs.edit();
        		editor.putBoolean("wasMyOwnNumber", false);
        		editor.commit();           	
                wasMyOwnNumber = false;
                workDone = false;
                okB.setOnClickListener(new View.OnClickListener() {			
        			@Override
        			public void onClick(View v) {
        				normalUpdate();
        				p.dismiss();
        				
        			}
        		});
                //p.dismiss();
            }
            else
            {
                //Toast.makeText(getActivity(), "Could not verify number!", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "Wrong number.", Toast.LENGTH_LONG).show();
            	mText.setText("Could not verify number.");
        		SharedPreferences.Editor editor = mPrefs.edit();
        		editor.putBoolean("wasMyOwnNumber", false);
        		editor.commit(); 
                wasMyOwnNumber = false;
                workDone = false;
                
                okB.setOnClickListener(new View.OnClickListener() {			
        			@Override
        			public void onClick(View v) {
        				p.dismiss();
        				
        			}
        		});
                //p.dismiss();
                //return;
            }
          

            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            String msg = phNo;
            try
            {
                SmsManager sms = SmsManager.getDefault();
                Log.d("Sending text", phNo);
                sms.sendTextMessage(phNo, null, msg, null, null);
                timeout();
            }
            catch(Exception ex)
            {
                Log.v("Exception :", ""+ex);
            }
            return null;
        }

        @Override
        protected void onPreExecute() 
        {
        	LinearLayout layout = ((LinearLayout)mEditText.getParent());
        	LinearLayout.LayoutParams lp = new    LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        	layout.removeView(mEditText);
        	mText.setText("Checking mobile number...");
        	mText.setTextSize(18);
        	mText.setLayoutParams(lp);
            // TODO Auto-generated method stub
            //progress = ProgressDialog.show(getActivity(), "","Checking Mobile Number...");
            //progress.setIndeterminate(true);
            //progress.getWindow().setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            super.onPreExecute();
        }
    }

    private boolean timeout()
    {
           int waited = 0;
           while (waited < SMS_ROUNDTRIP_TIMOUT)
           {
              try
              {
                Thread.sleep(100);
              }
              catch (InterruptedException e)
              {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
              waited += 100;
              if(phoneNumberConfirmationReceived())
              {
                  waited=SMS_ROUNDTRIP_TIMOUT;
                  workDone = true;
              }
           }
           /*Log.v("MainActivity:timeout2: Waited: " , ""+waited);
           Log.v("MainActivity:timeout2:Comparision: ", ""+ phoneNumberConfirmationReceived());
           Log.v("MainActivity:timeout2: WorkDone value after wait complete : ", ""+workDone);*/
        return workDone;
    }

    private boolean phoneNumberConfirmationReceived()
    {
    	wasMyOwnNumber = mPrefs.getBoolean("wasMyOwnNumber",false);
        if(wasMyOwnNumber)
        {
            workDone = true;
        }
        return workDone;
    }
}       	

