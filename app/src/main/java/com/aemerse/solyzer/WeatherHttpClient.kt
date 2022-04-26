package com.aemerse.solyzer

import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherHttpClient {
    fun getWeatherData(location: String): String? {
        var con: HttpURLConnection? = null
        var `is`: InputStream? = null
        try {
            val url = URL("$BASE_URL$location&APPID=7182581a0f72a68fcad8f3a6e29c9060")
            con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.doInput = true
            con.doOutput = true
            con.connect()

            // Let's read the response
            val buffer = StringBuffer()
            `is` = con.inputStream
            val br = BufferedReader(InputStreamReader(`is`))
            var line: String? = null
            while (br.readLine().also { line = it } != null) buffer.append("""
    $line
    
    """.trimIndent())
            `is`.close()
            con.disconnect()
            return buffer.toString()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try {
                `is`!!.close()
            } catch (t: Throwable) {
            }
            try {
                con!!.disconnect()
            } catch (t: Throwable) {
            }
        }
        return null
    }

    fun getForecastWeatherData(location: String, lang: String?, sForecastDayNum: String): String? {
        var con: HttpURLConnection? = null
        var `is`: InputStream? = null
        val forecastDayNum = sForecastDayNum.toInt()
        try {

            // Forecast
            var url = BASE_FORECAST_URL + location
            if (lang != null) url = "$url&lang=$lang"
            url = "$url&cnt=$forecastDayNum"
            con = URL(url).openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.doInput = true
            con.doOutput = true
            con.connect()

            // Let's read the response
            val buffer1 = StringBuffer()
            `is` = con.inputStream
            val br1 = BufferedReader(InputStreamReader(`is`))
            var line1: String? = null
            while (br1.readLine().also { line1 = it } != null) buffer1.append("""
    $line1
    
    """.trimIndent())
            `is`.close()
            con.disconnect()
            println("Buffer [$buffer1]")
            return buffer1.toString()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try {
                `is`!!.close()
            } catch (t: Throwable) {
            }
            try {
                con!!.disconnect()
            } catch (t: Throwable) {
            }
        }
        return null
    }

    fun getImage(code: String): ByteArray? {
        var con: HttpURLConnection? = null
        var `is`: InputStream? = null
        try {
            con = URL(IMG_URL + code)
                .openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.doInput = true
            con.doOutput = true
            con.connect()

            // Let's read the response
            `is` = con.inputStream
            val buffer = ByteArray(1024)
            val baos = ByteArrayOutputStream()
            while (`is`.read(buffer) != -1) baos.write(buffer)
            return baos.toByteArray()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try {
                `is`!!.close()
            } catch (t: Throwable) {
            }
            try {
                con!!.disconnect()
            } catch (t: Throwable) {
            }
        }
        return null
    }

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q="
        private const val IMG_URL = "http://openweathermap.org/img/w/"
        private const val BASE_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&q="
    }
}