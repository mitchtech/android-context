package edu.fsu.cs.contextprovider.sensor;

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

public class MagnetometerService extends AbstractContextService 
			implements SensorEventListener, OnSharedPreferenceChangeListener {
	
	private static final String TAG = "MagneticField Service";
	private static final boolean DEBUG = true;
		
	private SensorManager sm;
	private Sensor magneticfieldSensor;
	private int frequency;
	private int ignoreThreshold = 0;
	private int ignoreCounter = 0;
	
	@Override
	public void init() {
		if (!serviceEnabled){
		
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//			frequency = prefs.getInt(MagnetometerEditActivity.PREF_FREQUENCY, 50);
//			pluginId = prefs.getInt(MagnetometerEditActivity.KEY_PLUGIN_ID, -1);
//			ignoreThreshold = MagnetometerEditActivity.getRate(frequency);
			
			prefs.registerOnSharedPreferenceChangeListener(this);
		
			// make sure not to call it twice
			sm = (SensorManager) getSystemService(SENSOR_SERVICE);
			List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
			if (sensors != null && sensors.size() > 0) {
				magneticfieldSensor = sensors.get(0);
				sm.registerListener(this, magneticfieldSensor, SensorManager.SENSOR_DELAY_UI);
				serviceEnabled = true;
			}
			else {
				Toast.makeText(this, "Magnetic field sensor is not available on this device!", Toast.LENGTH_SHORT).show();
			}
		}
	}


	@Override
	public void onDestroy() {
		if (serviceEnabled){
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
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			
			if (ignoreCounter >= ignoreThreshold) {
				ignoreCounter = 0;
				
				if (DEBUG) Log.d(TAG, "send: x:" + event.values[0] + " y:" + event.values[1] + " z: " + event.values[2]);
//				Amarino.sendDataFromPlugin(this, pluginId, event.values.clone());
			}
			else {
				ignoreCounter++;
			}
		}
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs,
			String key) {
//		if (MagnetometerEditActivity.PREF_FREQUENCY.equals(key)){
//			ignoreThreshold = MagnetometerEditActivity.getRate(prefs.getInt(key, 50));
//		}
	}


	@Override
	public String getTAG() {
		return TAG;
	}
	
}
