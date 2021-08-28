package com.amadev.rando.ui.fragments.favorites

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amadev.rando.model.MovieDetailsResults
import com.amadev.rando.util.Util
import com.google.firebase.database.*
import com.amadev.rando.R
import com.google.firebase.auth.FirebaseAuth

sealed class Messages {
    object FailedToLoadFavoriteMovies : Messages()
}

class FavoritesFragmentViewModel(
    private val firebaseDatabase: FirebaseDatabase,
    private val context: Context
) : ViewModel() {

    companion object {
        const val FAVORITE_MOVIES = "Favorite movies"
        val failedToLoadFavoriteMovies = Messages.FailedToLoadFavoriteMovies
    }

    private val username = provideFirebaseUsername()

    private val _favoritesMoviesMutableLiveData = MutableLiveData<ArrayList<MovieDetailsResults>>()
    val favoritesMoviesLiveData = _favoritesMoviesMutableLiveData

    private val _popUpMessageMutableLiveData = MutableLiveData<String>()
    val popUpMessageLiveData = _popUpMessageMutableLiveData

    private val _progressBarVisibilityMutableLiveData = MutableLiveData<Boolean>()
    val progressBarLiveData = _progressBarVisibilityMutableLiveData


    fun getFavoriteMovies() {
        val firebaseReference =
            firebaseDatabase.getReference("Users")
                .child(Util.replaceFirebaseForbiddenChars(username))
                .child(FAVORITE_MOVIES)

        val query: Query = firebaseReference
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favoriteMoviesList = ArrayList<MovieDetailsResults>()

                snapshot.children.forEach { data ->
                    val test = data.getValue(MovieDetailsResults::class.java)
                    favoriteMoviesList.add(test!!)
                }
                _favoritesMoviesMutableLiveData.value = favoriteMoviesList
                _progressBarVisibilityMutableLiveData.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _progressBarVisibilityMutableLiveData.value = false
                _popUpMessageMutableLiveData.value = getMessages(failedToLoadFavoriteMovies)

            }
        })
    }

    private fun getMessages(messages: Messages) =
        when (messages) {
            is Messages.FailedToLoadFavoriteMovies -> context.getString(R.string.failedToLoadFavoriteMovies)
        }

    private fun provideFirebaseUsername(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        lateinit var username: String
        currentUser?.let {
            for (profiler in it.providerData) {
                username = profiler.email.toString()
            }
        }
        return username
    }

}