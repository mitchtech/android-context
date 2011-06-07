package edu.fsu.cs.contextprovider.sensor;

import edu.fsu.cs.contextprovider.ContextExpandableListActivity;
import edu.fsu.cs.contextprovider.data.ContextConstants;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class NetworkService extends Service {
	private static final String TAG = "NetworkService";
	private static final String locationType = LocationManager.NETWORK_PROVIDER;
	private static Location currentLocation = new Location(locationType);
	private static LocationManager manager;
	private static LocationListener listener;
	
	private static boolean isreliable = false;
	private static final boolean DEBUG_TTS = false;
	private static final boolean DEBUG = true;
	private static boolean running = false;

	public void onCreate() {
		startService();
	}	
	
	public void startService() {
		
		getPrefs();
		
		listener = new LocationListener() {
			public void onLocationChanged(Location location) {
				isreliable = true;
				currentLocation.set(location);
				if (DEBUG) {
					Log.i(TAG, "New location found: [" + location.getLongitude() + "," + location.getLatitude() + "] | Speed: [" + location.getSpeed() + "]");
				}
			}

			public void onProviderDisabled(String provider) {
				//manager.removeUpdates(this);
				Log.i(TAG, locationType + ": is no longer reliable");
				if (DEBUG_TTS == true) {
					ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
				}
				isreliable = false;
			}

			public void onProviderEnabled(String provider) {
				Log.i(TAG, locationType + ": is reliable");
				if (DEBUG_TTS == true) {
					ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
				}
				isreliable = true;
			}
			
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				switch (status) {
				case LocationProvider.OUT_OF_SERVICE:
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					if (DEBUG_TTS == true) {
						ContextExpandableListActivity.tts.speak("Network reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
					}
					isreliable = false;
					break;
				case LocationProvider.AVAILABLE:
					if (DEBUG_TTS == true) {
						ContextExpandableListActivity.tts.speak("Network reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
					}
					isreliable = true;
					break;
				default:
					if (DEBUG_TTS == true) {
						ContextExpandableListActivity.tts.speak("Network other event detected", TextToSpeech.QUEUE_FLUSH, null);
					}
				}
				// TODO Check the status here to update isreliable
			}
		};
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		currentLocation = manager.getLastKnownLocation(locationType);
		manager.requestLocationUpdates(locationType, 0, 0, listener);
		
		IntentFilter restartFilter = new IntentFilter();
		restartFilter.addAction(ContextConstants.CONTEXT_RESTART_INTENT);
		registerReceiver(restartIntentReceiver, restartFilter);
		running = true;
	}
	
	private void getPrefs() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		// prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// frequency = prefs.getInt(AccelerometerEditActivity.PREF_FREQUENCY,
		// 50);
		// ignoreThreshold = AccelerometerEditActivity.getRate(frequency);

		// prefs.registerOnSharedPreferenceChangeListener(this);
	}

	private void stopService() {
		// PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		Log.i("GPS", "Stopping the service now.");
		manager.removeUpdates(listener);
//		stopSelf();
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
	
	
	
	
	
	public static double getLatitude() {
		return currentLocation.getLatitude();
	}

	public static double getLongitude() {
		return currentLocation.getLongitude();
	}
	
	public static Location getLocation() {
		return currentLocation;
	}

	public static int getAltitude() {
		return (int)currentLocation.getAltitude();
	}
	
	public static boolean isReliable() {
		return isreliable;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "Service has been started.");
//		running = true;
		return 0;
	}

	public void onDestroy()
	{
		stopService();
//		running = false;

	}

}
