package com.amadev.rando.ui.fragments.nowPlayingFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.MovieDetailsResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NowPlayingViewModel(private val apiClient: ApiClient) : ViewModel() {

    private val nowPlayingMoviesArrayList = ArrayList<MovieDetailsResults>()

    private val nowPlayingMoviesResultsMutableLiveData =
        MutableLiveData<ArrayList<MovieDetailsResults>>()
    val nowPlayingMoviesResultsLiveData = nowPlayingMoviesResultsMutableLiveData

    fun getPopularMovies(page : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiService(apiClient).getNowPlayingMovies(page)
            if (response.isSuccessful) {
                response.body()?.let {
                    val results = it.results as ArrayList<MovieDetailsResults>
                    nowPlayingMoviesArrayList.addAll(results)
                    nowPlayingMoviesResultsMutableLiveData.postValue(nowPlayingMoviesArrayList)
                }
            }
        }
    }
}