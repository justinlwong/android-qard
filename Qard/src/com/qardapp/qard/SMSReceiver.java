package com.qardapp.qard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver
{
    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    Context mContext;
    private Intent mIntent;
    static String address, str = null;
    boolean isSame;
	private SharedPreferences mPrefs;

    // Retrieve SMS
    public void onReceive(Context context, Intent intent)
    {
        mContext = context;
        mIntent = intent;
        String action = intent.getAction();
        if(action.equals(ACTION_SMS_RECEIVED))
        {
            SmsMessage[] msgs = getMessagesFromIntent(mIntent);
            if (msgs != null)
            {
                for (int i = 0; i < msgs.length; i++)
                {
                    address = msgs[i].getOriginatingAddress();
                    str = msgs[i].getMessageBody().toString();
                }
            }
            Log.d("Originating Address : Sender :", ""+address);
            Log.d("Message from sender :", ""+str);
            // Used Shared Pref to store temp number
            mPrefs = context.getSharedPreferences("tokens", 0);
            isSame = PhoneNumberUtils.compare(str, mPrefs.getString("phoneNumber", "-1"));
            Log.d("Comparison :", "Yes this true. "+isSame);
            if(isSame)
            {
                mPrefs = context.getSharedPreferences("tokens", 0);
        		SharedPreferences.Editor editor = mPrefs.edit();
        		editor.putBoolean("wasMyOwnNumber", isSame);
        		editor.commit();    

            }

            // ---send a broadcast intent to update the SMS received in the
            // activity---
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms", str);
            context.sendBroadcast(broadcastIntent);
        }
    }

    public static SmsMessage[] getMessagesFromIntent(Intent intent)
    {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++)
        {
            pduObjs[i] = (byte[]) messages[i];
        }

        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++)
        {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
}