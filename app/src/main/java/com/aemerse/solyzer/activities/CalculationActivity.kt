package com.aemerse.solyzer.activities

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aemerse.solyzer.model.Graph
import com.aemerse.solyzer.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class CalculationActivity : AppCompatActivity() {
    var graph: GraphView? = null
    var e1: EditText? = null
    var e2: EditText? = null
    var e3: EditText? = null
    var tv: TextView? = null
    var b1: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)
        supportActionBar!!.hide()
        graph = findViewById<View>(R.id.graph) as GraphView?
        intent.extras!!.getString("LONG")?.let {
            intent.extras!!.getString("LAT")?.let { it1 ->
                doit(it1, it).execute()
            }
        }
        Graph()
    }

    inner class doit internal constructor(var lat: String, var longi: String) :
        AsyncTask<Void?, Void?, Void?>() {
        var words: String = ""
        var i: Int = 0
        var arr: ArrayList<String> = ArrayList()

        // String latitude=e1.getText().toString();
        //String longitude=e2.getText().toString();
        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val url1 = "https://eosweb.larc.nasa.gov/cgi-bin/sse/grid.cgi?&num=100124&lat="
                val url2 = "&hgt=100&submit=Submit&veg=17&sitelev=&email=skip@larc.nasa.gov&p=grid_id&p=swvdwncook&p=ret_tlt0&step=2&lon="
                val url: String = url1 + lat + url2 + longi
                val doc: Document = Jsoup.connect(url).get()
                val table: Element = doc.select("table")[4]
                val rows: Element = table.select("tr")[1]
                for (td: Element in rows.select("td")) {
                    // Elements td = row.select("td");
                    if (i == 0) {
                        i++
                        continue
                    } else {
                        arr.add(td.text().toString())
                    }
                }

                //words = doc.text();
                for (s: String in arr) {
                    words += "$s "
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            val g: Graph = Graph()
            g.setText(words)

            //arr.remove(i);
            val gridLabel: GridLabelRenderer = graph!!.gridLabelRenderer
            gridLabel.horizontalAxisTitle = "Month"
            gridLabel.verticalAxisTitle = "Electricity(kWH)"
            val series: BarGraphSeries<DataPoint?>? = g.generate()
            graph!!.addSeries(series)
            graph!!.viewport.isXAxisBoundsManual = true
            val staticLabelsFormatter: StaticLabelsFormatter = StaticLabelsFormatter(graph)
            staticLabelsFormatter.setHorizontalLabels(arrayOf("Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec",
                "   "))
            graph!!.gridLabelRenderer.setHorizontalLabelsAngle(90)
            graph!!.gridLabelRenderer.labelFormatter = staticLabelsFormatter
            series!!.isAnimated = true
            // series.setThickness(15);
            series.spacing = 70


            // series.setDataPointsRadius(10);
            //series.setDrawDataPoints(true);
            //  graph.getViewport().setXAxisBoundsManual(true);
            graph!!.viewport.setMinX(0.0) // set the min value of the viewport of x axis<br />
            graph!!.viewport.setMaxX(13.0) // set the max value of the viewport of x-axix</p>*/
            // set manual Y bounds<br />
            graph!!.viewport.isYAxisBoundsManual = true
            graph!!.viewport.setMinY(0.0) // set the min value of the viewport of y axis<br />
            graph!!.viewport.setMaxY(8.0)
            graph!!.gridLabelRenderer.labelsSpace = 2
            graph!!.gridLabelRenderer.labelHorizontalHeight = 50
            series.isDrawValuesOnTop = true
            series.valuesOnTopColor = Color.RED
            // set the max value of the viewport of y-axis<br />
            graph!!.viewport.isScrollable = true
            // graph.getViewport().setScrollableY(true);
            graph!!.isHorizontalScrollBarEnabled = true
        }
    }
}