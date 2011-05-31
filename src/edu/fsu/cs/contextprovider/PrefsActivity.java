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
	}


//	private void enabled(final boolean enable) {
//		String dialogTitle;
//		if (enable) {
//			dialogTitle = getString(R.string.enable_dialog_msg);
//		} else {
//			dialogTitle = getString(R.string.disable_dialog_msg);
//		}
//		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(dialogTitle).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				// enable monitor here
//			}
//		}).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				// Do nothing
//			}
//		}).show();
//	}

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

}