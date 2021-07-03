package com.amadev.rando.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.amadev.rando.R
import com.amadev.rando.YoutubeActivity
import com.amadev.rando.util.Animations.animateAlphaWithHandlerDelay
import com.amadev.rando.util.Animations.animationTravelYWithAlpha
import com.amadev.rando.util.Animations.scaleXY
import com.amadev.rando.util.Util.loading
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_choice.*

class ChoiceFragment : Fragment() {

    private lateinit var choiceFragmentViewModel: ChoiceFragmentViewModel

    var movieId = 0

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

        setUpViewModel()
        observeMovieDetails()
        loadImageWithGlide()
        doOverviewTvScrollable()

        title_tv.loading(requireContext(), CircularProgressDrawable(requireContext()))
        choiceFragmentViewModel.videoEndPoint.observe(viewLifecycleOwner) {

        }

//        customizeVisibilityWhileDataNotAvailable()
        trailer_btn.setOnClickListener {
            val intent = Intent(requireContext(), YoutubeActivity::class.java)
            intent.putExtra("videoId", choiceFragmentViewModel.videoEndPoint.value)
            startActivity(intent)

        }

        baselayout.setOnClickListener {
            disappearAnimation()

        }
//
        shufflebtn.setOnClickListener {
            animateShuffleButton()
            getPopularMoviesData()
            loadImageWithGlide()
//            customizeVisibilityWhileDataNotAvailable()
        }
    }
    var i = 0
    private fun disappearAnimation() {
        i++
        if (i == 1) {
            animationTravelYWithAlpha(details_layout, 200, -100f, 1f)
            animationTravelYWithAlpha(releasedate_genre_layout, 200, -100f, 1f)
            Handler().postDelayed({
                animationTravelYWithAlpha(details_layout, 150, 500f, 0f)
                animationTravelYWithAlpha(releasedate_genre_layout, 150, 500f, 0f)
            }, 300)
        } else {
            i = 0
            Handler().postDelayed({
                animationTravelYWithAlpha(details_layout, 150, 0f, 0.9f)
                animationTravelYWithAlpha(releasedate_genre_layout, 150, 0f, 0.9f)
            }, 100)
        }
    }


    private fun getPopularMoviesData() {
        choiceFragmentViewModel.getPopularMoviesData()
    }

    private fun setUpViewModel() {
        choiceFragmentViewModel.apply {
            getPopularMoviesData()
        }

    }

    private fun observeMovieDetails() {
        choiceFragmentViewModel.apply {
            movieTitleLiveData.observe(viewLifecycleOwner) {
                title_tv.text = it?.trim()
            }

            movieOverviewLiveData.observe(viewLifecycleOwner) {
                overview_tv.text = it?.trim()
            }

            movieReleaseDateLiveDate.observe(viewLifecycleOwner) {
                releasedate.text = it?.take(4)?.trim()
            }

            movieRatingLiveData.observe(viewLifecycleOwner) {
                rating.text = it.toString().trim()
            }

            movieIdLiveData.observe(viewLifecycleOwner) {
                choiceFragmentViewModel.getTrailerVideoData()

            }
        }
    }


    private fun animateShuffleButton() {
        scaleXY(shufflebtn, 150, 0.9f)
        Handler().postDelayed({
            scaleXY(shufflebtn, 100, 1f)
        }, 200)
    }

    private fun doOverviewTvScrollable() {
        overview_tv.movementMethod = ScrollingMovementMethod.getInstance()
    }


    private fun customizeVisibilityWhileDataNotAvailable() {
        title_tv.alpha = 0f
        rating.alpha = 0f
        overview_tv.alpha = 0f
        bcg_image.alpha = 0f
        divider.alpha = 0f

    }

    private fun customizeVisibilityWhileDataIsLoaded() {
        animateAlphaWithHandlerDelay(title_tv, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(rating, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(overview_tv, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(bcg_image, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(divider, 1000, 1f, 300)
    }

    fun loadImageWithGlide() {
        choiceFragmentViewModel.moviePosterEndPointLiveData.observe(viewLifecycleOwner) {
            val imageUrl = it?.trim()
            val media = "https://image.tmdb.org/t/p/original$imageUrl"
            Glide.with(requireView())
                .load(media)
                .into(bcg_image)
            if (bcg_image != null) {
                animateAlphaWithHandlerDelay(bcg_image, 500, 1f, 200)
            }
        }

    }

}
