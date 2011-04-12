package edu.fsu.cs.contextprovider.sensors;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneService extends AbstractContextService {

	private static final String TAG = "PhoneState Service";
	private static final boolean DEBUG = true;

	TelephonyManager tm;
	private PhoneStateListener phoneStateListener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
			case TelephonyManager.CALL_STATE_RINGING:
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (DEBUG)
					Log.d(TAG, "send: " + state);
				// Amarino.sendDataFromPlugin(PhoneService.this, pluginId,
				// state);
				break;
			}
		}
	};

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
