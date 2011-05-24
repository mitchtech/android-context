package edu.fsu.cs.contextprovider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PrefsActivity extends PreferenceActivity {
	private static final String TAG = "edu.fsu.cs.PrefsActivity";
	private static final String PREFS_NAME = "ContextPrefs";

	private static final int MENU_ABOUT_ID = Menu.FIRST;
	private static final int ABOUT_DIALOG = 0;
	private static final int DIALOG_ABOUT = 0;

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		prefs = getPreferenceScreen().getSharedPreferences();
//		remoteEyesUrlPref = (EditTextPreference) findPreference("REMOTE_EYES_PUT_URL");


	}

	// // general
	// public static final String PREFS_DECIMAL_PLACES = "DecimalPlaces";
	// public static final int PREFS_DECIMAL_PLACES_DEFAULT = 2;
	// public static final String PREFS_STARTUP_ENABLED = "StartupStartup";
	// public static final boolean PREFS_STARTUP_ENABLED_DEFAULT = false;
	// public static final String PREFS_SHAKE_ENABLED = "ShakeEnabled";
	// public static final boolean PREFS_SHAKE_ENABLED_DEFAULT = true;
	// public static final String PREFS_TTS_ENABLED = "TtsEnabled";
	// public static final boolean PREFS_TTS_ENABLED_DEFAULT = false;
	//
	// // location
	// public static final String PREFS_LOCATION_ENABLED = "LocationEnabled";
	// public static final boolean PREFS_LOCATION_ENABLED_DEFAULT = true;
	//
	// // movement
	// public static final String PREFS_MOVEMENT_ENABLED = "MovementEnabled";
	// public static final boolean PREFS_MOVEMENT_ENABLED_DEFAULT = true;
	//
	// // proximity
	// public static final String PREFS_PROXIMITY_ENABLED = "ProximityEnabled";
	// public static final boolean PREFS_PROXIMITY_ENABLED_DEFAULT = true;
	//
	// // weather
	// public static final String PREFS_WEATHER_ENABLED = "WeatherEnabled";
	// public static final boolean PREFS_WEATHER_ENABLED_DEFAULT = true;
	//
	// // system
	// public static final String PREFS_SYSTEM_ENABLED = "SystemEnabled";
	// public static final boolean PREFS_SYSTEM_ENABLED_DEFAULT = true;
	//
	// // telephony
	// public static final String PREFS_TELEPHONY_ENABLED = "TelephonyEnabled";
	// public static final boolean PREFS_TELEPHONY_ENABLED_DEFAULT = true;
	//
	// // social
	// public static final String PREFS_SOCIAL_ENABLED = "SocialEnabled";
	// public static final boolean PREFS_SOCIAL_ENABLED_DEFAULT = true;
	//
	// // finance
	// public static final String PREFS_FINANCE_ENABLED = "FinanceEnabled";
	// public static final boolean PREFS_FINANCE_ENABLED_DEFAULT = true;
	//
	// // derived
	// public static final String PREFS_DERIVED_ENABLED = "DerivedEnabled";
	// public static final boolean PREFS_DERIVED_ENABLED_DEFAULT = true;
	//
	// @Override
	// protected void onCreate(Bundle icicle) {
	// super.onCreate(icicle);
	// setPreferenceScreen(createPreferenceHierarchy());
	// }
	//
	// private PreferenceScreen createPreferenceHierarchy() {
	//
	// DigitsKeyListener integer = new DigitsKeyListener(false, false);
	// DigitsKeyListener decimal = new DigitsKeyListener(false, true);
	//
	// PreferenceScreen root =
	// getPreferenceManager().createPreferenceScreen(this);
	//
	// // general prefs
	// PreferenceCategory generalPrefs = new PreferenceCategory(this);
	// generalPrefs.setTitle("General");
	// root.addPreference(generalPrefs);
	// // ListPreference defaultGroup = new ListPreference(this);
	// // Decimal places
	// EditTextPreference decimalPlaces = new EditTextPreference(this);
	// decimalPlaces.setDialogTitle("Number of Decimal Places");
	// decimalPlaces.setKey(PREFS_DECIMAL_PLACES);
	// decimalPlaces.setTitle("Decimal Places");
	// decimalPlaces.setSummary("The number of decimal places to round to");
	// decimalPlaces.setDefaultValue(new
	// Integer(PREFS_DECIMAL_PLACES_DEFAULT).toString());
	// decimalPlaces.getEditText().setKeyListener(integer);
	// generalPrefs.addPreference(decimalPlaces);
	//
	// CheckBoxPreference runStartup = new CheckBoxPreference(this);
	// runStartup.setKey(PREFS_STARTUP_ENABLED);
	// runStartup.setTitle("Run at Start-up");
	// runStartup.setSummary("Enable/Disable ContextProvider Start at System Boot");
	// runStartup.setDefaultValue(new Boolean(PREFS_STARTUP_ENABLED_DEFAULT));
	// generalPrefs.addPreference(runStartup);
	//
	// CheckBoxPreference shakeEnabled = new CheckBoxPreference(this);
	// shakeEnabled.setKey(PREFS_SHAKE_ENABLED);
	// shakeEnabled.setTitle("Shake Response");
	// shakeEnabled.setSummary("Enable/Disable ContextProvider Instant-Context on Shake");
	// shakeEnabled.setDefaultValue(new Boolean(PREFS_SHAKE_ENABLED_DEFAULT));
	// generalPrefs.addPreference(shakeEnabled);
	//
	// CheckBoxPreference ttsEnabled = new CheckBoxPreference(this);
	// ttsEnabled.setKey(PREFS_TTS_ENABLED);
	// ttsEnabled.setTitle("TTS Context");
	// ttsEnabled.setSummary("Enable/Disable ContextProvider Context Speaking");
	// ttsEnabled.setDefaultValue(new Boolean(PREFS_TTS_ENABLED_DEFAULT));
	// generalPrefs.addPreference(ttsEnabled);
	//
	// // location prefs
	// PreferenceCategory locationPrefs = new PreferenceCategory(this);
	// locationPrefs.setTitle("Location Parameters");
	// root.addPreference(locationPrefs);
	//
	// CheckBoxPreference locationEnabled = new CheckBoxPreference(this);
	// locationEnabled.setKey(PREFS_LOCATION_ENABLED);
	// locationEnabled.setTitle("Location Context");
	// locationEnabled.setSummary("Enable/Disable ContextProvider Location Tracking");
	// locationEnabled.setDefaultValue(new
	// Boolean(PREFS_LOCATION_ENABLED_DEFAULT));
	// locationPrefs.addPreference(locationEnabled);
	//
	// // movement prefs
	// PreferenceCategory movementPrefs = new PreferenceCategory(this);
	// movementPrefs.setTitle("Movement Parameters");
	// root.addPreference(movementPrefs);
	//
	// CheckBoxPreference movementEnabled = new CheckBoxPreference(this);
	// movementEnabled.setKey(PREFS_MOVEMENT_ENABLED);
	// movementEnabled.setTitle("Movement Context");
	// movementEnabled.setSummary("Enable/Disable ContextProvider Movement Tracking");
	// movementEnabled.setDefaultValue(new
	// Boolean(PREFS_MOVEMENT_ENABLED_DEFAULT));
	// movementPrefs.addPreference(movementEnabled);
	//
	// // proximity prefs
	// PreferenceCategory proximityPrefs = new PreferenceCategory(this);
	// proximityPrefs.setTitle("Address Proximity Parameters");
	// root.addPreference(proximityPrefs);
	//
	// CheckBoxPreference proximityEnabled = new CheckBoxPreference(this);
	// proximityEnabled.setKey(PREFS_PROXIMITY_ENABLED);
	// proximityEnabled.setTitle("Address Proximity Context");
	// proximityEnabled.setSummary("Enable/Disable ContextProvider Address Proximity Tracking");
	// proximityEnabled.setDefaultValue(new
	// Boolean(PREFS_PROXIMITY_ENABLED_DEFAULT));
	// proximityPrefs.addPreference(proximityEnabled);
	//
	// // weather prefs
	// PreferenceCategory weatherPrefs = new PreferenceCategory(this);
	// weatherPrefs.setTitle("Weather Parameters");
	// root.addPreference(weatherPrefs);
	//
	// CheckBoxPreference weatherEnabled = new CheckBoxPreference(this);
	// weatherEnabled.setKey(PREFS_WEATHER_ENABLED);
	// weatherEnabled.setTitle("Weather Context");
	// weatherEnabled.setSummary("Enable/Disable ContextProvider Weather Tracking");
	// weatherEnabled.setDefaultValue(new
	// Boolean(PREFS_WEATHER_ENABLED_DEFAULT));
	// weatherPrefs.addPreference(weatherEnabled);
	//
	// // system prefs
	// PreferenceCategory systemPrefs = new PreferenceCategory(this);
	// systemPrefs.setTitle("System Broadcast Parameters");
	// root.addPreference(systemPrefs);
	//
	// CheckBoxPreference systemEnabled = new CheckBoxPreference(this);
	// systemEnabled.setKey(PREFS_SYSTEM_ENABLED);
	// systemEnabled.setTitle("System Broadcast Context");
	// systemEnabled.setSummary("Enable/Disable ContextProvider System Broadcast Tracking");
	// systemEnabled.setDefaultValue(new Boolean(PREFS_SYSTEM_ENABLED_DEFAULT));
	// systemPrefs.addPreference(systemEnabled);
	//
	// // telephony prefs
	// PreferenceCategory telephonyPrefs = new PreferenceCategory(this);
	// telephonyPrefs.setTitle("Telephony Parameters");
	// root.addPreference(telephonyPrefs);
	//
	// CheckBoxPreference telephonyEnabled = new CheckBoxPreference(this);
	// telephonyEnabled.setKey(PREFS_TELEPHONY_ENABLED);
	// telephonyEnabled.setTitle("Telephony Context");
	// telephonyEnabled.setSummary("Enable/Disable ContextProvider Phone/SMS Tracking");
	// telephonyEnabled.setDefaultValue(new
	// Boolean(PREFS_TELEPHONY_ENABLED_DEFAULT));
	// telephonyPrefs.addPreference(telephonyEnabled);
	//
	// // social prefs
	// PreferenceCategory socialPrefs = new PreferenceCategory(this);
	// socialPrefs.setTitle("Social Parameters");
	// root.addPreference(socialPrefs);
	//
	// CheckBoxPreference socialEnabled = new CheckBoxPreference(this);
	// socialEnabled.setKey(PREFS_SOCIAL_ENABLED);
	// socialEnabled.setTitle("Social Network Context");
	// socialEnabled.setSummary("Enable/Disable ContextProvider Social Network Tracking");
	// socialEnabled.setDefaultValue(new Boolean(PREFS_SOCIAL_ENABLED_DEFAULT));
	// socialPrefs.addPreference(socialEnabled);
	//
	// // finance prefs
	// PreferenceCategory financePrefs = new PreferenceCategory(this);
	// financePrefs.setTitle("Finance Parameters");
	// root.addPreference(financePrefs);
	//
	// CheckBoxPreference financeEnabled = new CheckBoxPreference(this);
	// financeEnabled.setKey(PREFS_FINANCE_ENABLED);
	// financeEnabled.setTitle("Finance Context");
	// financeEnabled.setSummary("Enable/Disable ContextProvider Finance Tracking");
	// financeEnabled.setDefaultValue(new
	// Boolean(PREFS_FINANCE_ENABLED_DEFAULT));
	// financePrefs.addPreference(financeEnabled);
	//
	// // derived prefs
	// PreferenceCategory derivedPrefs = new PreferenceCategory(this);
	// derivedPrefs.setTitle("Derived Parameters");
	// root.addPreference(derivedPrefs);
	//
	// CheckBoxPreference derivedEnabled = new CheckBoxPreference(this);
	// derivedEnabled.setKey(PREFS_DERIVED_ENABLED);
	// derivedEnabled.setTitle("Derived Context");
	// derivedEnabled.setSummary("Enable/Disable ContextProvider Derived Context Tracking");
	// derivedEnabled.setDefaultValue(new
	// Boolean(PREFS_DERIVED_ENABLED_DEFAULT));
	// derivedPrefs.addPreference(derivedEnabled);
	//
	// return root;
	// }

