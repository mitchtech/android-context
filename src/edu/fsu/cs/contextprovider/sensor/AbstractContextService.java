package edu.fsu.cs.contextprovider.sensor;

import edu.fsu.cs.contextprovider.ContextIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public abstract class AbstractContextService extends Service {

	protected int contextId;
	protected boolean serviceEnabled = false;
	protected static final boolean DEBUG = false;

	BroadcastReceiver disableReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null)
				return;
			String action = intent.getAction();
			if (SensorServiceReceiver.ACTION_DISABLE_ALL.equals(action)) {
				stopSelf();
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (DEBUG)
			Log.d(getTAG(), "onCreate");

		registerReceiver(disableReceiver, new IntentFilter(SensorServiceReceiver.ACTION_DISABLE_ALL));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return handleStart(intent, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		handleStart(intent, startId);
	}

	int handleStart(Intent intent, int startId) {
		if (intent == null) {
			// service was restarted after it was killed by the system due to
			// low memory condition
			init();
		} else {
			String action = intent.getAction();
			if (DEBUG)
				Log.d(getTAG(), action + " received");

			if (ContextIntent.ACTION_DISABLE.equals(action)) {
				stopSelf();
			} else if (ContextIntent.ACTION_ENABLE.equals(action)) {
				Log.d(getTAG(), "started");
				init();
			}
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unregisterReceiver(disableReceiver);
		Log.d(getTAG(), "stopped");
	}

	abstract public void init();

	abstract public String getTAG();
}
