package com.amadev.rando

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amadev.rando.repository.Repository

class ChoiceFragmentViewModelFactory(private val repository: Repository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChoiceFragmentViewModel(repository) as T
    }
}