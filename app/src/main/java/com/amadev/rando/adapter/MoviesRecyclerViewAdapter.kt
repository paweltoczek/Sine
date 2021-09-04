package com.amadev.rando.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.amadev.rando.R
import com.amadev.rando.databinding.CustomMoviesRecyclerviewPatternBinding
import com.amadev.rando.model.MovieDetailsResults
import com.amadev.rando.ui.fragments.movieDetails.MovieDetailsFragment
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide

class MoviesRecyclerViewAdapter(
    val view: View,
    var context: Context,
    var list: ArrayList<MovieDetailsResults>
) : RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>() {

    val activity = context as FragmentActivity

    class ViewHolder(val binding: CustomMoviesRecyclerviewPatternBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomMoviesRecyclerviewPatternBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.apply {
                movieName.text = list[position].title
                rating.text = list[position].vote_average.toString()
                ratingBar.rating = 1f
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
                R.id.action_mainFragment_to_movieDetailsFragment,
                bundle
            ).onClick(holder.itemView)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

}







