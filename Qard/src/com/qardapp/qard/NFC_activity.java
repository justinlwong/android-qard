package com.qardapp.qard;

import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

/*
 * This activity handles the NFC message and will launch the application after receiving the message
 * (This activity has no view)
 */
public class NFC_activity extends Activity implements CreateNdefMessageCallback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_nfc_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/*
	 * Unused (not generating NFC message)
	 */
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/*
	 * If we receive a NFC message, process it
	 */
	@Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }
	
    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /*
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        
        // Create a msg box to show the message received
        Toast.makeText(this, new String(msg.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
        
        // Send the receipt data to the MainActivity via bundles
        Intent mainIntent = new Intent(this,
                MainActivity.class);
        Bundle opt = new Bundle();
        opt.putString("nfc_data", new String (msg.getRecords()[0].getPayload()) );
        mainIntent.putExtras(opt);
        this.startActivity(mainIntent);
    }
	
}
