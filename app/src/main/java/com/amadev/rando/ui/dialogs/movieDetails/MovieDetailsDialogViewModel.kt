package com.amadev.rando.ui.dialogs.movieDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.CastModelResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsDialogViewModel(
    private val apiClient: ApiClient
) : ViewModel() {

    private val videoEndPointMutableLiveData = MutableLiveData<String>()
    val videoEndPointLiveData = videoEndPointMutableLiveData

    private val castListMutableLiveData = MutableLiveData<ArrayList<CastModelResults>>()
    val castListLiveData = castListMutableLiveData

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

    fun getCastDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ApiService(apiClient).getCastDetails(movieId)
            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val castList: ArrayList<CastModelResults> = ArrayList()
                        responseBody.cast.forEach { i ->
                            i.profile_path.let {
                                castList.add(i)
                            }
                        }
                        if (castList.isNotEmpty()) {
                            castListMutableLiveData.postValue(castList)
                        }
                    }
                }
            }
        }
    }

}