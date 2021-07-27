package com.amadev.rando.util

enum class Genre(val nameValue: String, val id: Int) {
    Action("Action", 28),
    Adventure("Adventure", 12),
    Animation("Animation", 16),
    Comedy("Comedy", 35),
    Crime("Crime", 80),
    Documentary("Documentary", 99),
    Drama("Drama", 18),
    Family("Family", 10751),
    Fantasy("Fantasy", 14),
    History("History", 36),
    Horror("Horror", 27),
    Music("Music", 10402),
    Mystery("Mystery", 9648),
    Romance("Romance", 10749),
    ScienceFiction("Science Fiction", 878),
    TvMovie("TV Movie", 10770),
    Thriller("Thriller", 53),
    War("War", 10752),
    Western("Western", 37)
}

object Genres {
    fun findGenreNameById(id: Int?): String = Genre.values().firstOrNull { it.id == id }?.nameValue
        ?: throw IllegalArgumentException("Genre with $id does not exist")
}