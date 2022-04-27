package com.aemerse.solyzer.activities

import android.app.ProgressDialog
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.aemerse.solyzer.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.google.android.material.tabs.TabLayout
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import com.jjoe64.graphview.series.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import kotlin.math.floor

class TbbdAct : AppCompatActivity() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = ProgressDialog(this) // this = YourActivity
        dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog!!.setMessage("Loading. Please wait...")
        dialog!!.isIndeterminate = true
        dialog!!.setCanceledOnTouchOutside(false)
        lat = intent.extras!!.getString("LAT")
        lng = intent.extras!!.getString("LONG")
        asyn = doit(lat!!, lng!!)
        asyn!!.execute()
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            var rootView: View? = null
            when (requireArguments().getInt(ARG_SECTION_NUMBER)) {
                1 -> {
                    rootView = inflater.inflate(R.layout.tab1estimations, container, false)
                    t1 = rootView.findViewById<View>(R.id.are) as TextView
                    t2 = rootView.findViewById<View>(R.id.pan) as TextView
                    t3 = rootView.findViewById<View>(R.id.sys) as TextView
                    t4 = rootView.findViewById<View>(R.id.ene) as TextView
                    t5 = rootView.findViewById<View>(R.id.effi) as TextView
                    val y = gg!![1].toDouble()
                    var rat: Double
                    val q = 0
                    var pp = 0
                    var i = 1
                    while (i <= 3) {
                        pp = pp * 10 + gg!![2][i].code - '0'.code
                        i++
                    }
                    val rr = String(gg!![2].toCharArray(), 0, 3)
                    t1!!.text = "Installation area \n(Recommended 75%)(sq.ft.)  \n: " + (17.5 * floor(y / 17.5)).toString() + " sq. ft."
                    t2?.text = "Number of Solar Panels  \n: " + floor(y / 17.5).toInt()+ " panels"
                    t3!!.text = "Rating of Solar Plant (kW)  \n: " + ((pp * q) / 1000.0.also { rat = it }).toString() + " kW"
                    finENE = (totalEn * y * 0.092903 * rat) / (2 * 52)
                    finENE = (10000 * finENE)
                    finENE /= 10000.0
                    t4!!.text = "Energy per annum (kWH/year) \n: $finENE kWH"
                    var efi = (pp * 100) / (1000 * 1.625803)
                    efi = (efi * 10000)
                    efi /= 10000.0
                    t5!!.text = "Efficiency of Solar Panels \n: $efi %"
                }
                2 -> {
                    rootView = inflater.inflate(R.layout.tab2graphs, container, false)



                    graphDH = rootView.findViewById<View>(R.id.graphDH) as GraphView
                    graphDH!!.visibility = GraphView.INVISIBLE
                    graphTP = rootView.findViewById<View>(R.id.graphTP) as GraphView
                    graphTP!!.visibility = GraphView.INVISIBLE
                    chart = rootView.findViewById<View>(R.id.chart) as BarChart
                    chart2 = rootView.findViewById<View>(R.id.chart2) as LineChart

                    /*final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                        }
                    }, 3000);*/asyn!!.showGraphs()
                    val data = BarData(xAxisValues, dataSet1)
                    chart!!.data = data
                    chart!!.setDescription("")
                    chart!!.animateXY(2000, 2000)
                    chart!!.invalidate()
                    val data2 = LineData(xAxisValues, dataSet2)
                    chart2!!.setDescription("")
                    chart2!!.data = data2
                    chart2!!.animateXY(2000, 2000)
                    chart2!!.invalidate()
                }
            }
            return rootView
        }

        //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        private val dataSet1: ArrayList<BarDataSet?>
             get() {
                var dataSets: ArrayList<BarDataSet?>? = null
                val valueSet1 = ArrayList<BarEntry>()
                for (i in 0..11) valueSet1.add(BarEntry((energy[i] * days[i] * (finENE / totalEn)).toFloat(),
                    i))
                val barDataSet1 = BarDataSet(valueSet1, "Averaged Energy Production (kWh/Month)")
                barDataSet1.setColors(intArrayOf(Color.rgb(233, 30, 99),
                    Color.rgb(3, 169, 244),
                    Color.rgb(255, 183, 77)))

                //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSets = ArrayList()
                dataSets.add(barDataSet1)
                return dataSets
            }
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        //set1.setColors(new int[]{Color.rgb(233,30,99),Color.rgb(3,169,244),Color.rgb(255,183,77)});

        //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        private val dataSet2: ArrayList<LineDataSet?>
            get() {
                var dataSets: ArrayList<LineDataSet?>? = null
                val valueSet1 = ArrayList<Entry>()
                for (i in 0..11) valueSet1.add(Entry((angle[i]).toFloat(), i))
                val set1 = LineDataSet(valueSet1, "Opt. Tilt Angle For Panels")
                set1.fillAlpha = 110
                // set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.color = Color.BLACK
                set1.setCircleColor(Color.BLACK)
                set1.lineWidth = 1f
                set1.circleSize = 3f
                set1.setDrawCircleHole(false)
                set1.valueTextSize = 9f
                set1.setDrawFilled(true)
                //set1.setColors(new int[]{Color.rgb(233,30,99),Color.rgb(3,169,244),Color.rgb(255,183,77)});

                //barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSets = ArrayList()
                dataSets.add(set1)
                return dataSets
            }
        private val xAxisValues: ArrayList<String>
             get() {
                val xAxis = ArrayList<String>()
                xAxis.add("JAN")
                xAxis.add("FEB")
                xAxis.add("MAR")
                xAxis.add("APR")
                xAxis.add("MAY")
                xAxis.add("JUN")
                xAxis.add("JUL")
                xAxis.add("AUG")
                xAxis.add("SEP")
                xAxis.add("OCT")
                xAxis.add("NOV")
                xAxis.add("DEC")
                return xAxis
            }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    inner class doit internal constructor(var lat: String, var longi: String) :
        AsyncTask<Void?, Void?, Void?>() {
        var words = ""
        override fun onPreExecute() {
            super.onPreExecute()
            dialog!!.show()
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val url = "https://eosweb.larc.nasa.gov/cgi-bin/sse/grid.cgi?&num=100124&lat=$lat&hgt=100&submit=Submit&veg=17&sitelev=&email=skip@larc.nasa.gov&p=swv_dwn&p=exp_dif&p=avg_dnr&p=daylight&p=ret_tlt0&p=TSKIN&step=2&lon=$longi"
                val doc = Jsoup.connect(url).get()
                Log.d("kuchii", doc.toString())
                var table: Element = doc.select("table")[2]
                var rows: Element = table.select("tr")[1]
                for (i in 1..12) energy[i - 1] = rows.child(i).text().toDouble()
                table = doc.select("table")[9]
                rows = table.select("tr")[1]
                for (i in 1..12) dayhour[i - 1] = rows.child(i).text().toDouble()
                table = doc.select("table")[10]
                rows = table.select("tr")[11]
                for (i in 1..12) angle[i - 1] = rows.child(i).text().toDouble()
                table = doc.select("table")[12]
                rows = table.select("tr")[1]
                for (i in 1..12) temp[i - 1] = rows.child(i).text().toDouble()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            getData(1)
            gg = intent.extras!!.getStringArray("input")
            setContentView(R.layout.activity_tbbd)

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

            // Set up the ViewPager with the sections adapter.
            mViewPager = findViewById<View>(R.id.container) as ViewPager
            mViewPager!!.adapter = mSectionsPagerAdapter
            val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
            tabLayout.setupWithViewPager(mViewPager)
            dialog!!.dismiss()
        }

        fun showGraphs() {
            var gridLabel: GridLabelRenderer = graphDH!!.gridLabelRenderer
            gridLabel.horizontalAxisTitle = "Month"
            gridLabel.verticalAxisTitle = "Hours of Daylight"
            setGraph(graphDH, 3)
            gridLabel = graphTP!!.gridLabelRenderer
            gridLabel.horizontalAxisTitle = "Month"
            gridLabel.verticalAxisTitle = "Average Temperature"
            setGraph(graphTP, 4)
        }

        fun setGraph(graph: GraphView?, flag: Int) {
            val series = LineGraphSeries(getData(flag))
            graph!!.addSeries(series)
            val staticLabelsFormatter = StaticLabelsFormatter(graph)
            staticLabelsFormatter.setHorizontalLabels(arrayOf("Jan ",
                "Feb ",
                "Mar ",
                "Apr ",
                "May ",
                "Jun ",
                "Jul ",
                "Aug ",
                "Sep ",
                "Oct ",
                "Nov ",
                "Dec "))
            graph.gridLabelRenderer.labelFormatter = staticLabelsFormatter
            graph.gridLabelRenderer.setHorizontalLabelsAngle(90)
            graph.gridLabelRenderer.padding = 40
            graph.setBackgroundColor(Color.rgb(235, 237, 239))

            series.backgroundColor = Color.rgb(26, 188, 156)
            series.setAnimated(true)
            series.thickness = 15
            series.dataPointsRadius = 10f
            series.isDrawDataPoints = true
            series.setOnDataPointTapListener { series, dataPoint ->
                Toast.makeText(
                    applicationContext, "" + dataPoint, Toast.LENGTH_SHORT).show()
            }
            graph.viewport.isXAxisBoundsManual = true
            graph.viewport.isYAxisBoundsManual = true
            graph.viewport.isScalable = true // enables horizontal zooming and scrolling
            graph.viewport.setScalableY(true) // enables vertical zooming and scrolling

            graph.visibility = GraphView.VISIBLE
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter((fm)!!) {
        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "ESTIMATIONS"
                1 -> return "GRAPHS"
            }
            return null
        }
    }

    companion object {
        var flaggg = 1
        var asyn: doit? = null
        var t1: TextView? = null
        var t2: TextView? = null
        var t3: TextView? = null
        var t4: TextView? = null
        var t5: TextView? = null
        var graphEN: GraphView? = null
        var graphAN: GraphView? = null
        var graphDH: GraphView? = null
        var graphTP: GraphView? = null
        var chart: BarChart? = null
        var chart2: LineChart? = null
        var chart3: LineChart? = null
        var chart4: LineChart? = null
        var lat: String? = null
        var lng: String? = null
        var gg: Array<String>? = null
        var dialog: ProgressDialog? = null
        var days = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var energy = DoubleArray(12)
        var dayhour = DoubleArray(12)
        var temp = DoubleArray(12)
        var angle = DoubleArray(12)
        var arrea = 0.0
        var totalEn = 0.0
        var finENE = 0.0
        fun getData(flag: Int): Array<DataPoint?> {
            val dp = arrayOfNulls<DataPoint>(12)
            when (flag) {
                1 -> {
                    totalEn = 0.0
                    var en: Double
                    var i = 0
                    while (i < 12) {
                        dp[i] = DataPoint(i + 1.0, (energy[i] * days[i]).also { en = it })
                        totalEn += en
                        i++
                    }
                }
                2 -> {
                    var i = 0
                    while (i < 12) {
                        dp[i] = DataPoint(i + 1.0, angle[i])
                        i++
                    }
                }
                3 -> {
                    var i = 0
                    while (i < 12) {
                        dp[i] = DataPoint(i + 1.0, dayhour[i])
                        i++
                    }
                }
                4 -> {
                    var i = 0
                    while (i < 12) {
                        dp[i] = DataPoint(i + 1.0, temp[i])
                        i++
                    }
                }
            }
            return dp
        }
    }
}