package com.aemerse.solyzer.model

import java.text.SimpleDateFormat
import java.util.*

class DayForecast {
	var weather = Weather()
	var forecastTemp = ForecastTemp()
	var timestamp: Long = 0

    inner class ForecastTemp {
		var day = 0f
		var min = 0f
		var max = 0f
		var night = 0f
		var eve = 0f
		var morning = 0f
    }

    companion object {
        private val sdf = SimpleDateFormat("dd/MM/yyyy")
    }
}