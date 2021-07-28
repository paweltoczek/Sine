package com.amadev.rando.ui.fragments.choice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.model.GenresList
import com.amadev.rando.model.PopularMoviesResults
import com.amadev.rando.model.VideoDetailsResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChoiceFragmentViewModel(private val api : ApiClient) : ViewModel() {

    var moviesDetailsList = ArrayList<PopularMoviesResults>()
    var videosDetailsList = ArrayList<VideoDetailsResults>()

    private val castListMutableLiveData = MutableLiveData<List<CastModelResults>>()
    val castListLiveData = castListMutableLiveData

    private val moviesGenreListMutableLiveData = MutableLiveData<List<GenresList>>()
    val moviesGenreListLiveData = moviesGenreListMutableLiveData

    private val genreNameListMutableLiveData = MutableLiveData<List<String>>()

    private val pageAlreadyCalled = MutableLiveData<Int>()
    private val currentPage = MutableLiveData<Int>()

    val videoEndPoint = MutableLiveData<String>()

    private val popularMoviesResultsMutableLiveData = MutableLiveData<PopularMoviesResults>()
    val popularMoviesResultsLiveData = popularMoviesResultsMutableLiveData

    private val messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData = messageMutableLiveData
    val progressBarVisibility = MutableLiveData<Boolean>()

    private fun getRandomPage(): Int {
        return (1 until 200).random()
    }

    fun getTrailerVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiService(api).getTrailerVideo(popularMoviesResultsMutableLiveData.value?.id)
            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.results?.let {
                        videoEndPoint.postValue( it
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
            val response = ApiService(api).getCastDetails(popularMoviesResultsMutableLiveData.value?.id)
            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val castList: ArrayList<CastModelResults> = ArrayList()
                        for (i in responseBody.cast) i.profile_path?.let { castList.add(i) }
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
                            popularMoviesResultsMutableLiveData.postValue(randomResults)
                        }
                        pageAlreadyCalled.postValue(currentPage.value)
                        progressBarVisibility.postValue(false)
                    }
                } else {
                    messageMutableLiveData.postValue("Please check internet connection")
                    progressBarVisibility.postValue(false)
                }
            }
        }
    }
}





