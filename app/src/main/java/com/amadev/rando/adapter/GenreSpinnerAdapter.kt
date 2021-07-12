package com.amadev.rando.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.amadev.rando.R
import com.amadev.rando.model.GenresList

class GenreSpinnerAdapter(val context: Context, val list: List<String>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val viewHolder: ViewHolder
        var view = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.genres_custom_spinner_item, parent, false)
            viewHolder = ViewHolder(view)
        } else {
            viewHolder = view.tag as ViewHolder
        }
        view?.tag = viewHolder
        viewHolder.genreName.text = list[position]
        return view
    }

    class ViewHolder(view: View) {
        var genreName: TextView = view.findViewById(R.id.spinner_genre_name)
    }


}