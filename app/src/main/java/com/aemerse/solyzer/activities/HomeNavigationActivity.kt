package com.aemerse.solyzer.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.aemerse.solyzer.CustomGrid
import com.aemerse.solyzer.R

class HomeNavigationActivity : AppCompatActivity(){
    var btnStart: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_home_navigation)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        btninit()
        initGrid()
    }

    private fun btninit() {
        btnStart = findViewById<View>(R.id.btnStart) as Button?
        btnStart!!.setOnClickListener {
            startActivity(Intent(applicationContext, LocationActivity::class.java))
        }
    }

    private fun initGrid() {
        val web: Array<String> = arrayOf(
            "Reduced Energy Bills",
            "Negligible Maintenance",
            "Eco Friendly",
            "Anyone can use"
        )
        val imageId: IntArray = intArrayOf(
            R.drawable.bulb,
            R.drawable.set,
            R.drawable.env,
            R.drawable.man,
            R.drawable.money
        )
        val adapter = CustomGrid(this, web, imageId)
        val grid: GridView = findViewById<View>(R.id.grid) as GridView
        grid.adapter = adapter
    }
}