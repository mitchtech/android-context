package edu.fsu.cs.contextprovider.sensor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneService extends AbstractContextService {

	private static final String TAG = "PhoneState Service";
	private static final boolean DEBUG = true;
	public static String PHONE_STATE = "NA";
	// public static String PHONE_STATE_UPDATE = "NA";
	public static long PHONE_STATE_UPDATE = 0;	
	public static long PHONE_STATE_SINCE_UPDATE = 0;	
	
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

	@Override
	public void onCreate() {
		if (!serviceEnabled) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			// pluginId = prefs.getInt(PhoneEditActivity.KEY_PLUGIN_ID, -1);
			serviceEnabled = true;
			tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	@Override
	public void init() {
		if (!serviceEnabled) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			// pluginId = prefs.getInt(PhoneEditActivity.KEY_PLUGIN_ID, -1);
			serviceEnabled = true;
			tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
	
	@Override
	public void onDestroy() {
		if (serviceEnabled) {
			tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			tm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
		super.onDestroy();
	}

	@Override
	public String getTAG() {
		return TAG;
	}

}
