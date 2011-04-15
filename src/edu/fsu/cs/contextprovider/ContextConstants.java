package edu.fsu.cs.contextprovider;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class ContextConstants {
	public static String TAG_GEO = "Geocoder";
	
	public static int TEXT = 0;
	public static int GRAPH = 1;
	public static int BARS = 2;
	
	
	
	public static int COMPASS_POLL_FREQUENCY;
	public static int COMPASS_IGNORE_THRESHOLD;
	
	
	
	
	
	public static float getMaxSensorRange(Context context, int sensorType, float defaultValue){
		float max = defaultValue; 
		SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(sensorType);
		
		if (sensors != null && sensors.size() > 0)
			max = sensors.get(0).getMaximumRange();
		
		return max;
	}

}
