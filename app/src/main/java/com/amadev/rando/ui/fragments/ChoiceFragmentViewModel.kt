package com.amadev.rando.ui.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amadev.rando.api.MoviesApi
import com.amadev.rando.api.RetrofitInstance
import com.amadev.rando.model.MoviesModel
import com.amadev.rando.model.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChoiceFragmentViewModel : ViewModel() {

    val list = ArrayList<Results>()

    private val moviesResponseMutableLiveData = MutableLiveData<ArrayList<Results>>()
    val movieResponseLiveData = moviesResponseMutableLiveData


    fun getData(call: Call<MoviesModel>) {
        call.enqueue(object : Callback<MoviesModel> {
            override fun onResponse(call: Call<MoviesModel>, response: Response<MoviesModel>) {
                if (response.isSuccessful) {
                    var responseBody = response.body()!!
                    list.add(responseBody.results.random())
                    moviesResponseMutableLiveData.value = list
                }
            }

            override fun onFailure(call: Call<MoviesModel>, t: Throwable) {/*
                Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()*/
                Log.e("error", t.message.toString())
            }
        })
    }

}
