package edu.fsu.cs.contextprovider.map;

public class GeoCalculationUtil {

  public static double milesPerHourToMetersPerSecond(double milesPerHour) {
    return 0.44704 * milesPerHour;
  }
  
  public static double itemPerDistanceToItemsPerSquareKilometer(double distanceMeters) {
    return (1000 * 1000) / (distanceMeters * distanceMeters);
  }
}
