package com.amadev.rando.api

import com.amadev.rando.model.Cast
import com.amadev.rando.model.PopularMoviesModel
import com.amadev.rando.model.VideoDetailsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApi {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Call<PopularMoviesModel>

    @GET("movie/{movie_id}/videos")
    fun getTrailerVideo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String
    ): Call<VideoDetailsModel>

    @GET("movie/{movie_id}/credits")
    fun getCastDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String
    ): Call<Cast>

}