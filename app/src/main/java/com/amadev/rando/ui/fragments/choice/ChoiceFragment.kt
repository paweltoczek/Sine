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
import com.amadev.rando.YoutubeActivity
import com.amadev.rando.adapter.CastAdapter
import com.amadev.rando.adapter.GenreSpinnerAdapter
import com.amadev.rando.databinding.FragmentChoiceBinding
import com.amadev.rando.model.GenresList
import com.amadev.rando.util.Animations.animateAlphaWithHandlerDelay
import com.amadev.rando.util.Animations.animationTravelYWithAlpha
import com.amadev.rando.util.Animations.scaleXY
import com.amadev.rando.util.Genres.findGenreNameById
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import org.koin.android.viewmodel.ext.android.viewModel

class ChoiceFragment : Fragment() {
    private var _binding: FragmentChoiceBinding? = null
    private val binding get() = _binding!!
    private var i = 0
    private val choiceFragmentViewModel: ChoiceFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
//        return inflater.inflate(R.layout.fragment_choice, container, false)
        _binding = FragmentChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpObservers()
        customizeAlphaWhileDataIsNotAvailable()
        setUpAdapter()
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
        binding.trailerBtn.setOnClickListener {
            setProgressBarVisible()
            val intent = Intent(requireContext(), YoutubeActivity::class.java)
            intent.putExtra("videoId", choiceFragmentViewModel.videoEndPoint.value)
            startActivity(intent)
        }

        binding.bcgImage.setOnClickListener {
            disappearAnimation(binding.detailsLayout)
        }

        binding.shufflebtn.setOnClickListener {
            binding.overviewTv.verticalScrollbarPosition = 0
            customizeAlphaWhileShuffleButtonIsPressed()
            setProgressBarVisible()
            animateShuffleButton()
            getPopularMovies()
        }
    }

    private fun getPopularMovies() {
        choiceFragmentViewModel.apply {
            getPopularMovies()
            getTrailerVideo()
            getCastDetails()
        }
    }

    private fun disappearAnimation(property: View) {
        i++
        if (i == 1) {
            animationTravelYWithAlpha(property, 200, -100f, 1f)
            Handler(Looper.myLooper()!!).postDelayed({
                animationTravelYWithAlpha(property, 150, 500f, 0f)
            }, 300)
        } else {
            i = 0
            Handler(Looper.myLooper()!!).postDelayed({
                animationTravelYWithAlpha(property, 150, 0f, 0.9f)
            }, 100)
        }
    }

    private fun setUpObservers() {
        choiceFragmentViewModel.apply {
            popularMoviesResultsLiveData.observe(viewLifecycleOwner) {
                    it.apply {
                        title?.let { binding.titleTv.text = title.trim() }
                        overview?.let { binding.overviewTv.text = overview.trim() }
                        release_date?.let { binding.releasedate.text = release_date.take(4).trim().toString() }
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
                        genre_ids?.let { ids ->
                            if (ids.size > 1) {
                                binding.moviegenre2.visibility = View.VISIBLE
                                binding.dotseparator2.visibility = View.VISIBLE
                                binding.moviegenre1.text = findGenreNameById(ids[0])
                                binding.moviegenre2.text = findGenreNameById(ids[1])
                            } else if (ids.isNotEmpty()) {
                                binding.moviegenre1.text = findGenreNameById(ids[0])
                                binding.moviegenre2.visibility = View.GONE
                                binding.dotseparator2.visibility = View.GONE
                            }
                        }
                    }
                    customizeAlphaWhileDataIsLoaded()
            }
            this.progressBarVisibility.observe(viewLifecycleOwner) { setProgressBarGone() }

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
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }



    }


//                moviesGenreListLiveData.observe(viewLifecycleOwner) {
//                val genreSpinnerAdapter = GenreSpinnerAdapter(requireContext(), it)
//                movie_genre_spinner.adapter = genreSpinnerAdapter
//                movie_genre_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                    override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                    }
//                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                        val selectedItemPos = movie_genre_spinner.selectedItemPosition
//                            choiceFragmentViewModel.getGenreId(selectedItemPos)
//                    }
//                }
//            }

    private fun animateShuffleButton() {
        scaleXY(binding.shufflebtn, 150, 0.9f)
        Handler(Looper.myLooper()!!).postDelayed({
            scaleXY(binding.shufflebtn, 100, 1f)
        }, 200)
    }

    private fun setTextViewVerticalMovementMethod(property: TextView) {
        property.movementMethod = ScrollingMovementMethod.getInstance()
    }

    private fun customizeAlphaWhileDataIsNotAvailable() {
        binding.titleTv.alpha = 0f
        binding.rating.alpha = 0f
        binding.overviewTv.alpha = 0f
        binding.bcgImage.alpha = 0f
        binding.ratingBar.alpha = 0f
        binding.trailerBtn.alpha = 0f
        binding.watchTrailer.alpha = 0f
    }

    private fun customizeAlphaWhileDataIsLoaded() {
        animateAlphaWithHandlerDelay(binding.titleTv, 300, 1f, 100)
        animateAlphaWithHandlerDelay(binding.rating, 300, 1f, 300)
        animateAlphaWithHandlerDelay(binding.overviewTv, 300, 1f, 600)
        animateAlphaWithHandlerDelay(binding.ratingBar, 1000, 1f, 300)
        animateAlphaWithHandlerDelay(binding.bcgImage, 500, 1f, 500)
        animateAlphaWithHandlerDelay(binding.trailerBtn, 500, 1f, 500)
        animateAlphaWithHandlerDelay(binding.watchTrailer, 500, 1f, 500)
        animateAlphaWithHandlerDelay(binding.releasedate, 500, 1f, 500)
    }

    private fun customizeAlphaWhileShuffleButtonIsPressed() {
        animateAlphaWithHandlerDelay(binding.titleTv, 100, 0f, 0)
        animateAlphaWithHandlerDelay(binding.rating, 100, 0f, 0)
        animateAlphaWithHandlerDelay(binding.overviewTv, 100, 0f, 0)
        animateAlphaWithHandlerDelay(binding.ratingBar, 100, 0f, 0)
        animateAlphaWithHandlerDelay(binding.bcgImage, 100, 0f, 0)
        animateAlphaWithHandlerDelay(binding.trailerBtn, 100, 0f, 0)
        animateAlphaWithHandlerDelay(binding.watchTrailer, 100, 0f, 0)
    }

    private fun setProgressBarVisible() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setProgressBarGone() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setUpAdapter() {
        val adapter = CastAdapter(requireView(), requireContext(), arrayListOf())
        choiceFragmentViewModel.castListLiveData.observe(viewLifecycleOwner) { list ->
            adapter.list.apply {
                clear()
                addAll(list)
            }
//            adapter.notifyDataSetChanged()
            binding.castRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.castRecyclerview.adapter = adapter
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

