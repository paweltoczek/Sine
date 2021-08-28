package com.amadev.rando.ui.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadev.rando.adapter.FavoriteMoviesRecyclerViewAdapter
import com.amadev.rando.databinding.FragmentFavoritesBinding
import com.amadev.rando.model.MovieDetailsResults
import com.amadev.rando.util.Util.showSnackBar
import org.koin.android.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoritesFragmentViewModel: FavoritesFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFavoriteMovies()
        setUpObservers()

    }

    private fun getFavoriteMovies() {
        favoritesFragmentViewModel.getFavoriteMovies()
    }

    private fun setUpObservers() {
        favoritesFragmentViewModel.apply {
            favoritesMoviesLiveData.observe(viewLifecycleOwner) { favoriteMoviesList ->
                if (favoriteMoviesList != null) {
                    setUpFavoriteMoviesAdapter(favoriteMoviesList)
                }
            }

            popUpMessageLiveData.observe(viewLifecycleOwner) {
                showSnackBar(requireView(), it)
            }

            progressBarLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    false -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setUpFavoriteMoviesAdapter(favoriteMoviesList: ArrayList<MovieDetailsResults>) {
        val reversedFavoriteMoviesList = favoriteMoviesList.reversed() as ArrayList<MovieDetailsResults>

        val adapter =
            FavoriteMoviesRecyclerViewAdapter(
                requireView(),
                requireContext(),
                arrayListOf()
            )
        adapter.list = reversedFavoriteMoviesList
        binding.apply {
            favoriteMoviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            favoriteMoviesRecyclerView.adapter = adapter
        }
    }
}