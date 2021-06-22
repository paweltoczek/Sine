package com.amadev.rando.data

import com.google.gson.annotations.SerializedName

data class Model(/*
    @SerializedName("title")
    val title: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("adult")
    val adult: Boolean?,

    @SerializedName("vote_average")
    val voteAverage: String?*/

    @SerializedName("results")
    val page: String?

)