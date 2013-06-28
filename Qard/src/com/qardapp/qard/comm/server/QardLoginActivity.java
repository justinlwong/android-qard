package com.qardapp.qard.comm.server;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.qardapp.qard.R;

public class QardLoginActivity extends SherlockFragmentActivity implements LoaderCallbacks<String>{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button button = (Button)findViewById(R.id.login_button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//String username = ((EditText)LoginActivity.this.findViewById(R.id.login_username)).getText().toString();
				//String password = ((EditText)LoginActivity.this.findViewById(R.id.login_password)).getText().toString();
				QardLoginActivity.this.getSupportLoaderManager().restartLoader(0, null, QardLoginActivity.this);
			}
		});
		QardLoginActivity.this.getSupportLoaderManager().initLoader(0, null, QardLoginActivity.this);

	}

	@Override
	public Loader<String> onCreateLoader(int arg0, Bundle arg1) {
		return new LoginTaskLoader(QardLoginActivity.this, (EditText)QardLoginActivity.this.findViewById(R.id.login_username), (EditText)QardLoginActivity.this.findViewById(R.id.login_password));

	}

	@Override
	public void onLoadFinished(Loader<String> loader, String token) {
		if (token != null) {
			finish();
		}
			
	}

	@Override
	public void onLoaderReset(Loader<String> arg0) {
		// TODO Auto-generated method stub
		
	}

}
