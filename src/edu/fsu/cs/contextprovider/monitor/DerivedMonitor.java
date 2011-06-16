package edu.fsu.cs.contextprovider.monitor;

import java.util.Timer;
import java.util.TimerTask;

import edu.fsu.cs.contextprovider.map.FloatingPointGeoPoint;
import edu.fsu.cs.contextprovider.map.Place;
import edu.fsu.cs.contextprovider.sensor.AccelerometerService;
import edu.fsu.cs.contextprovider.sensor.GPSService;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import android.util.Log;

public class DerivedMonitor extends TimerTask {
	private static final String TAG = "DerivedMonitor";
	private static final boolean DEBUG = true;
	private static final boolean DEBUG_TTS = false;
	private static final long ON_PERSON_STILL_THRESHOLD = 3600000; // 1 hour = 1000*60*60

	private static Timer timer = new Timer();
	private static DerivedMonitor derivedObj = new DerivedMonitor();
	private static boolean running = false;

	public static String place = "Place";
	public static String activity = "Activity";

	public static boolean shelter = true;
	public static boolean onPerson = true;

	public static String mood = "Mood";

	public static FloatingPointGeoPoint Home = null;
	public static FloatingPointGeoPoint Work = null;

	public static double homeDistance = 0;
	public static double workDistance = 0;

	public static double atThreshold = 50;
	public static double nearbyThreshold = 500;
	public static double farThreshold = 10000;

	/**
	 * Create a timer/thread to continuous run and keep the getMovement() state
	 * up to date
	 * 
	 * @param interval
	 *            rate at which to run the thread, in seconds
	 */
	public static void StartThread(int interval) {
		if (running == true) {
			return;
		}
		Log.i(TAG, "Start()");
		timer.schedule(derivedObj, 100, interval * 1000);
		running = true;
	}

	/**
	 * Stop the thread/timer that keeps the movement state up to date
	 */
	public static void StopThread() {
		Log.i(TAG, "Stop()");
		timer.purge();
		derivedObj = new DerivedMonitor();
		running = false;
	}

	@Override
	public void run() {
		calcPlace();
		calcShelter();
		calcOnPerson();
	}

	public void calcPlace() {
		if (Home != null && Work != null) {

			homeDistance = LocationMonitor.proximityTo(Home.getLongitude(), Home.getLatitude());
			workDistance = LocationMonitor.proximityTo(Work.getLongitude(), Work.getLatitude());

			if (homeDistance < atThreshold) {
				place = "AT HOME";
			} else if (workDistance < atThreshold) {
				place = "AT WORK";
			} else if (homeDistance < nearbyThreshold) {
				place = "NEAR HOME";
			} else if (workDistance < nearbyThreshold) {
				place = "NEAR WORK";
			} else if (homeDistance < farThreshold) {
				place = "FAR FROM HOME";
			} else if (workDistance < farThreshold) {
				place = "FAR FROM WORK";
			}
		}
	}

	public void calcShelter() {
		if (SystemMonitor.isBatteryPlugged()) {
			shelter = true;
		} else if (LocationMonitor.isInside() == true) {
			shelter = true;
		} else {
			shelter = false;
		}
	}

	public void calcOnPerson() {
		if (SystemMonitor.isBatteryPlugged()) {
			onPerson = false;
		} else if (AccelerometerService.getStepTimestamp() - System.currentTimeMillis() > ON_PERSON_STILL_THRESHOLD) {
			onPerson = false;
		} else {
			onPerson = true;
		}
	}

	public static String getPlace() {
		return place;
	}

	public static String getActivity() {
		return activity;
	}

	public static boolean getShelter() {
		return shelter;
	}

	public static String getShelterString() {
		if (shelter)
			return "Indoors";
		else
			return "Outdoors";
	}

	public static boolean getonPerson() {
		return onPerson;
	}

	public static String getOnPersonString() {
		if (onPerson)
			return "Device On Person";
		else
			return "Device NOT On Person";
	}

	public static String getMood() {
		return mood;
	}

}
