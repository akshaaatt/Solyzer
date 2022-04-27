package com.aemerse.solyzer.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aemerse.solyzer.MapViewController
import com.aemerse.solyzer.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback

class LocationActivity : AppCompatActivity(), OnMapReadyCallback,
    View.OnFocusChangeListener {
    var next: Button? = null
    var mvc: MapViewController? = null
    var latt: EditText? = null
    var longg: EditText? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        supportActionBar?.title = "Solyzer"
        mvc = MapViewController(this)
        val mapFragment: MapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
        assignIDS()
    }

    private fun assignIDS() {
        next = findViewById<View>(R.id.calculate) as Button?
        next!!.setOnClickListener {
            if (validateLatLng()) {
                val intent = Intent(applicationContext, InputActivity::class.java)
                val b = Bundle()
                b.putString("LAT", latt!!.text.toString())
                b.putString("LONG", longg!!.text.toString())
                intent.putExtras(b)
                startActivity(intent)
                //finish();
            } else Toast.makeText(applicationContext,
                "Invalid Latitude or Longitude",
                Toast.LENGTH_LONG).show()
        }
        latt = findViewById<View>(R.id.latt) as EditText?
        latt!!.onFocusChangeListener = this
        longg = findViewById<View>(R.id.longg) as EditText?
        longg!!.onFocusChangeListener = this
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mvc!!.onMapReady(googleMap)
    }

    fun setLatitudeLongitude(lat: Double, lng: Double) {
        latt!!.setText(lat.toString())
        longg!!.setText(lng.toString())
    }

    override fun onFocusChange(view: View, b: Boolean) {
        if (validateLatLng()) {
            mvc!!.setMarker(mvc!!.googleMap,
                latt!!.text.toString().toDouble(),
                longg!!.text.toString().toDouble())
            return
        }
        if (!latt!!.text.toString().isEmpty() && !longg!!.text.toString().isEmpty()) {
            if (view.id == latt!!.id) {
                Toast.makeText(this@LocationActivity, "Invalid Latitude", Toast.LENGTH_SHORT).show()
                latt!!.setText("")
            }
            if (view.id == longg!!.id) {
                Toast.makeText(this@LocationActivity, "Invalid Longitude", Toast.LENGTH_SHORT)
                    .show()
                longg!!.setText("")
            }
        }
    }

    fun validateLatLng(): Boolean {
        val la: Double
        val lo: Double
        val laa: String = latt!!.text.toString()
        val loo: String = longg!!.text.toString()
        if (laa.isNotEmpty() && loo.isNotEmpty()) {
            la = laa.toDouble()
            lo = loo.toDouble()
            if ((la >= -85.0) && (la <= 85.0) && (lo >= -180) && (lo <= 180)) return true
        }
        return false
    }
}