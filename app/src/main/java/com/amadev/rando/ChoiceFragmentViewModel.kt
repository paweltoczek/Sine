package com.amadev.rando

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amadev.rando.model.MoviesModel


class ChoiceFragmentViewModel : ViewModel() {

    val moviesResponse : MutableLiveData<MoviesModel> = MutableLiveData()


}