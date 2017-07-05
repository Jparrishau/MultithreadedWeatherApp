package com.imobile3.taylor.imobile3_weather_app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Created by taylorp on 7/5/2017.
 */

public final class GPS implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static GPS mInstance = new GPS();
    private static Activity mActivity;

    private static boolean mIsGPSEnabled = false;
    private static boolean mIsNetworkEnabled = false;
    private static boolean mCanGetLocation = false;
    private static boolean mIsPermissionEnabled = false;

    private Location mLocation;
    private double mLatitude;
    private double mLongitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

    private static LocationManager mLocationManager;

    private LocationPermissionResponseListener _locationPermissionListener;
    public static final int LOCATION_REQUEST_CODE = 200;

    private GPS() {}

    public static GPS sharedInstance(Activity activity) {
        mActivity = activity;
        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        mIsGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!mIsGPSEnabled && !mIsNetworkEnabled) {
            mCanGetLocation = false;
        } else {
            mCanGetLocation = true;
        }

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mIsPermissionEnabled = false;
        } else {
            mIsPermissionEnabled = true;
        }

        return mInstance;
    }

    public Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mIsPermissionEnabled = false;
        } else {
            if (mCanGetLocation) {
                if (mIsNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (mLocationManager != null) {
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (mLocation != null) {
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                        }
                    }
                }

                if (mIsGPSEnabled) {
                    if (mLocation == null) {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (mLocationManager != null) {
                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (mLocation != null) {
                                mLatitude = mLocation.getLatitude();
                                mLongitude = mLocation.getLongitude();
                            }
                        }
                    }
                }
            }
        }

        return mLocation;
    }

    public void stopUsingGPS() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.removeUpdates(GPS.this);
            }
        }
    }

    public double getLatitude() {
        if (mLocationManager != null) {
            mLatitude = mLocation.getLatitude();
        }

        return mLatitude;
    }

    public double getLongitude() {
        if (mLocationManager != null) {
            mLongitude = mLocation.getLongitude();
        }

        return mLongitude;
    }

    public boolean canGetLocation() {
        return mCanGetLocation;
    }

    public boolean isPermissionEnabled() {
        return mIsPermissionEnabled;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to enable it?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mActivity.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public void requestLocationPermission(LocationPermissionResponseListener listener) {
        _locationPermissionListener = listener;

        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GPS.LOCATION_REQUEST_CODE: {
                _locationPermissionListener.onResponse(grantResults[0] == PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    public static interface LocationPermissionResponseListener {
        public void onResponse(Boolean permissionGranted);
    }

}
