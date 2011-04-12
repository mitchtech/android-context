package edu.fsu.cs.contextprovider.sensors;

import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class AccelerometerService extends AbstractContextService implements SensorEventListener, OnSharedPreferenceChangeListener {

	private static final String TAG = "Accelerometer Service";
	private static final boolean DEBUG = true;
	private SensorManager sm;
	private Sensor accelerometerSensor;
	
	private int frequency;
	private int ignoreThreshold = 0;
	
	int ACCELEROMETER_POLL_FREQUENCY;
	int ACCELEROMETER_IGNORE_THRESHOLD;
		
	private int ignoreCounter = 0;

	@Override
	public void init() {
		if (!serviceEnabled) {

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			// frequency =prefs.getInt(AccelerometerEditActivity.PREF_FREQUENCY, 50);
			// pluginId = prefs.getInt(AccelerometerEditActivity.KEY_PLUGIN_ID,
			// -1);
			// ignoreThreshold = AccelerometerEditActivity.getRate(frequency);

			prefs.registerOnSharedPreferenceChangeListener(this);

			// make sure not to call it twice
			sm = (SensorManager) getSystemService(SENSOR_SERVICE);
			List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
			if (sensors != null && sensors.size() > 0) {
				accelerometerSensor = sensors.get(0);
				sm.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
				serviceEnabled = true;
			} else {
				Toast.makeText(this, "Accelerometer sensor is not available on this device!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onDestroy() {
		if (serviceEnabled) {
			sm.unregisterListener(this);
			PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		}
		super.onDestroy();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// we don't need this
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			if (ignoreCounter >= ignoreThreshold) {
				ignoreCounter = 0;

				if (DEBUG)
					Log.d(TAG, "send: x:" + event.values[0] + " y:" + event.values[1] + " z: " + event.values[2]);
				// Amarino.sendDataFromPlugin(this, pluginId,
				// event.values.clone());
			} else {
				ignoreCounter++;
			}
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		// if (AccelerometerEditActivity.PREF_FREQUENCY.equals(key)) {
		// ignoreThreshold = AccelerometerEditActivity.getRate(prefs.getInt(key,
		// 50));
		// }
	}

	@Override
	public String getTAG() {
		return TAG;
	}

}
