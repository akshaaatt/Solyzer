package com.aemerse.solyzer.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aemerse.solyzer.R
import com.aemerse.solyzer.Utility.validGSTIN

class MainActivity : AppCompatActivity() {
    var et: EditText? = null
    var tv: TextView? = null
    var b: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        et = findViewById<View>(R.id.editText) as EditText?
        tv = findViewById<View>(R.id.textView2) as TextView?
        b = findViewById<View>(R.id.button) as Button?
        b!!.setOnClickListener {
            if (isValidate) {
                try {
                    showMessage(validGSTIN(et!!.text.toString().trim { it <= ' ' }))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private val isValidate: Boolean get() {
            if (et!!.text.toString().trim().isNotEmpty() && et!!.text.toString().trim() != "") {
                return true
            }
            Toast.makeText(applicationContext, "Please enter GSTIN number", Toast.LENGTH_LONG)
                .show()
            return false
        }

    private fun showMessage(valid: Boolean) {
        if (valid) {
            tv!!.text = "Valid"
            return
        }
        tv!!.text = "Invalid"
    }
}