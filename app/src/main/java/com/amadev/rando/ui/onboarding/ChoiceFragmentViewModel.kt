package com.amadev.rando.ui.onboarding

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amadev.rando.data.Model
import com.amadev.rando.data.MoviesApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ChoiceFragmentViewModel : ViewModel() {

    private val moviesService = MoviesApiService()
    private val disposable = CompositeDisposable()

    val movies = MutableLiveData<List<Model>>()
    val loading = MutableLiveData<Boolean>()
    val moviesLoadingError = MutableLiveData<Boolean>()

    fun refresh() {
        fetchRemote()
    }

    private fun fetchRemote() {
        disposable.add(
            moviesService.getMovies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Model>>(){
                    override fun onSuccess(value: List<Model>) {
                        movies.value = value
                        moviesLoadingError.value = false
                        loading.value = false

                        Log.e("Data", movies.value.toString())
                    }

                    override fun onError(e: Throwable?) {
                        moviesLoadingError.value = true
                        loading.value = false
                        e?.printStackTrace()
                        Log.e("data", e?.printStackTrace().toString())
                    }

                })
        )
    }
}