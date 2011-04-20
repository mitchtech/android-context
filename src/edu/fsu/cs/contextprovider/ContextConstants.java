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
	
	public static final String PREFS_ADDRESS = "PREFS_ADDRESS";
	
	public static final String LOCATION_ALL = "LOCATION_ALL"; 
	public static final String LOCATION_ADDRESS = "Current Address";
	public static final String LOCATION_HOOD = "Current Neighborhood";
	public static final String LOCATION_ZIP = "Current Zip";
	public static final String LOCATION_LATITUDE = "Latitude";
	public static final String LOCATION_LONGITUDE = "Longitude";
	public static final String LOCATION_ALTITUDE = "Altitude";

	public static final String MOVEMENT_ALL = "MOVEMENT_ALL"; 
	public static final String MOVEMENT_STATE = "Movement State";
	public static final String MOVEMENT_SPEED = "Current Speed";
	public static final String MOVEMENT_BEARING = "Bearing";
	public static final String MOVEMENT_STEP_COUNT = "Total Steps";
	public static final String MOVEMENT_LAST_STEP = "Last Step Timestamp";

	public static final String PROXIMITY_ALL = "PROXIMITY_ALL"; 
	public static final String PROXIMITY_TO = "Proximity to";
	public static final String PROXIMITY_NEAR = "Location near";
	public static final String PROXIMITY_FAR = "Location far";

	public static final String WEATHER_ALL = "WEATHER_ALL"; 
	public static final String WEATHER_CUR_TEMP = "Current Temperature";
	public static final String WEATHER_CUR_CONDITION = "Current Conditions";
	public static final String WEATHER_CUR_HUMIDITY = "Humidity";
	public static final String WEATHER_CUR_WIND = "Wind Speed";

	public static final String SYSTEM_ALL = "SYSTEM_ALL"; 
	public static final String SYSTEM_BATTERY_LEVEL = "Battery Level";
	public static final String SYSTEM_BATTERY_LOW = "Battery Low";
	public static final String SYSTEM_PLUGGED = "System Plugged";
	public static final String SYSTEM_LAST_PLUGGED = "Last Plugged";

	public static final String TELEPHONY_ALL = "TELEPHONY_ALL"; 
	public static final String TELEPHONY_PHONE_STATE = "Phone State";
	public static final String TELEPHONY_PHONE_LAST_UPDATE = "Last Call";
	public static final String TELEPHONY_LAST_RECV = "Last Incoming Call";
	public static final String TELEPHONY_LAST_DIAL = "Last Outgoing Call";
	public static final String TELEPHONY_SMS_STATE = "SMS State";
	public static final String TELEPHONY_SMS_LAST_SENDER = "Last SMS Sender";
	public static final String TELEPHONY_SMS_LAST_MESSAGE = "Last SMS Message";
	public static final String TELEPHONY_SMS_LAST_UPDATE = "Last SMS Received";
	
	public static final String DERIVED_HEALTH = "Health";
	public static final String DERIVED_MOOD = "Mood";
	public static final String DERIVED_SHELTER = "Shelter";
	public static final String DERIVED_DENSITY = "Density";
	public static final String DERIVED_BIOME = "Biome";

	
	public static float getMaxSensorRange(Context context, int sensorType, float defaultValue){
		float max = defaultValue; 
		SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(sensorType);
		
		if (sensors != null && sensors.size() > 0)
			max = sensors.get(0).getMaximumRange();
		
		return max;
	}

}
