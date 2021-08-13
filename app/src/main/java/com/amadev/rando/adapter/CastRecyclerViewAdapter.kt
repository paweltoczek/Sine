package com.amadev.rando.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amadev.rando.databinding.CustomCastRecyclerviewPatternBinding
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide

class CastRecyclerViewAdapter(val view: View, var context: Context, var list: ArrayList<CastModelResults>) :
    RecyclerView.Adapter<CastRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: CustomCastRecyclerviewPatternBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomCastRecyclerviewPatternBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.actorName.text = list[position].name
            binding.actorImageRecyclerview.loadImageWithGlide(list[position].profile_path,
                getProgressDrawable(context))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}


