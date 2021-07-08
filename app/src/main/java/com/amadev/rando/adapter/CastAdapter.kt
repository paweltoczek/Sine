package com.amadev.rando.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amadev.rando.R
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import kotlinx.android.synthetic.main.custom_recyclerview_pattern.view.*

class CastAdapter(val view: View, var context: Context, var list: ArrayList<CastModelResults>) :
    RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    internal var listResult: ArrayList<CastModelResults> = list

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var castName: TextView = itemView.actorName
        var castImage: ImageView = itemView.actor_image_recyclerview

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_recyclerview_pattern, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.castName.text = list[position]?.name?.trim()
        holder.castImage.loadImageWithGlide(
            list[position]?.profile_path?.trim(),
            getProgressDrawable(context)
        )


    }

    override fun getItemCount(): Int {
        return list.size
    }
}


