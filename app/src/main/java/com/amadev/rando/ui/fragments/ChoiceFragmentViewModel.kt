package com.amadev.rando.ui.fragments

import android.graphics.Movie
import android.util.Log
import android.util.Size
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadev.rando.api.MoviesApi
import com.amadev.rando.api.RetrofitInstance
import com.amadev.rando.model.MoviesModel
import com.amadev.rando.model.Results
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChoiceFragmentViewModel : ViewModel() {

    var list = ArrayList<Results>()


    private val moviesResponseMutableLiveData = MutableLiveData<ArrayList<Results>>()
    val movieResponseLiveData = moviesResponseMutableLiveData
    var moviesListSize = MutableLiveData<Int>()



    fun getData(call: Call<MoviesModel>) {
        viewModelScope.launch {
            call.enqueue(object : Callback<MoviesModel> {
                override fun onResponse(call: Call<MoviesModel>, response: Response<MoviesModel>) {
                    if (response.isSuccessful) {
                        var responseBody = response.body()!!
                        list.addAll(responseBody.results)
                        moviesResponseMutableLiveData.value = list

//                        Log.e("listmut", moviesResponseMutableLiveData.value.toString())
//                        Log.e("listsiz", list.size.toString())
//                        Log.e("list", list.toString())

                    }
                }

                override fun onFailure(call: Call<MoviesModel>, t: Throwable) {/*
                Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()*/
                    Log.e("error", t.message.toString())
                }
            })
        }

    }

}
