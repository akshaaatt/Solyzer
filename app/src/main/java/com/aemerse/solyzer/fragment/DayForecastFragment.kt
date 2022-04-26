package com.aemerse.solyzer.fragment

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.aemerse.solyzer.R
import com.aemerse.solyzer.WeatherHttpClient
import com.aemerse.solyzer.model.DayForecast

class DayForecastFragment : Fragment() {
    private var dayForecast: DayForecast? = null
    private var iconWeather: ImageView? = null
    fun setForecast(dayForecast: DayForecast?) {
        this.dayForecast = dayForecast
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.dayforecast_fragment, container, false)
        val tempView = v.findViewById<View>(R.id.tempForecast) as TextView
        val descView = v.findViewById<View>(R.id.skydescForecast) as TextView
        tempView.text = (dayForecast!!.forecastTemp.min - 275.15).toInt().toString() + "-" + (dayForecast!!.forecastTemp.max - 275.15).toInt()
        descView.text = dayForecast!!.weather.currentCondition.descr
        iconWeather = v.findViewById<View>(R.id.forCondIcon) as ImageView
        // Now we retrieve the weather icon
        val task = JSONIconWeatherTask()
        task.execute(dayForecast!!.weather.currentCondition.icon)
        return v
    }

    @SuppressLint("StaticFieldLeak")
    private inner class JSONIconWeatherTask : AsyncTask<String?, Void?, ByteArray?>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): ByteArray? {
            var data: ByteArray? = null
            try {

                // Let's retrieve the icon
                data = p0[0]?.let { WeatherHttpClient().getImage(it) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return data
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(data: ByteArray?) {
            super.onPostExecute(data)
            if (data != null) {
                val img = BitmapFactory.decodeByteArray(data, 0, data.size)
                iconWeather!!.setImageBitmap(img)
            }
        }
    }
}