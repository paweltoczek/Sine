package com.amadev.rando.ui.fragments

import android.os.Bundle
import android.os.Handler
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
import com.amadev.rando.util.Animations.animationTravelYWithAlpha
import com.amadev.rando.util.Animations.scaleXY
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_choice.*

class ChoiceFragment : Fragment() {

    private lateinit var choiceFragmentViewModel: ChoiceFragmentViewModel
    private lateinit var imageEndPoint: String

    private var resultsAlreadyDisplayed = ArrayList<String>()
    private var currentPage = 1


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

        setUpApiRequestAndCall(currentPage)

        doOverviewTvScrollable()

        adaptDataToViews()

        customizeVisibilityWhileDataNotAvailable()

        var i = 0
        baselayout.setOnClickListener {
            i++
            if (i == 1) {
                animationTravelYWithAlpha(details_layout, 250, 500f, 0f)
                animationTravelYWithAlpha(releasedate_genre_layout, 250, -500f, 0f)
            } else {
                i = 0
                animationTravelYWithAlpha(details_layout, 250, 0f, 0.9f)
                animationTravelYWithAlpha(releasedate_genre_layout, 250, 0f, 0.9f)
            }
        }
//
        shufflebtn.setOnClickListener {

            scaleXY(shufflebtn, 150, 0.9f)
            Handler().postDelayed({
                scaleXY(shufflebtn, 100, 1f)
            }, 200)

            customizeVisibilityWhileDataNotAvailable()
            choiceFragmentViewModel.movieResponseLiveData.observe(viewLifecycleOwner) {

                var randomDetails: Int = (0 until it.size).random()
                it[randomDetails].title?.let { it1 -> resultsAlreadyDisplayed.add(it1) }

                it[randomDetails].title?.let { it1 -> Log.e("it", it1) }
                Log.e("it2", resultsAlreadyDisplayed.toString())
                Log.e("it2", resultsAlreadyDisplayed.size.toString())
                Log.e("it3", it.size.toString())

                while (resultsAlreadyDisplayed.contains(it[randomDetails].title)) {
                    randomDetails = (0 until it.size).random()
                    if (resultsAlreadyDisplayed.contains(it[randomDetails].title).not()) {
                        it[randomDetails].title?.let { it1 -> resultsAlreadyDisplayed.add(it1) }

                        title.text = it[randomDetails].title?.trim()
                        rating.text = it[randomDetails].vote_average.toString().trim()
                        overview_tv.text = it[randomDetails].overview?.trim()
                        releasedate.text = it[randomDetails].release_date?.take(4)
                        imageEndPoint = it[randomDetails].poster_path?.trim().toString()

                        bcg_image.loadImageWithGlide(
                            "https://image.tmdb.org/t/p/original$imageEndPoint",
                            getProgressDrawable(requireContext())
                        )
                        customizeVisibilityWhileDataIsLoaded()
                        break
                    } else {
                        resultsAlreadyDisplayed.clear()
                        currentPage++
                        setUpApiRequestAndCall(currentPage)
                        break
                    }
                }
            }
        }
    }

    private fun setUpApiRequestAndCall(declaredPages: Int) {
        val request = RetrofitInstance.buildService(MoviesApi::class.java)
        val call = request.getPopularMovies(BuildConfig.API_KEY, currentPage)
        choiceFragmentViewModel.getData(call)
    }

    private fun doOverviewTvScrollable() {
        overview_tv.movementMethod = ScrollingMovementMethod.getInstance()
    }

    private fun adaptDataToViews() {
//        choiceFragmentViewModel.movieResponseLiveData.observe(viewLifecycleOwner) {
//            var randomDetails: Int = (0 until it.size).random()
//            Log.e("mutablelistsize", it.size.toString())
//            title.text = it[randomDetails].title.trim()
//            rating.text = it[randomDetails].vote_average.toString().trim()
//            overview_tv.text = it[randomDetails].overview.trim()
//        //    releasedate.text = it[randomDetails].release_date.take(4).trim()
//            imageEndPoint = it[randomDetails].poster_path.trim()
////
//            customizeVisibilityWhileDataIsLoaded()
//            loadImageWithGlide(imageEndPoint)
//        }
    }

    private fun customizeVisibilityWhileDataNotAvailable() {
        title.alpha = 0f
        rating.alpha = 0f
        overview_tv.alpha = 0f
        bcg_image.alpha = 0f
        divider.alpha = 0f

    }

    private fun customizeVisibilityWhileDataIsLoaded() {
        animateAlphaWithHandlerDelay(title, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(rating, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(overview_tv, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(bcg_image, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(divider, 1000, 1f, 300)
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
