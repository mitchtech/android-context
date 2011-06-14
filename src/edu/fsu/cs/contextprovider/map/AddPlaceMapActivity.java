package edu.fsu.cs.contextprovider.map;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.fsu.cs.contextprovider.R;
import edu.fsu.cs.contextprovider.R.drawable;
import edu.fsu.cs.contextprovider.R.id;
import edu.fsu.cs.contextprovider.R.layout;
import edu.fsu.cs.contextprovider.data.ContextConstants;
import edu.fsu.cs.contextprovider.monitor.DerivedMonitor;

public class AddPlaceMapActivity extends MapActivity {
	private MyLocationOverlay myLocationOverlay;
	protected MapView map;
	private MapController mapController;
	private Criteria criteria;
	private LocationManager locationManager;
	private PlaceItemizedOverlay overlay;
	private Location currentLocation;
	protected final AtomicReference<Place> destinationReference = new AtomicReference<Place>();
	protected Drawable destinationDrawable;
	protected Drawable placeDrawable;
	
	private boolean DEBUG = true;

	private int REQUEST_ID = -1;
	
	SharedPreferences prefs;
	private double placeLat;
	private double placeLon;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placemap);
		Intent intent = getIntent();
		REQUEST_ID = intent.getIntExtra(ContextConstants.PLACE_REQUEST_ID, -1);

		getPrefs();
		
		map = (MapView) findViewById(R.id.mapView);
		createOverlay();

		// Mapcontroller set zoom and center
		mapController = map.getController();
		// mapController.setCenter(point);
		mapController.setZoom(16);
		map.setBuiltInZoomControls(true);

		// Get the system location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Set the criteria
		criteria = new Criteria();
//		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		// other options Criteria.POWER_HIGH Criteria.POWER_MEDIUM
		criteria.setPowerRequirement(Criteria.POWER_LOW);
	}

	@Override
	public void onStart() {
		super.onStart();

		// best provider for this criteria
		String provider = locationManager.getBestProvider(criteria, true);
		if (provider != null) {

			// Update with last know location
			Location location = locationManager.getLastKnownLocation(provider);
			update(location);
			// Start listening for location changes
			locationManager.requestLocationUpdates(provider, 60000, /* every 1min */ 1000, /* min 1km */locationListener);
		}
	}

	public void onStop() {
		// Stop listening for location changes
		locationManager.removeUpdates(locationListener);

		super.onStop();
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			update(location);
		}
		public void onProviderDisabled(String provider) {
			update(null);
		}
		public void onProviderEnabled(String provider) {
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	
	public void createOverlay() {
		List<Overlay> mapOverlays = map.getOverlays();
		
		switch (REQUEST_ID) {
		case ContextConstants.SET_HOME_REQUEST:
			if (DerivedMonitor.Home != null) {			
				OverlayItem overlayitem = new OverlayItem(DerivedMonitor.Home.getGeoPoint(), "Home", "Home");
				overlay = new PlaceItemizedOverlay(getResources().getDrawable(R.drawable.home));
				overlay.addOverlay(overlayitem);
				mapOverlays.add(overlay);
			}
			break;
		case ContextConstants.SET_WORK_REQUEST:
			if (DerivedMonitor.Work != null) {
				OverlayItem overlayitem = new OverlayItem(DerivedMonitor.Work.getGeoPoint(), "Work", "Work");
				overlay = new PlaceItemizedOverlay(getResources().getDrawable(R.drawable.work));
				overlay.addOverlay(overlayitem);
				mapOverlays.add(overlay);
			}
			break;
		default:
			break;
		}

		mapOverlays.add(new PlaceOverlay(this));

		// MyLocation manage your position and enable compass
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, map);
		mapOverlays.add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();
	}
	
	
	private void getPrefs() {
		prefs = getSharedPreferences(ContextConstants.CONTEXT_PREFS, MODE_PRIVATE);
		
		switch (REQUEST_ID) {
		case ContextConstants.SET_HOME_REQUEST:
			
			break;
		case ContextConstants.SET_WORK_REQUEST:
			if (DerivedMonitor.Work != null) {
				
			}
			break;
		default:
			break;
		}
		
		String placeString = prefs.getString(ContextConstants.HOME_COORDINATES, "");		
		if (DEBUG) {
//			Log.d(TAG, "accuracyDismissDelay: " + accuracyDismissDelay + "  dismissDelay: " + dismissDelay);
		}
	}
	
	
