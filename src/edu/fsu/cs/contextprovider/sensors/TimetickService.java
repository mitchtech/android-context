package edu.fsu.cs.contextprovider.sensors;

import java.util.Date;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class TimetickService extends AbstractContextService {

	private static final String TAG = "TimeTick Service";
	private static final boolean DEBUG = true;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			int minutes = new Date().getMinutes();

			if (DEBUG)
				Log.d(TAG, "send: " + minutes);
			// Amarino.sendDataFromPlugin(context, pluginId, minutes);
		}
	};

	@Override
	public void init() {
		if (!serviceEnabled) {

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//			pluginId = prefs.getInt(TimetickEditActivity.KEY_PLUGIN_ID, -1);

			IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
			registerReceiver(receiver, filter);

			serviceEnabled = true;
		}
	}

	@Override
	public void onDestroy() {
		if (serviceEnabled) {
			unregisterReceiver(receiver);
		}
		super.onDestroy();
	}

	@Override
	public String getTAG() {
		return TAG;
	}

}
