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

import android.content.*;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.fsu.cs.contextprovider.monitor.LocationMonitor;
import edu.fsu.cs.contextprovider.monitor.MovementMonitor;
import edu.fsu.cs.contextprovider.sensor.AccelerometerService;

public class ContextProvider extends ContentProvider {
	private static final String DATABASE_NAME = "cntxt.db";
	private static final int CNTXT = 1;
	private static final int CNTXT_ID = 2;
	private static final UriMatcher MATCHER;
	private static HashMap<String, String> CNTXT_LIST_PROJECTION;

	private static void getAll(Map<String, String> results) {
		getLocation(results);
		getMovement(results);

	
	}
	
	private static void getLocation(Map<String, String> results) {
		results.put("LOCATION_ADDRESS", LocationMonitor.getAddress());
		results.put("LOCATION_HOOD", LocationMonitor.getNeighborhood());
		results.put("LOCATION_ZIP", LocationMonitor.getZip());		
		results.put("LOCATION_LATITUDE", String.valueOf(LocationMonitor.getLatitude()));
		results.put("LOCATION_LONGITUDE", String.valueOf(LocationMonitor.getLongitude()));
		results.put("LOCATION_ALTITUDE", String.valueOf(LocationMonitor.getAltitude()));
	}	
	
	private static void getMovement(Map<String, String> results) {
		results.put("MOVEMENT_STATE", MovementMonitor.getMovementState());
		results.put("MOVEMENT_SPEED", String.valueOf(MovementMonitor.getSpeedMph()));
		results.put("MOVEMENT_BEARING", String.valueOf(LocationMonitor.getBearing()));
		results.put("MOVEMENT_STEP_COUNT", String.valueOf(AccelerometerService.getStepCount()));
		results.put("MOVEMENT_LAST_STEP", String.valueOf(AccelerometerService.getStepTimestamp()));
	}
	
	private static void getFinance(Map<String, String> results) {
		results.put("COMPANY NAME", "GOOGLE");
		results.put("COMPANY SYMBOL", "GOOG");
	}
	
	
	
	
	public static LinkedHashMap<String, String> getAllOrdered() {
		LinkedHashMap<String, String> results = new LinkedHashMap<String, String>();
		getAll(results);
		return results;
	}
	
	public static HashMap<String, String> getAllUnordered() {
		HashMap<String, String> results = new HashMap<String, String>();
		getAll(results);
		return results;
	}
	
