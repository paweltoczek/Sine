package com.amadev.rando.ui.fragments.popularFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.MovieDetailsResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PopularFragmentViewModel(private val apiClient: ApiClient) : ViewModel() {

    private val popularMoviesArrayList = ArrayList<MovieDetailsResults>()

    private val popularMoviesResultsMutableLiveData =
        MutableLiveData<ArrayList<MovieDetailsResults>>()
    val popularMoviesResultsLiveData = popularMoviesResultsMutableLiveData

    fun getPopularMovies(page : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiService(apiClient).getPopularMovie(page)
            if (response.isSuccessful) {
                response.body()?.let {
                    val results = it.results as ArrayList<MovieDetailsResults>
                    popularMoviesArrayList.addAll(results)
                    popularMoviesResultsMutableLiveData.postValue(popularMoviesArrayList)
                }
            }
        }
    }
}