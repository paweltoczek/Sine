package com.amadev.rando.ui.fragments.choiceFragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadev.rando.R
import com.amadev.rando.YoutubeActivity
import com.amadev.rando.adapter.CastRecyclerViewAdapter
import com.amadev.rando.adapter.GenreSpinnerAdapter
import com.amadev.rando.adapter.GenresRecyclerViewAdapter
import com.amadev.rando.databinding.FragmentChoiceBinding
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.model.GenresList
import com.amadev.rando.ui.dialogs.logout.LogoutDialog
import com.amadev.rando.ui.dialogs.overviewDialog.OverviewDialog
import com.amadev.rando.util.Animations.animateAlphaWithHandlerDelay
import com.amadev.rando.util.Animations.animationTravelYWithAlpha
import com.amadev.rando.util.Animations.rotateAnimation
import com.amadev.rando.util.Animations.scaleXY
import com.amadev.rando.util.Genres.findGenreNameById
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import com.amadev.rando.util.Util.showSnackBar
import org.koin.android.viewmodel.ext.android.viewModel

class ChoiceFragment : Fragment() {

    private var _binding: FragmentChoiceBinding? = null
    private val binding get() = _binding!!
    private val choiceFragmentViewModel: ChoiceFragmentViewModel by viewModel()
    private var i = 0
    private var selectedGenreId = 0

