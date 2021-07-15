package com.amadev.rando.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amadev.rando.BuildConfig
import com.amadev.rando.model.PopularMoviesModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//object PopularMoviesRepository {
//
//    val popularMovies = MutableLiveData<PopularMoviesModel>()
//    private val request = ApiClient.buildService(ApiInterface::class.java)
//    val callPopularMovies =
//        request.getPopularMoviesByPage(BuildConfig.TMDB_API_KEY, 1)
//
//
//    fun callPopularMovies(): MutableLiveData<PopularMoviesModel> {
//        callPopularMovies.enqueue(object : Callback<PopularMoviesModel> {
//            override fun onFailure(call: Call<PopularMoviesModel>, t: Throwable) {
//                Log.v("DEBUG : ", t.message.toString())
//            }
//
//            override fun onResponse(
//                call: Call<PopularMoviesModel>,
//                response: Response<PopularMoviesModel>,
//            ) {
//                Log.v("DEBUG : ", response.body().toString())
//
//                val data = response.body()
//
//                val msg = data!!
//                popularMovies.value = data
//            }
//        })
//        Log.e("popular", popularMovies.value.toString())
//        return popularMovies
//    }
//}