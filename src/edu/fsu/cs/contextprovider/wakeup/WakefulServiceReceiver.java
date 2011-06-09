package edu.fsu.cs.contextprovider.wakeup;

import edu.fsu.cs.contextprovider.ContextService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class WakefulServiceReceiver extends BroadcastReceiver {
	private static final String TAG = "WakefulServiceReceiver";

	private static final int PERIOD =  30; // 300000; 	// 5 minutes
	
	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, edu.fsu.cs.contextprovider.wakeup.WakeupAlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+10000, PERIOD * 1000, pi);

		context.startService(new Intent(context, edu.fsu.cs.contextprovider.ContextService.class));
	}
}
