package com.amadev.rando.ui.fragments.mainFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amadev.rando.adapter.MoviesRecyclerViewAdapter
import com.amadev.rando.databinding.FragmentMainBinding
import com.amadev.rando.model.MovieDetailsResults
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val mainFragmentViewModel: MainFragmentViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMovies()
        setUpObservers()

        binding.topRated.setOnClickListener {

            getMovies()
        }
    }

    private fun setUpObservers() {
        mainFragmentViewModel.apply {
            topRatedMoviesResultsLiveData.observe(viewLifecycleOwner) {
                setUpTopRatedRecyclerViewAdapter(it)
            }

            popularMoviesResultsLiveData.observe(viewLifecycleOwner) {
                setUpMostPopularRecyclerViewAdapter(it)
            }

            upcomingMoviesResultsLiveData.observe(viewLifecycleOwner) {
                setUpUpcomingRecyclerViewAdapter(it)
            }

        }
    }


    private fun getMovies() {
        mainFragmentViewModel.apply {
            getTopRatedMovies()
            getPopularMovies()
            getUpcomingMovies()
        }
    }

    private fun setUpMostPopularRecyclerViewAdapter(list: ArrayList<MovieDetailsResults>) {
        val adapter = MoviesRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())

        adapter.list.apply {
            clear()
            addAll(list)

            binding.apply {
                mostPopularRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                mostPopularRecyclerView.adapter = adapter
            }
        }
    }

    private fun setUpTopRatedRecyclerViewAdapter(list: ArrayList<MovieDetailsResults>) {
        val adapter = MoviesRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())

        adapter.list.apply {
            clear()
            addAll(list)

            binding.apply {
                topRatedRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                topRatedRecyclerView.adapter = adapter
            }
        }
    }

    private fun setUpUpcomingRecyclerViewAdapter(list: ArrayList<MovieDetailsResults>) {
        val adapter = MoviesRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
        adapter.list.apply {
            clear()
            addAll(list)

            binding.apply {
                upcomingRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                upcomingRecyclerView.adapter = adapter
            }
        }
    }

}
