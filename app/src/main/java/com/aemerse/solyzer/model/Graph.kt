package com.aemerse.solyzer.model

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.*

class Graph {
    var series: BarGraphSeries<DataPoint?>? = null
    var graph: GraphView? = null
    var word: String? = null
    var months = arrayOf("Jan",
        "Feb",
        "March",
        "April",
        "May",
        "June",
        "July",
        "Aug",
        "Sep",
        "Octo",
        "Nov",
        "Dec")

    fun generate(): BarGraphSeries<DataPoint?> {
        series = BarGraphSeries(data)
        return series!!
    }

    val data: Array<DataPoint?>
        get() {
            var d: Double
            var i = 0.0
            val dp = arrayOfNulls<DataPoint>(12)
            val s = word!!.split(" ").toTypedArray()
            for (temp in s) {
                d = temp.toDouble()
                dp[i.toInt()] = DataPoint(i + 1, d)
                i++
            }
            return dp
        }

    fun setText(s: String?) {
        word = s
    }
}