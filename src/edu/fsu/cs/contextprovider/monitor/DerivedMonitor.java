package edu.fsu.cs.contextprovider.monitor;

import java.util.Timer;
import java.util.TimerTask;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import android.util.Log;


public class DerivedMonitor extends TimerTask {
	private static final String TAG = "DerivedMonitor";
	private static final boolean DEBUG = true;
	private static final boolean DEBUG_TTS = false;

	private static Timer timer = new Timer();
	private static DerivedMonitor derivedObj = new DerivedMonitor();
	private static boolean running = false;
	
	public static String place = "Place";
	public static String activity = "Activity";
	public static boolean shelter = true;
	public static boolean pocket = true;
	public static String mood = "Mood";
	
	
	/**
	 * Create a timer/thread to continuous run and keep the getMovement() state up to date
	 * 
	 * @param interval rate at which to run the thread, in seconds
	 */
	public static void StartThread(int interval) {
		if (running == true) {
			return;
		}
		Log.i(TAG, "Start()");
		timer.schedule(derivedObj, 100, interval*1000);
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
		return String.valueOf(shelter);
	}
	
	public static boolean getPocket() {
		return pocket;
	}
	
	public static String getPocketString() {
		return String.valueOf(pocket);
	}

	public static String getMood() {
		return mood;
	}

}
