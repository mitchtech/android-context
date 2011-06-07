package edu.fsu.cs.contextprovider.map;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class PlaceOverlay extends Overlay {
	private final AddPlaceMapActivity activity;

	public PlaceOverlay(AddPlaceMapActivity placeMapActivity) {
		this.activity = placeMapActivity;
	}

	@Override
	public boolean onTap(GeoPoint point, MapView map) {
		super.onTap(point, map);
		activity.placeSelected(new Place(new FloatingPointGeoPoint(point)));
		return true;
	}
}
