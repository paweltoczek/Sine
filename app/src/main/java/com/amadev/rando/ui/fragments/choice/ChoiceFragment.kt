package com.amadev.rando.ui.fragments.choice

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadev.rando.R
import com.amadev.rando.YoutubeActivity
import com.amadev.rando.adapter.CastAdapter
import com.amadev.rando.util.Animations.animateAlphaWithHandlerDelay
import com.amadev.rando.util.Animations.animationTravelYWithAlpha
import com.amadev.rando.util.Animations.scaleXY
import com.amadev.rando.util.Genres.getGenres
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import kotlinx.android.synthetic.main.fragment_choice.*

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

        this.let {
            choiceFragmentViewModel =
                ViewModelProvider(this).get(ChoiceFragmentViewModel::class.java)
        }

        getPopularMoviesData()
        observeMovieDetails()
        setTextViewVerticalMovementMethod(overview_tv)
        customizeAlphaWhileDataIsNotAvailable()
        setUpAdapter()
        setUpOnClickListeners()
        setUpOnBackPressedCallback()

    }

    private fun setUpOnClickListeners() {
        trailer_btn.setOnClickListener {
            setProgressBarVisible()
            val intent = Intent(requireContext(), YoutubeActivity::class.java)
            intent.putExtra("videoId", choiceFragmentViewModel.videoEndPoint.value)
            startActivity(intent)

        }

        bcg_image.setOnClickListener {
            disappearAnimation(details_layout)
        }

        shufflebtn.setOnClickListener {
            overview_tv.verticalScrollbarPosition = 0
            customizeAlphaWhileShuffleButtonIsPressed()
            setProgressBarVisible()
            animateShuffleButton()
            getPopularMoviesData()
        }
    }


    var i = 0
    private fun disappearAnimation(property: View) {
        i++
        if (i == 1) {
            animationTravelYWithAlpha(property, 200, -100f, 1f)
            Handler().postDelayed({
                animationTravelYWithAlpha(property, 150, 500f, 0f)
            }, 300)
        } else {
            i = 0
            Handler().postDelayed({
                animationTravelYWithAlpha(property, 150, 0f, 0.9f)
            }, 100)
        }
    }


    private fun getPopularMoviesData() {
        choiceFragmentViewModel.getPopularMoviesData()
    }

    private fun observeMovieDetails() {
        choiceFragmentViewModel.apply {
            movieTitleLiveData.observe(viewLifecycleOwner) {
                title_tv.text = it?.trim()
                Handler().postDelayed({
                    customizeAlphaWhileDataIsLoaded()
                }, 1000)
            }

            movieOverviewLiveData.observe(viewLifecycleOwner) {
                overview_tv.text = it?.trim()
                setProgressBarGone()
            }

            movieReleaseDateLiveDate.observe(viewLifecycleOwner) {
                releasedate.text = it?.take(4)?.trim()
            }

            movieRatingLiveData.observe(viewLifecycleOwner) {
                rating.text = it.toString().trim()
                if (it != null) {
                    ratingBar.rating = (it / 2).toFloat()
                }
            }

            movieIdLiveData.observe(viewLifecycleOwner) {
                choiceFragmentViewModel.apply {
                    getTrailerVideoData()
                    getCastDetails()
                }
            }

            moviePosterEndPointLiveData.observe(viewLifecycleOwner){
                bcg_image.loadImageWithGlide(it, getProgressDrawable(requireContext()))
            }

            movieGenreIdLiveData.observe(viewLifecycleOwner) {
                if (it.size > 1) {
                    moviegenre2.visibility = View.VISIBLE
                    dotseparator2.visibility = View.VISIBLE
                    moviegenre1.text = getGenres(it[0])
                    moviegenre2.text = getGenres(it[1])
                } else if (it.isNotEmpty()) {
                    moviegenre1.text = getGenres(it[0])
                    moviegenre2.visibility = View.GONE
                    dotseparator2.visibility = View.GONE
                }
            }
        }
    }


    private fun animateShuffleButton() {
        scaleXY(shufflebtn, 150, 0.9f)
        Handler().postDelayed({
            scaleXY(shufflebtn, 100, 1f)
        }, 200)
    }

    private fun setTextViewVerticalMovementMethod(property: TextView) {
        property.movementMethod = ScrollingMovementMethod.getInstance()

    }

    private fun customizeAlphaWhileDataIsNotAvailable() {
        title_tv.alpha = 0f
        rating.alpha = 0f
        overview_tv.alpha = 0f
        bcg_image.alpha = 0f
        ratingBar.alpha = 0f
        trailer_btn.alpha = 0f
    }

    private fun customizeAlphaWhileDataIsLoaded() {
        animateAlphaWithHandlerDelay(title_tv, 300, 1f, 100)
        animateAlphaWithHandlerDelay(rating, 300, 1f, 300)
        animateAlphaWithHandlerDelay(overview_tv, 300, 1f, 600)
        animateAlphaWithHandlerDelay(ratingBar, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(bcg_image, 500, 1f, 500)
        animateAlphaWithHandlerDelay(trailer_btn, 500, 1f, 500)
    }

    private fun customizeAlphaWhileShuffleButtonIsPressed() {
        animateAlphaWithHandlerDelay(title_tv, 100, 0f, 0)
        animateAlphaWithHandlerDelay(rating, 100, 0f, 0)
        animateAlphaWithHandlerDelay(overview_tv, 100, 0f, 0)
        animateAlphaWithHandlerDelay(ratingBar, 100, 0f, 0)
        animateAlphaWithHandlerDelay(bcg_image, 100, 0f, 0)
        animateAlphaWithHandlerDelay(trailer_btn, 100, 0f, 0)
    }

    private fun setProgressBarVisible() {
        progressBar.visibility = View.VISIBLE
    }

    private fun setProgressBarGone() {
        progressBar.visibility = View.GONE
    }

    private fun setUpAdapter() {
        val adapter = CastAdapter(requireView(), requireContext(), arrayListOf())
        choiceFragmentViewModel.castList.observe(viewLifecycleOwner) { list ->
            adapter.list.apply {
                clear()
                addAll(list)
            }
            adapter.notifyDataSetChanged()
            cast_recyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            cast_recyclerview.adapter = adapter
        }
    }

    private fun setUpOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}

