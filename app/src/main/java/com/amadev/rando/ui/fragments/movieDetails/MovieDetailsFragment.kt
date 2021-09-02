package com.amadev.rando.ui.fragments.movieDetails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadev.rando.R
import com.amadev.rando.YoutubeActivity
import com.amadev.rando.adapter.CastRecyclerViewAdapter
import com.amadev.rando.adapter.GenresRecyclerViewAdapter
import com.amadev.rando.databinding.MovieDetailsFragmentBinding
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.model.MovieDetailsResults
import com.amadev.rando.util.Genres
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailsFragment : Fragment() {

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel()
    lateinit var intent: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpObservers()
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.apply {
            watchTrailer.setOnClickListener {
                startYoutubeActivity()
            }
            back.setOnClickListener {
                navigateToMainFragment()
            }
        }
    }

    private fun navigateToMainFragment() {
        findNavController().navigate(R.id.action_movieDetailsFragment_to_mainFragment)
    }

    private fun startYoutubeActivity() {
        startActivity(intent)
    }

    private fun setUpObservers() {
        binding.apply {
            movieDetailsViewModel.apply {
                castList.observe(viewLifecycleOwner) {
                    setUpCastRecyclerView(it as ArrayList<CastModelResults>)
                }
                videoEndPoint.observe(viewLifecycleOwner) {
                    setUpYoutubeIntent(it)
                }
                movieDetailsMutableLiveData.observe(viewLifecycleOwner) {
                    it.genre_ids?.let { genresIntList ->
                        setUpMovieGenresRecyclerView(setUpGenreList(genresIntList))
                    }
                    it.title?.let { title ->
                        movieName.text = title.trim()
                    }
                    it.release_date?.let { year ->
                        releaseDate.text = year.take(4)
                    }
                    it.vote_average?.let { votes ->
                        rating.text = votes.toString()
                        ratingBar.rating = (votes / 2).toFloat()
                    }
                    it.overview?.let { overviewText ->
                        overviewTv.text = overviewText.trim()
                        Log.e("overview", overviewText)
                    }
                    it.poster_path?.let { uri ->
                        movieImage.loadImageWithGlide(uri, getProgressDrawable(requireContext()))
                    }

                }
            }
        }

    }

    private fun setUpYoutubeIntent(videoEndPoint: String): Intent {
        if (videoEndPoint.isEmpty().not()) {
            intent = Intent(requireContext(), YoutubeActivity::class.java)
            intent.putExtra("videoId", videoEndPoint)
        }
        return intent
    }

    private fun setUpCastRecyclerView(list: ArrayList<CastModelResults>) {
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

    private fun setUpGenreList(genresIdList: List<Int>): ArrayList<String> {
        val genreIdArrayList = ArrayList<String>()
        genresIdList.forEach { i ->
            val genreName = Genres.findGenreNameById(i)
            genreIdArrayList.add(genreName)
        }
        return genreIdArrayList
    }

    private fun setUpMovieGenresRecyclerView(list: ArrayList<String>) {
        val recyclerVAdapter =
            GenresRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
        recyclerVAdapter.list.apply {
            clear()
            addAll(list)
        }
        binding.apply {
            genresRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = recyclerVAdapter
            }
        }
    }

    private fun getMovieDetailsArgs(): ArrayList<MovieDetailsResults> {
        return arguments?.getSerializable("movieDetails") as ArrayList<MovieDetailsResults>
    }

    private fun getRecyclerviewClickedPositionArgs(): Int {
        return arguments?.getSerializable("position") as Int
    }

    private fun setUpViewModel() {
        movieDetailsViewModel.setUpMovieDetails(
            getMovieDetailsArgs(),
            getRecyclerviewClickedPositionArgs()
        )
    }
}