package com.amadev.rando.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amadev.rando.databinding.CustomRecyclerviewPatternBinding
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide

class CastAdapter(val view: View, var context: Context, var list: ArrayList<CastModelResults>) :
    RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    class ViewHolder(val binding: CustomRecyclerviewPatternBinding) :
        RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomRecyclerviewPatternBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
//            with(list[position].name){
//                binding.actorName.text = this
//
//            }
            binding.actorName.text = list[position].name
            binding.actorImageRecyclerview.loadImageWithGlide(list[position]?.profile_path,
                getProgressDrawable(context))
        }
//        holder.castName.text = list[position]?.name?.trim()
//        holder.castImage.loadImageWithGlide(
//            list[position]?.profile_path?.trim(),
//            getProgressDrawable(context)
//        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
}


