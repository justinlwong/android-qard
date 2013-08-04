package com.qardapp.qard.settings;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.settings.services.AccountChecker;
import com.qardapp.qard.settings.services.PopupDialog;
import com.qardapp.qard.settings.services.SyncContactsActivity;
import com.qardapp.qard.util.ImageUtil;

import android.widget.ListView;
import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.R;

public class SettingsProfileFragment extends BaseFragment {

	private SettingsProfileAdapter adapter;
	private Drawable darkBackground;
	ImageView iView;
	Button b;
	Cursor cursor;
	ContentResolver res;
	View rootView;
	LinearLayout profilelayout;
	View fullView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// New layout
		rootView = inflater.inflate(R.layout.settings_profile_layout1,
				container, false);
		ListView listView = (ListView) rootView.findViewById(R.id.settings_service_list);
		
		res = getActivity().getContentResolver();
		cursor = res.query(FriendsProvider.MY_URI, null, null, null, null);
		cursor.moveToFirst();
		
		// Get layout
		profilelayout = (LinearLayout)rootView.findViewById(R.id.profilelayout);
		addFullView();
				
		adapter = new SettingsProfileAdapter(getActivity(), null, this );
		listView.setAdapter(adapter);
		
		return rootView;
	}
	
	public void addFullView() {
		fullView = LayoutInflater.from(getActivity()).inflate(R.layout.settings_service_item, null);
		iView = (ImageView) fullView.findViewById(R.id.settings_service_item_image);
		b = (Button) fullView.findViewById(R.id.settings_service_item_button);

		darkBackground = getActivity().getResources().getDrawable(R.drawable.settings_service_item_button_background_dark);
		
		// Set username and picture
		String first_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_FIRST_NAME));
		String last_name = cursor.getString(cursor.getColumnIndex(FriendsDatabaseHelper.COLUMN_LAST_NAME));
		iView.setImageBitmap(ImageUtil.getProfilePic(getActivity(), 0));
		
		// Add conditional text here
		if (first_name == null || last_name == null) {
			b.setText("Add full name");
			b.setTextColor(Color.WHITE);
			b.setBackground(darkBackground.getConstantState().newDrawable());
		}
		else {
			b.setText(first_name + last_name);
			b.setTextColor(Color.DKGRAY);
			b.setBackgroundColor(Color.LTGRAY);
		}
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showEditDialog();
			}
		});
		
		b.setText(first_name + " " + last_name);
		fullView.setPadding(0, 0, 0, 12);
		profilelayout.addView(fullView);
				
	}
	

	@Override
	public void updateViews() {	
		ContentResolver res = getActivity().getContentResolver();
		Cursor cursor = res.query(FriendsProvider.MY_URI, null, null, null, null);
		adapter.changeCursor(cursor);
	}
	
    private void showEditDialog() {
        FragmentManager fm = this.getFragmentManager();        
        UsernamePopup popupDialog = (UsernamePopup) fm.findFragmentByTag("username_fragment");
       		
        popupDialog = new UsernamePopup();
        FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();
        //loginDialog.show(transaction, "login_fragment");
        transaction.add(popupDialog,"username_fragment").commit();
    }

}
