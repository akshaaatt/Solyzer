package com.aemerse.solyzer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aemerse.solyzer.fragment.DayForecastFragment
import com.aemerse.solyzer.model.WeatherForecast
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Francesco
 */
class DailyForecastPageAdapter(
    private val numDays: Int,
    private val fm: FragmentManager,
    private val forecast: WeatherForecast
) : FragmentPagerAdapter(
    fm) {
    // Page title
    override fun getPageTitle(position: Int): CharSequence? {
        // We calculate the next days adding position to the current date
        val d = Date()
        val gc: Calendar = GregorianCalendar()
        gc.time = d
        gc.add(GregorianCalendar.DAY_OF_MONTH, position)
        return sdf.format(gc.time)
    }

    override fun getItem(num: Int): Fragment {
        val f = DayForecastFragment()
        f.setForecast(forecast.getForecast(num))
        return f
    }

    /* 
	 * Number of the days we have the forecast
	 */
    override fun getCount(): Int {
        return numDays
    }

    companion object {
        private val sdf = SimpleDateFormat("E, dd-MM")
    }
}