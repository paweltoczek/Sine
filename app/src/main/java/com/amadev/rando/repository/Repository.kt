package com.amadev.rando.repository

import com.amadev.rando.api.RetrofitInstance
import com.amadev.rando.model.MoviesModel

class Repository {

    suspend fun getMovies() : MoviesModel {
        return RetrofitInstance.api.getMovies()
    }

}