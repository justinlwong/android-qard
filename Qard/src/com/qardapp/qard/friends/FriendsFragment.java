package com.qardapp.qard.friends;

import java.util.ArrayList;

import com.qardapp.qard.FriendProfileActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.comm.server.FriendsInfoLoader;
import com.qardapp.qard.comm.server.ServerNotifications;
import com.qardapp.qard.database.FriendsProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FriendsFragment extends Fragment implements LoaderCallbacks<ArrayList<ServerNotifications>>{
	
	private static int FRIENDS_INFO_LOADER_ID = 0;
	private FriendsCursorAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.friends_layout,
				container, false);
		ListView listView = (ListView) rootView
				.findViewById(R.id.friends_listview);
		
		SearchView search = (SearchView) rootView.findViewById(R.id.friends_search);
		search.setIconifiedByDefault(false);

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
				/*FriendProfileFragment frag = new FriendProfileFragment();
				FragmentTransaction transaction = FriendsFragment.this.getFragmentManager().beginTransaction();
				transaction.replace(R.id.pager, frag);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();*/
				Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
				Bundle opt = new Bundle();
		        opt.putString("id",  id + "");
		        intent.putExtras(opt);
				startActivity(intent);
			}
		});
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

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
		Cursor cursor = null;
		ContentResolver res = getActivity().getContentResolver();
		cursor = res.query(FriendsProvider.CONTENT_URI, null, null, null, null);
		adapter.swapCursor(cursor);
	}
	
}
