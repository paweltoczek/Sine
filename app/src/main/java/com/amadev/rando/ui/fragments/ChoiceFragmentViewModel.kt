package com.amadev.rando.ui.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.BuildConfig
import com.amadev.rando.api.MoviesApi
import com.amadev.rando.api.RetrofitInstance
import com.amadev.rando.model.PopularMoviesModel
import com.amadev.rando.model.PopularMoviesResults
import com.amadev.rando.model.VideoDetailsModel
import com.amadev.rando.model.VideoDetailsResults
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChoiceFragmentViewModel : ViewModel() {


    val request = RetrofitInstance.buildService(MoviesApi::class.java)


    var moviesDetailsList = ArrayList<PopularMoviesResults>()
    var videosDetailsList = ArrayList<VideoDetailsResults>()

    private val movieIdMutableLiveData = MutableLiveData<Int?>()
    val movieIdLiveData = movieIdMutableLiveData

    private val movieTitleMutableLiveData = MutableLiveData<String?>()
    val movieTitleLiveData = movieTitleMutableLiveData

    private val movieOverviewMutableLiveData = MutableLiveData<String?>()
    val movieOverviewLiveData = movieOverviewMutableLiveData

    private val movieReleaseDateMutableLiveData = MutableLiveData<String?>()
    val movieReleaseDateLiveDate = movieReleaseDateMutableLiveData

    private val movieRatingMutableLiveData = MutableLiveData<Double?>()
    val movieRatingLiveData = movieRatingMutableLiveData

    private val moviePosterEndPointMutableLiveData = MutableLiveData<String?>()
    val moviePosterEndPointLiveData = moviePosterEndPointMutableLiveData

    val pageAlreadyCalled = MutableLiveData<Int>()
    val currentPage = MutableLiveData<Int>()

    val videoEndPoint = MutableLiveData<String>()
    val videoEndPoitError = MutableLiveData<String>()

    fun getPopularMoviesData() {
        getPopularMoviesDataPrivate()
    }


    fun getRandomPage(): Int {
        val page = (1 until 200).random()
        return page
    }

    private fun getPopularMoviesDataPrivate() {
        viewModelScope.launch {

            currentPage.value = getRandomPage()

            while (pageAlreadyCalled.value == currentPage.value) {
                currentPage.value = getRandomPage()
                if (pageAlreadyCalled.value != currentPage.value) {
                    break
                } else
                    break
            }

            Log.e("page", pageAlreadyCalled.value.toString())
            Log.e("pagecurrent", currentPage.toString())

            val callPopularMovies =
                request.getPopularMovies(BuildConfig.TMDB_API_KEY, currentPage.value!!)

            callPopularMovies.enqueue(object : Callback<PopularMoviesModel> {

                override fun onResponse(
                    call: Call<PopularMoviesModel>,
                    response: Response<PopularMoviesModel>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()!!
                        val randomResults = responseBody.results.random()

                        randomResults.apply {
                            movieIdMutableLiveData.value = id
                            movieTitleMutableLiveData.value = title
                            movieOverviewMutableLiveData.value = overview
                            movieReleaseDateMutableLiveData.value = release_date
                            movieRatingMutableLiveData.value = vote_average
                            moviePosterEndPointMutableLiveData.value = poster_path
                        }
                        pageAlreadyCalled.value = currentPage.value
                    }
                }

                override fun onFailure(call: Call<PopularMoviesModel>, t: Throwable) {/*
                Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()*/
                    Log.e("error", t.message.toString())
                }
            })
        }
    }


    fun getTrailerVideoData() {
        viewModelScope.launch {
            val callVideoDetails =
                request.getTrailerVideo(movieIdMutableLiveData.value!!, BuildConfig.TMDB_API_KEY)
            callVideoDetails.enqueue(object : Callback<VideoDetailsModel> {
                override fun onResponse(
                    call: Call<VideoDetailsModel>,
                    response: Response<VideoDetailsModel>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.results?.let { videosDetailsList.addAll(it) }
                        if (responseBody?.results?.isEmpty() == true) {
                            videoEndPoitError.value = "empty"
                        } else
                        videoEndPoint.value = responseBody?.results?.last()?.key.toString()
                    }
                }

                override fun onFailure(call: Call<VideoDetailsModel>, t: Throwable) {
                }
            })
        }
    }


}
