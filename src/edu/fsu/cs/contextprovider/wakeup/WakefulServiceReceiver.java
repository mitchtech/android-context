package edu.fsu.cs.contextprovider.wakeup;

import edu.fsu.cs.contextprovider.data.ContextConstants;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class WakefulServiceReceiver extends BroadcastReceiver {
	private static final String TAG = "WakefulServiceReceiver";
	private static final boolean DEBUG = true;

	private boolean startupEnabled;
	private boolean accuracyPopupEnabled;
	private String accuracyPopupPeriod;
	private int period; // = 30; // 300000; // 5 minutes

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences prefs = context.getSharedPreferences(ContextConstants.CONTEXT_PREFS, 0);
		// SharedPreferences prefs =
		// PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		startupEnabled = prefs.getBoolean(ContextConstants.PREFS_STARTUP_ENABLED, true);
		accuracyPopupEnabled = prefs.getBoolean(ContextConstants.PREFS_ACCURACY_POPUP_ENABLED, true);
		// accuracyPopupPeriod =
		// prefs.getInt(ContextConstants.PREFS_ACCURACY_POPUP_FREQ, 30);
		accuracyPopupPeriod = prefs.getString(ContextConstants.PREFS_ACCURACY_POPUP_FREQ, "30");
		period = Integer.parseInt(accuracyPopupPeriod);

		if (DEBUG) {
			Log.d(TAG, "accuracyPopupEnabled: " + accuracyPopupEnabled + "  accuracyPopupPeriod: " + accuracyPopupPeriod + "  period: " + period);
		}

		if (startupEnabled) {
			context.startService(new Intent(context, edu.fsu.cs.contextprovider.ContextService.class));
		}

		if (accuracyPopupEnabled) {
			AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(context, edu.fsu.cs.contextprovider.wakeup.WakeupAlarmReceiver.class);
			PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
			manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10000, period * 1000, pi);
		}
	}

}
