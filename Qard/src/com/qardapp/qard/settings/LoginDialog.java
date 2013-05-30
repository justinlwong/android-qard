package com.qardapp.qard.settings;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginDialog extends DialogFragment implements OnEditorActionListener{

    public interface LoginDialogListener  {
        void onFinishEditDialog(String inputText);
    }

    private EditText mEditText;
    private String serviceType;

    public LoginDialog() {
        // Empty constructor required for DialogFragment
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container);
        mEditText = (EditText) view.findViewById(R.id.screenName);
        
        Bundle bund = this.getArguments();
        int serviceId = bund.getInt("serviceType");
        
        if (serviceId == Services.TWITTER.id)
        {
        	serviceType = "Twitter";
        }
        
        getDialog().setTitle("Add "+serviceType+" Profile Info");

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
            // Return input text to activity
            LoginDialogListener fragment = (LoginDialogListener) this.getParentFragment();
            fragment.onFinishEditDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
	
}
