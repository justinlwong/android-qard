package com.qardapp.qard.friends;

import com.qardapp.qard.FriendProfileActivity;
import com.qardapp.qard.R;
import com.qardapp.qard.database.FriendsProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FriendsFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.friends_layout,
				container, false);
		ListView listView = (ListView) rootView
				.findViewById(R.id.friends_listview);
		
		SearchView search = (SearchView) rootView.findViewById(R.id.friends_search);
		search.setIconifiedByDefault(false);

		
		Cursor cursor = null;
		ContentResolver res = getActivity().getContentResolver();
		cursor = res.query(FriendsProvider.CONTENT_URI, null, null, null, null);
		
		listView.setAdapter(new FriendsCursorAdapter(rootView.getContext(), cursor, FriendsCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
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
	
}
