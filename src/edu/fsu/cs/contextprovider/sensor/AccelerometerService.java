package edu.fsu.cs.contextprovider.sensor;

import java.util.List;
import java.util.Stack;

import edu.fsu.cs.contextprovider.ContextExpandableListActivity;
import edu.fsu.cs.contextprovider.PrefsActivity;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class AccelerometerService extends Service implements SensorEventListener, OnSharedPreferenceChangeListener {

	private static final String TAG = "AccelerometerService";
	private static boolean serviceEnabled = false;
	private static final boolean DEBUG = true;
	private SensorManager sm;
	private Sensor accelerometerSensor;

	private int ignoreThreshold = 0;

	int ACCELEROMETER_POLL_FREQUENCY;
	int ACCELEROMETER_IGNORE_THRESHOLD;

	private int ignoreCounter = 0;

	public final static int READINGS_REMEMBER_MAX = 100;

	/*
	 * TODO: Make this a circular buffer or a double ended queue It seems that
	 * deque (double ended queue) is only in the newest (3.0) android sdk
	 */
	public static Stack<Float> xHistory = new Stack<Float>();
	public static Stack<Float> yHistory = new Stack<Float>();
	public static Stack<Float> zHistory = new Stack<Float>();
	public static float x = 0, y = 0, z = 0;
	public static float lastX = 0, lastY = 0, lastZ = 0;
	private static long step_count = 0;
	private static long step_timestamp = 0;
	private static int shakeCount = 0;

	/* Accelerometer -> walking calculation variables */
	private static float mLimit = 10;
	private static float mLastValues[] = new float[3 * 2];
	private static float mScale[] = new float[2];
	private static float mYOffset;

	private static float mLastDirections[] = new float[3 * 2];
	private static float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private static float mLastDiff[] = new float[3 * 2];
	private static int mLastMatch = -1;

	// preferences
	SharedPreferences prefs;
	private boolean shakeEnabled;

	public AccelerometerService() {
		xHistory.ensureCapacity(READINGS_REMEMBER_MAX);
		yHistory.ensureCapacity(READINGS_REMEMBER_MAX);
		zHistory.ensureCapacity(READINGS_REMEMBER_MAX);

		int h = 480; // TODO: remove this constant
		mYOffset = h * 0.5f;
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
	}

	static public long getStepCount() {
		return step_count;
	}

	static public long getStepTimestamp() {
		return step_timestamp;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "init() called for AccelerometerService");
		if (!serviceEnabled) {

			prefs = PreferenceManager.getDefaultSharedPreferences(this);
			// frequency =prefs.getInt(AccelerometerEditActivity.PREF_FREQUENCY,
			// 50);
			// pluginId = prefs.getInt(AccelerometerEditActivity.KEY_PLUGIN_ID,
			// -1);
			// ignoreThreshold = AccelerometerEditActivity.getRate(frequency);

			prefs.registerOnSharedPreferenceChangeListener(this);

			// make sure not to call it twice
			sm = (SensorManager) getSystemService(SENSOR_SERVICE);
			List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
			if (sensors != null && sensors.size() > 0) {
				accelerometerSensor = sensors.get(0);
				sm.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
				serviceEnabled = true;
			} else {
				Toast.makeText(this, "Accelerometer sensor is not available on this device!", Toast.LENGTH_SHORT).show();
			}
		}
	}

