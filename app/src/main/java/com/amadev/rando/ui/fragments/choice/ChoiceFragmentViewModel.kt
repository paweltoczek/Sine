package com.amadev.rando.ui.fragments.choice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.model.GenresList
import com.amadev.rando.model.PopularMoviesResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChoiceFragmentViewModel(private val api : ApiClient) : ViewModel() {

    private val castListMutableLiveData = MutableLiveData<List<CastModelResults>>()
    val castListLiveData = castListMutableLiveData

    private val moviesGenreListMutableLiveData = MutableLiveData<List<GenresList>>()
    val moviesGenreListLiveData = moviesGenreListMutableLiveData

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
                        for (i in responseBody.cast) i.profile_path?.let {
                            castList.add(i)
                        }
                        if (castList.isNotEmpty()) {
                            castListMutableLiveData.postValue(castList)
                        }
                    }
                }
            }
        }
    }

    fun getGenreList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiService(api).getGenreList()
            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        moviesGenreListMutableLiveData.postValue(it.genres)
                    }
                }
            }
        }
    }

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





