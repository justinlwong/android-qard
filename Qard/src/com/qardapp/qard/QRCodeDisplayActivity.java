package com.qardapp.qard;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.qardapp.qard.qrcode.QRCodeManager;

public class QRCodeDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode_display);
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			String msg = extra.getString("msg");
			if (msg != null && !(msg.isEmpty())) {
		
				ImageView qrcode = (ImageView) findViewById(R.id.qrcode_image);
				QRCodeManager.genQRCode (msg, qrcode);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qrcode_display, menu);
		return true;
	}

}