//	public void createOverlay(){
//		mapOverlays=mapView.getOverlays();
//		OverlayItem item; 
//		drawable = this.getResources().getDrawable(R.drawable.icon_map);
//		MixOverlay mixOverlay = new MixOverlay(this, drawable);
//
//		for(Marker marker:markerList) {
//			if(marker.isActive()) {
//				GeoPoint point = new GeoPoint((int)(marker.getLatitude()*1E6), (int)(marker.getLongitude()*1E6));
//				item = new OverlayItem(point, "", "");
//				mixOverlay.addOverlay(item);
//			}
//		}
//		//Solved issue 39: only one overlay with all marker instead of one overlay for each marker
//		mapOverlays.add(mixOverlay);
//
//		MixOverlay myOverlay;
//		drawable = this.getResources().getDrawable(R.drawable.loc_icon);
//		myOverlay = new MixOverlay(this, drawable);
//
//		item = new OverlayItem(startPoint, "Your Position", "");
//		myOverlay.addOverlay(item);
//		mapOverlays.add(myOverlay); 
//	}
	
	
	

	private void update(Location location) {
		if (location != null) {
			// Update your current location
			currentLocation = location;
			Double lat = location.getLatitude() * 1E6;
			Double lng = location.getLongitude() * 1E6;
			GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
			mapController.setCenter(point);
			mapController.setZoom(15);
			Toast.makeText(this, "My Location:" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_LONG);
		}
	}	
	
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void placeSelected(Place place) {
		destinationReference.set(place);
		final FloatingPointGeoPoint point = place.getLocation();

		// List<Overlay> mapOverlays = map.getOverlays();
		// mapOverlays.add(new PlaceOverlay(this));
		// OverlayItem overlayitem = new OverlayItem(point.getGeoPoint(),
		// "Home", "Home");
		// overlay = new
		// PlaceItemizedOverlay(getResources().getDrawable(R.drawable.home));
		// overlay.addOverlay(overlayitem);

		new AlertDialog.Builder(this).setMessage("Add Place?").setIcon(R.drawable.location).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				switch (REQUEST_ID) {
				case ContextConstants.SET_HOME_REQUEST:
					DerivedMonitor.Home = new FloatingPointGeoPoint(point.getLatitude(), point.getLongitude());
					Toast.makeText(getApplicationContext(),
							"Home Updated\nLat:" + point.getLatitude() + "\nLon:" + point.getLongitude() + "\n Press Back to Exit", Toast.LENGTH_LONG).show();
					break;
				case ContextConstants.SET_WORK_REQUEST:

					DerivedMonitor.Work = new FloatingPointGeoPoint(point.getLatitude(), point.getLongitude());
					Toast.makeText(getApplicationContext(),
							"Work Updated\nLat:" + point.getLatitude() + "\nLon:" + point.getLongitude() + "\n Press Back to Exit", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
				
				map.invalidate();
				createOverlay();

			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing, just let another destination be chosen.
			}
		}).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int base = Menu.FIRST;
		/* define the first */
		MenuItem item1 = menu.add(base, base, base, "Map");
		MenuItem item2 = menu.add(base, base + 1, base + 1, "Satellite");
		MenuItem item3 = menu.add(base, base + 2, base + 2, "My Location");
		MenuItem item4 = menu.add(base, base + 3, base + 3, "Return");

		/* assign icons to the menu items */
		item1.setIcon(android.R.drawable.ic_menu_gallery);
		item2.setIcon(android.R.drawable.ic_menu_mapmode);
		item3.setIcon(android.R.drawable.ic_menu_mylocation);
		item4.setIcon(android.R.drawable.ic_menu_close_clear_cancel);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/* Satellite View */
		case 1:
			map.setSatellite(false);
			break;
		/* street View */
		case 2:
			map.setSatellite(true);
			break;
		/* go to users location */
		case 3:
			if (currentLocation != null) { 
				update(currentLocation);
			}
			break;
		/* List View */
		case 4:
			finish();
			break;
		}
		return true;
	}

}