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
	
	
	public static final String DERIVED_ALL = "DERIVED_ALL";
	public static final String DERIVED_HEALTH = "Health";
	public static final String DERIVED_MOOD = "Mood";
	public static final String DERIVED_SHELTER = "Shelter";
	public static final String DERIVED_POCKET = "Pocket";

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
		// TODO Auto-generated method stub
		return null;
	}

	public static String getActivity() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getShelter() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getPocket() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getMood() {
		// TODO Auto-generated method stub
		return null;
	}

}
