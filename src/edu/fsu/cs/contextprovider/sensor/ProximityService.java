package edu.fsu.cs.contextprovider.sensor;

import java.util.List;

import edu.fsu.cs.contextprovider.data.ContextConstants;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ProximityService extends Service implements SensorEventListener, OnSharedPreferenceChangeListener {

	private static final String TAG = "ProximitySensor Service";
	private static final boolean DEBUG = true;
	protected boolean running = false;
	
	private SensorManager sm;
	private Sensor proximitySensor;
	
	SharedPreferences prefs;
	
	public void init() {
		if (!running) {
			startService();
		}
	}
	
	private void startService() {

		getPrefs();

		// make sure not to call it twice
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_PROXIMITY);
		if (sensors != null && sensors.size() > 0) {
			proximitySensor = sensors.get(0);
			sm.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_UI);
			running = true;
		} else {
			Toast.makeText(this, "Proximity sensor is not available on this device!", Toast.LENGTH_SHORT).show();
		}

		IntentFilter restartFilter = new IntentFilter();
		restartFilter.addAction(ContextConstants.CONTEXT_RESTART_INTENT);
		registerReceiver(restartIntentReceiver, restartFilter);
	}
	
	private void getPrefs() {
		prefs = getSharedPreferences(ContextConstants.CONTEXT_PREFS, MODE_WORLD_READABLE);
//		accelPoll = prefs.getInt(ContextConstants.PREFS_ACCEL_POLL_FREQ, 1);
//		ignoreThreshold = prefs.getInt(ContextConstants.PREFS_ACCEL_IGNORE_THRESHOLD, 0);
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	private void stopService() {
		sm.unregisterListener(this);
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		unregisterReceiver(restartIntentReceiver);
		running = false;
	}
	
	BroadcastReceiver restartIntentReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, TAG + "Restart Intent: " + intent.getAction());
			if (running) {
				stopService();
			}
			startService();
		}
	};
	
	@Override
	public void onDestroy() {
		if (running) {
			stopService();
		}
		super.onDestroy();
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// we don't need this
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {

			int cm = (int) event.values[0];

			if (DEBUG) Log.d(TAG, "send: " + cm);
//			Amarino.sendDataFromPlugin(this, pluginId, cm);
		}
	}

	public String getTAG() {
		return TAG;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		
	}

}
