package com.aemerse.solyzer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomGrid constructor(private val mContext: Context, private val web: Array<String>, private val Imageid: IntArray) : BaseAdapter() {

    override fun getCount(): Int {
        return web.size
    }

    override fun getItem(position: Int): Any {
        return web[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val grid: View
        val inflater: LayoutInflater = mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            grid = inflater.inflate(com.aemerse.solyzer.R.layout.grid_single, null)
            val textView: TextView = grid.findViewById<View>(com.aemerse.solyzer.R.id.grid_text) as TextView
            val imageView: ImageView = grid.findViewById<View>(com.aemerse.solyzer.R.id.grid_image) as ImageView
            textView.text = web[position]
            imageView.setImageResource(Imageid.get(position))
        } else {
            grid = convertView
        }
        return grid
    }
}