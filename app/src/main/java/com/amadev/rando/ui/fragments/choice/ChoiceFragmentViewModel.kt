package com.amadev.rando.ui.fragments.choice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiInterface
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.model.GenresList
import com.amadev.rando.model.PopularMoviesResults
import com.amadev.rando.model.VideoDetailsResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChoiceFragmentViewModel(val api : ApiClient) : ViewModel() {

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

    private fun getRandomPage(): Int {
        val page = (1 until 200).random()
        return page
    }

    fun getTrailerVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiService(api).getTrailerVideo(movieIdMutableLiveData.value)
            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        videoEndPoint.postValue(responseBody
                            .results
                            .last()
                            .key
                            .toString())
                    }
                }
            }
        }
    }

    fun getCastDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiService(api).getCastDetails(movieIdMutableLiveData.value)
            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val castList: ArrayList<CastModelResults> = ArrayList<CastModelResults>()
                        castList.addAll(it.cast)
                        Log.e("castlist", castList.toString())
                        castListMutableLiveData.postValue(castList)
                    }
                }
            }
        }
    }

//
//    // fun getGenreList is not finished yet. Skip code until u see "dick" word
//    fun getGenresList() {
//        val callGenresList = request.getGenreList(BuildConfig.TMDB_API_KEY)
//        callGenresList.enqueue(object : Callback<Genre> {
//            override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
//                val genreList: ArrayList<GenresList> = ArrayList()
//                val genreNameList = ArrayList<String>()
//                val responseBody = response.body()!!.genres
//                genreNameList.add(0,"Any")
//                for (i in responseBody) {
//                    genreNameList.add(i.name)
//                    genreNameListMutableLiveData.value = genreNameList
//                    Log.e("genreList", genreNameList.toString())
//                }
//                genreList.addAll(responseBody)
//                moviesGenreListMutableLiveData.value = response.body()!!.genres
//                Log.e("movgenid", movieGenreIdMutableLiveData.value.toString())
//            }
//            override fun onFailure(call: Call<Genre>, t: Throwable) {
//            }
//        })
//    }
//
//    fun getGenreId(selectedPositionNo: Int) {
//        var sele = moviesGenreListMutableLiveData.value?.get(selectedPositionNo)?.id
//        Log.e("selectedPos", sele.toString())
//    }


    fun getPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {

            currentPage.postValue(getRandomPage())
            while (pageAlreadyCalled.value == currentPage.value) {
                currentPage.postValue(getRandomPage())
                if (pageAlreadyCalled.value != currentPage.value) {
                    break
                } else
                    break
            }
            val response = currentPage.value?.let { ApiService(api).getPopularMovie(it) }
            if (response != null) {
                if (response.isSuccessful) {
                    val responseBody = response?.body()
                    responseBody?.let {
                        val randomResults = responseBody.results.random()
                        randomResults.apply {
                            movieIdMutableLiveData.postValue(id)
                            movieTitleMutableLiveData.postValue(title)
                            movieOverviewMutableLiveData.postValue(overview)
                            movieReleaseDateMutableLiveData.postValue(release_date)
                            movieRatingMutableLiveData.postValue(vote_average)
                            moviePosterEndPointMutableLiveData.postValue(poster_path)
                            movieGenreIdMutableLiveData.postValue(genre_ids)
                            Log.e("movieid", movieIdMutableLiveData.value.toString())
                        }
                        pageAlreadyCalled.postValue(currentPage.value)
                    }
                }
            }
        }
    }
}


