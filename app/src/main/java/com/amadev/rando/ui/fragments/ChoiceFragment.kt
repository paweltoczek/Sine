package com.amadev.rando.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amadev.rando.BuildConfig
import com.amadev.rando.R
import com.amadev.rando.api.MoviesApi
import com.amadev.rando.api.RetrofitInstance
import com.amadev.rando.util.Animations.animateAlphaWithHandlerDelay
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_choice.*

class ChoiceFragment : Fragment() {

    private lateinit var choiceFragmentViewModel: ChoiceFragmentViewModel
    private lateinit var imageEndPoint: String


    val declaredPages = 15

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choice, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.let {
            choiceFragmentViewModel =
                ViewModelProvider(this).get(ChoiceFragmentViewModel::class.java)
        }
//        val request = RetrofitInstance.buildService(MoviesApi::class.java)
//        val call = request.getMovies(BuildConfig.API_KEY, 1)
//        choiceFragmentViewModel.getData(call)

        setUpApiRequestAndCall(declaredPages)

        doOverviewTvScrollable()

        adaptDataToViews()

        customizeVisibilityWhileDataNotAvailable()
//
        shufflebtn.setOnClickListener {
            adaptDataToViews()
        }
    }

    private fun setUpApiRequestAndCall(declaredPages: Int) {
        val request = RetrofitInstance.buildService(MoviesApi::class.java)
        var i = 1
        do {
            val call = request.getMovies(BuildConfig.API_KEY, declaredPages)
            choiceFragmentViewModel.getData(call)
            i++
            Log.e("page", i.toString())
        } while (i != declaredPages)


    }

    private fun doOverviewTvScrollable() {
        overview.movementMethod = ScrollingMovementMethod.getInstance()
    }

    private fun adaptDataToViews(
        randomPage: Int = (0 until declaredPages).random()

    ) {
        choiceFragmentViewModel.movieResponseLiveData.observe(viewLifecycleOwner) {
            var randomDetails: Int = (0 until it.size).random()

            title.text = it[randomDetails].title.trim()
            rating.text = it[randomDetails].vote_average.toString().trim()
            overview.text = it[randomDetails].overview.trim()
            releasedate.text = it[randomDetails].release_date.take(4).trim()
            imageEndPoint = it[randomDetails].poster_path.trim()
//
            customizeVisibilityWhileDataIsLoaded()
            loadImageWithGlide(imageEndPoint)
        }
    }

    private fun customizeVisibilityWhileDataNotAvailable() {
        title.alpha = 0f
        rating.alpha = 0f
        overview.alpha = 0f
        releasedate.alpha = 0f
        bcg_image.alpha = 0f

    }

    private fun customizeVisibilityWhileDataIsLoaded() {
        animateAlphaWithHandlerDelay(title, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(rating, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(overview, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(releasedate, 1000, 1f, 300)
    }

    fun loadImageWithGlide(imageUrl: String) {
        val media = "https://image.tmdb.org/t/p/original$imageUrl"

        Glide.with(requireView())
            .load(media)
            .into(bcg_image)

        if(bcg_image != null) {
            animateAlphaWithHandlerDelay(bcg_image,500, 1f, 200)
        }
    }

}
