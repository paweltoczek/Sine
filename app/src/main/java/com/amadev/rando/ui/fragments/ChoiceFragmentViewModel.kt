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

    private var currentPage = 1

    val request = RetrofitInstance.buildService(MoviesApi::class.java)
    val callPopularMovies = request.getPopularMovies(BuildConfig.API_KEY, currentPage)
    val callVideoDetails = request.getTrailerVideo(1, BuildConfig.API_KEY)

    var resultsAlreadyDisplayed = ArrayList<String>()

    var moviesDetailsList = ArrayList<PopularMoviesResults>()
    var videosDetailsList = ArrayList<VideoDetailsResults>()

    private val moviesResponseMutableLiveData = MutableLiveData<ArrayList<PopularMoviesResults>>()
    val movieResponseLiveData = moviesResponseMutableLiveData

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

    val movieDetailsListMutableLiveData = moviesDetailsList

//    fun getPopularMoviesData() {
//        getPopularMoviesDataPrivate()
//    }


    fun getPopularMoviesDataPrivate() {
        viewModelScope.launch {
            callPopularMovies.enqueue(object : Callback<PopularMoviesModel> {

                override fun onResponse(
                    call: Call<PopularMoviesModel>,
                    response: Response<PopularMoviesModel>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()!!
                        moviesDetailsList.addAll(responseBody.results)
                        moviesResponseMutableLiveData.value = moviesDetailsList

                        val randomPosition = (0 until moviesDetailsList.size).random()
                        val randomResults = responseBody.results[randomPosition]

                        randomResults.apply {
                            movieIdMutableLiveData.value = id
                            movieTitleMutableLiveData.value = title
                            movieOverviewMutableLiveData.value = overview
                            movieReleaseDateMutableLiveData.value = release_date
                            movieRatingMutableLiveData.value = vote_average
                            moviePosterEndPointMutableLiveData.value = poster_path
                        }
                        Log.e("listmut", moviesResponseMutableLiveData.value.toString())
                    }
                }

                override fun onFailure(call: Call<PopularMoviesModel>, t: Throwable) {/*
                Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()*/
                    Log.e("error", t.message.toString())
                }
            })
        }
    }

    fun getRandomMovieDetails() {
        val randomPosition = (0 until moviesDetailsList.size).random()
        movieDetailsListMutableLiveData[randomPosition].apply {
            movieIdMutableLiveData.value = id
            movieTitleMutableLiveData.value = title
            movieOverviewMutableLiveData.value = overview
            movieReleaseDateMutableLiveData.value = release_date
            movieRatingMutableLiveData.value = vote_average
            moviePosterEndPointMutableLiveData.value = poster_path
        }
    }


    fun getTrailerVideoData(/*call: Call<VideoDetailsModel>*/) {
        viewModelScope.launch {
            callVideoDetails.enqueue(object : Callback<VideoDetailsModel> {
                override fun onResponse(
                    call: Call<VideoDetailsModel>,
                    response: Response<VideoDetailsModel>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()!!
                        videosDetailsList.addAll(responseBody.results)
                        Log.e("vid", responseBody.results.last().key.toString())
                    }
                }

                override fun onFailure(call: Call<VideoDetailsModel>, t: Throwable) {
                }
            })
        }
    }


}
