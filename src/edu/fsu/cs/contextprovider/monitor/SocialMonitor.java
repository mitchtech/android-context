package edu.fsu.cs.contextprovider.monitor;

import java.util.Timer;
import java.util.TimerTask;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import android.util.Log;


public class SocialMonitor extends TimerTask {
	private static final String TAG = "SocialMonitor";
	private static final boolean DEBUG = true;
	private static final boolean DEBUG_TTS = false;

	private static Timer timer = new Timer();
	private static SocialMonitor twitterObj = new SocialMonitor();
	private static boolean running = false;
	private static Twitter twitter = null;
	private static String currentTwitterStatus = new String();

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
		timer.schedule(twitterObj, 100, interval*1000);
		running = true;
		if (twitter == null) {
			twitter = new Twitter("crm04d@fsu.edu","android");
		}
	}

	/**
	 * Stop the thread/timer that keeps the movement state up to date
	 */
	public static void StopThread() {
		Log.i(TAG, "Stop()");
		timer.purge();
		twitterObj = new SocialMonitor();
		running = false;
	}

	@Override
	public void run() {
		String tmp = null;
		if (twitter != null) {
			try { 
				tmp = twitter.getStatus("crm04d").toString();
			} catch(TwitterException x) { 
				Log.e(TAG, "Twitter failed [" + x + "]");
			}
			if (tmp != null) {
				setCurrentTwitterStatus(tmp);
			}
			if (DEBUG == true) Log.i(TAG, "Twitter Status: " + tmp);
		} else {
			if (DEBUG == true) Log.i(TAG, "Twitter is null");
		}
	}


	public static void setCurrentTwitterStatus(String currentTwitterStatus) {
		SocialMonitor.currentTwitterStatus = currentTwitterStatus;
	}


	public static String getCurrentTwitterStatus() {
		return currentTwitterStatus;
	}
}
