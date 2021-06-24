package com.amadev.rando

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amadev.rando.api.MoviesApi
import com.amadev.rando.api.RetrofitInstance
import com.amadev.rando.model.MoviesModel
import kotlinx.android.synthetic.main.fragment_choice.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChoiceFragment : Fragment() {

    private lateinit var choiceFragmentViewModel: ChoiceFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choice, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        choiceFragmentViewModel =
//            ViewModelProvider(this, viewModelFactory).get(ChoiceFragmentViewModel::class.java)




        draw_btn.setOnClickListener {
            val request = RetrofitInstance.buildService(MoviesApi::class.java)
            val call = request.getMovies(getString(R.string.api_key))
            val list = ArrayList<Any>()

            call.enqueue(object : Callback<MoviesModel> {
                override fun onResponse(call: Call<MoviesModel>, response: Response<MoviesModel>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()!!
                        val l = list.add(responseBody!!.results.random())

                        Log.e("res", list.toString())


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