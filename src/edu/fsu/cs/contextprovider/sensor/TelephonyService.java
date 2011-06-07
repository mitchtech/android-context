package edu.fsu.cs.contextprovider.sensor;

import java.util.List;

import edu.fsu.cs.contextprovider.data.ContextConstants;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class TelephonyService extends Service {

	private static final String TAG = "SMS Service";
	private static final boolean DEBUG = true;
	protected boolean serviceEnabled = false;
	
	public static String PHONE_STATE = "NA";
	public static long PHONE_STATE_UPDATE = 0;	
	public static long PHONE_LAST_CALL = 0;
	public static long PHONE_LAST_NUMBER_RECV = 0;
	public static long PHONE_LAST_NUMBER_DIAL = 0;	
	
	public static String SMS_STATE = "NA";
	public static String SMS_LAST_SENDER = "NA";
	public static String SMS_LAST_MESSAGE = "NA";
	public static long SMS_STATE_UPDATE = 0;	
	public static long SMS_STATE_SINCE_UPDATE = 0;	
	
	private static final int MAX_LENGTH = 30;
	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	

	TelephonyManager tm;
	private PhoneStateListener phoneStateListener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				PHONE_STATE = "CALL_STATE_IDLE";
				PHONE_STATE_UPDATE = Long.valueOf(System.currentTimeMillis());
				if (DEBUG) Log.d(TAG, "State: " + PHONE_STATE);
				break;				
			
			case TelephonyManager.CALL_STATE_RINGING:
				PHONE_STATE = "CALL_STATE_RINGING";
				PHONE_STATE_UPDATE = Long.valueOf(System.currentTimeMillis());
				if (DEBUG) Log.d(TAG, "State: " + PHONE_STATE);
				break;			
				
			case TelephonyManager.CALL_STATE_OFFHOOK:
				PHONE_STATE = "CALL_STATE_IDLE";
				PHONE_STATE_UPDATE = Long.valueOf(System.currentTimeMillis());
				if (DEBUG) Log.d(TAG, "State: " + PHONE_STATE);
				break;
			}
		}
	};
	
	
	
	
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_SMS_RECEIVED.equals(intent.getAction())) {
				Bundle bundle = intent.getExtras();

				if (bundle != null) {
					Object[] pdu = (Object[]) bundle.get("pdus");
					SmsMessage[] messages = new SmsMessage[pdu.length];

					for (int i = 0; i < pdu.length; i++) {
						messages[i] = SmsMessage.createFromPdu((byte[]) pdu[i]);
					}

					for (SmsMessage message : messages) {
						String msg = message.getMessageBody();
						String msgSender = message.getDisplayOriginatingAddress(); 

						if (msg.length() > MAX_LENGTH)
							msg = msg.substring(0, MAX_LENGTH);

						if (DEBUG)
							Log.d(TAG, "send: " + msg);
						
						SMS_STATE = "SMS_STATE_IDLE";
						SMS_STATE_UPDATE = Long.valueOf(System.currentTimeMillis());
						SMS_LAST_SENDER = msgSender;
						SMS_LAST_MESSAGE = msg;
						if (DEBUG) Log.d(TAG, "State: " + SMS_STATE);
						break;			


					}
				}
			}
		}
	};


	public void init() {
		if (!serviceEnabled) {
			startService();
		}
	}
	
	private void startService() {

		getPrefs();

		// make sure not to call it twice
		IntentFilter filter = new IntentFilter(ACTION_SMS_RECEIVED);
		registerReceiver(receiver, filter);
		// don't forget to set plug-in enabled if everything was initialized
		serviceEnabled = true;
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		IntentFilter restartFilter = new IntentFilter();
		restartFilter.addAction(ContextConstants.CONTEXT_RESTART_INTENT);
		registerReceiver(restartIntentReceiver, restartFilter);
	}
	
	private void getPrefs() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		// prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// frequency = prefs.getInt(AccelerometerEditActivity.PREF_FREQUENCY,
		// 50);
		// ignoreThreshold = AccelerometerEditActivity.getRate(frequency);

		// prefs.registerOnSharedPreferenceChangeListener(this);
	}

	private void stopService() {
		// PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		unregisterReceiver(restartIntentReceiver);
		unregisterReceiver(receiver);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}
	
	BroadcastReceiver restartIntentReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, TAG + "Restart Intent: " + intent.getAction());
			if (serviceEnabled) {
				stopService();
			}
			startService();
		}
	};
	
	@Override
	public void onDestroy() {
		if (serviceEnabled) {
			stopService();
		}
		super.onDestroy();
	}
	
	public String getTAG() {
		return TAG;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
