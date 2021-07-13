package com.amadev.rando.util

object Genres {
    fun getGenres(genreId: Int?): String {
        var genre = ""
        when (genreId) {
            28 -> {
                genre = "Action"
            }
            12 -> {
                genre = "Adventure"
            }
            16 -> {
                genre = "Animation"
            }
            35 -> {
                genre = "Comedy"
            }
            80 -> {
                genre = "Crime"
            }
            99 -> {
                genre = "Documentary"
            }
            18 -> {
                genre = "Drama"
            }
            10751 -> {
                genre = "Family"
            }
            14 -> {
                genre = "Fantasy"
            }
            36 -> {
                genre = "History"
            }
            27 -> {
                genre = "Horror"
            }
            10402 -> {
                genre = "Music"
            }
            9648 -> {
                genre = "Mystery"
            }
            10749 -> {
                genre = "Romance"
            }
            878 -> {
                genre = "Scienie Fiction"
            }
            10770 -> {
                genre = "TV Movie"
            }
            53 -> {
                genre = "Thriller"
            }
            10752 -> {
                genre = "War"
            }
            37 -> {
                genre = "Western"
            }
        }
        return genre
    }
}