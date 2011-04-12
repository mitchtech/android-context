package edu.fsu.cs.contextprovider.sensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class BatteryService extends AbstractContextService {

	private static final String TAG = "BatteryLevel Service";
	private static final boolean DEBUG = true;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

			if (DEBUG)
				Log.d(TAG, "send: " + batteryLevel);
			// Amarino.sendDataFromPlugin(context, pluginId, batteryLevel);
		}
	};

	@Override
	public void init() {
		if (!serviceEnabled) {
			/* here should be your specific initialization code */

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			// pluginId = prefs.getInt(BatteryEditActivity.KEY_PLUGIN_ID, -1);

			IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			registerReceiver(receiver, filter);

			// don't forget to set plug-in enabled if everything was initialized
			// fine
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
