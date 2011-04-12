package edu.fsu.cs.contextprovider.services;


import java.util.Stack;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class AccelService extends Service implements SensorEventListener
{
	private final static String TAG = "AccelService";
	public final static int READINGS_REMEMBER_MAX = 100;

	/* TODO: Make this a circular buffer or a double ended queue
	 * It seems that deque (double ended queue) is only in the newest (3.0) android sdk
	 */
	public static Stack<Float> xHistory = new Stack<Float>();
	public static Stack<Float> yHistory = new Stack<Float>();
	public static Stack<Float> zHistory = new Stack<Float>();
	public static float x = 0, y = 0, z = 0;
	public static long step_count = 0;
	public static long step_timestamp = 0;

	/* Accelerometer -> walking calculation variables */
	private static float   mLimit = 10;
	private static float   mLastValues[] = new float[3*2];
	private static float   mScale[] = new float[2];
	private static float   mYOffset;

	private static float   mLastDirections[] = new float[3*2];
	private static float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
	private static float   mLastDiff[] = new float[3*2];
	private static int     mLastMatch = -1;

	public AccelService() {
		xHistory.ensureCapacity(READINGS_REMEMBER_MAX);
		yHistory.ensureCapacity(READINGS_REMEMBER_MAX);
		zHistory.ensureCapacity(READINGS_REMEMBER_MAX);

		int h = 480; // TODO: remove this constant
		mYOffset = h * 0.5f;
		mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
		mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
		
	}

	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor; 
		/* TODO: Replace synchronized(): Object level lock, make granularity finer with an explicity lock */
		synchronized (this) {
			if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
				return;
			}
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				if (xHistory.size() == READINGS_REMEMBER_MAX) {
					xHistory.remove(xHistory.size()-1);
					yHistory.remove(yHistory.size()-1);
					zHistory.remove(zHistory.size()-1);
				}

				xHistory.push(event.values[0]);
				yHistory.push(event.values[1]);
				zHistory.push(event.values[2]);
				x = event.values[0];
				y = event.values[0];
				z = event.values[0];

				Log.i(TAG, "New: [" + event.values[0] + "," + event.values[1] + "," + event.values[2] + "]");
				String history = new String();
				for (int i=0; i < xHistory.size(); ++i) {
					Float a = xHistory.get(i);
					Float b = yHistory.get(i);
					Float c = zHistory.get(i);

					history += "[" + a + "," + b + "," + c + "]" + ", ";            		
				}
				Log.i(TAG, "Old: " + history);
			}
		}
		
		/* Check if a step was taken */
		boolean stepTaken = isStepTaken();
		if (stepTaken == true) {
			step_count++;
			step_timestamp = System.currentTimeMillis();
			Log.i(TAG, "Step Taken: [" + step_count + "] | At: [" + step_timestamp + "]");
		}
		Log.i(TAG, "Sensor changed");
	}

	private boolean isStepTaken() {
		boolean stepTaken = false;
		float vSum = 0;
		float v;
		int k = 0;

		vSum += mYOffset + AccelService.x * mScale[1];
		vSum += mYOffset + AccelService.y * mScale[1];
		vSum += mYOffset + AccelService.z * mScale[1];

		v = vSum / 3;

		float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
		if (direction == - mLastDirections[k]) {
			// Direction changed
			int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
			mLastExtremes[extType][k] = mLastValues[k];
			float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

			if (diff > mLimit) {

				boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k]*2/3);
				boolean isPreviousLargeEnough = mLastDiff[k] > (diff/3);
				boolean isNotContra = (mLastMatch != 1 - extType);

				if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
					Log.i(TAG, "step");
					stepTaken = true;
					mLastMatch = extType;
				}
				else {
					mLastMatch = -1;
				}
			}
			mLastDiff[k] = diff;
		}
		mLastDirections[k] = direction;
		mLastValues[k] = v;
		return stepTaken;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void setSensitivity(float sensitivity) {
		mLimit = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
	}
}