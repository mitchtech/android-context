package edu.fsu.cs.contextprovider.sensor;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import edu.fsu.cs.contextprovider.ContextExpandableListActivity;

/**
 * Most objects are declared static because we want only 1 instance.  We could have instead used an iBinder but it is a bit
 * of overkill for this situation.
 * @author meyers
 *
 */
public class GPSService extends Service
{
	private static final String TAG = "GPSService";
	private static final boolean DEBUG_TTS = false;
	private static final boolean DEBUG = true;
	private static final String locationType = LocationManager.GPS_PROVIDER;
	private static LocationManager manager;
	private static LocationListener listener;
	@SuppressWarnings("unused")
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
		listener = new LocationListener() {
			public void onLocationChanged(Location location) {
				isreliable = true;
				currentLocation.set(location);
				
				Log.i(TAG, "New location found: [" + location.getLongitude() + "," + location.getLatitude() + "] | Speed: [" + location.getSpeed() + "]");
			}

			public void onProviderDisabled(String provider) {
				//manager.removeUpdates(this);
				if (DEBUG == true) {
					Log.i(TAG, locationType + ": is no longer reliable");
				}
				isreliable = false;
				if (DEBUG_TTS == true) {
					ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
				}
			}

			public void onProviderEnabled(String provider) {
				Log.i(TAG, locationType + ": is reliable");
				//isreliable = true;
				if (DEBUG_TTS == true) {
					ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
				}
			}
			
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				switch (status) {
				case LocationProvider.OUT_OF_SERVICE:
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					isreliable = false;
					if (DEBUG_TTS == true) {
						ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
					}
					if (DEBUG == true) {
						Log.i(TAG, "GPS Temporarily unavailable");
					}
					break;
				case LocationProvider.AVAILABLE:
					isreliable = true;
					if (DEBUG_TTS == true) {
						ContextExpandableListActivity.tts.speak("GPS reliability is " + String.valueOf(isreliable), TextToSpeech.QUEUE_FLUSH, null);
					}
					if (DEBUG == true) {
						Log.i(TAG, "GPS Available");
					}
					break;
				default:
					if (DEBUG_TTS == true) {
						ContextExpandableListActivity.tts.speak("Other GPS event detected", TextToSpeech.QUEUE_FLUSH, null);
					}
					if (DEBUG == true) {
						Log.i(TAG, "GPS State unkown");
					}
				}
				// TODO Check the status here to update isreliable
			}
		};
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		currentLocation = manager.getLastKnownLocation(locationType);
		manager.requestLocationUpdates(locationType, 0, 0, listener);


	}

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "Service has been started.");
		running = true;
		return 0;
	}

	public void onDestroy()
	{
		running = false;
		Log.i("GPS", "Stopping the service now.");
		manager.removeUpdates(listener);
		stopSelf();
	}
	

}
