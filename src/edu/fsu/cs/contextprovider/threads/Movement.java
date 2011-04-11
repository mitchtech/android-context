package edu.fsu.cs.contextprovider.threads;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import edu.fsu.cs.contextprovider.services.GPSService;

/**
 * The structure of this class allows for only 1 Movement object to be created.  Because of this the Start() and Stop()
 * will stop the "Global" movement (movementObj).
 * @author meyers
 *
 */
public class Movement extends TimerTask {
	private static String TAG = "Movement";
	private static int latitude;
	private static int longitude;
	private static float speed;
	private static Timer timer = new Timer();
	private static Movement movementObj = new Movement();
	private static boolean running = false;
	
	private static MovementState currentMovementState = null;

	public enum MovementState {
			STILL 					(0, 0),
			WALKING					(1, 2),
			WALKING_ALMOST_RUNNING	(3, 4),
			RUNNING					(5, 10),
			RUNNING_ALMOST_DRIVING	(11, 15),
			DRIVING					(16, 200);
			private final int min;
			private final int max;
			
			MovementState() {
				this.min = -1;
				this.max = -1;
			}
			
			MovementState(int min, int max) {
				this.min = min;
				this.max = max;
			}
			
			public boolean isInside(int val) {
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
	public static void Start(int interval) {
		if (running == true) {
			return;
		}
		timer.schedule(movementObj, 100, interval*1000);
		running = true;
	}
	
	/**
	 * Stop the thread/timer that keeps the movement state up to date
	 */
	public static void Stop() {
		timer.cancel();
		running = false;
	}
	
	public static String getMovement() {
		return currentMovementState.toString();
	}

	@Override
	public void run() {

		if (GPSService.isReliable() == true) {

			latitude = GPSService.getLatitude();
			longitude = GPSService.getLongitude();
			speed = GPSService.getSpeed();
			
			for (MovementState s : MovementState.values()) {
				if (s.isInside((int)speed) == true) {
					currentMovementState = s;
					Log.i(TAG, "Changed movement state to: [" + currentMovementState + "]");
					break;
				}
			}

			Log.i(TAG, "GPS location (" + latitude + ", " + longitude + ") | Speed: [" + speed + "]");

			// Use the accelerometer 
		} else {


		}
	}
}