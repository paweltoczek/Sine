package com.amadev.rando.ui.dialogs.movieDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsDialogViewModel(
    private val apiClient: ApiClient
) : ViewModel() {

    private val videoEndPointMutableLiveData = MutableLiveData<String>()
    val videoEndPointLiveData = videoEndPointMutableLiveData

    fun getTrailerVideo(movieId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ApiService(apiClient)
                    .getTrailerVideo(movieId)

            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.results?.let {
                        if (it.isNotEmpty()) {
                            videoEndPointMutableLiveData.postValue(it.let {
                                it
                                    .last()
                                    .key
                                    .toString()
                            })
                        }
                    }
                }
            }
        }
    }
}