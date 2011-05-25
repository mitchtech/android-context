package edu.fsu.cs.contextprovider;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class ContextConstants {

	public static final String PREFS_ADDRESS = "PREFS_ADDRESS";

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
	public static final String WEATHER_CUR_TEMP = "Temperature";
	public static final String WEATHER_CUR_CONDITION = "Condition";
	public static final String WEATHER_CUR_HUMIDITY = "Humidity";
	public static final String WEATHER_CUR_WIND = "Wind";
	
	public static final String PROXIMITY_ALL = "PROXIMITY_ALL";
	public static final String PROXIMITY_TO = "Proximity to";
	public static final String PROXIMITY_NEAR = "Location near";
	public static final String PROXIMITY_FAR = "Location far";
	
	public static final String SOCIAL_ALL = "SOCIAL_ALL";
	public static final String SOCIAL_TWITTER_LAST_MESSAGE = "Last Tweet Message";
	public static final String SOCIAL_TWITTER_LAST_TIME = "Last Tweet Time";
	
	public static final String TELEPHONY_ALL = "TELEPHONY_ALL";
	public static final String TELEPHONY_PHONE_STATE = "Phone State";
	public static final String TELEPHONY_PHONE_LAST_UPDATE = "Last Call";
	public static final String TELEPHONY_LAST_RECV = "Last Incoming Call";
	public static final String TELEPHONY_LAST_DIAL = "Last Outgoing Call";
	public static final String TELEPHONY_SMS_STATE = "SMS State";
	public static final String TELEPHONY_SMS_LAST_SENDER = "Last SMS Sender";
	public static final String TELEPHONY_SMS_LAST_MESSAGE = "Last SMS Message";
	public static final String TELEPHONY_SMS_LAST_UPDATE = "Last SMS Received";

	public static final String SYSTEM_ALL = "SYSTEM_ALL";
	public static final String SYSTEM_BATTERY_LEVEL = "Battery Level";
	public static final String SYSTEM_BATTERY_LOW = "Battery Low";
	public static final String SYSTEM_PLUGGED = "System Plugged";
	public static final String SYSTEM_LAST_PLUGGED = "Last Plugged";
	public static final String SYSTEM_USER_LAST_PRESENT = "User Last Present";
	
	public static final String DERIVED_ALL = "DERIVED_ALL";
	public static final String DERIVED_HEALTH = "Health";
	public static final String DERIVED_MOOD = "Mood";
	public static final String DERIVED_SHELTER = "Shelter";
	public static final String DERIVED_POCKET = "Pocket";
	public static final String DERIVED_DENSITY = "Density";
	public static final String DERIVED_BIOME = "Biome";



	public static float getMaxSensorRange(Context context, int sensorType, float defaultValue) {
		float max = defaultValue;
		SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(sensorType);

		if (sensors != null && sensors.size() > 0)
			max = sensors.get(0).getMaximumRange();

		return max;
	}

}
