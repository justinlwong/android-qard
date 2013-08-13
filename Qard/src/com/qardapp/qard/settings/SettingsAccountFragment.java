package com.qardapp.qard.settings;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.comm.server.QardLoginActivity;
import com.qardapp.qard.comm.server.ServerHelper;
import com.qardapp.qard.comm.server.UpdateUserTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;

public class SettingsAccountFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.settings_account_layout2,
				container, false);
		/*
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
		
*/
		
		return rootView;
	}

	@Override
	public void updateViews() {
		/*
		TextView current_id_field = (TextView) getView().findViewById(R.id.settings_current_id);
		String user_id = ServerHelper.getUserId(getActivity());
		if (user_id != null) {
			current_id_field.setText("(DEBUG) Current qard id = " + user_id);
		} else {
			current_id_field.setText("(DEBUG) Current qard id not found, restart?");
		}
		
		ContentResolver res = getActivity().getContentResolver();
		Cursor cursor = res.query(FriendsProvider.MY_URI, null, null, null, null);
		
		cursor.moveToFirst();
		
		EditText firstNameText = (EditText) getView().findViewById(R.id.settings_first_name);
		EditText lastNameText = (EditText) getView().findViewById(R.id.settings_first_name);
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
		firstNameText.setText(first_name);
		lastNameText.setText(last_name);*/
	}
}
