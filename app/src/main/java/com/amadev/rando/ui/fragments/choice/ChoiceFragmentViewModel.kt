package com.amadev.rando.ui.fragments.choice
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.R
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.model.GenresList
import com.amadev.rando.model.MovieDetailsResults
import com.amadev.rando.util.Util.replaceFirebaseForbiddenChars
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class Messages {
    object FailedAddingToFavorites : Messages()
    object AddedToFavorites : Messages()
}

class ChoiceFragmentViewModel(
    private val context: Context,
    private val api: ApiClient,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : ViewModel() {

    companion object {
        const val FAVORITE_MOVIES = "Favorite movies"
        var failedAddingToFavorites = Messages.FailedAddingToFavorites
        var addedToFavorites = Messages.AddedToFavorites
    }

    private val username = provideFirebaseUsername()

    private val castListMutableLiveData = MutableLiveData<List<CastModelResults>>()
    val castListLiveData = castListMutableLiveData

    private val moviesGenreListMutableLiveData = MutableLiveData<List<GenresList>>()
    val moviesGenreListLiveData = moviesGenreListMutableLiveData

    private val pageAlreadyCalled = MutableLiveData<Int>()
    private val currentPage = MutableLiveData<Int>()

    val videoEndPoint = MutableLiveData<String>()

    private val popularMoviesRandomResultsMutableLiveData = MutableLiveData<MovieDetailsResults>()
    val popularMoviesResultsLiveData = popularMoviesRandomResultsMutableLiveData

    private val popUpMessageMutableLiveData = MutableLiveData<String>()
    val progressBarVisibility = MutableLiveData<Boolean>()

    private fun getRandomPage(): Int {
        return (1 until 400).random()
    }

    private fun getMessage(messages: Messages) =
        when (messages) {
            is Messages.FailedAddingToFavorites -> context.getString(R.string.failedAddingToFavorites)
            is Messages.AddedToFavorites -> context.getString(R.string.addedToFavorites)
        }

    fun getTrailerVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ApiService(api).getTrailerVideo(popularMoviesRandomResultsMutableLiveData.value?.id)
            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.results?.let {
                        if (it.isNotEmpty()) {
                            videoEndPoint.postValue(it.let {
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

    fun getCastDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ApiService(api).getCastDetails(popularMoviesRandomResultsMutableLiveData.value?.id)
            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        val castList: ArrayList<CastModelResults> = ArrayList()
                        for (i in responseBody.cast)
                            i.profile_path.let {
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
                    val genresList = ArrayList<GenresList>()
                    responseBody?.let {
                        genresList.addAll(it.genres)
                        moviesGenreListMutableLiveData.postValue(genresList)
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
                    val responseBody = response.body()

                    responseBody?.let {
                        val randomResults = responseBody.results.random()
                        randomResults.apply {
                            popularMoviesRandomResultsMutableLiveData.postValue(randomResults)
                        }
                        pageAlreadyCalled.postValue(currentPage.value)
                        progressBarVisibility.postValue(false)
                    }
                } else {
                    popUpMessageMutableLiveData.postValue("Please check internet connection")
                    progressBarVisibility.postValue(false)
                }
            }
        }
    }

    fun getMovieByGenre(selectedGenreId: Int) {
        progressBarVisibility.postValue(true)
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
                    if (response.body() != null) {
                        for (i in response.body()!!.results.iterator()) {
                            if (i.genre_ids!!.contains(selectedGenreId)) {
                                popularMoviesRandomResultsMutableLiveData.postValue(i)
                                progressBarVisibility.postValue(false)
//                                progressBarVisibility.postValue(false)
                                break
                            } else {
                                getMovieByGenre(selectedGenreId)
//                                progressBarVisibility.postValue(false)
                                break
                            }
                        }
                    }
                    pageAlreadyCalled.postValue(currentPage.value)
                }
            } else {
                popUpMessageMutableLiveData.postValue("Please check internet connection")
                progressBarVisibility.postValue(false)
            }
        }
    }

    fun addCurrentMovieToFavoriteMovies() {
        val firebaseReference =
            firebaseDatabase.getReference("Users")
                .child(replaceFirebaseForbiddenChars(username))
                .child(FAVORITE_MOVIES)

        firebaseReference
            .push()
            .setValue(popularMoviesRandomResultsMutableLiveData.value)
            .addOnSuccessListener {
                popUpMessageMutableLiveData.value = getMessage(addedToFavorites)
            }
            .addOnFailureListener {
                popUpMessageMutableLiveData.value = getMessage(failedAddingToFavorites)

            }
    }

    private fun provideFirebaseUsername(): String {
        val currentUser = firebaseAuth.currentUser
        when {
            currentUser != null -> {
                currentUser.let {
                    lateinit var username: String
                    for (profiler in it.providerData) {
                        username = profiler.email.toString()
                    }
                }
            }
        }
        return username
    }

}