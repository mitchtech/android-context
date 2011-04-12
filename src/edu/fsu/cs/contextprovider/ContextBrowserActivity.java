/* Copyright (c) 2008-2011 -- CommonsWare, LLC

	 Licensed under the Apache License, Version 2.0 (the "License");
	 you may not use this file except in compliance with the License.
	 You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

	 Unless required by applicable law or agreed to in writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 See the License for the specific language governing permissions and
	 limitations under the License.
 */

package edu.fsu.cs.contextprovider;

import edu.fsu.cs.contextprovider.services.GPSService;
import edu.fsu.cs.contextprovider.threads.Movement;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import edu.fsu.cs.contextprovider.R;


public class ContextBrowserActivity extends ListActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "ContextBrowserActivity";
	private static final int ADD_ID = Menu.FIRST + 1;
	private static final int EDIT_ID = Menu.FIRST + 2;
	private static final int DELETE_ID = Menu.FIRST + 3;
	private static final int GEO_ID = Menu.FIRST + 4;
	private static final String[] PROJECTION = new String[] {
		ContextProvider.Cntxt._ID, ContextProvider.Cntxt.TITLE,
		ContextProvider.Cntxt.VALUE };
	private Cursor contextCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Start GPS Service */
		Intent intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.services.GPSService.class);
		startService(intent);
		
		/* Start Accelerometer Service */
		intent = new Intent(this.getApplicationContext(), edu.fsu.cs.contextprovider.services.AccelService.class);
		startService(intent);

		/* Start movement context */
		Movement.StartThread(5);

		contextCursor = managedQuery(ContextProvider.Cntxt.CONTENT_URI,
				PROJECTION, null, null, null);

		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.row,
				contextCursor, new String[] { ContextProvider.Cntxt.TITLE,
				ContextProvider.Cntxt.VALUE }, new int[] {
				R.id.title, R.id.value });

		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}

	@Override
	public void onDestroy() {
		Movement.StopThread();
		super.onDestroy();
		contextCursor.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ADD_ID, Menu.NONE, "Add").setIcon(R.drawable.add)
		.setAlphabeticShortcut('a');
		menu.add(Menu.NONE, GEO_ID, Menu.NONE, "Geo Loc").setIcon(R.drawable.add)
		.setAlphabeticShortcut('a');

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ADD_ID:
			add();
			return (true);
		case GEO_ID:
			Toast.makeText(getApplicationContext(), "Trying to get geolocation", Toast.LENGTH_SHORT).show();
			String res = GPSService.getZip();
			Toast.makeText(getApplicationContext(), "GeoLocation: " + res, Toast.LENGTH_LONG).show();
			return true;
		}

		return (super.onOptionsItemSelected(item));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, DELETE_ID, Menu.NONE, "Delete")
		.setIcon(R.drawable.delete).setAlphabeticShortcut('d');
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
			.getMenuInfo();

			delete(info.id);
			return true;

		}
		return (super.onOptionsItemSelected(item));
	}

	private void add() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View addView = inflater.inflate(R.layout.add_edit, null);
		final DialogWrapper wrapper = new DialogWrapper(addView);

		new AlertDialog.Builder(this)
		.setTitle(R.string.add_context)
		.setView(addView)
		.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				processAdd(wrapper);
			}
		})
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				// ignore, just dismiss
			}
		}).show();
	}

	private void delete(final long rowId) {
		if (rowId > 0) {
			new AlertDialog.Builder(this)
			.setTitle(R.string.delete_context)
			.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {
					processDelete(rowId);
				}
			})
			.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {
					// ignore, just dismiss
				}
			}).show();
		}
	}

	private void processAdd(DialogWrapper wrapper) {
		ContentValues values = new ContentValues(2);

		values.put(ContextProvider.Cntxt.TITLE, wrapper.getTitle());
		values.put(ContextProvider.Cntxt.VALUE, wrapper.getValue());

		getContentResolver()
		.insert(ContextProvider.Cntxt.CONTENT_URI, values);
		contextCursor.requery();
	}

	private void processDelete(long rowId) {
		Uri uri = ContentUris.withAppendedId(
				ContextProvider.Cntxt.CONTENT_URI, rowId);
		getContentResolver().delete(uri, null, null);
		contextCursor.requery();
	}

	class DialogWrapper {
		EditText titleField = null;
		EditText valueField = null;
		View base = null;

		DialogWrapper(View base) {
			this.base = base;
			valueField = (EditText) base.findViewById(R.id.value);
		}

		String getTitle() {
			return (getTitleField().getText().toString());
		}

		float getValue() {
			return (new Float(getValueField().getText().toString())
			.floatValue());
		}

		private EditText getTitleField() {
			if (titleField == null) {
				titleField = (EditText) base.findViewById(R.id.title);
			}

			return (titleField);
		}

		private EditText getValueField() {
			if (valueField == null) {
				valueField = (EditText) base.findViewById(R.id.value);
			}

			return (valueField);
		}
	}
}