//	@Override
//	public void onResume() {
//		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
//		super.onResume();
//	}
//
//	@Override
//	public void onPause() {
//		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
//		super.onPause();
//	}

	private void enabled(final boolean enable) {
		String dialogTitle;
		if (enable) {
			dialogTitle = getString(R.string.enable_dialog_msg);
		} else {
			dialogTitle = getString(R.string.disable_dialog_msg);
		}
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(dialogTitle).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// enable monitor here
			}
		}).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Do nothing
			}
		}).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ABOUT_ID, 0, "About").setIcon(android.R.drawable.ic_menu_info_details);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ABOUT_ID:
			showDialog(ABOUT_DIALOG);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DIALOG_ABOUT:
			dialog = getAboutBox();
			break;

		default:
			dialog = null;
		}
		return dialog;
	}

	private AlertDialog getAboutBox() {
		String title = getString(R.string.app_name) + " build " + getVersion(this);

		return new AlertDialog.Builder(PrefsActivity.this).setTitle(title).setView(View.inflate(this, R.layout.about, null)).setIcon(R.drawable.context64).setPositiveButton("OK", null).create();

	}

	public static String getVersion(Context context) {
		String version = "1.0";
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Package name not found", e);
		}
		return version;
	}

	// public static int getDecimalPlaces(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// String s = settings.getString(PREFS_DECIMAL_PLACES, new
	// Integer(PREFS_DECIMAL_PLACES_DEFAULT).toString());
	// int i;
	// try {
	// i = Integer.parseInt(s);
	// } catch (Exception e) {
	// i = PREFS_DECIMAL_PLACES_DEFAULT;
	// }
	// return i;
	// }
	//
	// public static boolean getStartupEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_STARTUP_ENABLED, new
	// Boolean(PREFS_STARTUP_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getShakeEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_SHAKE_ENABLED, new
	// Boolean(PREFS_SHAKE_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getTtsEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_TTS_ENABLED, new
	// Boolean(PREFS_TTS_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getLocationEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_LOCATION_ENABLED, new
	// Boolean(PREFS_LOCATION_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getMovementEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_MOVEMENT_ENABLED, new
	// Boolean(PREFS_MOVEMENT_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getProximityEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_PROXIMITY_ENABLED, new
	// Boolean(PREFS_PROXIMITY_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getWeatherEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_WEATHER_ENABLED, new
	// Boolean(PREFS_WEATHER_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getSystemEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_SYSTEM_ENABLED, new
	// Boolean(PREFS_SYSTEM_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getTelephonyEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_TELEPHONY_ENABLED, new
	// Boolean(PREFS_TELEPHONY_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getSocialEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_SOCIAL_ENABLED, new
	// Boolean(PREFS_SOCIAL_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getFinanceEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_FINANCE_ENABLED, new
	// Boolean(PREFS_FINANCE_ENABLED_DEFAULT));
	// }
	//
	// public static boolean getDerivedEnabled(Context ctx) {
	// SharedPreferences settings =
	// PreferenceManager.getDefaultSharedPreferences(ctx);
	// return settings.getBoolean(PREFS_DERIVED_ENABLED, new
	// Boolean(PREFS_DERIVED_ENABLED_DEFAULT));
	// }

}