package com.amadev.rando.api

import com.amadev.rando.model.MoviesModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface MoviesApi {

    @GET("3/movie/popular")
    fun getMovies(@Query("api_key") key: String, @Query("page") page: Int): Call<MoviesModel>

}