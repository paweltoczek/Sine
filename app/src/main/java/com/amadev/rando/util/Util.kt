package com.amadev.rando.util

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.amadev.rando.BuildConfig
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_choice.view.*

object Util {

    fun getRandomPage(): Int = (0 until 200).random()

    fun getProgressDrawable(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 10f
            centerRadius = 50f
            colorSchemeColors
            start()
        }
    }

    fun ImageView.loadImageWithGlide(
        uri: String?,
        circularProgressDrawable: CircularProgressDrawable
    ) {
        val options = RequestOptions()
            .placeholder(circularProgressDrawable)
        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(BuildConfig.TMDB_PICTURE_BASE_URL+uri)
            .into(this)
    }
}