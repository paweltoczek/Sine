package com.amadev.rando.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsModel(

//    @SerializedName("page") val page : Int? = null,
    @SerializedName("results") val results : List<MovieDetailsResults> = ArrayList<MovieDetailsResults>()
//    @SerializedName("total_pages") val total_pages : Int? ,
//    @SerializedName("total_results") val total_results : Int
) : Parcelable
@Parcelize
data class MovieDetailsResults (

    @SerializedName("adult") val adult: Boolean? = null,
//    @SerializedName("backdrop_path") val backdrop_path : String,
    @SerializedName("genre_ids") val genre_ids: List<Int>? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("original_language") val original_language: String? = null,
//    @SerializedName("original_title") val original_title : String,
    @SerializedName("overview") val overview: String? = null,
//    @SerializedName("popularity") val popularity : Double,
    @SerializedName("poster_path") val poster_path: String? = null,
    @SerializedName("release_date") val release_date: String? = null,
    @SerializedName("title") val title: String? = null,
//    @SerializedName("video") val video : Boolean,
    @SerializedName("vote_average") val vote_average: Double? = null,
//    @SerializedName("vote_count") val vote_count : Int
) : Parcelable