	public static final class Cntxt implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://edu.fsu.cs.contextprovider/cntxt");
		public static final String DEFAULT_SORT_ORDER = "title";
		public static final String TITLE = "title";
		public static final String VALUE = "value";
	}

	static {
		MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		MATCHER.addURI("edu.fsu.cs.contextprovider", "cntxt", CNTXT);
		MATCHER.addURI("edu.fsu.cs.contextprovider", "cntxt/#", CNTXT_ID);

		CNTXT_LIST_PROJECTION = new HashMap<String, String>();

		CNTXT_LIST_PROJECTION.put(ContextProvider.Cntxt._ID,
				ContextProvider.Cntxt._ID);
		CNTXT_LIST_PROJECTION.put(ContextProvider.Cntxt.TITLE,
				ContextProvider.Cntxt.TITLE);
		CNTXT_LIST_PROJECTION.put(ContextProvider.Cntxt.VALUE,
				ContextProvider.Cntxt.VALUE);
	}

	public String getDbName() {
		return (DATABASE_NAME);
	}

	public int getDbVersion() {
		return (1);
	}

	private class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(android.content.Context context) {
			super(context, DATABASE_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Cursor c = db
					.rawQuery(
							"SELECT name FROM sqlite_master WHERE type='table' AND name='cntxt'",
							null);

			try {
				if (c.getCount() == 0) {
					db.execSQL("CREATE TABLE cntxt (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, value REAL);");

					ContentValues cv = new ContentValues();

					cv.put(Cntxt.TITLE, "Gravity, Death Star I");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_DEATH_STAR_I);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Earth");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_EARTH);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Jupiter");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_JUPITER);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Mars");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_MARS);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Mercury");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_MERCURY);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Moon");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_MOON);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Neptune");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_NEPTUNE);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Pluto");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_PLUTO);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Saturn");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_SATURN);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Sun");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_SUN);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, The Island");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_THE_ISLAND);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Uranus");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_URANUS);
					db.insert("cntxt", getNullColumnHack(), cv);

					cv.put(Cntxt.TITLE, "Gravity, Venus");
					cv.put(Cntxt.VALUE, SensorManager.GRAVITY_VENUS);
					db.insert("cntxt", getNullColumnHack(), cv);
				}
			} finally {
				c.close();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			android.util.Log.w("Cntxt",
					"Upgrading database, which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS constants");
			onCreate(db);
		}
	}

	private SQLiteDatabase db;

	@Override
	public boolean onCreate() {
		db = (new DatabaseHelper(getContext())).getWritableDatabase();

		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(getTableName());

		if (isCollectionUri(url)) {
			qb.setProjectionMap(getDefaultProjection());
		} else {
			qb.appendWhere(getIdColumnName() + "="
					+ url.getPathSegments().get(1));
		}

		String orderBy;

		if (TextUtils.isEmpty(sort)) {
			orderBy = getDefaultSortOrder();
		} else {
			orderBy = sort;
		}

		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	@Override
	public String getType(Uri url) {
		if (isCollectionUri(url)) {
			return (getCollectionType());
		}

		return (getSingleType());
	}

	@Override
	public Uri insert(Uri url, ContentValues initialValues) {
		long rowID;
		ContentValues values;

		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		if (!isCollectionUri(url)) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		for (String colName : getRequiredColumns()) {
			if (values.containsKey(colName) == false) {
				throw new IllegalArgumentException("Missing column: " + colName);
			}
		}

		populateDefaultValues(values);

		rowID = db.insert(getTableName(), getNullColumnHack(), values);
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(getContentUri(), rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}

		throw new SQLException("Failed to insert row into " + url);
	}

	@Override
	public int delete(Uri url, String where, String[] whereArgs) {
		int count;
		long rowId = 0;

		if (isCollectionUri(url)) {
			count = db.delete(getTableName(), where, whereArgs);
		} else {
			String segment = url.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.delete(
					getTableName(),
					getIdColumnName()
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
		}

		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	@Override
	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		int count;

		if (isCollectionUri(url)) {
			count = db.update(getTableName(), values, where, whereArgs);
		} else {
			String segment = url.getPathSegments().get(1);
			count = db.update(
					getTableName(),
					values,
					getIdColumnName()
							+ "="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
		}

		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	private boolean isCollectionUri(Uri url) {
		return (MATCHER.match(url) == CNTXT);
	}

	private HashMap<String, String> getDefaultProjection() {
		return (CNTXT_LIST_PROJECTION);
	}

	private String getTableName() {
		return ("cntxt");
	}

	private String getIdColumnName() {
		return ("_id");
	}

	private String getDefaultSortOrder() {
		return ("title");
	}

	private String getCollectionType() {
		return ("vnd.android.cursor.dir/vnd.contextprovider.cntxt");
	}

	private String getSingleType() {
		return ("vnd.android.cursor.item/vnd.contextprovider.cntxt");
	}

	private String[] getRequiredColumns() {
		return (new String[] { "title" });
	}

	private void populateDefaultValues(ContentValues values) {
//		Long now = Long.valueOf(System.currentTimeMillis());
//		Resources r = Resources.getSystem();

		if (values.containsKey(ContextProvider.Cntxt.VALUE) == false) {
			values.put(ContextProvider.Cntxt.VALUE, 0.0f);
		}
	}

	private String getNullColumnHack() {
		return ("title");
	}

	private Uri getContentUri() {
		return (ContextProvider.Cntxt.CONTENT_URI);
	}
}