package com.amadev.rando.ui.fragments.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amadev.rando.model.PopularMoviesResults
import com.amadev.rando.ui.fragments.choice.ChoiceFragmentViewModel
import com.amadev.rando.util.Util
import com.google.firebase.database.*

class FavoritesFragmentViewModel(
    private val firebaseUsername: String,
    private val firebaseDatabase: FirebaseDatabase
) : ViewModel() {

    companion object {
        const val FAVORITE_MOVIES = "Favorite movies"
    }

    private val username = firebaseUsername

    private val _favoritesMoviesMutableLiveData = MutableLiveData<ArrayList<PopularMoviesResults>>()
    val favoritesMoviesLiveData = _favoritesMoviesMutableLiveData


    fun getFavoriteMovies() {
        val firebaseReference =
            firebaseDatabase.getReference("Users")
                .child(Util.replaceFirebaseForbiddenChars(username))
                .child(ChoiceFragmentViewModel.FAVORITE_MOVIES)

        val query: Query = firebaseReference
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favoriteMoviesList = ArrayList<PopularMoviesResults>()

                snapshot.children.forEach { data ->
                    val test = data.getValue(PopularMoviesResults::class.java)
                    favoriteMoviesList.add(test!!)
                }
                _favoritesMoviesMutableLiveData.value = favoriteMoviesList
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}