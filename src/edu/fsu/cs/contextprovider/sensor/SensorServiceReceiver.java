package edu.fsu.cs.contextprovider.sensor;
import edu.fsu.cs.contextprovider.ContextIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SensorServiceReceiver extends BroadcastReceiver {
	
	public static final String ACTION_DISABLE_ALL = "edu.fsu.cs.contextprovider.DISABLE_ALL";
	private static final String TAG = "SensorsReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) return;
		String action = intent.getAction();
		if (action == null) return;
		
		Intent i;
		
		String className = intent.getStringExtra(ContextIntent.EXTRA_PLUGIN_SERVICE_CLASS_NAME);
		if (className == null) {
			if (ContextIntent.ACTION_DISABLE.equals(action)){
				// disable all plugins
				i = new Intent(ACTION_DISABLE_ALL);
				i.setPackage(context.getPackageName());
				i.replaceExtras(intent);
				context.sendBroadcast(i);
			}
			
			return;
		}
		
		Log.d(TAG, "request for " + className);
		if (className.equals(context.getPackageName() + ".compass.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.CompassService.class);
		}
		else if (className.equals(context.getPackageName() + ".accelerometer.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.AccelerometerService.class);
		}
		else if (className.equals(context.getPackageName() + ".orientation.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.OrientationService.class);
		}
		else if (className.equals(context.getPackageName() + ".timetick.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.TimetickService.class);
		}
		else if (className.equals(context.getPackageName() + ".lightsensor.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.LightService.class);
		}
		else if (className.equals(context.getPackageName() + ".magneticfield.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.MagnetometerService.class);
		}
		else if (className.equals(context.getPackageName() + ".proximity.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.ProximityService.class);
		}
		else if (className.equals(context.getPackageName() + ".phonestate.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.PhoneService.class);
		}
		else if (className.equals(context.getPackageName() + ".batterylevel.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.BatteryService.class);
		}
		else if (className.equals(context.getPackageName() + ".sms.BackgroundService")){
			i = new Intent(context, edu.fsu.cs.contextprovider.sensor.SmsService.class);
		}
		else {
			return;
		}
		
		i.setAction(action); // this might be enable or disable, service should decide what to do
		i.replaceExtras(intent);
		context.startService(i);
	}
}
