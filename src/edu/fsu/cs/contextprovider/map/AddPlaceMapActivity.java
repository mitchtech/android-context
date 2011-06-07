package edu.fsu.cs.contextprovider.map;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placemap);
		
		// geopoint for overlayitem
		Double latitude = 37.423156 * 1E6;
		Double longuitude = -122.084917 * 1E6;
		GeoPoint point = new GeoPoint(latitude.intValue(), longuitude.intValue());
		
//		// create new overlayitem
//		OverlayItem overlayitem = new OverlayItem(point, "Googleplex", "Google");
//		overlay = new PlaceItemizedOverlay(getResources().getDrawable(R.drawable.location));
//		overlay.addOverlay(overlayitem);
		
		// add overlay to map
		map = (MapView) findViewById(R.id.mapView);
		List<Overlay> mapOverlays = map.getOverlays();
//		mapOverlays.add(overlay);

		
		mapOverlays.add(new PlaceOverlay(this));
		
		// MyLocation manage your position and enable compass
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, map);
		mapOverlays.add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();

		// Mapcontroller set zoom and center
		mapController = map.getController();
		mapController.setCenter(point);
		mapController.setZoom(16);
		map.setBuiltInZoomControls(true);

		// Get the system location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Set the criteria
		criteria = new Criteria();
		// other option Criteria.ACCURACY_COARSE
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
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
			locationManager.requestLocationUpdates(provider, 60000, // every
																	// 1min
					1000, // change locations min 1km
					locationListener);
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
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	};

	private void update(Location location) {
		if (location != null) {
			// Update your current location
			currentLocation = location;
			Double lat = location.getLatitude() * 1E6;
			Double lng = location.getLongitude() * 1E6;
			GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
			mapController.setCenter(point);
			Toast.makeText(this, "My Location:" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_LONG);
			// get some data in other thread
			// RefreshListview
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

		new AlertDialog.Builder(this).setMessage("Add Place?").setIcon(R.drawable.location)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
//						startActivity(startGameIntent);
						setPlace(point);
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing, just let another destination be
						// chosen.
					}
				}).show();		
	}
	
	public void setPlace(FloatingPointGeoPoint point) {
		Toast.makeText(getApplicationContext(), "Place Added\nLat:" + point.getLatitude() + 
				"\nLon:" + point.getLatitude(), Toast.LENGTH_LONG).show();
	}
	

}