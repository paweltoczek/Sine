package com.amadev.rando.ui.fragments.choice

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadev.rando.R
import com.amadev.rando.YoutubeActivity
import com.amadev.rando.adapter.CastRecyclerViewAdapter
import com.amadev.rando.adapter.GenreSpinnerAdapter
import com.amadev.rando.adapter.GenresRecyclerViewAdapter
import com.amadev.rando.databinding.FragmentChoiceBinding
import com.amadev.rando.model.GenresList
import com.amadev.rando.util.Animations.animateAlphaWithHandlerDelay
import com.amadev.rando.util.Animations.animationTravelYWithAlpha
import com.amadev.rando.util.Animations.scaleXY
import com.amadev.rando.util.Genres.findGenreNameById
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel

class ChoiceFragment : Fragment() {
    private var _binding: FragmentChoiceBinding? = null
    private val binding get() = _binding!!
    private var i = 0
    private val choiceFragmentViewModel: ChoiceFragmentViewModel by viewModel()

    private var selectedGenreId = 0

    companion object {
        var ANIMATION_DURATION_TIME_ms: Long = 0
        var ALPHA: Float = 0f
        var HANDLER_DELAY_ms: Long = 0
        var TARGET_Y: Float = 0f
        var SCALE: Float = 0f
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.myLooper()!!).postDelayed({
            choiceFragmentViewModel.getPopularMovies()
        }, (4000L.also { HANDLER_DELAY_ms = it }))

