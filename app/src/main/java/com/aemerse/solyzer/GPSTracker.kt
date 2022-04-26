package com.aemerse.solyzer

import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings

class GPSTracker constructor(private val mContext: Context) : Service(), LocationListener {
    var isGPSEnabled: Boolean = false
    var isNetworkEnabled: Boolean = false
    var canGetLocation: Boolean = false
    @JvmField
    var location: Location? = null
    @JvmField
    var latitude: Double = 0.0
    @JvmField
    var longitude: Double = 0.0
    var locationManager: LocationManager? = null

    @Throws(SecurityException::class)
    fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager?
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGPSEnabled || isNetworkEnabled) {
                canGetLocation = true
                if (isNetworkEnabled) locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    60000,
                    10f,
                    this)
                if (locationManager != null) location =
                    locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (isGPSEnabled) if (location == null) {
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        60000,
                        10f,
                        this)
                    if (locationManager != null) location =
                        locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
                if (location != null) {
                    latitude = location!!.getLatitude()
                    longitude = location!!.getLongitude()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location
    }

    fun getLatitude(): Double {
        if (location != null) latitude = location!!.getLatitude()
        return latitude
    }

    fun getLongitude(): Double {
        if (location != null) longitude = location!!.getLongitude()
        return longitude
    }

    fun canGetLocation(): Boolean {
        return canGetLocation
    }

    fun showSettingsAlert() {
        AlertDialog.Builder(mContext)
            .setTitle("GPS Disabled")
            .setMessage("Do you want to enable GPS?")
            .setPositiveButton("Settings") { dialog, which ->
                mContext.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            .show()
    }

    override fun onLocationChanged(location: Location) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    init {
        getLocation()
    }
}