package com.amadev.rando.api

import com.amadev.rando.model.MoviesModel
import retrofit2.http.GET


interface MoviesApi {

    @GET("3/movie/popular?api_key=611dca8cf117ec0c95b0da6ed0fbe87a/")
    suspend fun getMovies() : MoviesModel

}