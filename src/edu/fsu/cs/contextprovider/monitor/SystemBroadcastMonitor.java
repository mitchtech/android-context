package edu.fsu.cs.contextprovider.monitor;

import java.util.Set;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

/**
 * A BroadcastReceiver that listens for relevantedu.fsu.cs.contextprovider. system updates and saves them
 * to the content provider.
 */
public class SystemBroadcastMonitor extends BroadcastReceiver {

	private static final String TAG = "SystemBroadcastMonitor";
	
	// direct user/device interaction
	private static String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
	private static String SCREEN_ON = "android.intent.action.SCREEN_ON";
	private static String USER_PRESENT = "android.intent.action.USER_PRESENT";
	private static String HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
	private static String DOCK_EVENT = "android.intent.action.DOCK_EVENT";
	private static String MEDIA_BUTTON = "android.intent.action.MEDIA_BUTTON";
	private static String CAMERA_BUTTON = "android.intent.action.CAMERA_BUTTON";
	private static String INPUT_METHOD_CHANGED = "android.intent.action.INPUT_METHOD_CHANGED";
	private static String AIRPLANE_MODE = "android.intent.action.AIRPLANE_MODE";
	private static String UID_REMOVED = "android.intent.action.UID_REMOVED";
	private static String WALLPAPER_CHANGED = "android.intent.action.WALLPAPER_CHANGED";
	private static String CONFIGURATION_CHANGED = "android.intent.action.CONFIGURATION_CHANGED";
	private static String LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED";
	private static String ALARM_CHANGED = "android.intent.action.ALARM_CHANGED";
	private static long USER_LAST_PRESENT = 0;
	
	
	
	// date/time intents
	private static String TIME_TICK = "android.intent.action.TIME_TICK";
	private static String TIME_SET = "android.intent.action.TIME_SET";
	private static String DATE_CHANGED = "android.intent.action.DATE_CHANGED";
	private static String TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED";

	
	// power intents/connections
	private static String BATTERY_CHANGED = "android.intent.action.BATTERY_CHANGED";
	private static String BATTERY_LOW = "android.intent.action.BATTERY_LOW";
	private static String BATTERY_OKAY = "android.intent.action.BATTERY_OKAY";
	private static String ACTION_POWER_CONNECTED = "android.intent.action.ACTION_POWER_CONNECTED";
	private static String ACTION_POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";
	private static String UMS_CONNECTED = "android.intent.action.UMS_CONNECTED";
	private static String UMS_DISCONNECTED = "android.intent.action.UMS_DISCONNECTED";
	private static boolean BATTERY_PLUGGED = false;
	private static boolean BATTERY_LEVEL_LOW = false;
	private static int BATTERY_LEVEL = 0;
	private static long BATTERY_LAST_PLUGGED = 0;
	
	
	// services
	private static String SYNC_STATE_CHANGED = "android.intent.action.SYNC_STATE_CHANGED";
	private static String GTALK_CONNECTED = "android.intent.action.GTALK_CONNECTED";
	private static String GTALK_DISCONNECTED = "android.intent.action.GTALK_DISCONNECTED";
	private static String PROVIDER_CHANGED = "android.intent.action.PROVIDER_CHANGED";
	private static String NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
	
	
	// actual system intents
	private static String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
	private static String CLOSE_SYSTEM_DIALOGS = "android.intent.action.CLOSE_SYSTEM_DIALOGS";
	private static String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
	private static String ACTION_REQUEST_SHUTDOWN = "android.intent.action.ACTION_REQUEST_SHUTDOWN";
	private static String REBOOT = "android.intent.action.REBOOT";
	private static String REMOTE_INTENT = "android.intent.action.REMOTE_INTENT";
	private static String PRE_BOOT_COMPLETED = "android.intent.action.PRE_BOOT_COMPLETED";
	
	// application intents
	private static String PACKAGE_INSTALL = "android.intent.action.PACKAGE_INSTALL";
	private static String PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
	private static String PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED";
	private static String PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
	private static String PACKAGE_CHANGED = "android.intent.action.PACKAGE_CHANGED";
	private static String PACKAGE_RESTARTED = "android.intent.action.PACKAGE_RESTARTED";
	private static String PACKAGE_DATA_CLEARED = "android.intent.action.PACKAGE_DATA_CLEARED";
	
