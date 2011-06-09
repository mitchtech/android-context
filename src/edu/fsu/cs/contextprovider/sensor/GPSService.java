package edu.fsu.cs.contextprovider.sensor;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;
import edu.fsu.cs.contextprovider.ContextExpandableListActivity;
import edu.fsu.cs.contextprovider.data.ContextConstants;

/**
 * Most objects are declared static because we want only 1 instance.  We could have instead used an iBinder but it is a bit
 * of overkill for this situation.
 * @author meyers
 *
 */
public class GPSService extends Service implements OnSharedPreferenceChangeListener
{
	private static final String TAG = "GPSService";
	private static final boolean DEBUG_TTS = false;
	private static final boolean DEBUG = false;
	
	private SharedPreferences prefs;
	
	private static final String locationType = LocationManager.GPS_PROVIDER;
	private static LocationManager manager;
	private static LocationListener listener;

	private static boolean running = false;
	private static boolean isreliable = false;
	private static Location currentLocation = new Location(locationType);
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/** method for clients */
	public static double getLatitude() {
		return currentLocation.getLatitude();
	}

	public static double getLongitude() {
		return currentLocation.getLongitude();
	}

	public static float getSpeed() {
		if (currentLocation == null) {
			return 0;
		}
		return currentLocation.getSpeed();
	}

	
	public static double getAltitude() {
		return currentLocation.getAltitude();
	}
	
	/* TODO: Maybe convert this to N, S, E, or W */
	public static float getBearing() {
		return currentLocation.getBearing();
	}
	
	public static Location getLocation() {
		return currentLocation;
	}
	
	/**
	 * Often times the GPS goes in and out of service.  We perform some logic to determine
	 * if the current GPS reading is reliable or not.
	 * 
	 * @return
	 */
	public static boolean isReliable() {
		return isreliable;
	}
	
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
				if (DEBUG) {
					Log.i(TAG, locationType + ": is no longer reliable");
				}
				isreliable = false;
				if (DEBUG_TTS) {
					ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
				}
			}

			public void onProviderEnabled(String provider) {
				Log.i(TAG, locationType + ": is reliable");
				//isreliable = true;
				if (DEBUG_TTS) {
					ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
				}
			}
			
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				switch (status) {
				case LocationProvider.OUT_OF_SERVICE:
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					isreliable = false;
					if (DEBUG_TTS) {
						ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
					}
					if (DEBUG) {
						Log.i(TAG, "GPS Temporarily unavailable");
					}
					break;
				case LocationProvider.AVAILABLE:
					isreliable = true;
					if (DEBUG_TTS) {
						ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
					}
					if (DEBUG) {
						Log.i(TAG, "GPS Available");
					}
					break;
				default:
					if (DEBUG_TTS) {
						ContextExpandableListActivity.tts.speak("Other GPS event detected", TextToSpeech.QUEUE_FLUSH, null);
					}
					if (DEBUG) {
						Log.i(TAG, "GPS State unkown");
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
		prefs = getSharedPreferences(ContextConstants.CONTEXT_PREFS, MODE_WORLD_READABLE);
//		accelPoll = prefs.getInt(ContextConstants.PREFS_ACCEL_POLL_FREQ, 1);
//		ignoreThreshold = prefs.getInt(ContextConstants.PREFS_ACCEL_IGNORE_THRESHOLD, 0);
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	private void stopService() {
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
	

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (DEBUG) {
			Log.i(TAG, "Service has been started");
		}
		return 0;
	}

	public void onDestroy()
	{
		stopService();
		if (DEBUG) {
			Log.i("GPS", "Stopping the service");
		}
		manager.removeUpdates(listener);
		stopSelf();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//		if (key.equals(ContextConstants.PREFS_ACCEL_POLL_FREQ)) {
//			Toast.makeText(this, "PREFS_ACCEL_POLL_FREQ", Toast.LENGTH_SHORT).show();
//			getPrefs();
//			stopService();
//			startService();
//		} else if (key.equals(ContextConstants.PREFS_ACCEL_IGNORE_THRESHOLD)) {
//			Toast.makeText(this, "PREFS_ACCEL_IGNORE_THRESHOLD", Toast.LENGTH_SHORT).show();
//			getPrefs();
//		}
	}
	

}
