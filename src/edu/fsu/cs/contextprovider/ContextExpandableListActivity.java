/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.fsu.cs.contextprovider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;
import edu.fsu.cs.contextprovider.dialog.AddressDialog;
import edu.fsu.cs.contextprovider.finance.GoogleFinanceHandler;
import edu.fsu.cs.contextprovider.finance.GoogleFinanceQuote;
import edu.fsu.cs.contextprovider.monitor.LocationMonitor;
import edu.fsu.cs.contextprovider.monitor.MovementMonitor;
import edu.fsu.cs.contextprovider.monitor.SocialMonitor;
import edu.fsu.cs.contextprovider.monitor.SystemBroadcastMonitor;
import edu.fsu.cs.contextprovider.monitor.WeatherMonitor;
import edu.fsu.cs.contextprovider.rpc.ContextProviderService;
import edu.fsu.cs.contextprovider.rpc.IContextProviderService;
import edu.fsu.cs.contextprovider.sensor.AccelerometerService;
import edu.fsu.cs.contextprovider.sensor.LightService;
import edu.fsu.cs.contextprovider.sensor.TelephonyService;
import edu.fsu.cs.contextprovider.weather.GoogleWeatherHandler;
import edu.fsu.cs.contextprovider.weather.WeatherCurrentCondition;
import edu.fsu.cs.contextprovider.weather.WeatherSet;

public class ContextExpandableListActivity extends ExpandableListActivity implements OnChildClickListener, TextToSpeech.OnInitListener {
	private static final String PKG = "edu.fsu.cs.contextprovider";
	private static final String TAG = "ContextExpandableListActivity";

	private static final String NAME = "NAME";
	private static final String VALUE = "VALUE";

	private ExpandableListAdapter mAdapter;

	private static final int MENU_REFRESH_ID = Menu.FIRST + 1;
	private static final int MENU_ADD_ID = Menu.FIRST + 2;
	private static final int MENU_SHARE_ID = Menu.FIRST + 3;
	private static final int MENU_PREFS_ID = Menu.FIRST + 4;
	private static final int MENU_ABOUT_ID = Menu.FIRST + 5;

	private static final int CONTEXT_DELETE_ID = 0;

	private static final int PREFS_EDIT = 0;

	private static final int DIALOG_ABOUT = 0;
	private static final String CSV_FILENAME = "Context.csv";

	// preferences
	private boolean locationEnabled;
	private boolean movementEnabled;
	private boolean proximityEnabled;
	private boolean weatherEnabled;
	private boolean systemEnabled;
	private boolean telephonyEnabled;
	private boolean socialEnabled;
	private boolean financeEnabled;
	private boolean derivedEnabled;

	private boolean startupEnabled;
	private boolean ttsEnabled;

	public static boolean running = false;
	public static TextToSpeech tts;

	ClipboardManager clip = null;

	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

	public static IContextProviderService mService = null;

	Vector<ContextListItem> Clist = new Vector<ContextListItem>();
	// ArrayAdapter<ContextListItem> adapter = null;

	ArrayAdapter<ContextListItem> addressAdapter = null;
	private CopyState copyState = new CopyState();

	private ServiceConnection conn = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IContextProviderService.Stub.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName name) {
			unbindService(conn);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPrefs();

		tts = new TextToSpeech(this, this);

		running = true;

		Intent intent = null;

		if (clip == null) {
			clip = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		}

		/* Start ContextProviderService */
		bindService(new Intent(this, ContextProviderService.class), conn, Context.BIND_AUTO_CREATE);

		if (locationEnabled) {
			/* Start GPS Service */
			intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.GPSService.class);
			startService(intent);

			/* Start Network Service */
			intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.NetworkService.class);
			startService(intent);

			/* Start LocationMonitor */
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			LocationMonitor.StartThread(5, geocoder);

			refreshLocation();
		}
		if (movementEnabled) {

			/* Start Accelerometer Service */
			intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.AccelerometerService.class);
			startService(intent);

			/* Start movement context */
			MovementMonitor.StartThread(5);

			refreshMovement();
		}
		if (proximityEnabled) {
			refreshProximity();
		}
		if (weatherEnabled) {
			/* Start weather monitor */
			WeatherMonitor.StartThread(60);

			refreshWeather();
		}
		if (systemEnabled) {
			refreshSystem();
		}
		if (telephonyEnabled) {
			/* Start Phone/SMS State Monitor Services */
			intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.sensor.TelephonyService.class);
			startService(intent);

			refreshTelephony();
		}
