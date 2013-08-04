package com.qardapp.qard.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.UpdateUserTask;

public class UsernamePopup extends DialogFragment{

    public interface LoginDialogListener  {
        void onFinishEditDialog(String inputText, int id);
    }

    private EditText fEditText;
    private EditText lEditText;    
    private String firstName;
    private String lastName;
    UsernamePopup p;

    public UsernamePopup() {
        // Empty constructor required for DialogFragment
    }

        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	   
        View view = null;

        view = inflater.inflate(R.layout.username_fragment, container);      
        fEditText = (EditText) view.findViewById(R.id.firstname);
        lEditText = (EditText) view.findViewById(R.id.lastname);
        
        Button okB = (Button)view.findViewById(R.id.okButton2);
        Button cancelB = (Button)view.findViewById(R.id.cancelButton2);       

        p = this;
        
        getDialog().setTitle("Update name");        	

        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        
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
    
    public boolean update()
    {
        // Get user input
        firstName = fEditText.getText().toString();
        lastName = lEditText.getText().toString();
        
        if (firstName.length() == 0 || lastName.length() == 0)
        {
            Toast.makeText(getActivity(), "Field must not be empty.", Toast.LENGTH_LONG).show();
            this.dismiss();
            return false;
        }
        
		UpdateUserTask task = new UpdateUserTask(getActivity(),
				firstName,
				lastName,
				"testname",
				"12341234");
		task.execute();
		
		getActivity().runOnUiThread(new Runnable() {
		    public void run() {

	            Toast.makeText(getActivity(), "Added username!", Toast.LENGTH_LONG).show();

		    }
		});
        
        this.dismiss();
        return true;
    	
    }
    
        	
}
