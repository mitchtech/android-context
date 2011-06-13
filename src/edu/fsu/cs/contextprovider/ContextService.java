package edu.fsu.cs.contextprovider;

import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import net.smart_entity.DateField;
import net.smart_entity.DoubleField;
import net.smart_entity.EntityManager;
import net.smart_entity.IntegerField;
import net.smart_entity.StringField;
import net.smart_entity.TextField;

import edu.fsu.cs.contextprovider.data.AccuracyEntity;
import edu.fsu.cs.contextprovider.data.ContextConstants;
import edu.fsu.cs.contextprovider.data.DerivedEntity;
import edu.fsu.cs.contextprovider.data.LocationEntity;
import edu.fsu.cs.contextprovider.data.MovementEntity;
import edu.fsu.cs.contextprovider.data.SocialEntity;
import edu.fsu.cs.contextprovider.data.SystemEntity;
import edu.fsu.cs.contextprovider.data.WeatherEntity;
import edu.fsu.cs.contextprovider.monitor.DerivedMonitor;
import edu.fsu.cs.contextprovider.monitor.LocationMonitor;
import edu.fsu.cs.contextprovider.monitor.MovementMonitor;
import edu.fsu.cs.contextprovider.monitor.SocialMonitor;
import edu.fsu.cs.contextprovider.monitor.SystemMonitor;
import edu.fsu.cs.contextprovider.monitor.WeatherMonitor;
import edu.fsu.cs.contextprovider.sensor.AccelerometerService;
import edu.fsu.cs.contextprovider.wakeup.WakefulIntentService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ContextService extends Service implements OnSharedPreferenceChangeListener {

	private static final String TAG = "ContextService";
	private static final boolean DEBUG = true;

	private static Timer popupTimer = new Timer();
	private static Timer locationStoreTimer = new Timer();
	private static Timer movementStoreTimer = new Timer();
	private static Timer weatherStoreTimer = new Timer();
	private static Timer derivedStoreTimer = new Timer();

//	private Context ctx;
	EntityManager entityManager;
	SharedPreferences prefs;

	// location prefs
	private boolean locationEnabled;
//	private boolean locationProximityEnabled;
	private String locationPollFreq;
	private String locationStoreFreq;
	// movement prefs
	private boolean movementEnabled;
	private String movementPollFreq;
	private String movementStoreFreq;
	// weather prefs
	private boolean weatherEnabled;
	private String weatherPollFreq;
	private String weatherStoreFreq;
	// social prefs
	private boolean socialEnabled;
	// system prefs
	private boolean systemEnabled;
	// derived prefs
	private boolean derivedEnabled;
	private String derivedCalcFreq;
	private String derivedStoreFreq;
	// general prefs
	private boolean accuracyPopupEnabled;
	private String accuracyPopupPeriod;

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		prefs = getSharedPreferences(ContextConstants.CONTEXT_PREFS, MODE_PRIVATE);
//		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//		ctx = this;
		getPrefs();
		startService();
	}

	private void startService() {

		startMonitors();
		
		prefs.registerOnSharedPreferenceChangeListener(this);

		IntentFilter storeFilter = new IntentFilter();
		storeFilter.addAction(ContextConstants.CONTEXT_STORE_INTENT);
		registerReceiver(contextIntentReceiver, storeFilter);

		IntentFilter restartFilter = new IntentFilter();
		restartFilter.addAction(ContextConstants.CONTEXT_RESTART_INTENT);
		registerReceiver(restartIntentReceiver, restartFilter);

		// if (accuracyPopupEnabled)
		// popupTimer.schedule(new ContextPopupTask(), (accuracyPopupPeriod * 1000)); // seconds*1000
	}

	private void stopService() {
		stopMonitors();
//		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		prefs.unregisterOnSharedPreferenceChangeListener(this);

		unregisterReceiver(restartIntentReceiver);
		unregisterReceiver(contextIntentReceiver);
	}

	private void startMonitors() {

		Intent intent = null;

		if (locationEnabled) {
			/* Start GPS Service */
			intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.GPSService.class);
			startService(intent);
			/* Start Network Service */
			intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.NetworkService.class);
			startService(intent);
			/* Start LocationMonitor */
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			LocationMonitor.StartThread(Integer.parseInt(locationPollFreq), geocoder);
			// locationStoreTimer.schedule(new LocationStoreTask(),
			// (locationStoreFreq * 1000)); // seconds*1000
		}
		if (movementEnabled) {
			/* Start Accelerometer Service */
			intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.AccelerometerService.class);
			startService(intent);
			/* Start movement context */
			MovementMonitor.StartThread(Integer.parseInt(movementPollFreq));
			// movementStoreTimer.schedule(new MovementStoreTask(),
			// (movementStoreFreq * 1000)); // seconds*1000
		}
		if (weatherEnabled) {
			/* Start weather monitor */
			WeatherMonitor.StartThread(Integer.parseInt(weatherPollFreq));
			// weatherStoreTimer.schedule(new WeatherStoreTask(),
			// (weatherStoreFreq * 1000)); // seconds*1000
		}
		if (systemEnabled) {
			// /* Start Phone/SMS State Monitor Services */
			// intent = new Intent(this.getApplicationContext(),
			// edu.fsu.cs.contextprovider.sensor.TelephonyService.class);
			// startService(intent);
		}
		if (socialEnabled) {
			// /* Start social monitor */
			// SocialMonitor.StartThread(weatherPollFreq);
		}
		if (derivedEnabled) {
			/* Start derived monitor */
			DerivedMonitor.StartThread(Integer.parseInt(derivedCalcFreq));
			// derivedStoreTimer.schedule(new DerivedStoreTask(),
			// (derivedStoreFreq * 1000)); // seconds*1000
		}
	}

	private void stopMonitors() {

		if (locationEnabled) {
			LocationMonitor.StopThread();
			locationStoreTimer.cancel();
		}
		if (movementEnabled) {
			MovementMonitor.StopThread();
			movementStoreTimer.cancel();
		}
		if (weatherEnabled) {
			WeatherMonitor.StopThread();
			weatherStoreTimer.cancel();
		}
		if (derivedEnabled) {
			DerivedMonitor.StopThread();
			derivedStoreTimer.cancel();
		}
	}

	private void getPrefs() {
		accuracyPopupEnabled = prefs.getBoolean(ContextConstants.PREFS_ACCURACY_POPUP_ENABLED, true);
		accuracyPopupPeriod = prefs.getString(ContextConstants.PREFS_ACCURACY_POPUP_FREQ, "45");

		locationEnabled = prefs.getBoolean(ContextConstants.PREFS_LOCATION_ENABLED, true);
//		locationProximityEnabled = prefs.getBoolean(ContextConstants.PREFS_LOCATION_PROXIMITY_ENABLED, true);
		locationPollFreq = prefs.getString(ContextConstants.PREFS_LOCATION_POLL_FREQ, "30");
		locationStoreFreq = prefs.getString(ContextConstants.PREFS_LOCATION_STORE_FREQ, "30");

		movementEnabled = prefs.getBoolean(ContextConstants.PREFS_MOVEMENT_ENABLED, true);
		movementPollFreq = prefs.getString(ContextConstants.PREFS_MOVEMENT_POLL_FREQ, "5");
		movementStoreFreq = prefs.getString(ContextConstants.PREFS_MOVEMENT_STORE_FREQ, "30");

		weatherEnabled = prefs.getBoolean(ContextConstants.PREFS_WEATHER_ENABLED, true);
		weatherPollFreq = prefs.getString(ContextConstants.PREFS_WEATHER_POLL_FREQ, "60");
		weatherStoreFreq = prefs.getString(ContextConstants.PREFS_WEATHER_STORE_FREQ, "30");

		socialEnabled = prefs.getBoolean(ContextConstants.PREFS_SOCIAL_ENABLED, true);
		systemEnabled = prefs.getBoolean(ContextConstants.PREFS_SYSTEM_ENABLED, true);

		derivedEnabled = prefs.getBoolean(ContextConstants.PREFS_DERIVED_ENABLED, true);
		derivedCalcFreq = prefs.getString(ContextConstants.PREFS_DERIVED_CALC_FREQ, "5");
		derivedStoreFreq = prefs.getString(ContextConstants.PREFS_DERIVED_STORE_FREQ, "30");
		
	}

	private class ContextPopupTask extends TimerTask {
		public void run() {
			toastHandler.sendEmptyMessage(0);
			popupTimer.schedule(new ContextPopupTask(), (Integer.parseInt(accuracyPopupPeriod) * 1000)); // seconds*1000
		}
	}

	private class LocationStoreTask extends TimerTask {
		public void run() {
			try {
				StoreLocation();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			locationStoreTimer.schedule(new LocationStoreTask(), (Integer.parseInt(locationStoreFreq) * 1000)); // seconds*1000
		}
	}

	private class MovementStoreTask extends TimerTask {
		public void run() {
			try {
				StoreMovement();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			movementStoreTimer.schedule(new MovementStoreTask(), (Integer.parseInt(movementStoreFreq) * 1000)); // seconds*1000
		}
	}

	private class WeatherStoreTask extends TimerTask {
		public void run() {
			try {
				StoreWeather();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			weatherStoreTimer.schedule(new WeatherStoreTask(), (Integer.parseInt(weatherStoreFreq) * 1000)); // seconds*1000
		}
	}

	private class DerivedStoreTask extends TimerTask {
		public void run() {
			try {
				StoreDerived();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			derivedStoreTimer.schedule(new DerivedStoreTask(), (Integer.parseInt(derivedStoreFreq) * 1000)); // seconds*1000
		}
	}

	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
		stopService();
	}

	private final Handler toastHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(getApplicationContext(), "Context Accuracy Popup", Toast.LENGTH_SHORT).show();
			// Intent intent = new Intent(this, edu.fsu.cs.contextprovider.ContextAccuracyActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);
		}
	};

	BroadcastReceiver contextIntentReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Received Intent: " + intent.getAction());
			int placeAccurate, movementAccurate, activityAccurate, shelterAccurate, onPersonAccurate;

			placeAccurate = intent.getIntExtra(ContextConstants.PLACE_ACCURATE, 10);
			movementAccurate = intent.getIntExtra(ContextConstants.MOVEMENT_ACCURATE, 10);
			activityAccurate = intent.getIntExtra(ContextConstants.ACTIVITY_ACCURATE, 10);
			shelterAccurate = intent.getIntExtra(ContextConstants.SHELTER_ACCURATE, 10);
			onPersonAccurate = intent.getIntExtra(ContextConstants.ONPERSON_ACCURATE, 10);

			Toast.makeText(
					getApplicationContext(),
					"ContextService Accuracy: \n" + "Place: " + placeAccurate + "\n" + "Movement: " + movementAccurate + "\n" + "Activity: " + activityAccurate
							+ "\n" + "Shelter: " + shelterAccurate + "\n" + "OnPerson: " + onPersonAccurate, Toast.LENGTH_LONG).show();

			try {
				StoreAccuracy(placeAccurate, movementAccurate, activityAccurate, shelterAccurate, onPersonAccurate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	BroadcastReceiver restartIntentReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Received Intent: " + intent.getAction());
			Toast.makeText(getApplicationContext(), "ContextService Restart", Toast.LENGTH_LONG).show();
			stopMonitors();
			getPrefs();
			startMonitors();
		}
	};

	private void StoreAllContext() throws Exception {
		StoreLocation();
		StoreMovement();
		StoreWeather();
		StoreSocial();
		StoreSystem();
		StoreDerived();
	}

	private void StoreLocation() throws Exception {
		try {
			entityManager = EntityManager.GetManager(this);
			LocationEntity location = new LocationEntity();
			location.Timestamp.setValue(new Date());
			location.Address.setValue(LocationMonitor.getAddress());
			location.Neighborhood.setValue(LocationMonitor.getNeighborhood());
			location.Zip.setValue(LocationMonitor.getZip());
			location.Latitude.setValue(LocationMonitor.getLatitude());
			location.Longitude.setValue(LocationMonitor.getLongitude());
			location.Altitude.setValue(LocationMonitor.getAltitude());
			int uid = entityManager.store(location);
			// LocationEntity fetchedLocation = (LocationEntity)
			// entityManager.fetchById(uid);
			// String address = fetchedLocation.Address.getValue();
		} catch (Exception e) {
			throw e;
		}
	}

	private void StoreMovement() throws Exception {
		try {
			entityManager = EntityManager.GetManager(this);
			MovementEntity movement = new MovementEntity();
			movement.Timestamp.setValue(new Date());
			movement.State.setValue(MovementMonitor.getMovementState());
			movement.Speed.setValue((double) MovementMonitor.getSpeedMph());
			movement.Bearing.setValue((double) LocationMonitor.getBearing());
			movement.Steps.setValue((int) AccelerometerService.getStepCount());
			movement.LastStep.setValue(AccelerometerService.getLastStepTimestamp());
			int uid = entityManager.store(movement);
		} catch (Exception e) {
			throw e;
		}
	}

	private void StoreWeather() throws Exception {
		try {
			entityManager = EntityManager.GetManager(this);
			WeatherEntity weather = new WeatherEntity();
			weather.Timestamp.setValue(new Date());
			weather.Condition.setValue(WeatherMonitor.getWeatherCond());
			weather.Temperature.setValue(WeatherMonitor.getWeatherTemp());
			weather.Humidity.setValue(WeatherMonitor.getWeatherHumid());
			weather.Wind.setValue(WeatherMonitor.getWeatherWindCond());
			weather.HazardLevel.setValue(WeatherMonitor.getWeatherHazard());
			int uid = entityManager.store(weather);
		} catch (Exception e) {
			throw e;
		}
	}

	private void StoreSocial() throws Exception {
		try {
			entityManager = EntityManager.GetManager(this);
			SocialEntity social = new SocialEntity();
			social.Timestamp.setValue(new Date());
			social.Contact.setValue(SocialMonitor.getContact());
			social.Communication.setValue(SocialMonitor.getCommunication());
			social.Message.setValue(SocialMonitor.getMessage());
			social.LastIncoming.setValue(SocialMonitor.getLastInDate());
			social.LastOutgoing.setValue(SocialMonitor.getLastOutDate());
			int uid = entityManager.store(social);
		} catch (Exception e) {
			throw e;
		}
	}

	private void StoreSystem() throws Exception {
		try {
			entityManager = EntityManager.GetManager(this);
			SystemEntity system = new SystemEntity();
			system.Timestamp.setValue(new Date());
			system.State.setValue(SystemMonitor.getState());
			system.BatteryLevel.setValue(SystemMonitor.getBatteryLevel());
			system.Plugged.setValue(SystemMonitor.isBatteryPluggedString());
			system.LastPlugged.setValue(SystemMonitor.getBatteryLastPluggedDate());
			system.LastPresent.setValue(SystemMonitor.getUserLastPresentDate());
			system.SSID.setValue(SystemMonitor.getSSID());
			system.Signal.setValue(SystemMonitor.getSignal());
			int uid = entityManager.store(system);
		} catch (Exception e) {
			throw e;
		}
	}

	private void StoreDerived() throws Exception {
		try {
			entityManager = EntityManager.GetManager(this);
			DerivedEntity derived = new DerivedEntity();
			derived.Timestamp.setValue(new Date());
			derived.Place.setValue(DerivedMonitor.getPlace());
			derived.Activity.setValue(DerivedMonitor.getActivity());
			derived.Shelter.setValue(DerivedMonitor.getShelterString());
			derived.Pocket.setValue(DerivedMonitor.getOnPersonString());
			derived.Mood.setValue(DerivedMonitor.getMood());
			int uid = entityManager.store(derived);
		} catch (Exception e) {
			throw e;
		}
	}

	private void StoreAccuracy(int place, int movement, int activity, int shelter, int onPerson) throws Exception {
		try {
			entityManager = EntityManager.GetManager(this);
			AccuracyEntity accuracy = new AccuracyEntity();
			accuracy.Timestamp.setValue(new Date());
			accuracy.Place.setValue(place);
			accuracy.Movement.setValue(movement);
			accuracy.Activity.setValue(activity);
			accuracy.Shelter.setValue(shelter);
			accuracy.OnPerson.setValue(onPerson);
			int uid = entityManager.store(accuracy);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		if (DEBUG) {
			Toast.makeText(this, "ContextService prefs changed", Toast.LENGTH_SHORT).show();
		}
		
		getPrefs();
//		Toast.makeText(getApplicationContext(), "ContextService prefs changed", Toast.LENGTH_SHORT).show();
//        if (key.equals(ContextConstants.PREFS_ACCURACY_POPUP_ENABLED) || key.equals(ContextConstants.PREFS_ACCURACY_POPUP_PERIOD)) {
//        	Toast.makeText(this, "ACCURACY_POPUP changed", Toast.LENGTH_SHORT).show();
//        	
//        	AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//			Intent intent = new Intent(getBaseContext(), edu.fsu.cs.contextprovider.wakeup.WakeupAlarmReceiver.class);
//			PendingIntent pi = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);
//        	
//        	if (accuracyPopupEnabled) {
//        		manager.cancel(pi);
//    			manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10000, accuracyPopupPeriod * 1000, pi);
//        	} else {
//        		manager.cancel(pi);
//        	}
//        }
        			
		stopService();
		startService();
	}

}