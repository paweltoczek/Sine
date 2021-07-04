package com.amadev.rando.api

import com.amadev.rando.model.PopularMoviesModel
import com.amadev.rando.model.VideoDetailsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApi {

    @GET("3/movie/popular")
    fun getPopularMovies(@Query("api_key") key: String, @Query("page") page: Int): Call<PopularMoviesModel>

    @GET("3/movie/{movie_id}/videos")
    fun getTrailerVideo(@Path("movie_id") movieId : Int, @Query("api_key") key: String) : Call<VideoDetailsModel>

}