package com.example.app_delivery;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationListener_event implements LocationListener {
    Criteria criteria =new Criteria();
    LocationManager locationManager;
    public LocationListener_event(){
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        try{
          //  locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

            String provider = locationManager.getBestProvider(criteria, true);

            if (!locationManager.isProviderEnabled(provider)&&locationManager.getLastKnownLocation(provider)!=null) {

                locationManager.requestLocationUpdates(

                        provider,

                        1000,

                        10, this);

            } else {

                criteria.setAccuracy(Criteria.ACCURACY_COARSE);

                provider = locationManager.getBestProvider(criteria, true);

                locationManager.requestLocationUpdates(

                        provider,

                        1000,

                        10, this);

            }
        }catch(SecurityException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