    companion object {
        var ANIMATION_DURATION_TIME_ms: Long = 0
        var ALPHA: Float = 0f
        var HANDLER_DELAY_ms: Long = 0
        var TARGET_Y: Float = 0f
        var SCALE: Float = 0f
        var ROTATION_DEGREE = 0f
        var ANYGENRE = 0
        var SHUFFLE_BTN_PRESSED = 0
        const val MAX_OVERVIEW_TEXT_LENGTH = 190
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

            hideMovieDetailsBtn.setOnClickListener {
                disappearAnimation(binding.detailsLayout)
            }

            shufflebtn.setOnClickListener {
                SHUFFLE_BTN_PRESSED + 1
                if (selectedGenreId != ANYGENRE) {
                    choiceFragmentViewModel.getMovieByGenre(selectedGenreId)
                } else {
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

            logOutBtn.setOnClickListener {
                provideLogOutDialog()
            }

            addToFavorite.setOnClickListener {
                addToFavoriteMovies()
            }

            addToFavorite.setOnLongClickListener {
//                findNavController().navigate(R.id.action_choiceFragment_to_favoritesFragment)
                true
            }
        }


    }

    private fun addToFavoriteMovies() {
        choiceFragmentViewModel.addCurrentMovieToFavoriteMovies()
    }

    private fun setUpOverviewTextView(text: String) {
        val textLength = text.length
        binding.apply {
            when {
                textLength >= MAX_OVERVIEW_TEXT_LENGTH -> {
                    overviewTv.text = text
                        .take(
                            MAX_OVERVIEW_TEXT_LENGTH - getString(R.string.readMore)
                                .length
                        )

                    readMore.visibility = View.VISIBLE
                    readMore.setOnClickListener {
                        provideOverviewDialog(text)
                    }
                }
                else -> {
                    overviewTv.text = text
                    readMore.visibility = View.GONE
                }
            }
        }
    }


    private fun provideOverviewDialog(overviewText: String) {
        val dialog = OverviewDialog(overviewText)
        dialog.show(childFragmentManager, null)
    }

    private fun provideLogOutDialog() {
        val logoutDialog = LogoutDialog()
        logoutDialog.show(childFragmentManager, null)
    }

    private fun watchTrailerVideo() {
        choiceFragmentViewModel.apply {
            if (videoEndPoint.value != null) {
                val intent = Intent(requireContext(), YoutubeActivity::class.java)
                intent.putExtra("videoId", videoEndPoint.value)
                startActivity(intent)
            } else {
                showSnackBar(requireView(), getString(R.string.trailerIsNotAvailable))
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
                        overview?.let { setUpOverviewTextView(it) }
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
                            binding.bcgImage.loadImageWithGlide(
                                poster_path,
                                getProgressDrawable(requireContext())
                            )
                        }

                        genre_ids?.let { genreIdList ->
                            val genresArrayList = setUpGenreList(genreIdList)
                            setUpGenreRecyclerViewAdapter(genresArrayList)
                        }
                    }
                customizeAlphaWhileDataIsLoaded()
            }

            progressBarVisibility.observe(viewLifecycleOwner) {
                setUpProgressBar(it)
            }
            moviesGenreListLiveData.observe(viewLifecycleOwner) {
                setUpGenresSpinnerAdapter(it)
            }
            castListLiveData.observe(viewLifecycleOwner) {
                setUpCastRecyclerViewAdapter(it as ArrayList<CastModelResults>)
            }
        }
    }

    private fun setUpGenreList(genresIdList: List<Int>): ArrayList<String> {
        val genreIdArrayList = ArrayList<String>()
        genresIdList.forEach { i ->
            val genreName = findGenreNameById(i)
            genreIdArrayList.add(genreName)
        }
        return genreIdArrayList
    }

    private fun setUpProgressBar(visibility: Boolean) {
        if (visibility) {
            setProgressBarVisible()
        } else {
            setProgressBarGone()
        }
    }

    private fun setUpGenresSpinnerAdapter(genresList: List<GenresList>) {
        val spinner = binding.movieGenreSpinner
        val genreSpinnerAdapter = GenreSpinnerAdapter(requireContext(), genresList)
        spinner.adapter = genreSpinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val selectedPosition = spinner.selectedItemPosition
                genresList[selectedPosition].id
                selectedGenreId = genresList[selectedPosition].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setProgressBarVisible() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setProgressBarGone() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setUpCastRecyclerViewAdapter(list: ArrayList<CastModelResults>) {
        val adapter = CastRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())

        adapter.list.apply {
            clear()
            addAll(list)

            binding.apply {
                castRecyclerview.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                castRecyclerview.adapter = adapter
            }
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
        binding.titleTv.alpha = ALPHA
        binding.rating.alpha = ALPHA
        binding.overviewTv.alpha = ALPHA
        binding.bcgImage.alpha = ALPHA
        binding.ratingBar.alpha = ALPHA
        binding.trailerBtn.alpha = ALPHA
        binding.watchTrailer.alpha = ALPHA
        binding.releasedate.alpha = ALPHA
        binding.castRecyclerview.alpha = ALPHA
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
        animateAlphaWithHandlerDelay(binding.readMore,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (500L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.castRecyclerview,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (500L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.genreRecyclerview,
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
        animateAlphaWithHandlerDelay(binding.readMore,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (1f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.releasedate,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.castRecyclerview,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
        animateAlphaWithHandlerDelay(binding.genreRecyclerview,
            (300L.also { ANIMATION_DURATION_TIME_ms = it }),
            (0f.also { ALPHA = it }),
            (0L.also { HANDLER_DELAY_ms = it }))
    }

    private fun disappearAnimation(property: View) {
        i++
        if (i == 1) {

            Handler(Looper.myLooper()!!).postDelayed({
                rotateAnimation(
                    binding.hideMovieDetailsBtn,
                    (180f.also { ROTATION_DEGREE = it }),
                    (150L.also { ANIMATION_DURATION_TIME_ms = it })
                )

                animationTravelYWithAlpha(
                    property,
                    (150L.also { ANIMATION_DURATION_TIME_ms = it }),
                    (700f.also { TARGET_Y = it }),
                    (0.9f.also { ALPHA = it }))

            }, (300L.also { HANDLER_DELAY_ms = it }))

        } else {

            i = 0
            Handler(Looper.myLooper()!!).postDelayed({

                rotateAnimation(
                    binding.hideMovieDetailsBtn,
                    (0f.also { ROTATION_DEGREE = it }),
                    (150L.also { ANIMATION_DURATION_TIME_ms = it })
                )

                animationTravelYWithAlpha(property,
                    (150L.also { ANIMATION_DURATION_TIME_ms = it }),
                    (0f.also { TARGET_Y = it }),
                    (0.9f.also { ALPHA = it }))

            }, (100L.also { HANDLER_DELAY_ms = it }))
        }
    }

}

