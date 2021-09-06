package com.amadev.rando.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.amadev.rando.databinding.CustomFavoriteMoviesRecyclerviewPatternBinding
import com.amadev.rando.model.MovieDetailsResults
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide

class FavoriteMoviesRecyclerViewAdapter(
    val view: View,
    var context: Context,
    var list: ArrayList<MovieDetailsResults>,
    private val action : Int
) : RecyclerView.Adapter<FavoriteMoviesRecyclerViewAdapter.ViewHolder>() {

    val activity = context as FragmentActivity

    class ViewHolder(val binding: CustomFavoriteMoviesRecyclerviewPatternBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomFavoriteMoviesRecyclerviewPatternBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.apply {
                movieName.text = list[position].title
                overview.text = list[position].overview
                movieImage.loadImageWithGlide(
                    list[position].poster_path,
                    getProgressDrawable(context)
                )
            }
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("movieDetails", list[position])
            Navigation.createNavigateOnClickListener(
                action,
                bundle
            ).onClick(holder.itemView)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

}




