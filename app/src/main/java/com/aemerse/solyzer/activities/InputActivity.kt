package com.aemerse.solyzer.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aemerse.solyzer.R

class InputActivity : AppCompatActivity() {
    var iv: ImageView? = null
    var s1: Spinner? = null
    var s2: Spinner? = null
    var e1: EditText? = null
    var e2: EditText? = null
    var e3: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        iv = findViewById<View>(R.id.imageView) as ImageView?
        s1 = findViewById<View>(R.id.spinner) as Spinner?
        s2 = findViewById<View>(R.id.spinner2) as Spinner?
        e1 = findViewById<View>(R.id.editText) as EditText?
        e2 = findViewById<View>(R.id.editText3) as EditText?
        e3 = findViewById<View>(R.id.editText4) as EditText?
        var sub: ArrayList<String?> = ArrayList()
        sub.add("(220-250W)")
        sub.add("(250-280W)")
        sub.add("(280-320W)")
        sub.add("(320-350W)")
        sub.add("(350-380W)")
        var adp: ArrayAdapter<String?>? =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sub)
        s1!!.adapter = adp
        sub = ArrayList()
        sub.add("Residential")
        sub.add("Institutional")
        sub.add("Industrial")
        sub.add("Government")
        sub.add("Commercial")
        adp = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sub)
        s2!!.adapter = adp
        val b: Bundle? = intent.extras
        iv!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val s: Array<String?> = arrayOfNulls(5)
                s[0] = ""
                s[1] = ""
                s[2] = ""
                s[3] = ""
                s[4] = ""
                s[0] = e1!!.text.toString()
                s[1] = e2!!.text.toString()
                s[2] = s1!!.selectedItem.toString()
                s[3] = s1!!.selectedItem.toString()
                s[4] = e3!!.text.toString()
                var flag = true
                for (i in 0..4) if (s[i]!!.compareTo("") == 0) flag = false
                if (!flag) {
                    Toast.makeText(applicationContext,
                        "Details Incomplete.",
                        Toast.LENGTH_SHORT).show()
                    return
                }
                val i = Intent(applicationContext, TbbdAct::class.java)
                b!!.putStringArray("input", s)
                i.putExtras((b))
                startActivity(i)
            }
        })
    }
}