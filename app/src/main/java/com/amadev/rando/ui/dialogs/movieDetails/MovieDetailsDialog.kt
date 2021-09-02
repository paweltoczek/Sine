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
import com.amadev.rando.adapter.CastRecyclerViewAdapter
import com.amadev.rando.adapter.GenresRecyclerViewAdapter
import com.amadev.rando.databinding.MovieDetailsDialogBinding
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.model.MovieDetailsResults
import com.amadev.rando.ui.dialogs.overviewDialog.OverviewDialog
import com.amadev.rando.util.Genres
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import com.amadev.rando.util.Util.showSnackBar
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailsDialog(private val results: MovieDetailsResults) : DialogFragment() {

    private var _binding: MovieDetailsDialogBinding? = null
    private val binding get() = _binding!!
    private val movieDetailsDialogViewModel: MovieDetailsDialogViewModel by viewModel()

    companion object {
        const val MAX_OVERVIEW_TEXT_LENGTH = 150
    }

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
        setUpDialogFullscreen()
        setUpOverviewTextView(results.overview.toString())
    }

    private fun setUpDialogFullscreen() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun setUpWindowAnimation() {
        dialog?.window?.attributes?.windowAnimations = R.style.DialogScaleAnimation
    }

    private fun setUpViewModel() {
        movieDetailsDialogViewModel.apply {
            results.id?.let { getTrailerVideo(it) }
            results.id?.let { getCastDetails(it) }
        }
    }

    private fun setUpObservers() {
        movieDetailsDialogViewModel.apply {
            videoEndPointLiveData.observe(viewLifecycleOwner) {
                binding.trailerBtn.setOnClickListener {
                    if (videoEndPointLiveData.value != null) {
                        videoEndPointLiveData.value?.let { setUpYouTubeActivityIntent(it) }
                    } else {
                        showSnackBar(requireView(), getString(R.string.trailerIsNotAvailable))
                    }
                }
            }
            castListLiveData.observe(viewLifecycleOwner) {
                setUpCastRecyclerView(it)
            }
        }
    }

    private fun provideOverviewDialog(overviewText: String) {
        val dialog = OverviewDialog(overviewText)
        dialog.show(childFragmentManager, null)
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
                }
                else -> {
                    overviewTv.text = text
                    readMore.visibility = View.GONE
                }
            }
        }
    }

    private fun setUpYouTubeActivityIntent(videoEndPoint: String) {
        val intent = Intent(requireContext(), YoutubeActivity::class.java)
        intent.putExtra("videoId", videoEndPoint)
        startActivity(intent)
    }

    private fun setUpOnClickListeners() {
        binding.apply {
            closeDialogBtn.setOnClickListener {
                closeDialog()
            }
            readMore.setOnClickListener {
                provideOverviewDialog(results.overview.toString())
            }
        }
    }

    private fun closeDialog() {
        dialog?.dismiss()
    }

    private fun setUpMoviePosterImage() {
        binding.bcgImage.loadImageWithGlide(
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
        setUpGenreRecyclerView(setUpGenreList())
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


    private fun setUpGenreRecyclerView(genresList: ArrayList<String>) {
        val adapter = GenresRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
        adapter.list = genresList
        binding.apply {
            genreRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            genreRecyclerview.adapter = adapter
        }
    }

    private fun setUpCastRecyclerView(castList: ArrayList<CastModelResults>) {
        val adapter = CastRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
        adapter.list = castList
        binding.apply {
            castRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            castRecyclerview.adapter = adapter
        }
    }
}