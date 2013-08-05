package com.qardapp.qard;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.qardapp.qard.comm.QardMessage;
import com.qardapp.qard.nfc.NFCManager;
import com.qardapp.qard.qrcode.QRCodeManager;
import com.qardapp.qard.widget.QardWidgetProvider;

public class QRCodeDisplayActivity extends Activity {
	private Button profile_send, profile_send_text, profile_send_share;
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
				Log.d("MSG", msg);

				NFCManager.sendNFC(msg, this);
			}
		}
		profile_send = (Button) findViewById(R.id.profile_send);
		profile_send.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent();

				//intent.putExtra("msg", QardMessage.getMessage(this.getClass()));
				//startActivity(intent);
			}
		});
		profile_send_text = (Button) findViewById(R.id.profile_send_text);
		profile_send_text.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String smsBody="NexCircle SMS TEST";
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", smsBody); 
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			}
		});
		profile_send_share = (Button) findViewById(R.id.profile_send_share);
		profile_send_share.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = "Text Share Content TEST";
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share your profile via"));
				
				/*
				String msg = QardMessage.getMessage(getBaseContext());
				ImageView view = (ImageView) findViewById(R.id.qrcode_image);
				view.setImageBitmap(QRCodeManager.genQRCodeBitmap(msg, 10));
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("/sdcard/test.jpg"));
				shareIntent.setType("image/jpeg");
				startActivity(Intent.createChooser(shareIntent, "Share NexCircle"));
				*/
			}
		});
		
	    
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qrcode_display, menu);
		return true;
	}

}
