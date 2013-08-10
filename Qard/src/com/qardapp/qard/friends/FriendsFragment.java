package com.qardapp.qard.friends;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.comm.server.FriendsInfoLoader;
import com.qardapp.qard.comm.server.ServerNotifications;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.friends.profile.FriendProfileFragment;

public class FriendsFragment extends BaseFragment implements LoaderCallbacks<ArrayList<ServerNotifications>>{
	
	private static int FRIENDS_INFO_LOADER_ID = 0;
	
	private boolean searchOpen = false;
	private FriendsCursorAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.friends_layout,
				container, false);
		GridView listView = (GridView) rootView
				.findViewById(R.id.friends_listview);
		
		EditText search = (EditText) rootView.findViewById(R.id.friends_search);
		//search.setIconifiedByDefault(false);
		
		/*
		Cursor cursor = null;
		ContentResolver res = getActivity().getContentResolver();
		cursor = res.query(FriendsProvider.CONTENT_URI, null, null, null, null); */
		adapter = new FriendsCursorAdapter(rootView.getContext(), null, FriendsCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	    adapter.setFilterQueryProvider(new FilterQueryProvider() {
	        public Cursor runQuery(CharSequence constraint) {
	            // Search for states whose names begin with the specified letters.
		        ContentResolver cr = getActivity().getContentResolver();
		        Log.d("runq",constraint.toString());
          	    String whereName = FriendsDatabaseHelper.COLUMN_FIRST_NAME + " LIKE ?";
          	    String[] whereNameParams = new String[] { constraint.toString() + "%" };
	            Cursor cursor = cr.query(FriendsProvider.CONTENT_URI, null, whereName, whereNameParams, null);
	            return cursor;

	        }
	    });
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				FragmentManager manager = FriendsFragment.this.getFragmentManager();
				FriendProfileFragment fragment = (FriendProfileFragment) manager.findFragmentByTag(MainActivity.FRAGNAME_FRIENDS_PROFILE);
				if (fragment == null)
					fragment = new FriendProfileFragment();
				fragment.setId(id);
				FragmentTransaction transaction = FriendsFragment.this.getFragmentManager().beginTransaction();
				transaction.replace(R.id.main_container, fragment, MainActivity.FRAGNAME_FRIENDS_PROFILE);
				transaction.addToBackStack(null);
				// Commit the transaction
				transaction.commit();

			}
		});
		
		search.addTextChangedListener(new TextWatcher() {
		     
		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        // When user changed the Text
		    	Log.d("here",cs.toString());
		        adapter.getFilter().filter(cs.toString());   
		    }
		     
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		            int arg3) {
		        // TODO Auto-generated method stub
		         
		    }
		     
		    @Override
		    public void afterTextChanged(Editable arg0) {
		        // TODO Auto-generated method stub                          
		    }

		});
			
		rootView.requestFocus();
		return rootView;
	}

	
	
	@Override
	public Loader<ArrayList<ServerNotifications>> onCreateLoader(int id,
			Bundle arg1) {
		if (id == FRIENDS_INFO_LOADER_ID)
			return new FriendsInfoLoader(this.getActivity());
		return null;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<ServerNotifications>> arg0,
			ArrayList<ServerNotifications> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<ServerNotifications>> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void openSearchKeyboard() {
		if (getView() != null) {
			View v = getView().findViewById(R.id.friends_search);
			v.requestFocus();
			searchOpen = false;
		} else {
			searchOpen = true;
		}
	}

	@Override
	public void updateViews() {
		Cursor cursor = null;
		ContentResolver res = getActivity().getContentResolver();
		cursor = res.query(FriendsProvider.CONTENT_URI, null, null, null, null);
		adapter.changeCursor(cursor);
		if (searchOpen) {
			getView().findViewById(R.id.friends_search).requestFocus();
			searchOpen = false;
		}
	}
	
}
