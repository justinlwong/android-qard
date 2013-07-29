package com.qardapp.qard.settings.services;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
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

public class DisplayNotePopup extends DialogFragment{

    public interface LoginDialogListener  {
        void onFinishEditDialog(String inputText, int id);
    }

    private TextView mText;
    private String data;

    DisplayNotePopup p;


    public DisplayNotePopup() {
        // Empty constructor required for DialogFragment
    }

        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        Bundle bund = this.getArguments();
        data = bund.getString("data");
        
        View view = inflater.inflate(R.layout.displaynote_fragment, container); 
        
        getDialog().setTitle("Note for friends!");
        
        // Find and set text view
        mText = (TextView)view.findViewById(R.id.text1);
        mText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        mText.setPadding(25, 25, 25, 25);
        mText.setText(data);    

        p = this;

        return view;
    }

           	
}