//	private void getPrefs() {
//		shakeEnabled = PrefsActivity.getShakeEnabled(getApplicationContext());
//	}

	@Override
	public void onDestroy() {
		if (serviceEnabled) {
			sm.unregisterListener(this);
			PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		}
		super.onDestroy();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// we don't need this
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;

		if (sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
			if (DEBUG) {
				Log.i(TAG, "Why exactly is the sensor type not TYPE_ACCELEROMETER?!?");
			}
			return;
		}

		if (ignoreCounter >= ignoreThreshold) {
			ignoreCounter = 0;
		} else {
			ignoreCounter++;
			return;
		}

		/*
		 * TODO: Replace synchronized(): Object level lock, make granularity
		 * finer with an explicitly lock
		 */
		synchronized (this) {
			// if (xHistory.size() == READINGS_REMEMBER_MAX) {
			// xHistory.remove(xHistory.size()-1);
			// yHistory.remove(yHistory.size()-1);
			// zHistory.remove(zHistory.size()-1);
			// }
			//
			// xHistory.push(event.values[0]);
			// yHistory.push(event.values[1]);
			// zHistory.push(event.values[2]);
			lastX = x;
			lastY = y;
			lastZ = z;

			x = event.values[0];
			y = event.values[0];
			z = event.values[0];
			//
			// if (DEBUG) {
			// Log.i(TAG, "New: [" + event.values[0] + "," + event.values[1] +
			// "," + event.values[2] + "]");
			// String history = new String();
			// for (int i=0; i < xHistory.size(); ++i) {
			// Float a = xHistory.get(i);
			// Float b = yHistory.get(i);
			// Float c = zHistory.get(i);
			//
			// history += "[" + a + "," + b + "," + c + "]" + ", ";
			// }
			//
			// Log.i(TAG, "Old: " + history);
			// }
		}

		/* Check if a step was taken */
		boolean stepTaken = isStepTaken();
		if (stepTaken == true) {
			step_count++;
			step_timestamp = System.currentTimeMillis();
			Log.i(TAG, "Step Taken: [" + step_count + "] | At: [" + step_timestamp + "]");
		}

		if (shakeEnabled) {

			if (ContextExpandableListActivity.running == false) {
				boolean isShaken = isShakeEnough(x, y, z);
				if (isShaken == true) {
					if (DEBUG == true) {
						Log.i(TAG, "Shake detected, going to start activity");
					}
					Intent intent = new Intent(this, edu.fsu.cs.contextprovider.ContextExpandableListActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		}

	}

	private boolean isStepTaken() {
		boolean stepTaken = false;
		float vSum = 0;
		float v;
		int k = 0;

		vSum += mYOffset + x * mScale[1];
		vSum += mYOffset + y * mScale[1];
		vSum += mYOffset + z * mScale[1];

		v = vSum / 3;

		float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
		if (direction == -mLastDirections[k]) {
			// Direction changed
			int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
			mLastExtremes[extType][k] = mLastValues[k];
			float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

			if (diff > mLimit) {

				boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
				boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
				boolean isNotContra = (mLastMatch != 1 - extType);

				if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
					Log.i(TAG, "step");
					stepTaken = true;
					mLastMatch = extType;
				} else {
					mLastMatch = -1;
				}
			}
			mLastDiff[k] = diff;
		}
		mLastDirections[k] = direction;
		mLastValues[k] = v;
		return stepTaken;
	}

	private boolean isShakeEnough(float x, float y, float z) {
		double force = 0.0d;
		force += Math.pow((x - lastX) / SensorManager.GRAVITY_EARTH, 2.0);
		force += Math.pow((y - lastY) / SensorManager.GRAVITY_EARTH, 2.0);
		force += Math.pow((z - lastZ) / SensorManager.GRAVITY_EARTH, 2.0);
		force = Math.sqrt(force);

		lastX = x;
		lastY = y;
		lastZ = z;

		Log.i(TAG, "Force detected [" + force + "]");
		if (force > Defaults.THRESHOLD) {
			Log.i(TAG, "Shake detected but we haven't reached our limit yet");
			shakeCount++;
			if (shakeCount > Defaults.SHAKE_COUNT) {
				shakeCount = 0;
				lastX = 0;
				lastY = 0;
				lastZ = 0;
				return true;
			}
		}
		return false;
	}

	private static class Defaults {
		public static final float THRESHOLD = (float) 0.275;
		public static final int SHAKE_COUNT = 4;
	}

	public static void setSensitivity(float sensitivity) {
		mLimit = sensitivity; // 1.97 2.96 4.44 6.66 10.00 15.00 22.50 33.75
								// 50.62
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		// if (AccelerometerEditActivity.PREF_FREQUENCY.equals(key)) {
		// ignoreThreshold = AccelerometerEditActivity.getRate(prefs.getInt(key,
		// 50));
		// }
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
