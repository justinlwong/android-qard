package com.qardapp.qard.settings;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginDialog extends DialogFragment implements OnEditorActionListener{

    public interface LoginDialogListener  {
        void onFinishEditDialog(String inputText, int id);
    }

    private EditText mEditText;
    private TextView mText;
    private int serviceId;
    private String serviceType;
    private String userInput;
    private Button okButton;
    private Button cancelButton;
    String data;

    public LoginDialog() {
        // Empty constructor required for DialogFragment
    }
    
    private class performAPIcall extends AsyncTask<Integer, Void, Void> {

       protected Void doInBackground(Integer... args) {
    	   int serviceID = args[0];
    	   data = null;
    	   
    	   if (serviceID == Services.TWITTER.id)
    	   {
    		   data = TwitterApiRequest.retrieveUserInfo(userInput);
    	   }    	   
    	   else if (serviceId == Services.FLICKR.id)
    	   {
    		   data = FlickrApiRequest.retrieveUserInfo(userInput);
    	   }
    	   
        	   // Update on UI: Set Status and User can dismiss dialog
    	   getActivity().runOnUiThread(new Runnable() {
    		    public void run() {
    		       cancelButton.setVisibility(View.VISIBLE);
    		       cancelButton.setText("OK");	
    		       if (data != null) {
    		    	   updateDatabase(data);
	    		       // Set Listener to dismiss dialog
		               getDialog().setTitle("Successful!");
		               getDialog().setCanceledOnTouchOutside(true); 
    		       } else {
       	               getDialog().setTitle("Invalid User Id");
       	               getDialog().setCanceledOnTouchOutside(true);     		    	   
    		       }
    		    }
    		});
  		  
    	   return null;

       }
    }
    
    public void updateDatabase(String data)
    {
		ContentResolver res = getActivity().getContentResolver();
		ContentValues values = new ContentValues();
		values.put(FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID, 0);
		values.put(FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID, serviceId);
		values.put(FriendsDatabaseHelper.COLUMN_FS_DATA, data);
		
		// Delete existing entry and replace
		String where = FriendsDatabaseHelper.COLUMN_FS_FRIEND_ID + "=? AND " + FriendsDatabaseHelper.COLUMN_FS_SERVICE_ID + "=?";
		String[] args = new String[] { "0", String.valueOf(serviceId)};
		res.delete(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+serviceId), where, args);
		res.insert(Uri.withAppendedPath(FriendsProvider.CONTENT_URI, "/0/service/"+serviceId), values);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	setStyle(R.style.LoginDialog,0);
    	
        View view = inflater.inflate(R.layout.login_fragment, container);
        mEditText = (EditText) view.findViewById(R.id.screenName);
        mText = (TextView) view.findViewById(R.id.instructions);
        okButton = (Button)view.findViewById(R.id.okButton);
        cancelButton = (Button)view.findViewById(R.id.cancelButton);        
        
        Bundle bund = this.getArguments();
        serviceId = bund.getInt("serviceType");
        
        if (serviceId == Services.TWITTER.id)
        {
        	serviceType = "Twitter";
        } else if (serviceId == Services.FLICKR.id)
        {
        	serviceType = "Flickr";
        }
        
        getDialog().setTitle("Add "+serviceType+" Profile Info");
        
        // Set listeners to buttons
        okButton.setText("Submit");
        okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getEditText();				
			}
        	
        });
        
        cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();			
			}
        	
        });

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {        	
            getEditText();
        }
        return false;
    }
    
    public void getEditText()
    {
    	// Disappear buttons
    	cancelButton.setVisibility(View.INVISIBLE);
    	((ViewGroup) okButton.getParent()).removeView(okButton);
        getDialog().setTitle("Verifying ...");
        // Prevent dialog closing for now
        getDialog().setCanceledOnTouchOutside(false);  
        
        // Get user input
        userInput = mEditText.getText().toString();

        ViewGroup layout = (ViewGroup) mEditText.getParent();
        layout.removeView(mEditText);
        layout.removeView(mText);
        
        // Launch Async Task to make api call and update database and then dismiss dialog when finished           
        new performAPIcall().execute(serviceId);
        //fragment.onFinishEditDialog(mEditText.getText().toString(),serviceId);
        //this.dismiss();  	
    }
        	
}
