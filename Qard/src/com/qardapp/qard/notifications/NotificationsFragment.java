package com.qardapp.qard.notifications;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.Services;
import com.qardapp.qard.comm.server.GetFriendsTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.friends.FriendsCursorAdapter;
import com.qardapp.qard.friends.FriendsFragment;
import com.qardapp.qard.friends.profile.FriendProfileFragment;
import com.qardapp.qard.friends.profile.FriendsProfileAdapter;
import com.qardapp.qard.util.ImageUtil;

public class NotificationsFragment extends BaseFragment{

	private NotificationsCursorAdapter adapter;
	
	public static String selector = null;
	public static String last_query = null;
	public static String selector_args[] = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		GetFriendsTask task = new GetFriendsTask(getActivity());
		task.execute();
		View rootView = inflater.inflate(R.layout.notifications_layout,
				container, false);
		ListView listView = (ListView) rootView
				.findViewById(R.id.notifications_listview);
		
		adapter = new NotificationsCursorAdapter(rootView.getContext(), null, NotificationsCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	    //adapter.setFilterQueryProvider(new FilterQueryProvider() {
	        //public Cursor runQuery(CharSequence constraint) {
	            // Search for states whose names begin with the specified letters.
		        //ContentResolver cr = getActivity().getContentResolver();
		        //Log.d("runq",constraint.toString());
          	    //String whereName = FriendsDatabaseHelper.COLUMN_FIRST_NAME + " LIKE ?";
          	    //String[] whereNameParams = new String[] { constraint.toString() + "%" };
	            //Cursor cursor = cr.query(FriendsProvider.CONTENT_URI, null, whereName, whereNameParams, null);
	            //return cursor;

	        //}
	    //});
		listView.setAdapter(adapter);

		//listView.setOnItemClickListener(new OnItemClickListener() {

//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				FragmentManager manager = NotificationsFragment.this.getFragmentManager();
//				FriendProfileFragment fragment = (FriendProfileFragment) manager.findFragmentByTag(MainActivity.FRAGNAME_FRIENDS_PROFILE);
//				if (fragment == null)
//					fragment = new FriendProfileFragment();
//				fragment.setId((int)id);
//				FragmentTransaction transaction = NotificationsFragment.this.getFragmentManager().beginTransaction();
//				transaction.replace(R.id.main_container, fragment, MainActivity.FRAGNAME_FRIENDS_PROFILE);
//				transaction.addToBackStack(null);
//				// Commit the transaction
//				transaction.commit();
//
//			}
//		});
//			
		rootView.requestFocus();
		return rootView;
	}

	@Override
	public void updateViews() {
		Cursor cursor = null;
		ContentResolver res = getActivity().getContentResolver();
		//String fprefix = FriendsDatabaseHelper.TABLE_FRIENDS + ".";
		String where = FriendsDatabaseHelper.COLUMN_CONFIRMED + "=?";
		String[] args = new String[] {"false"};
		cursor = res.query(FriendsProvider.CONTENT_URI, null, where, args, null);
		// add where argument to only add those who have yet to be added
		adapter.changeCursor(cursor);

	}

}
