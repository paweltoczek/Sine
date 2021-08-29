package com.amadev.rando.ui.dialogs.movieDetails

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadev.rando.R
import com.amadev.rando.YoutubeActivity
import com.amadev.rando.adapter.GenresRecyclerViewAdapter
import com.amadev.rando.databinding.MovieDetailsDialogBinding
import com.amadev.rando.model.MovieDetailsResults
import com.amadev.rando.util.Genres
import com.amadev.rando.util.Util
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailsDialog(private val results: MovieDetailsResults) : DialogFragment() {

    private var _binding: MovieDetailsDialogBinding? = null
    private val binding get() = _binding!!
    private val movieDetailsDialogViewModel: MovieDetailsDialogViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setUpObservers()
        setUpTextViews()
        setUpRatingBar()
        setUpGenreList()
        setUpWindowAnimation()
        setUpGenresRecyclerView()
        setUpWindowBackground()
        setUpMoviePosterImage()
        setUpOnClickListeners()
        setUpObservers()
    }

    private fun setUpWindowAnimation() {
        dialog?.window?.attributes?.windowAnimations = R.style.DialogScaleAnimation
    }

    private fun setUpViewModel() {
        movieDetailsDialogViewModel.apply {
            results.id?.let { getTrailerVideo(it) }
        }
    }

    private fun setUpObservers() {
        movieDetailsDialogViewModel.apply {
            videoEndPointLiveData.observe(viewLifecycleOwner) {
                binding.playTrailer.setOnClickListener {
                    if (videoEndPointLiveData.value != null) {
                        videoEndPointLiveData.value?.let { setUpYouTubeActivityIntent(it) }
                    } else {
                        Util.showSnackBar(requireView(), getString(R.string.trailerIsNotAvailable))
                    }
                }
            }
        }
    }

    private fun setUpYouTubeActivityIntent(videoEndPoint : String) {
        val intent = Intent(requireContext(), YoutubeActivity::class.java)
        intent.putExtra("videoId", videoEndPoint)
        startActivity(intent)
    }

    private fun setUpOnClickListeners() {
        binding.apply {
            closeDialogBtn.setOnClickListener {
                closeDialog()
            }
        }
    }

    private fun closeDialog() {
        dialog?.dismiss()
    }

    private fun setUpMoviePosterImage() {
        binding.moviePoster.loadImageWithGlide(
            results.poster_path,
            getProgressDrawable(requireContext())
        )
    }

    private fun setUpWindowBackground() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setUpTextViews() {
        binding.apply {
            movieName.text = results.title
            releasedate.text = results.release_date?.take(4)
            overviewTv.text = results.overview
            rating.text = results.vote_average.toString()
        }
    }

    private fun setUpRatingBar() {
        binding.ratingBar.rating = (results.vote_average?.div(2))!!.toFloat()
    }

    private fun setUpGenresRecyclerView() {
        setUpGenreRecyclerViewAdapter(setUpGenreList())
    }

    private fun setUpGenreList(): ArrayList<String> {
        val genesList = results.genre_ids
        val genreIdArrayList = ArrayList<String>()
        genesList?.forEach { i ->
            val genreName = Genres.findGenreNameById(i)
            genreIdArrayList.add(genreName)
        }
        return genreIdArrayList
    }


    private fun setUpGenreRecyclerViewAdapter(genresList: ArrayList<String>) {
        val adapter = GenresRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
        adapter.list = genresList
        binding.apply {
            genresRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            genresRecyclerview.adapter = adapter
        }
    }
}