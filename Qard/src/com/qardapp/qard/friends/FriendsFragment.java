package com.qardapp.qard.friends;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

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
	
	public static String selector = null;
	public static String last_query = null;
	public static String selector_args[] = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.friends_layout,
				container, false);
		GridView listView = (GridView) rootView
				.findViewById(R.id.friends_listview);
		
		SearchView search = (SearchView) rootView.findViewById(R.id.friends_search);
		search.setIconifiedByDefault(false);
		//***********************
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
		
		
		//////////////////////
		/*
		Cursor cursor = null;
		ContentResolver res = getActivity().getContentResolver();
		cursor = res.query(FriendsProvider.CONTENT_URI, null, null, null, null); */
		adapter = new FriendsCursorAdapter(rootView.getContext(), null, FriendsCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		listView.setAdapter(adapter);
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
