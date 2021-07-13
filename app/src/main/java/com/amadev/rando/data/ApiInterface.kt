package com.amadev.rando.data

import com.amadev.rando.model.Cast
import com.amadev.rando.model.Genre
import com.amadev.rando.model.PopularMoviesModel
import com.amadev.rando.model.VideoDetailsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Call<PopularMoviesModel>

    @GET("movie/{movie_id}/videos")
    fun getTrailerVideo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String,
    ): Call<VideoDetailsModel>

    @GET("movie/{movie_id}/credits")
    fun getCastDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String,
    ): Call<Cast>

    @GET("genre/movie/list")
    fun getGenreList(@Query("api_key") key: String) : Call<Genre>

}