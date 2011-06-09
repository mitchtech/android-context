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
	public static final String DERIVED_ONPERSON = "OnPerson";
	public static final String DERIVED_MOOD = "Mood";
		
	// for accuracy checking screen
	public static final String PLACE_ACCURATE = "PLACE_ACCURATE";
	public static final String MOVEMENT_ACCURATE = "MOVEMENT_ACCURATE";
	public static final String ACTIVITY_ACCURATE = "ACTIVITY_ACCURATE";
	public static final String SHELTER_ACCURATE = "SHELTER_ACCURATE";
	public static final String ONPERSON_ACCURATE = "ONPERSON_ACCURATE";
	
	
	// intent IDs
	public static final String CONTEXT_STORE_INTENT = "edu.fsu.cs.contextprovider.store";
	public static final String CONTEXT_RESTART_INTENT = "edu.fsu.cs.contextprovider.restart";
	
	public static final int SET_HOME_REQUEST = 0;
	public static final int SET_WORK_REQUEST = 1;
	public static final String PLACE_REQUEST_ID = "PLACE_REQUEST_ID";
//	public static final String SET_HOME_REQUEST_ID = "SET_HOME_REQUEST";
//	public static final String SET_WORK_REQUEST_ID = "SET_WORK_REQUEST";
	
	// location prefs
	public static final String PREFS_LOCATION_ENABLED = "PREFS_LOCATION_ENABLED";
	public static final String PREFS_LOCATION_PROXIMITY_ENABLED = "PREFS_LOCATION_PROXIMITY_ENABLED";
	public static final String PREFS_LOCATION_POLL_FREQ = "PREFS_LOCATION_POLL_FREQ";
	public static String PREFS_LOCATION_STORE_FREQ = "PREFS_LOCATION_STORE_FREQ";
	// movement prefs
	public static final String PREFS_MOVEMENT_ENABLED = "PREFS_MOVEMENT_ENABLED";
	public static final String PREFS_MOVEMENT_POLL_FREQ = "PREFS_MOVEMENT_POLL_FREQ";
	public static final String PREFS_MOVEMENT_STORE_FREQ = "PREFS_MOVEMENT_STORE_FREQ";	
	public static final String PREFS_ACCEL_POLL_FREQ = "PREFS_ACCEL_POLL_FREQ";
	public static final String PREFS_ACCEL_IGNORE_THRESHOLD = "PREFS_ACCEL_IGNORE_THRESHOLD";
	
	// weather prefs
	public static final String PREFS_WEATHER_ENABLED = "PREFS_WEATHER_ENABLED";
	public static final String PREFS_WEATHER_POLL_FREQ = "PREFS_WEATHER_POLL_FREQ";
	public static final String PREFS_WEATHER_STORE_FREQ = "PREFS_WEATHER_STORE_FREQ";
	// social prefs
	public static final String PREFS_SOCIAL_ENABLED = "PREFS_SOCIAL_ENABLED";
	// system prefs
	public static final String PREFS_SYSTEM_ENABLED = "PREFS_SYSTEM_ENABLED";
	// derived prefs
	public static final String PREFS_DERIVED_ENABLED = "PREFS_DERIVED_ENABLED";
	public static final String PREFS_DERIVED_CALC_FREQ = "PREFS_DERIVED_CALC_FREQ";
	public static final String PREFS_DERIVED_STORE_FREQ = "PREFS_DERIVED_STORE_FREQ";
	// general prefs
	public static final String PREFS_STARTUP_ENABLED = "PREFS_STARTUP_ENABLED";
	public static final String PREFS_ACCURACY_POPUP_ENABLED = "PREFS_ACCURACY_POPUP_ENABLED";
	public static final String PREFS_ACCURACY_POPUP_AUDIO_ENABLED = "PREFS_ACCURACY_POPUP_AUDIO_ENABLED";
	public static final String PREFS_ACCURACY_POPUP_AUDIO = "PREFS_ACCURACY_POPUP_AUDIO";
	public static final String PREFS_ACCURACY_POPUP_VIBRATE_ENABLED = "PREFS_ACCURACY_POPUP_VIBRATE_ENABLED";
	public static final String PREFS_ACCURACY_POPUP_PERIOD = "PREFS_ACCURACY_POPUP_PERIOD";
	public static final String PREFS_ACCURACY_POPUP_DISMISS_DELAY = "PREFS_ACCURACY_POPUP_DISMISS_DELAY";
	// debug
	public static final String PREFS_TTS_ENABLED = "PREFS_TTS_ENABLED";
	public static final String PREFS_SHAKE_ENABLED = "PREFS_SHAKE_ENABLED";
	
	
	
	public static float getMaxSensorRange(Context context, int sensorType, float defaultValue) {
		float max = defaultValue;
		SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(sensorType);

		if (sensors != null && sensors.size() > 0)
			max = sensors.get(0).getMaximumRange();

		return max;
	}

}
