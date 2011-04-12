/*******************************************************************************
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package edu.fsu.cs.contextprovider.monitor;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


/**
 * The class is responsible for communication with the Location Service. It provides access to
 * Location Services External Attribute, as well as Initiates Location Change Intents.
 */
public class LocationMonitor implements SystemServiceEventMonitor {
  private static final String SYSTEM_SERVICE_NAME = "LOCATION_SERVICE";
  private static final String MONITOR_NAME = "LocationMonitor";
  private static Location lastLocation;
  /** Minimum frequency in updates(in milliseconds). Default value is 300000 (5 minutes). */
  private static final long MIN_PROVIDER_UPDATE_INTERVAL = 300000;
  /** Minimum change in location(in meters). Default value is 50 meters. */
  private static final float MIN_PROVIDER_UPDATE_DISTANCE = 50;
  private static final String PROVIDER = LocationManager.GPS_PROVIDER;

  private Context context;
  
  public LocationMonitor(Context context) {
    this.context = context;
  }
  
  public void init() {
    lastLocation = null;
    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    if (lm == null) {
      Log.i("LocationService", "Could not obtain LOCATION_SERVICE from the system.");
      return;
    }
    lm.requestLocationUpdates(PROVIDER, MIN_PROVIDER_UPDATE_INTERVAL, MIN_PROVIDER_UPDATE_DISTANCE,
        locationListener);
  }

  public void stop() {
    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    lm.removeUpdates(locationListener);
  }

  private final LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {

    	if (location != null && lastLocation != location) {
        
        String temp = location.toString();
        
        
      }
    }

    /** Required to implement. Do nothing. */
    public void onProviderDisabled(String provider) {
    }

    /** Required to implement. Do nothing. */
    public void onProviderEnabled(String provider) {
    }

    /** Required to implement. Do nothing. */
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
  };

  public String getMonitorName() {
    return MONITOR_NAME;
  }

  public String getSystemServiceName() {
    return SYSTEM_SERVICE_NAME;
  }
}
