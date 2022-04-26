package com.aemerse.solyzer.model

class WeatherForecast {
    private val daysForecast: MutableList<DayForecast> = ArrayList()
    fun addForecast(forecast: DayForecast) {
        daysForecast.add(forecast)
        println("Add forecast [$forecast]")
    }

    fun getForecast(dayNum: Int): DayForecast {
        return daysForecast[dayNum]
    }
}