package com.aemerse.solyzer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.aemerse.solyzer.activities.LocationActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapViewController constructor(private val mAct: Context) {
    var googleMap: GoogleMap? = null
    private var gps: com.aemerse.solyzer.GPSTracker? = null

    fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(21.1766, 79.0614), 11.0f))
        val controls: UiSettings = googleMap!!.uiSettings
        controls.isZoomControlsEnabled = true
        controls.isCompassEnabled = true
        controls.isMapToolbarEnabled = false
        controls.isMyLocationButtonEnabled = true
        if (ActivityCompat.checkSelfPermission(mAct,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mAct,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) return
        googleMap!!.isMyLocationEnabled = true
        googleMap!!.setOnMyLocationButtonClickListener(object : OnMyLocationButtonClickListener {
            override fun onMyLocationButtonClick(): Boolean {
                gps = com.aemerse.solyzer.GPSTracker(mAct)
                if (gps!!.canGetLocation()) {
                    if (gps!!.getLatitude() != 0.0 && gps!!.getLongitude() != 0.0) {
                        setMarker(googleMap, gps!!.getLatitude(), gps!!.getLongitude())
                    }
                    return true
                } else {
                    gps!!.showSettingsAlert()
                    return false
                }
            }
        })
        googleMap!!.setOnMapClickListener { pt ->
            setMarker(googleMap, pt.latitude, pt.longitude)
            //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }

    fun setMarker(map: GoogleMap?, lat: Double, lng: Double) {
        val options = MarkerOptions()
        map!!.clear()
        val point = LatLng(lat, lng)
        options.position(point)
        map.addMarker(options.position(point))
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 19.0f));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 19.0f))
        (mAct as LocationActivity).setLatitudeLongitude(lat, lng)
    }
}