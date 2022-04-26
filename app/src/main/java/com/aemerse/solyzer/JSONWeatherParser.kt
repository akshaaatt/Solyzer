package com.aemerse.solyzer

import com.aemerse.solyzer.model.DayForecast
import com.aemerse.solyzer.model.Location
import com.aemerse.solyzer.model.Weather
import com.aemerse.solyzer.model.WeatherForecast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object JSONWeatherParser {
    @Throws(JSONException::class)
    fun getWeather(data: String?): Weather {
        val weather: Weather = Weather()
        println("Data [$data]")
        // We create out JSONObject from the data
        val jObj = JSONObject(data)

        // We start extracting the info
        val loc = Location()
        val coordObj: JSONObject =
            getObject("coord", jObj)
        loc.latitude =
            getFloat("lat", coordObj)
        loc.longitude =
            getFloat("lon", coordObj)
        val sysObj: JSONObject =
            getObject("sys", jObj)
        loc.country =
            getString("country", sysObj)
        loc.sunrise = getInt("sunrise", sysObj)
            .toLong()
        loc.sunset = getInt("sunset", sysObj).toLong()
        loc.city = getString("name", jObj)
        weather.location = loc

        // We get weather info (This is an array)
        val jArr: JSONArray = jObj.getJSONArray("weather")

        // We use only the first value
        val JSONWeather: JSONObject = jArr.getJSONObject(0)
        weather.currentCondition.weatherId =
            getInt("id", JSONWeather)
        weather.currentCondition.descr =
            getString("description",
                JSONWeather)
        weather.currentCondition.condition =
            getString("main", JSONWeather)
        weather.currentCondition.icon =
            getString("icon", JSONWeather)
        val mainObj: JSONObject =
            getObject("main", jObj)
        weather.currentCondition.humidity = getInt(
            "humidity",
            mainObj).toFloat()
        weather.currentCondition.pressure = getInt(
            "pressure",
            mainObj).toFloat()
        weather.temperature.maxTemp =
            getFloat("temp_max", mainObj)
        weather.temperature.minTemp =
            getFloat("temp_min", mainObj)
        weather.temperature.temp =
            getFloat("temp", mainObj)

        // Wind
        val wObj: JSONObject = getObject("wind", jObj)
        weather.wind.speed = getFloat("speed", wObj)
        weather.wind.deg = getFloat("deg", wObj)

        // Clouds
        val cObj: JSONObject = getObject("clouds", jObj)
        weather.clouds.perc = getInt("all", cObj)

        // We download the icon to show
        return weather
    }

    @Throws(JSONException::class)
    fun getForecastWeather(data: String?): WeatherForecast {
        val forecast: WeatherForecast = WeatherForecast()

        // We create out JSONObject from the data
        val jObj: JSONObject = JSONObject(data)
        val jArr: JSONArray = jObj.getJSONArray("list") // Here we have the forecast for every day

        // We traverse all the array and parse the data
        for (i in 0 until jArr.length()) {
            val jDayForecast: JSONObject = jArr.getJSONObject(i)

            // Now we have the json object so we can extract the data
            val df = DayForecast()

            // We retrieve the timestamp (dt)
            df.timestamp = jDayForecast.getLong("dt")

            // Temp is an object
            val jTempObj: JSONObject = jDayForecast.getJSONObject("temp")
            df.forecastTemp.day = jTempObj.getDouble("day").toFloat()
            df.forecastTemp.min = jTempObj.getDouble("min").toFloat()
            df.forecastTemp.max = jTempObj.getDouble("max").toFloat()
            df.forecastTemp.night = jTempObj.getDouble("night").toFloat()
            df.forecastTemp.eve = jTempObj.getDouble("eve").toFloat()
            df.forecastTemp.morning = jTempObj.getDouble("morn").toFloat()

            // Pressure & Humidity
            df.weather.currentCondition.pressure = jDayForecast.getDouble("pressure").toFloat()
            df.weather.currentCondition.humidity = jDayForecast.getDouble("humidity").toFloat()

            // ...and now the weather
            val jWeatherArr: JSONArray = jDayForecast.getJSONArray("weather")
            val jWeatherObj: JSONObject = jWeatherArr.getJSONObject(0)
            df.weather.currentCondition.weatherId =
                getInt("id", jWeatherObj)
            df.weather.currentCondition.descr =
                getString("description",
                    jWeatherObj)
            df.weather.currentCondition.condition =
                getString("main", jWeatherObj)
            df.weather.currentCondition.icon =
                getString("icon", jWeatherObj)
            forecast.addForecast(df)
        }
        return forecast
    }

    @Throws(JSONException::class)
    private fun getObject(tagName: String, jObj: JSONObject): JSONObject {
        return jObj.getJSONObject(tagName)
    }

    @Throws(JSONException::class)
    private fun getString(tagName: String, jObj: JSONObject): String {
        return jObj.getString(tagName)
    }

    @Throws(JSONException::class)
    private fun getFloat(tagName: String, jObj: JSONObject): Float {
        return jObj.getDouble(tagName).toFloat()
    }

    @Throws(JSONException::class)
    private fun getInt(tagName: String, jObj: JSONObject): Int {
        return jObj.getInt(tagName)
    }
}