package com.amadev.rando.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amadev.rando.BuildConfig
import com.amadev.rando.model.PopularMoviesModel
import com.amadev.rando.util.Util.getRandomPage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ApiService {
//
//    private val request = ApiClient.buildService(ApiInterface::class.java)
//    private var pageAlreadyCalled: Int = 0
//    lateinit var responseBody : Response<PopularMoviesModel>
//
//    fun getPopularMoviesDataService(currentPage: MutableLiveData<Int>): Response<PopularMoviesModel> {
//        while (pageAlreadyCalled == currentPage.value) {
//            currentPage.value = getRandomPage()
//            if (pageAlreadyCalled != currentPage.value) {
//                break
//            } else
//                break
//        }
//        val callPopularMovies =
//            request.getPopularMovies(BuildConfig.TMDB_API_KEY, currentPage.value!!)
//        callPopularMovies.enqueue(object : Callback<PopularMoviesModel> {
//            override fun onResponse(
//                call: Call<PopularMoviesModel>,
//                response: Response<PopularMoviesModel>,
//            ) {
//                if (response.isSuccessful) {
//                    responseBody = response
//                }
//                pageAlreadyCalled = currentPage.value!!
//            }
//
//            override fun onFailure(call: Call<PopularMoviesModel>, t: Throwable) {
//                Log.e("error", t.message.toString())
//            }
//        })
//        return responseBody
//    }
}