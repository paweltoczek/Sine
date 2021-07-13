package com.amadev.rando.ui.fragments.choice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.BuildConfig
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiInterface
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChoiceFragmentViewModel : ViewModel() {

    private val request = ApiClient.buildService(ApiInterface::class.java)

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

    private val movieGenreIdMutableLiveData = MutableLiveData<List<Int?>>()
    val movieGenreIdLiveData = movieGenreIdMutableLiveData

    private val castListMutableLiveData = MutableLiveData<List<CastModelResults>>()
    val castListLiveData = castListMutableLiveData

    private val moviesGenreListMutableLiveData = MutableLiveData<List<GenresList>>()
    val moviesGenreListLiveData = moviesGenreListMutableLiveData

    private val genreNameListMutableLiveData = MutableLiveData<List<String>>()
    val genreNameListLiveData = genreNameListMutableLiveData

    val genreIdMutableLiveData = MutableLiveData<GenresList>()

    val pageAlreadyCalled = MutableLiveData<Int>()
    val currentPage = MutableLiveData<Int>()

    val videoEndPoint = MutableLiveData<String>()
    val videoEndPoitError = MutableLiveData<String>()

    fun getPopularMoviesData() {
        getPopularMoviesDataPrivate()
    }

    private fun getRandomPage(): Int {
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
                            movieGenreIdMutableLiveData.value = genre_ids
                            Log.e("movieid", movieIdMutableLiveData.value.toString())
                        }
                        pageAlreadyCalled.value = currentPage.value
                    }
                }
                override fun onFailure(call: Call<PopularMoviesModel>, t: Throwable) {
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

    fun getCastDetails() {
        viewModelScope.launch {
            val callCastDetails =
                request.getCastDetails(movieIdMutableLiveData.value!!, BuildConfig.TMDB_API_KEY)
            callCastDetails.enqueue(object : Callback<Cast> {
                override fun onResponse(
                    call: Call<Cast>,
                    response: Response<Cast>
                ) {
                    val listMovies: ArrayList<CastModelResults> = ArrayList<CastModelResults>()
                    if (response.isSuccessful) {
                        val responseBody = response.body()!!.cast
                        listMovies.addAll(responseBody)
                        castListMutableLiveData.value = responseBody
                        Log.e("rawlist", listMovies.toString())
                    }
                }
                override fun onFailure(call: Call<Cast>, t: Throwable) {
                    Log.e("castError", "error")
                }
            })
        }
    }


    // fun getGenreList is not finished yet. Skip code until u see "dick" word
    fun getGenresList() {
        val callGenresList = request.getGenreList(BuildConfig.TMDB_API_KEY)
        callGenresList.enqueue(object : Callback<Genre> {
            override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
                val genreList: ArrayList<GenresList> = ArrayList()
                val genreNameList = ArrayList<String>()
                val responseBody = response.body()!!.genres
                genreNameList.add(0,"Any")
                for (i in responseBody) {
                    genreNameList.add(i.name)
                    genreNameListMutableLiveData.value = genreNameList
                    Log.e("genreList", genreNameList.toString())
                }
                genreList.addAll(responseBody)
                moviesGenreListMutableLiveData.value = response.body()!!.genres
                Log.e("movgenid", movieGenreIdMutableLiveData.value.toString())
            }
            override fun onFailure(call: Call<Genre>, t: Throwable) {
            }
        })
    }

    fun getGenreId(selectedPositionNo: Int) {
        var sele = moviesGenreListMutableLiveData.value?.get(selectedPositionNo)?.id
        Log.e("selectedPos", sele.toString())
    }

    //dick
}

