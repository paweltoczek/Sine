package com.amadev.rando.util

object Genres {
    fun getGenres(genreId: Int?): String {
        var genre = ""
        if (genreId == 28) {
            genre = "Action"
        }
        if (genreId == 12) {
            genre = "Adventure"
        }
        if (genreId == 16) {
            genre = "Animation"
        }
        if (genreId == 35) {
            genre = "Comedy"
        }
        if (genreId == 80) {
            genre = "Crime"
        }
        if (genreId == 99) {
            genre = "Documentary"
        }
        if (genreId == 18) {
            genre = "Drama"
        }
        if (genreId == 10751) {
            genre = "Family"
        }
        if (genreId == 14) {
            genre = "Fantasy"
        }
        if (genreId == 36) {
            genre = "History"
        }
        if (genreId == 27) {
            genre = "Horror"
        }
        if (genreId == 10402) {
            genre = "Music"
        }
        if (genreId == 9648) {
            genre = "Mystery"
        }
        if (genreId == 10749) {
            genre = "Romance"
        }
        if (genreId == 878) {
            genre = "Scienie Fiction"
        }
        if (genreId == 10770) {
            genre = "TV Movie"
        }
        if (genreId == 53) {
            genre = "Thriller"
        }
        if (genreId == 10752) {
            genre = "War"
        }
        if (genreId == 37) {
            genre = "Western"
        }
        return genre
    }
}