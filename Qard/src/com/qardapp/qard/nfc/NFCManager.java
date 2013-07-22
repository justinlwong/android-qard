package com.qardapp.qard.nfc;

import com.qardapp.qard.comm.QardMessage;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.widget.Toast;

public class NFCManager {
	
	public static String NFC_APPLICATION_TAG = "application/com.qardapp.qard.nfc_activity";

	
	public static void sendMyNFC (Activity activity) {
		sendNFC(QardMessage.getMessage(activity), activity);
	}
	
	public static void sendNFC (String msg, Activity activity) {
	    NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
	            NFC_APPLICATION_TAG.getBytes(), new byte[] {}, msg.getBytes());
	    NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {textRecord});
		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(activity);
		// Check whether we have NFC enabled
		if (adapter != null)
			adapter.setNdefPushMessage(ndefMessage, activity);
		else {
			Toast.makeText(activity, "No NFC device found", Toast.LENGTH_SHORT).show();
			Log.d("NFC","NotFound");
		}
	}
}