        setUpViewModel()
        setUpObservers()
        customizeAlphaWhileDataIsNotAvailable()
        setUpCastRecyclerViewAdapter()
        setTextViewVerticalMovementMethod(binding.overviewTv)
        setUpOnClickListeners()
        setUpOnBackPressedCallback()

    }

    private fun setUpViewModel() {
        choiceFragmentViewModel.apply {
            getPopularMovies()
            getGenreList()
        }
    }

    private fun setUpOnClickListeners() {
        binding.apply {
            trailerBtn.setOnClickListener {
                setProgressBarVisible()
                watchTrailerVideo()
            }

            bcgImage.setOnClickListener {
                disappearAnimation(binding.detailsLayout)
            }

            shufflebtn.setOnClickListener {
                if (selectedGenreId != 0) {
                    choiceFragmentViewModel.getMovieByGenre(selectedGenreId)
                } else {
                    binding.overviewTv.verticalScrollbarPosition = 0
                    customizeAlphaWhileShuffleButtonIsPressed()
                    setProgressBarVisible()
                    animateShuffleButton()
                    getPopularMovies()
                }
            }

            genreAny.setOnClickListener {
                genreAny.visibility = View.GONE
                movieGenreSpinner.visibility = View.VISIBLE
                movieGenreSpinner.performClick()
            }
        }
    }

    private fun watchTrailerVideo() {
        choiceFragmentViewModel.apply {
            if (videoEndPoint.value != null) {
                val intent = Intent(requireContext(), YoutubeActivity::class.java)
                intent.putExtra("videoId", videoEndPoint.value)
                startActivity(intent)
            } else {
                val snack = Snackbar.make(requireView(),
                    getString(R.string.trailerIsNotAvailable),
                    Snackbar.LENGTH_LONG)
                snack.show()
            }
        }
    }

    private fun getPopularMovies() {
        choiceFragmentViewModel.apply {
            getPopularMovies()
            getTrailerVideo()
            getCastDetails()
        }
    }

    private fun setUpObservers() {
        choiceFragmentViewModel.apply {
            popularMoviesResultsLiveData.observe(viewLifecycleOwner) {
                    it.apply {
                        title?.let { binding.titleTv.text = title.trim() }
                        overview?.let { binding.overviewTv.text = overview.trim() }
                        release_date?.let { binding.releasedate.text = release_date.take(4).trim() }
                        vote_average?.let {
                            binding.rating.text = vote_average.toString().trim()
                            binding.ratingBar.rating = (vote_average / 2).toFloat()
                        }
                        id?.let {
                            choiceFragmentViewModel.getTrailerVideo()
                            choiceFragmentViewModel.getCastDetails()
                        }
                        poster_path?.let {
                            binding.bcgImage.loadImageWithGlide(poster_path,
                                getProgressDrawable(requireContext()))
                        }
                        genre_ids?.let {
                            val genresList = ArrayList<String>()
                            genre_ids.forEach { i ->
                                val genreName = findGenreNameById(i)
                                genresList.add(genreName)
                            }
                            setUpGenreRecyclerViewAdapter(genresList)
                        }
                    }
                customizeAlphaWhileDataIsLoaded()
            }
            progressBarVisibility.observe(viewLifecycleOwner) {
                if (it == true) {
                    setProgressBarVisible()
                } else {
                    setProgressBarGone()
                }
            }
            moviesGenreListLiveData.observe(viewLifecycleOwner) {
                setUpGenresSpinnerAdapter(it)
            }
        }
    }

    private fun setUpGenresSpinnerAdapter(genresList: List<GenresList>) {
        val spinner = binding.movieGenreSpinner
        val genreSpinnerAdapter = GenreSpinnerAdapter(requireContext(), genresList)
        spinner.adapter = genreSpinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val selectedPosition = spinner.selectedItemPosition
                genresList[selectedPosition].id
                Log.e("selectedPosition", selectedPosition.toString())
                Log.e("selectedPositionId", genresList[selectedPosition].id.toString())
                selectedGenreId = genresList[selectedPosition].id
//                choiceFragmentViewModel.getMovieByGenre(genresList[selectedPosition].id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setTextViewVerticalMovementMethod(property: TextView) {
        property.movementMethod = ScrollingMovementMethod.getInstance()
    }

    private fun setProgressBarVisible() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setProgressBarGone() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setUpCastRecyclerViewAdapter() {
        val adapter = CastRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
        choiceFragmentViewModel.castListLiveData.observe(viewLifecycleOwner) { list ->
            adapter.list.apply {
                clear()
                addAll(list)
            }
            binding.castRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.castRecyclerview.adapter = adapter
        }
    }

    private fun setUpGenreRecyclerViewAdapter(genresList: ArrayList<String>) {
        val adapter = GenresRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
        adapter.list = genresList

        binding.apply {
            genreRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            genreRecyclerview.adapter = adapter
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

    private fun animateShuffleButton() {
        scaleXY(binding.shufflebtn,
            (150L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0.9f.also { SCALE = it }))
        Handler(Looper.myLooper()!!).postDelayed({
            scaleXY(binding.shufflebtn,
                (100L.also { ANIMATION_DURATION_TIME_ms = it }),
                (1f.also { ALPHA = it }))
        }, (200L.also { HANDLER_DELAY_ms = it }))
    }

    private fun customizeAlphaWhileDataIsNotAvailable() {
        binding.titleTv.alpha = 0f.also { ALPHA = it }
        binding.rating.alpha = 0f.also { ALPHA = it }
        binding.overviewTv.alpha = 0f.also { ALPHA = it }
        binding.bcgImage.alpha = 0f.also { ALPHA = it }
        binding.ratingBar.alpha = 0f.also { ALPHA = it }
        binding.trailerBtn.alpha = 0f.also { ALPHA = it }
        binding.watchTrailer.alpha = 0f.also { ALPHA = it }
    }

    private fun customizeAlphaWhileDataIsLoaded() {
        animateAlphaWithHandlerDelay(binding.titleTv,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (300L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.rating,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (300L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.overviewTv,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (300L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.ratingBar,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (300L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.bcgImage,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (500L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.trailerBtn,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (500L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.watchTrailer,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (500L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.releasedate,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (500L.also { HANDLER_DELAY_ms = it }))
    }

    private fun customizeAlphaWhileShuffleButtonIsPressed() {
        animateAlphaWithHandlerDelay(binding.titleTv,
            (100L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.rating,
            (100L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.overviewTv,
            (100L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.ratingBar,
            (100L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.bcgImage,
            (100L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.trailerBtn,
            (100L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.watchTrailer,
            (100L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
    }

    private fun disappearAnimation(property: View) {
        i++
        if (i == 1) {
            animationTravelYWithAlpha(
                property,
                (200L.also { ANIMATION_DURATION_TIME_ms = it }),
                ((-100f).also { TARGET_Y = it }),
                (1f.also { ALPHA = it }))
            Handler(Looper.myLooper()!!).postDelayed({
                animationTravelYWithAlpha(
                    property,
                    (150L.also { ANIMATION_DURATION_TIME_ms = it }),
                    (500f.also { TARGET_Y = it }),
                    (0f.also { ALPHA = it }))
            }, (300L.also { HANDLER_DELAY_ms = it }))
        } else {
            i = 0
            Handler(Looper.myLooper()!!).postDelayed({
                animationTravelYWithAlpha(property,
                    (150L.also { ANIMATION_DURATION_TIME_ms = it }),
                    (0f.also { TARGET_Y = it }),
                    (0.9f.also { ALPHA = it }))
            }, (100L.also { HANDLER_DELAY_ms = it }))
        }
    }
}

