package edu.fsu.cs.contextprovider.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.fsu.cs.contextprovider.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.text.Html;
import android.widget.Toast;

public class PrefsActivity extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener {

	public static final String VERSION = "versionPreference";

	private static final int DIALOG_CHANGELOG = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);

		Preference helpPref = (Preference) findPreference("helpPreference");
		helpPref.setIntent(new Intent(this, edu.fsu.cs.contextprovider.shared.HelpActivity.class));

		Preference changelogPref = (Preference) findPreference("changelogPreference");
		changelogPref.setOnPreferenceClickListener(this);

		Preference contactDeveloperPref = (Preference) findPreference("contactDeveloperPreference");
		contactDeveloperPref.setOnPreferenceClickListener(this);

		Preference aboutPref = (Preference) findPreference("aboutPreference");
		aboutPref.setIntent(new Intent(this, edu.fsu.cs.contextprovider.shared.AboutActivity.class));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public boolean onPreferenceClick(Preference preference) {

		if (preference.getKey().equals("changelogPreference")) {
			showDialog(DIALOG_CHANGELOG);
		}

		else if (preference.getKey().equals("contactDeveloperPreference")) {
			final Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:mikmitch1@gmail.com"));
			try {
				startActivity(emailIntent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, R.string.error_email, Toast.LENGTH_LONG).show();
			}
		}

		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CHANGELOG:
			StringBuilder changelog = new StringBuilder();
			BufferedReader input;
			try {
				InputStream is = getResources().openRawResource(R.raw.changelog);
				input = new BufferedReader(new InputStreamReader(is));
				String line;
				while ((line = input.readLine()) != null) {
					changelog.append(line);
					changelog.append("<br/>");
				}
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return new AlertDialog.Builder(this).setTitle(R.string.changelog_title).setMessage(Html.fromHtml(changelog.toString())).setPositiveButton(R.string.close, null).create();
		}
		return null;
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		return false;
	}

}