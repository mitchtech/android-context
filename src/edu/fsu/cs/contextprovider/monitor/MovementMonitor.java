package edu.fsu.cs.contextprovider.monitor;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import edu.fsu.cs.contextprovider.ContextBrowserActivity;
import edu.fsu.cs.contextprovider.sensor.AccelerometerService;
import edu.fsu.cs.contextprovider.sensor.GPSService;

/**
 * The structure of this class allows for only 1 Movement object to be created.  Because of this the Start() and Stop()
 * will stop the "Global" movement (movementObj).
 * @author Meyers
 *
 */
public class MovementMonitor extends TimerTask {
	private static final String TAG = "Movement";
	
	private static final boolean DEBUG_TTS = true;
	private static final float METERS_PER_SECOND_TO_MPH = (float)0.44704;
	
	private static int latitude;
	private static int longitude;
	private static float speed;
	private static Timer timer = new Timer();
	private static MovementMonitor movementObj = new MovementMonitor();
	private static boolean running = false;
	
	private static MovementState currentMovementState = null;
	
	public enum MovementState {
		STILL 					(0, 0, 1001),
		WALKING					((float)0.5, 2, 1000),
		WALKING_ALMOST_RUNNING	(3, 4, 700),
		RUNNING					(5, 10, 500),
		RUNNING_ALMOST_DRIVING	(11, 15, 100),
		DRIVING					(16, 200, 0);
		private final float min;
		private final float max;
		private final long stride_time;

		MovementState(float min, float max, long stride_time) {
			this.min = min*METERS_PER_SECOND_TO_MPH;
			this.max = max*METERS_PER_SECOND_TO_MPH;
			this.stride_time = stride_time;
		}

		public boolean isInside(float val) {
			if (val >= min && val <= max) {
				return true;
			}
			return false;
		}

		public String toString() {
			return this.name();
		}
	}	

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
		timer.schedule(movementObj, 100, interval*1000);
		running = true;
	}

	/**
	 * Stop the thread/timer that keeps the movement state up to date
	 */
	public static void StopThread() {
		Log.i(TAG, "Stop()");
		timer.purge();
		movementObj = new MovementMonitor();
		running = false;
	}

	public static String getMovementState() {
		return currentMovementState.toString();
	}
	
	public static float getSpeedMph() {
		return GPSService.getSpeed()*METERS_PER_SECOND_TO_MPH;
	}

	@Override
	public void run() {

		if (GPSService.isReliable() == true) {
			currentMovementState = determineMovementStateFromGps();
			Log.i(TAG, "GPS determined state: " + currentMovementState);
			if (DEBUG_TTS == true) {
				ContextBrowserActivity.tts.speak("GPS Movement " + currentMovementState, TextToSpeech.QUEUE_FLUSH, null);
			}
			// Use the accelerometer 
		} else {
			currentMovementState = determineMovementStateFromAccelerometer();
			Log.i(TAG, "Accelerometer determined state: " + currentMovementState);
			if (DEBUG_TTS == true) {
				ContextBrowserActivity.tts.speak("Accelerometer Movement " + currentMovementState, TextToSpeech.QUEUE_FLUSH, null);
			}

		}
		

	}

	/**
	 * Pulls the speed from the GPS to determine the users movement state.
	 * If unknown then assume STILL
	 * @return new movement state based on speed
	 */
	private MovementState determineMovementStateFromGps() {
		MovementState newState = MovementState.STILL;
		latitude = GPSService.getLatitude();
		longitude = GPSService.getLongitude();
		speed = GPSService.getSpeed();

		for (MovementState s : MovementState.values()) {
			if (s.isInside(speed) == true) {
				newState = s;
				Log.i(TAG, "Changed movement state to: [" + currentMovementState + "]");
				break;
			}
		}
		Log.i(TAG, "GPS location (" + latitude + ", " + longitude + ") | Speed: [" + speed + "]");
		return newState;
	}

	private MovementState determineMovementStateFromAccelerometer() {
		MovementState newState = MovementState.STILL;
		long currentTime = System.currentTimeMillis();
		long stepTime = AccelerometerService.step_timestamp;
		long sinceStepTime = currentTime - stepTime;
		
		long diffCurrentStrideTime = 0, diffConsiderStrideTime = 0;
		Log.i(TAG, "Time since last step: " + sinceStepTime + " | CurrentTime: " + currentTime + " | StepTime: " + stepTime);

		/* Find the state that has the closest stride time */
		for (MovementState s : MovementState.values()) {
			diffCurrentStrideTime = Math.abs(newState.stride_time - sinceStepTime);
			diffConsiderStrideTime = Math.abs(s.stride_time - sinceStepTime);
			Log.i(TAG, "Considering: [" + diffConsiderStrideTime + "] | Current: [" + diffCurrentStrideTime + "]");
			if (diffConsiderStrideTime < diffCurrentStrideTime) {
				newState = s;
			}
		}
		return newState;
	}
}