//		if (socialEnabled) {
//			/* Start social monitor */
//			SocialMonitor.StartThread(60);
//
//			refreshSocial();
//		}
//		if (financeEnabled) {
//			refreshFinance();
//		}
		if (derivedEnabled) {
			refreshDerived();
		}

		// Set up our adapter
		mAdapter = new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1, new String[] { NAME, VALUE }, new int[] { android.R.id.text1, android.R.id.text2 },
				childData, android.R.layout.simple_expandable_list_item_2, new String[] { NAME, VALUE }, new int[] { android.R.id.text1, android.R.id.text2 });

		// mAdapter = new SimpleExpandableListAdapter(this, groupData,
		// R.layout.group_row, new String[] { NAME, VALUE }, new int[] {
		// android.R.id.text1, android.R.id.text2 },
		// childData, R.layout.child_row, new String[] { NAME, VALUE }, new
		// int[] { android.R.id.text1, android.R.id.text2 });

		setListAdapter(mAdapter);

		refresh();
	}

	public void onInit(int status) {
		Locale loc = Locale.getDefault();
		if (tts.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
			tts.setLanguage(loc);
		}
		if (ttsEnabled)
			tts.speak("Text to Speach Initialized", TextToSpeech.QUEUE_FLUSH, null);
	}

	private void getPrefs() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		// general
		startupEnabled = prefs.getBoolean("PREFS_STARTUP_ENABLED", true);
		ttsEnabled = prefs.getBoolean("PREFS_TTS_ENABLED", true);
		
		// categories
		locationEnabled = prefs.getBoolean("PREFS_LOCATION_ENABLED", true);
		movementEnabled = prefs.getBoolean("PREFS_MOVEMENT_ENABLED", true);
