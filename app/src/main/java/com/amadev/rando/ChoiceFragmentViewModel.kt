package com.amadev.rando

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.model.MoviesModel
import com.amadev.rando.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class ChoiceFragmentViewModel(private val repository: Repository) : ViewModel() {

    val moviesResponse : MutableLiveData<MoviesModel> = MutableLiveData()

    fun getMovies() {
        viewModelScope.launch {
            val response = repository.getMovies()

            moviesResponse.value = response
        }
    }

}