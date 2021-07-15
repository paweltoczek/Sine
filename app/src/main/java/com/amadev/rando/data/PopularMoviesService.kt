package com.amadev.rando.data

import com.amadev.rando.BuildConfig
import com.amadev.rando.model.PopularMoviesModel
import retrofit2.Response

class PopularMoviesService(val api: ApiClient) {

    fun getPopularMovie(page: Int): Response<PopularMoviesModel> {
        val callResponse = api.buildService(ApiInterface::class.java)
            .getPopularMoviesByPage(BuildConfig.TMDB_API_KEY, page)
        val response = callResponse.execute()
        return response
    }
}