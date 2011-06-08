package edu.fsu.cs.contextprovider.map;

import android.os.Bundle;


public class Place {

  private final FloatingPointGeoPoint location;
  
//  public static final String DESTINATION_BUNDLE_KEY = "";
  
  public Place(FloatingPointGeoPoint location) {
    this.location = location;
  }
  
  public FloatingPointGeoPoint getLocation() {
    return location;
  }
  
//  public String toString() {
//    // TODO: remove this method once we're sure it's not being used.
////    throw new NotImplementedException();
//  }
  
  public static Place fromString(String string) {
    FloatingPointGeoPoint fpgp =
        FloatingPointGeoPoint.fromString(string);
    return fpgp == null ? null : new Place(fpgp);
  }
  
//  public static Place fromBundle(Bundle bundle) {
//    if (!bundle.containsKey(DESTINATION_BUNDLE_KEY)) {
//      return null;
//    }
//    FloatingPointGeoPoint fpgp =
//        FloatingPointGeoPoint.fromString(bundle.getString(DESTINATION_BUNDLE_KEY));
//    return fpgp == null ? null : new Place(fpgp);
//  }
//
//  public void toBundle(Bundle bundle) {
//    bundle.putString(DESTINATION_BUNDLE_KEY, location.toString());
//  }
}
