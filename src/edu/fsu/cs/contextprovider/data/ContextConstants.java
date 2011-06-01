package edu.fsu.cs.contextprovider.data;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class ContextConstants {

	public static final String CONTEXT_PREFS = "CONTEXT_PREFS";
	public static final String PREFS_ADDRESS = "PREFS_ADDRESS";
	
	
	public static final String CONTEXT_TIMESTAMP = "Timestamp";
	public static final String CONTEXT_ACCURACY = "Accuracy";

	public static final String LOCATION_ALL = "LOCATION_ALL";
	public static final String LOCATION_ADDRESS = "Address";
	public static final String LOCATION_HOOD = "Neighborhood";
	public static final String LOCATION_ZIP = "Zip";
	public static final String LOCATION_LATITUDE = "Latitude";
	public static final String LOCATION_LONGITUDE = "Longitude";
	public static final String LOCATION_ALTITUDE = "Altitude";
	
	public static final String MOVEMENT_ALL = "MOVEMENT_ALL";
	public static final String MOVEMENT_STATE = "State";
	public static final String MOVEMENT_SPEED = "Speed";
	public static final String MOVEMENT_BEARING = "Bearing";
	public static final String MOVEMENT_STEP_COUNT = "Steps";
	public static final String MOVEMENT_LAST_STEP = "LastStep";

	public static final String WEATHER_ALL = "WEATHER_ALL";
	public static final String WEATHER_TEMPERATURE = "Temperature";
	public static final String WEATHER_CONDITION = "Condition";
	public static final String WEATHER_HUMIDITY = "Humidity";
	public static final String WEATHER_WIND = "Wind";
	public static final String WEATHER_HAZARD = "HazardLevel";
		
	public static final String SOCIAL_ALL = "SOCIAL_ALL";
	public static final String SOCIAL_CONTACT = "Contact";
	public static final String SOCIAL_COMMUNICATION = "Communication";
	public static final String SOCIAL_MESSAGE = "Message";
	public static final String SOCIAL_LAST_IN = "LastIn";
	public static final String SOCIAL_LAST_OUT = "LastOut";

	public static final String SYSTEM_ALL = "SYSTEM_ALL";
	public static final String SYSTEM_STATE = "State";
	public static final String SYSTEM_BATTERY_LEVEL = "BatteryLevel";
	public static final String SYSTEM_PLUGGED = "SystemPlugged";
	public static final String SYSTEM_LAST_PLUGGED = "LastPlugged";
	public static final String SYSTEM_LAST_PRESENT = "UserLastPresent";
	public static final String SYSTEM_WIFI_SSID = "SSID";
	public static final String SYSTEM_WIFI_SIGNAL = "Signal";
	
	public static final String DERIVED_ALL = "DERIVED_ALL";
	public static final String DERIVED_PLACE = "Place";
	public static final String DERIVED_ACTIVITY = "Activity";
	public static final String DERIVED_SHELTER = "Shelter";
	public static final String DERIVED_POCKET = "Pocket";
	public static final String DERIVED_MOOD = "Mood";
	
	
	// for accuracy checking screen
	public static String LOCATION_ACCURATE = "LOCATION_ACCURATE";
	public static String MOVEMENT_ACCURATE = "MOVEMENT_ACCURATE";
	public static String WEATHER_ACCURATE = "WEATHER_ACCURATE";
	public static String SOCIAL_ACCURATE = "SOCIAL_ACCURATE";
	public static String SYSTEM_ACCURATE = "SYSTEM_ACCURATE";
	public static String DERIVED_ACCURATE = "DERIVED_ACCURATE";
	
	public static String CONTEXT_STORE_INTENT = "edu.fsu.cs.contextprovider.store";

	public static String CONTEXT_RESTART_INTENT = "edu.fsu.cs.contextprovider.restart";

	
	
//	public static final String DERIVED_DENSITY = "Density";
//	public static final String DERIVED_BIOME = "Biome";
//	public static final String PROXIMITY_ALL = "PROXIMITY_ALL";
//	public static final String PROXIMITY_TO = "Proximity to";
//	public static final String PROXIMITY_NEAR = "Location near";
//	public static final String PROXIMITY_FAR = "Location far";

	public static float getMaxSensorRange(Context context, int sensorType, float defaultValue) {
		float max = defaultValue;
		SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(sensorType);

		if (sensors != null && sensors.size() > 0)
			max = sensors.get(0).getMaximumRange();

		return max;
	}

}
