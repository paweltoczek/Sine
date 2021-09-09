package com.amadev.rando.ui.fragments.upcomingFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.MovieDetailsResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpcomingFragmentViewModel(private val apiClient: ApiClient) : ViewModel() {

    private val upcomingMoviesArrayList = ArrayList<MovieDetailsResults>()

    private val upcomingMoviesResultsMutableLiveData =
        MutableLiveData<ArrayList<MovieDetailsResults>>()
    val upcomingMoviesResultsLiveData = upcomingMoviesResultsMutableLiveData

    fun getUpcomingMovies(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiService(apiClient).getUpcomingMovie(page)
            if (response.isSuccessful) {
                response.body()?.let {
                    val results = it.results as ArrayList<MovieDetailsResults>
                    upcomingMoviesArrayList.addAll(results)
                    upcomingMoviesResultsMutableLiveData.postValue(upcomingMoviesArrayList)
                }
            }
        }
    }
}