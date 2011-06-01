package edu.fsu.cs.contextprovider.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.fsu.cs.contextprovider.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SharedListActivity extends ListActivity {

	private static final String TAG = "net.mitchtech.shared.SharedListActivity";
	
	private static final int MENU_PREFS = Menu.FIRST;
	private static final int MENU_ABOUT = Menu.FIRST + 1;
	private static final int MENU_HELP = Menu.FIRST + 2;
	private static final int MENU_SHARE = Menu.FIRST + 3;	
	private static final int MENU_CONTACT = Menu.FIRST + 4;
	private static final int MENU_CHANGE_LOG = Menu.FIRST + 5;	
	private static final int MENU_EXIT = Menu.FIRST + 6;
	
	private static final String MENU_TEXT_PREFS = "Prefs";
	private static final String MENU_TEXT_ABOUT = "About";
	private static final String MENU_TEXT_HELP = "Help";	
	private static final String MENU_TEXT_CONTACT = "Contact";
	private static final String MENU_TEXT_SHARE = "Share";
	private static final String MENU_TEXT_CHANGE_LOG = "Change Log";
	private static final String MENU_TEXT_EXIT = "Exit";
	
	//private static final int DIALOG_CHANGE_LOG = 10;
	private static final int DIALOG_FIRST_TIME = 20;
	private static final int DIALOG_CHANGELOG = 21;
	

	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, MENU_PREFS, 0, MENU_TEXT_PREFS).setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(0, MENU_ABOUT, 0, MENU_TEXT_ABOUT).setIcon(android.R.drawable.ic_menu_info_details);
		menu.add(0, MENU_HELP, 0, MENU_TEXT_HELP).setIcon(android.R.drawable.ic_menu_help);
		menu.add(0, MENU_SHARE, 0, MENU_TEXT_SHARE).setIcon(android.R.drawable.ic_menu_share);
		menu.add(0, MENU_CONTACT, 0, MENU_TEXT_CONTACT).setIcon(android.R.drawable.ic_menu_send);
		menu.add(0, MENU_CHANGE_LOG, 0, MENU_TEXT_CHANGE_LOG).setIcon(android.R.drawable.ic_menu_agenda);
		menu.add(0, MENU_EXIT, 0, MENU_TEXT_EXIT).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PREFS:
			Intent preferencesIntent = new Intent(this, edu.fsu.cs.contextprovider.shared.PrefsActivity.class);
			startActivity(preferencesIntent);
			return true;
			
		case MENU_ABOUT:
			Intent aboutIntent = new Intent(this, edu.fsu.cs.contextprovider.shared.AboutActivity.class);
			startActivity(aboutIntent);
			return true;
			
		case MENU_HELP:
			Intent helpIntent = new Intent(this, edu.fsu.cs.contextprovider.shared.HelpActivity.class);
			startActivity(helpIntent);
			return true;
			
		case MENU_SHARE:
			Intent shareIntent = new Intent(this, edu.fsu.cs.contextprovider.shared.HelpActivity.class);
			startActivity(shareIntent);
			return true;
			
		case MENU_CONTACT:		
			Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:mikmitch1@gmail.com"));
			try {
				startActivity(emailIntent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, R.string.error_email, Toast.LENGTH_LONG).show();
			}
			
			return true;
			
		case MENU_CHANGE_LOG:
			showDialog(DIALOG_CHANGELOG);
			return true;
			
		case MENU_EXIT:
			this.finish();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
//		case DIALOG_CHANGE_LOG:
//			dialog = getAboutBox();
//			break;
			
		case DIALOG_FIRST_TIME:
			dialog = new AlertDialog.Builder(this).setTitle(R.string.welcome).setMessage(R.string.welcome_msg).setPositiveButton(R.string.close, null).create();
			break;
			
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

			dialog = new AlertDialog.Builder(this).setTitle(R.string.changelog_title).setMessage(Html.fromHtml(changelog.toString())).setPositiveButton(R.string.close, null).create();
			break;
			
		default:
			dialog = null;
		}
		return dialog;
	}

//	private AlertDialog getAboutBox() {
//		String title = getString(R.string.app_name) + " build " + getVersion(this);
//		return new AlertDialog.Builder(this).setTitle(title).setView(View.inflate(this, R.layout.about, null)).setIcon(R.drawable.icon).setPositiveButton("OK", null).create();
//	}
//	
//	
//	public static String getVersion(Context context) {
//		String version = "1.0";
//		try {
//			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//			version = pi.versionName;
//		} catch (PackageManager.NameNotFoundException e) {
//			Log.e(TAG, "Package name not found", e);
//		}
//		return version;
//	}


}
