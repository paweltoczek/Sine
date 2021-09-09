package com.amadev.rando.model

import com.google.gson.annotations.SerializedName


data class Genre (
    @SerializedName("genres") val genres : List<GenresList>
)

data class GenresList(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)