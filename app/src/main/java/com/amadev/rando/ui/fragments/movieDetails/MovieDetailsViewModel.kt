package com.amadev.rando.ui.fragments.movieDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.data.ApiClient
import com.amadev.rando.data.ApiService
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.model.MovieDetailsResults
import com.amadev.rando.ui.fragments.choice.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val apiClient : ApiClient,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : ViewModel() {

    companion object {
        const val FAVORITE_MOVIES = "Favorite movies"
        var failedAddingToFavorites = Messages.FailedAddingToFavorites
        var addedToFavorites = Messages.AddedToFavorites
    }

    private val _movieDetailsMutableLiveData = MutableLiveData<MovieDetailsResults?>()
    val movieDetailsMutableLiveData = _movieDetailsMutableLiveData

    private val _videoEndPoint = MutableLiveData<String>()
    val videoEndPoint = _videoEndPoint

    private val _castList = MutableLiveData<List<CastModelResults>>()
    val castList = _castList

    private val username = provideFirebaseUsername()

    fun setUpMovieDetails(results: MovieDetailsResults?) {
        if (results != null) {
            _movieDetailsMutableLiveData.value = results
            results.id.let {
                getTrailerVideo(it)
                getCastDetails(it)
            }
        }
    }

    private fun getTrailerVideo(movieId : Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ApiService(apiClient).getTrailerVideo(movieId)
            response?.let {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.results?.let {
                        if (it.isNotEmpty()) {
                            _videoEndPoint.postValue(it.let {
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

    private fun getCastDetails(movieId : Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ApiService(apiClient).getCastDetails(movieId)
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
                            _castList.postValue(castList)
                        }
                    }
                }
            }
        }
    }

//    fun addCurrentMovieToFavoriteMovies() {
//        val firebaseReference =
//            firebaseDatabase.getReference("Users")
//                .child(Util.replaceFirebaseForbiddenChars(username))
//                .child(ChoiceFragmentViewModel.FAVORITE_MOVIES)
//
//        firebaseReference
//            .push()
//            .setValue(popularMoviesRandomResultsMutableLiveData.value)
//            .addOnSuccessListener {
//                popUpMessageMutableLiveData.value = getMessage(ChoiceFragmentViewModel.addedToFavorites)
//            }
//            .addOnFailureListener {
//                popUpMessageMutableLiveData.value = getMessage(ChoiceFragmentViewModel.failedAddingToFavorites)
//
//            }
//    }

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
