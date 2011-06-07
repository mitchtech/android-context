package edu.fsu.cs.contextprovider.map;

import com.google.android.maps.GeoPoint;

public class FloatingPointGeoPoint {

  private static final long serialVersionUID = -3659066238832850779L;

  private double latitude;
  private double longitude;

  public FloatingPointGeoPoint(GeoPoint point) {
    this.latitude = point.getLatitudeE6() / 1E6f;
    this.longitude = point.getLongitudeE6() / 1E6f;
  }
  
  public FloatingPointGeoPoint(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
  
  public double getLatitude() {
    return latitude;
  }
  
  public double getLongitude() {
    return longitude;
  }
    
  public void setLatitude(double latitude) {
	this.latitude = latitude;
}

public void setLongitude(double longitude) {
	this.longitude = longitude;
}

public GeoPoint getGeoPoint() {
    return new GeoPoint((int) Math.round(getLatitude() * 1E6),
        (int) Math.round(getLongitude() * 1E6));
  }
  
  /**
   * Deprecated in favor of {@link #toString(double, double)}.
   */
  @Deprecated
  public String toString() {
    return toString(latitude, longitude);
  }
  
  public static String toString(double lat, double lon) {
    StringBuilder builder = new StringBuilder();
    builder.append(lat);
    builder.append("x");
    builder.append(lon);
    return builder.toString();
  }
  
  public static FloatingPointGeoPoint fromString(String stringRep) {
    String[] parts = stringRep.split("x");
    if (parts.length != 2) {
      return null;
    }
    try {
      Double latitude = Double.parseDouble(parts[0]);
      Double longitude = Double.parseDouble(parts[1]);
      return new FloatingPointGeoPoint(latitude, longitude);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
