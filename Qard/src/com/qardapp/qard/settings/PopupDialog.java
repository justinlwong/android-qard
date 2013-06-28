package com.qardapp.qard.settings;

import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class PopupDialog extends DialogFragment implements OnEditorActionListener{

    public interface LoginDialogListener  {
        void onFinishEditDialog(String inputText, int id);
    }

    private EditText mEditText;
    private TextView mText;
    private int serviceId;
    private String serviceType;
    private String userInput;
    PopupDialog p;

    public PopupDialog() {
        // Empty constructor required for DialogFragment
    }

        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container);
        mEditText = (EditText) view.findViewById(R.id.screenName);
        mText = (TextView) view.findViewById(R.id.instructions);
        Button okB = (Button)view.findViewById(R.id.okButton);
        Button cancelB = (Button)view.findViewById(R.id.cancelButton);
        
        Bundle bund = this.getArguments();
        serviceId = bund.getInt("serviceType");
        p = this;
        
        if (serviceId == Services.PHONE.id)
        {
        	serviceType = "Phone";
        }
        
        getDialog().setTitle("Add "+serviceType+" Info");

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
    
    public boolean update()
    {
        // Get user input
        userInput = mEditText.getText().toString();

        ViewGroup layout = (ViewGroup) mEditText.getParent();
        layout.removeView(mEditText);
        layout.removeView(mText);
        
        if (userInput.length() == 0)
        {
            Toast.makeText(getActivity(), "Phone Number must not be empty.", Toast.LENGTH_LONG).show();
            this.dismiss();
            return false;
        }
        
        UpdateDatabase.updateDatabase(userInput, serviceId, getActivity());
        
		getActivity().runOnUiThread(new Runnable() {
		    public void run() {
                Toast.makeText(getActivity(), "Added " + serviceType + " information!", Toast.LENGTH_LONG).show();
		    }
		});
        
        this.dismiss();
        return true;
    	
    }
    
        	
}
