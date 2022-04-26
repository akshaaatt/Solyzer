package com.aemerse.solyzer.model

import java.io.Serializable

class Location : Serializable {
    var longitude = 0f
    var latitude = 0f
    var sunset: Long = 0
    var sunrise: Long = 0
    var country: String? = null
    var city: String? = null
}