	// storage intents
	private static String DEVICE_STORAGE_LOW = "android.intent.action.DEVICE_STORAGE_LOW";
	private static String DEVICE_STORAGE_OK = "android.intent.action.DEVICE_STORAGE_OK";
	private static String MANAGE_PACKAGE_STORAGE = "android.intent.action.MANAGE_PACKAGE_STORAGE";
	private static String MEDIA_REMOVED = "android.intent.action.MEDIA_REMOVED";
	private static String MEDIA_UNMOUNTED = "android.intent.action.MEDIA_UNMOUNTED";
	private static String MEDIA_CHECKING = "android.intent.action.MEDIA_CHECKING";
	private static String MEDIA_NOFS = "android.intent.action.MEDIA_NOFS";
	private static String MEDIA_MOUNTED = "android.intent.action.MEDIA_MOUNTED";
	private static String MEDIA_SHARED = "android.intent.action.MEDIA_SHARED";
	private static String MEDIA_BAD_REMOVAL = "android.intent.action.MEDIA_BAD_REMOVAL";
	private static String MEDIA_UNMOUNTABLE = "android.intent.action.MEDIA_UNMOUNTABLE";
	private static String MEDIA_EJECT = "android.intent.action.MEDIA_EJECT";
	private static String MEDIA_SCANNER_STARTED = "android.intent.action.MEDIA_SCANNER_STARTED";
	private static String MEDIA_SCANNER_FINISHED = "android.intent.action.MEDIA_SCANNER_FINISHED";
	private static String MEDIA_SCANNER_SCAN_FILE = "android.intent.action.MEDIA_SCANNER_SCAN_FILE";
	
	// non-system system intents
	private static String WIFI_STATE_CHANGED = "android.net.wifi.WIFI_STATE_CHANGED";
	private static String BACKGROUND_DATA_SETTING_CHANGED = "android.net.conn.BACKGROUND_DATA_SETTING_CHANGED";
	private static String BT_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
	private static String RINGER_MODE_CHANGED = "android.media.RINGER_MODE_CHANGED";
	private static String SYNC_CONN_STATUS_CHANGED = "com.android.sync.SYNC_CONN_STATUS_CHANGED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "B/C intent=" + intent);
//		fields.put(BroadcastSchema.BroadcastTable.TITLE, intent.getAction());
//		fields.put(BroadcastSchema.BroadcastTable.EXTRAS, extrasToString(extras));
//		fields.put(BroadcastSchema.BroadcastTable.TIME,	System.currentTimeMillis());
		
		String intentName = intent.getAction();
		Bundle extras = intent.getExtras();
		
		
		if (intentName == USER_PRESENT || intentName == SCREEN_ON || intentName == SCREEN_OFF)
		{
			USER_LAST_PRESENT = System.currentTimeMillis();
		}
		
		
		
		else if (intentName == ACTION_POWER_CONNECTED || intentName == UMS_CONNECTED)
		{
			BATTERY_PLUGGED = true;
		}
		else if (intentName == ACTION_POWER_DISCONNECTED || intentName == UMS_DISCONNECTED)
		{
			BATTERY_PLUGGED = false;
			BATTERY_LAST_PLUGGED = System.currentTimeMillis();
		}
		else if (intentName == BATTERY_CHANGED)
		{
			BATTERY_LEVEL = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		}
		else if (intentName == BATTERY_LOW)
		{
			BATTERY_LEVEL_LOW = true;
		}
		else if (intentName == BATTERY_OKAY)
		{
			BATTERY_LEVEL_LOW = false;
		}

	}

	
	
	
	
	
	private static final String extrasToString(Bundle extras) {
		if (extras == null)
			return "";

		String val = "";
		Set<String> keys = extras.keySet();
		for (String key : keys)
			val += key + "=" + extras.get(key);

		return val;
	}


	

}
