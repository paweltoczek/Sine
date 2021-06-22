package com.amadev.rando.data

import io.reactivex.Single
import retrofit2.http.GET


interface MoviesApi {
    @GET("/3/movie/popular?api_key=611dca8cf117ec0c95b0da6ed0fbe87a/")
    fun getMovies(): Single<List<Model>>
}