//		proximityEnabled = PrefsActivity.getProximityEnabled(getApplicationContext());
//		weatherEnabled = PrefsActivity.getWeatherEnabled(getApplicationContext());
//		systemEnabled = PrefsActivity.getSystemEnabled(getApplicationContext());
//		telephonyEnabled = PrefsActivity.getTelephonyEnabled(getApplicationContext());
//		socialEnabled = PrefsActivity.getSocialEnabled(getApplicationContext());
//		financeEnabled = PrefsActivity.getFinanceEnabled(getApplicationContext());
//		derivedEnabled = PrefsActivity.getDerivedEnabled(getApplicationContext());
		
	}

	private void refresh() {
		if (mService == null) {
			return;
		}
		Map<String, String> cntx = null;
		try {
			cntx = (Map<String, String>) mService.getAll();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		groupData.clear();
		childData.clear();

		if (locationEnabled)
			refreshLocation();
		if (movementEnabled)
			refreshMovement();
		if (proximityEnabled)
			refreshProximity();
		if (weatherEnabled)
			refreshWeather();
		if (systemEnabled)
			refreshSystem();
		if (telephonyEnabled)
			refreshTelephony();
		if (socialEnabled)
			refreshSocial();
		if (financeEnabled)
			refreshFinance();
		if (derivedEnabled)
			refreshDerived();

		BaseExpandableListAdapter refresh = (BaseExpandableListAdapter) mAdapter;
		refresh.notifyDataSetChanged();
		copyState.reset();
		// refresh.notifyDataSetInvalidated();
	}

	private void refreshLocation() {
		if (mService == null) {
			return;
		}
		Map<String, String> locationMap = new HashMap<String, String>();
		groupData.add(locationMap);
		locationMap.put(NAME, "Location");
		locationMap.put(VALUE, "Location");
		List<Map<String, String>> location = new ArrayList<Map<String, String>>();
		Map<String, String> curChildMap = new HashMap<String, String>();
		location.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.LOCATION_ADDRESS);
		curChildMap.put(VALUE, LocationMonitor.getAddress());
		curChildMap = new HashMap<String, String>();
		location.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.LOCATION_HOOD);
		curChildMap.put(VALUE, LocationMonitor.getNeighborhood());
		curChildMap = new HashMap<String, String>();
		location.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.LOCATION_ZIP);
		curChildMap.put(VALUE, LocationMonitor.getZip());
		curChildMap = new HashMap<String, String>();
		location.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.LOCATION_LATITUDE);
		curChildMap.put(VALUE, String.valueOf(LocationMonitor.getLatitude()));
		curChildMap = new HashMap<String, String>();
		location.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.LOCATION_LONGITUDE);
		curChildMap.put(VALUE, String.valueOf(LocationMonitor.getLongitude()));
		curChildMap = new HashMap<String, String>();
		location.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.LOCATION_ALTITUDE);
		curChildMap.put(VALUE, String.valueOf(LocationMonitor.getAltitude()));

		childData.add(location);
	}

	private void refreshMovement() {
		if (mService == null) {
			return;
		}
		Map<String, String> movementMap = new HashMap<String, String>();
		groupData.add(movementMap);
		movementMap.put(NAME, "Movement");
		movementMap.put(VALUE, "Movement");
		List<Map<String, String>> movement = new ArrayList<Map<String, String>>();
		Map<String, String> curChildMap = new HashMap<String, String>();
		movement.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.MOVEMENT_STATE);
		curChildMap.put(VALUE, MovementMonitor.getMovementState());
		curChildMap = new HashMap<String, String>();
		movement.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.MOVEMENT_SPEED);
		curChildMap.put(VALUE, String.valueOf(MovementMonitor.getSpeedMph()));
		curChildMap = new HashMap<String, String>();
		movement.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.MOVEMENT_SPEED);
		curChildMap.put(VALUE, String.valueOf(LocationMonitor.getBearing()));
		curChildMap = new HashMap<String, String>();
		movement.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.MOVEMENT_STEP_COUNT);
		curChildMap.put(VALUE, String.valueOf(AccelerometerService.getStepCount()));
		curChildMap = new HashMap<String, String>();
		movement.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.MOVEMENT_LAST_STEP);
		curChildMap.put(VALUE, String.valueOf(AccelerometerService.getStepTimestamp()));

		childData.add(movement);
	}

	private void refreshProximity() {
		if (mService == null) {
			return;
		}
		Map<String, String> proximityMap = new HashMap<String, String>();
		groupData.add(proximityMap);
		proximityMap.put(NAME, "Proximity");
		proximityMap.put(VALUE, "Proximity");
		List<Map<String, String>> proximity = new ArrayList<Map<String, String>>();

		SharedPreferences pref = getSharedPreferences(ContextConstants.PREFS_ADDRESS, 0);
		Map<String, String> list = (Map<String, String>) pref.getAll();
		for (Map.Entry<String, String> entry : list.entrySet()) {
			Map<String, String> curChildMap = new HashMap<String, String>();
			proximity.add(curChildMap);
			curChildMap.put(NAME, entry.getKey());
			double prox = 0;
			try {
				prox = mService.proximityToAddress(entry.getValue());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			curChildMap.put(VALUE, String.valueOf(prox));
		}

		childData.add(proximity);
	}

	private void refreshWeather() {
		if (mService == null) {
			return;
		}

		Map<String, String> weatherMap = new HashMap<String, String>();
		groupData.add(weatherMap);
		weatherMap.put(NAME, "Weather");
		weatherMap.put(VALUE, "Weather");
		List<Map<String, String>> weather = new ArrayList<Map<String, String>>();
		Map<String, String> curChildMap = new HashMap<String, String>();
		weather.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.WEATHER_CUR_TEMP);
		curChildMap.put(VALUE, WeatherMonitor.getWeatherTemp() + " degrees F");
		curChildMap = new HashMap<String, String>();
		weather.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.WEATHER_CUR_CONDITION);
		curChildMap.put(VALUE, WeatherMonitor.getWeatherCond());
		curChildMap = new HashMap<String, String>();
		weather.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.WEATHER_CUR_HUMIDITY);
		curChildMap.put(VALUE, WeatherMonitor.getWeatherHumid());
		curChildMap = new HashMap<String, String>();
		weather.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.WEATHER_CUR_WIND);
		curChildMap.put(VALUE, WeatherMonitor.getWeatherWindCond());

		childData.add(weather);
	}

	private void refreshSystem() {
		if (mService == null) {
			return;
		}
		Map<String, String> systemMap = new HashMap<String, String>();
		groupData.add(systemMap);
		systemMap.put(NAME, "System");
		systemMap.put(VALUE, "System");
		List<Map<String, String>> system = new ArrayList<Map<String, String>>();
		Map<String, String> curChildMap = new HashMap<String, String>();
		system.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.SYSTEM_BATTERY_LEVEL);
		curChildMap.put(VALUE, String.valueOf(SystemBroadcastMonitor.getBatteryLevel()));
		curChildMap = new HashMap<String, String>();
		system.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.SYSTEM_BATTERY_LOW);
		curChildMap.put(VALUE, String.valueOf(SystemBroadcastMonitor.isBatteryLow()));
		curChildMap = new HashMap<String, String>();
		system.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.SYSTEM_PLUGGED);
		curChildMap.put(VALUE, String.valueOf(SystemBroadcastMonitor.isBatteryPlugged()));
		curChildMap = new HashMap<String, String>();
		system.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.SYSTEM_LAST_PLUGGED);
		curChildMap.put(VALUE, String.valueOf(SystemBroadcastMonitor.getBatteryLastPlugged()));
		curChildMap = new HashMap<String, String>();
		system.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.SYSTEM_USER_LAST_PRESENT);
		curChildMap.put(VALUE, String.valueOf(SystemBroadcastMonitor.getUserLastPresent()));

		childData.add(system);
	}

	private void refreshTelephony() {
		if (mService == null) {
			return;
		}
		Map<String, String> telephonyMap = new HashMap<String, String>();
		groupData.add(telephonyMap);
		telephonyMap.put(NAME, "Telephony");
		telephonyMap.put(VALUE, "Telephony");
		List<Map<String, String>> telephony = new ArrayList<Map<String, String>>();
		Map<String, String> curChildMap = new HashMap<String, String>();
		telephony.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.TELEPHONY_PHONE_STATE);
		curChildMap.put(VALUE, TelephonyService.PHONE_STATE);
		curChildMap = new HashMap<String, String>();
		telephony.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.TELEPHONY_PHONE_LAST_UPDATE);
		curChildMap.put(VALUE, String.valueOf(TelephonyService.PHONE_STATE_UPDATE));
		curChildMap = new HashMap<String, String>();
		telephony.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.TELEPHONY_LAST_RECV);
		curChildMap.put(VALUE, String.valueOf(TelephonyService.PHONE_LAST_NUMBER_RECV));
		curChildMap = new HashMap<String, String>();
		telephony.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.TELEPHONY_LAST_DIAL);
		curChildMap.put(VALUE, String.valueOf(TelephonyService.PHONE_LAST_NUMBER_DIAL));
		curChildMap = new HashMap<String, String>();
		telephony.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.TELEPHONY_SMS_STATE);
		curChildMap.put(VALUE, TelephonyService.SMS_STATE);
		curChildMap = new HashMap<String, String>();
		telephony.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.TELEPHONY_SMS_LAST_SENDER);
		curChildMap.put(VALUE, TelephonyService.SMS_LAST_SENDER);
		curChildMap = new HashMap<String, String>();
		telephony.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.TELEPHONY_SMS_LAST_MESSAGE);
		curChildMap.put(VALUE, TelephonyService.SMS_LAST_MESSAGE);
		curChildMap = new HashMap<String, String>();
		telephony.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.TELEPHONY_SMS_LAST_UPDATE);
		curChildMap.put(VALUE, String.valueOf(TelephonyService.SMS_STATE_UPDATE));

		childData.add(telephony);
	}

	private void refreshSocial() {
		if (mService == null) {
			return;
		}
		Map<String, String> socialMap = new HashMap<String, String>();
		groupData.add(socialMap);
		socialMap.put(NAME, "Social");
		socialMap.put(VALUE, "Social");
		List<Map<String, String>> social = new ArrayList<Map<String, String>>();
		Map<String, String> curChildMap = new HashMap<String, String>();
		social.add(curChildMap);
		curChildMap.put(NAME, "SOCIAL_TWITTER_LAST");
		String status = SocialMonitor.getCurrentTwitterStatus();
		if (status == null) {
			curChildMap.put(VALUE, "NA");
		} else {
			curChildMap.put(VALUE, SocialMonitor.getCurrentTwitterStatus());
		}

		childData.add(social);
	}

	private GoogleFinanceQuote getQuotes(String quotesToFind) {
		try {
			URL financeUrl = new URL(this.getString(R.string.google_finance_url) + quotesToFind);
			XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			GoogleFinanceHandler financeHandler = new GoogleFinanceHandler();
			xmlReader.setContentHandler(financeHandler);
			xmlReader.parse(new InputSource(financeUrl.openStream()));
			return financeHandler.getQuotes().get(0);
		} catch (MalformedURLException e) {
			Log.i(PKG, TAG + ": MalformedURLException caught!");
		} catch (IOException e) {
			Log.i(PKG, TAG + ": IOException caught!");
		} catch (SAXException e) {
			Log.i(PKG, TAG + ": SAXException caught!");
		} catch (ParserConfigurationException e) {
			Log.i(PKG, TAG + ": ParserConfigurationException caught!");
		}
		return null;
	}

	private void refreshFinance() {
		if (mService == null) {
			return;
		}

		String company = "GOOG";
		GoogleFinanceQuote quote = getQuotes(company);

		Map<String, String> financeMap = new HashMap<String, String>();
		groupData.add(financeMap);
		financeMap.put(NAME, "Finance");
		financeMap.put(VALUE, "Finance");
		List<Map<String, String>> finance = new ArrayList<Map<String, String>>();
		Map<String, String> curChildMap = new HashMap<String, String>();
		finance.add(curChildMap);
		curChildMap.put(NAME, "COMPANY_NAME");
		if (quote == null) {
			curChildMap.put(VALUE, "NA");
		} else {
			curChildMap.put(VALUE, quote.getCompany());
		}
		curChildMap = new HashMap<String, String>();
		finance.add(curChildMap);
		curChildMap.put(NAME, "COMPANY_SYMBOL");
		if (quote == null) {
			curChildMap.put(VALUE, "NA");
		} else {
			curChildMap.put(VALUE, quote.getPrettySymbol());
		}
		curChildMap = new HashMap<String, String>();
		finance.add(curChildMap);
		curChildMap.put(NAME, "CURRENCY");
		curChildMap.put(VALUE, quote.getCurrency());
		curChildMap = new HashMap<String, String>();
		finance.add(curChildMap);
		curChildMap.put(NAME, "OPEN_PRICE");
		curChildMap.put(VALUE, String.valueOf(quote.getOpen()));
		curChildMap = new HashMap<String, String>();
		finance.add(curChildMap);
		curChildMap.put(NAME, "CLOSE_PRICE");
		curChildMap.put(VALUE, String.valueOf(quote.getyClose()));
		curChildMap = new HashMap<String, String>();
		finance.add(curChildMap);
		curChildMap.put(NAME, "LOW_PRICE");
		curChildMap.put(VALUE, String.valueOf(quote.getLow()));
		curChildMap = new HashMap<String, String>();
		finance.add(curChildMap);
		curChildMap.put(NAME, "HIGH_PRICE");
		curChildMap.put(VALUE, String.valueOf(quote.getHigh()));
		curChildMap = new HashMap<String, String>();
		finance.add(curChildMap);
		curChildMap.put(NAME, "LAST_PRICE");
		curChildMap.put(VALUE, String.valueOf(quote.getLast()));
		curChildMap = new HashMap<String, String>();
		finance.add(curChildMap);
		curChildMap.put(NAME, "DISCLAIMER");
		curChildMap.put(VALUE, "http://www.google.com" + quote.getDisclaimerUrl());
		childData.add(finance);
	}

	private void refreshDerived() {
		if (mService == null) {
			return;
		}
		Map<String, String> derivedMap = new HashMap<String, String>();
		groupData.add(derivedMap);
		derivedMap.put(NAME, "Derived");
		derivedMap.put(VALUE, "Derived");
		List<Map<String, String>> derived = new ArrayList<Map<String, String>>();
		Map<String, String> curChildMap = new HashMap<String, String>();
		derived.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.DERIVED_HEALTH);
		curChildMap.put(VALUE, "Well");
		curChildMap = new HashMap<String, String>();
		derived.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.DERIVED_MOOD);
		curChildMap.put(VALUE, "Happy");
		curChildMap = new HashMap<String, String>();
		derived.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.DERIVED_POCKET);
		curChildMap.put(VALUE, String.valueOf(LightService.isInPocket()));
		curChildMap = new HashMap<String, String>();
		derived.add(curChildMap);
		curChildMap.put(NAME, ContextConstants.DERIVED_SHELTER);
		curChildMap.put(VALUE, String.valueOf(LocationMonitor.isInside()));

		childData.add(derived);
	}

	@Override
	public void onDestroy() {
		MovementMonitor.StopThread();
		running = false;
		super.onDestroy();
		tts.shutdown();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_REFRESH_ID, Menu.NONE, "Refresh").setIcon(android.R.drawable.ic_menu_rotate).setAlphabeticShortcut('r');
		menu.add(Menu.NONE, MENU_ADD_ID, Menu.NONE, "Add").setIcon(android.R.drawable.ic_menu_add).setAlphabeticShortcut('a');
		menu.add(Menu.NONE, MENU_SHARE_ID, Menu.NONE, "Share").setIcon(android.R.drawable.ic_menu_share).setAlphabeticShortcut('s');
		menu.add(Menu.NONE, MENU_PREFS_ID, Menu.NONE, "Prefs").setIcon(android.R.drawable.ic_menu_preferences).setAlphabeticShortcut('p');
		menu.add(Menu.NONE, MENU_ABOUT_ID, Menu.NONE, "About").setIcon(android.R.drawable.ic_menu_info_details).setAlphabeticShortcut('i');

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_REFRESH_ID:
			refresh();
			// adapter.notifyDataSetChanged();
			return true;
		case MENU_ADD_ID:
			refresh();
			Context context = getApplicationContext();
			String address = null;
			try {
				address = mService.getCurrentAddress();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AddressDialog.add addAddressDialog = new AddressDialog.add(this, address);
			addAddressDialog.show();
			this.refreshProximity();
			return (true);
		case MENU_SHARE_ID:
			try {
				exportToFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// adapter.notifyDataSetChanged();
			return true;
		case MENU_PREFS_ID:
			editPrefs();
			return true;
		case MENU_ABOUT_ID:
			showDialog(DIALOG_ABOUT);
			return true;
		}

		return (super.onOptionsItemSelected(item));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, CONTEXT_DELETE_ID, Menu.NONE, "Delete").setIcon(android.R.drawable.ic_menu_delete).setAlphabeticShortcut('d');
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case CONTEXT_DELETE_ID:
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			// delete(info.id);
			return true;

		}
		return (super.onOptionsItemSelected(item));
	}

	private class ContextListItem {
		private String name;
		private String value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String toString() {
			return name + ": " + value;
		}

	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		String copiedString = null;
		Context context = getApplicationContext();
		List<Map<String, String>> category = childData.get(groupPosition);
		Map<String, String> item = category.get(childPosition);

		String name = item.get(NAME);
		String value = item.get(VALUE);

		boolean wasClicked = copyState.click(groupPosition, childPosition);
		/* copy value on first click */
		if (wasClicked == false) {
			copiedString = value;
			/* copy name on second click */
		} else {
			copiedString = name;
		}

		Toast.makeText(context, "Copied: [" + copiedString + "]", Toast.LENGTH_SHORT).show();
		clip.setText(copiedString);
		Log.i("LIST", "Name [" + name + "] | Value [" + value + "] | id [" + id + "]");

		return true;
	}

	private class CopyState {
		int groupPosition = -1;
		int childPosition = -1;
		boolean wasClicked = false;

		CopyState() {

		}

		void reset() {
			wasClicked = false;
			groupPosition = -1;
			childPosition = -1;
		}

		boolean click(int group, int child) {
			if (groupPosition != group || childPosition != child) {
				wasClicked = true;
				groupPosition = group;
				childPosition = child;
				return false;
			}

			if (wasClicked == true) {
				wasClicked = false;
				return true;
			} else {
				wasClicked = true;
				return false;
			}
		}
	}

	private void editPrefs() {
		Intent i = new Intent(this, PrefsActivity.class);
		startActivityForResult(i, PREFS_EDIT);
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

		return new AlertDialog.Builder(ContextExpandableListActivity.this).setTitle(title).setView(View.inflate(this, R.layout.about, null)).setIcon(R.drawable.context64)
				.setPositiveButton("OK", null).create();

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

	public void exportToFile() throws IOException {
		String path = Environment.getExternalStorageDirectory() + "/" + CSV_FILENAME;

		File file = new File(path);
		file.createNewFile();

		if (!file.isFile()) {
			throw new IllegalArgumentException("Should not be a directory: " + file);
		}
		if (!file.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: " + file);
		}

		Writer output = new BufferedWriter(new FileWriter(file));

		HashMap<String, String> cntx = null;

		String line;
		// ContextProvider.getAll(cntx);

		cntx = ContextProvider.getAllUnordered();
		for (LinkedHashMap.Entry<String, String> entry : cntx.entrySet()) {
			ContextListItem item = new ContextListItem();
			item.setName(entry.getKey());
			item.setValue(entry.getValue());
			// Clist.add(item);
			line = item.toString();
			output.write(line + "\n");
		}

		output.close();

		Toast.makeText(this, String.format("Saved", path), Toast.LENGTH_LONG).show();

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
		shareIntent.setType("text/plain");
		startActivity(Intent.createChooser(shareIntent, "Share Context Using..."));
	}

}
