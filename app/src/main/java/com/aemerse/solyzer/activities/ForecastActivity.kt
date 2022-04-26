package com.aemerse.solyzer.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.aemerse.solyzer.R
import com.aemerse.solyzer.WeatherHttpClient
import com.aemerse.solyzer.adapter.DailyForecastPageAdapter
import com.aemerse.solyzer.model.Weather
import com.aemerse.solyzer.model.WeatherForecast
import org.json.JSONException

class ForecastActivity : AppCompatActivity() {
    private var cityText: TextView? = null
    private var condDescr: TextView? = null
    private var temp: TextView? = null
    private val press: TextView? = null
    private val windSpeed: TextView? = null
    private val windDeg: TextView? = null
    private var unitTemp: TextView? = null
    private val hum: TextView? = null
    private var imgView: ImageView? = null
    var city: String = "Nagpur, INDIA"
    var lang: String = "en"
    private var pager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        initView()
        initJSON()
    }

    private fun initJSON() {
        val task = JSONWeatherTask()
        task.execute(city, lang)
        val task1 = JSONForecastWeatherTask()
        task1.execute(city, lang, forecastDaysNum)
    }

    private fun initView() {
        cityText = findViewById<View>(R.id.cityText) as TextView?
        temp = findViewById<View>(R.id.temp) as TextView?
        unitTemp = findViewById<View>(R.id.unittemp) as TextView?
        unitTemp!!.text = "�C"
        condDescr = findViewById<View>(R.id.skydesc) as TextView?
        imgView = findViewById<View>(R.id.condIcon) as ImageView?
        pager = findViewById<View>(R.id.pager) as ViewPager?
    }

    private inner class JSONWeatherTask : AsyncTask<String?, Void?, Weather?>() {
        override fun doInBackground(vararg p0: String?): Weather? {
            var weather: Weather? = Weather()
            val data: String? = (p0[0]
                ?.let { (WeatherHttpClient()).getWeatherData(it) })
            try {
                weather = com.aemerse.solyzer.JSONWeatherParser.getWeather(data)
                println("Weather [$weather]")
                // Let's retrieve the icon
                weather.iconData = ((WeatherHttpClient()).getImage(
                    (weather.currentCondition.icon)!!))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return weather
        }

        override fun onPostExecute(weather: Weather?) {
            super.onPostExecute(weather)
            if (weather!!.iconData != null && weather.iconData!!.isNotEmpty()) {
                val img: Bitmap = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData!!.size)
                imgView!!.setImageBitmap(img)
            }
            cityText!!.text = weather.location!!.city + "," + weather.location!!.country
            temp!!.text = "" + Math.round((weather.temperature.temp - 275.15))
            condDescr!!.text = weather.currentCondition.condition + "(" + weather.currentCondition.descr + ")"
            /*

			temp.setText("" + Math.round((weather.temperature.getTemp() - 275.15)) + "�C");
			hum.setText("" + weather.currentCondition.getHumidity() + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			windDeg.setText("" + weather.wind.getDeg() + "�");
			*/
        }
    }

    private inner class JSONForecastWeatherTask : AsyncTask<String?, Void?, WeatherForecast?>() {
        override fun doInBackground(vararg p0: String?): WeatherForecast? {
            val data: String? = (p0.get(0)?.let {
                p0[2]?.let { it1 ->
                    (WeatherHttpClient()).getForecastWeatherData(it, p0[1], it1)
                }
            })
            Log.e("RUSh data", "" + data)
            var forecast: WeatherForecast? = WeatherForecast()
            try {
                forecast = com.aemerse.solyzer.JSONWeatherParser.getForecastWeather(data)
                println("Weather [$forecast]")
                // Let's retrieve the icon
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return forecast
        }

        override fun onPostExecute(forecastWeather: WeatherForecast?) {
            super.onPostExecute(forecastWeather)
            val adapter = DailyForecastPageAdapter(forecastDaysNum.toInt(), supportFragmentManager, (forecastWeather)!!)
            pager!!.adapter = adapter
        }
    }

    companion object {
        private val forecastDaysNum: String = "3"
    }
}