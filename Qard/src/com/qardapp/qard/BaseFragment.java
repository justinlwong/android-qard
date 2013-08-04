package com.qardapp.qard;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BaseFragment extends SherlockFragment{
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
		updateViews();
	}
	
	public abstract void updateViews();

	public void updateUser(){};

}
