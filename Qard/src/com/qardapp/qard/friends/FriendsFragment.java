package com.qardapp.qard.friends;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.SearchView.OnQueryTextListener;

import com.qardapp.qard.BaseFragment;
import com.qardapp.qard.MainActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.comm.server.GetFriendsTask;
import com.qardapp.qard.database.FriendsDatabaseHelper;
import com.qardapp.qard.database.FriendsProvider;
import com.qardapp.qard.friends.profile.FriendProfileFragment;

public class FriendsFragment extends BaseFragment {
	
	
	private boolean searchOpen = false;
	private FriendsCursorAdapter adapter;
	
	public static String selector = null;
	public static String last_query = null;
	public static String selector_args[] = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		GetFriendsTask task = new GetFriendsTask(getActivity());
		task.execute();
		View rootView = inflater.inflate(R.layout.friends_layout,
				container, false);
		GridView listView = (GridView) rootView
				.findViewById(R.id.friends_listview);
		
		EditText search = (EditText) rootView.findViewById(R.id.friends_search);
		//search.setIconifiedByDefault(false);
		
		/*
		//Start GG
		// Check if there was a last search filter query
				if (last_query != null)
					search.setQuery(last_query, false);

				// If the user types into the search bar, update the list
				search.setOnQueryTextListener(new OnQueryTextListener() {

					public boolean onQueryTextSubmit(String query) {

						// Filter if there is something in the search bar
						if (!(query.equals(""))) {
							String args[] = { "%" + query + "%", "%" + query + "%" };
							selector_args = args.clone();
							selector = FriendsDatabaseHelper.COLUMN_FIRST_NAME
									+ " LIKE ? OR "
									+ FriendsDatabaseHelper.COLUMN_LAST_NAME
									+ " LIKE ?";
							Log.e("hi", "got here");
							//showFriends(view, selector, selector_args);
							last_query = query;
						} else {
							selector = null;
							selector_args = null;
							last_query = null;
							Log.e("hi", "not got here");
							//showFriends(view, null, null);
						}
						return false;
					}

					@Override
					public boolean onQueryTextChange(String newText) {
						// Filter if there is something in the search bar
						if (newText.length() >= 3) {
							String args[] = { "%" + newText + "%", "%" + newText + "%" };
							selector_args = args.clone();
							selector = FriendsDatabaseHelper.COLUMN_FIRST_NAME
									+ " LIKE ? OR "
									+ FriendsDatabaseHelper.COLUMN_LAST_NAME
									+ " LIKE ?";
							Log.e("hi", "got here change");
							//showFriends(view, selector, selector_args);
							last_query = newText;
						} else if (selector != null) {
							selector = null;
							selector_args = null;
							//showFriends(view, null, null);
							last_query = null;
						}

						return false;
					}
				});
		
		*/
		//End GG
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
