package com.amadev.rando.ui.fragments.mainFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        setUpOnClickListeners()
        setUpSearchMoviesEditText()
    }

    private fun searchMovies(query: String) {
        mainFragmentViewModel.getSearchedMovies(query)
    }

    private fun setUpSearchMoviesEditText() {
        binding.searchMoviesEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isEmpty().not()){
                        searchMovies(s.toString())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun setUpOnClickListeners() {
        binding.apply {
            topRated.setOnClickListener {
                getMovies()
            }
            searchMovies.setOnClickListener {
                setUpSearchOnViewsVisibility()
            }
            searchMoviesOff.setOnClickListener {
                setUpSearchOffViewsVisibility()
                clearSearchedText()
            }
        }
    }

    private fun clearSearchedText() {
        binding.searchMoviesEditText.text.clear()
    }

    private fun setUpSearchOnViewsVisibility() {
        binding.apply {
            setUpViewVisibilityGone(searchMovies)
            setUpViewVisibilityGone(upcoming)
            setUpViewVisibilityGone(upcomingRecyclerView)
            setUpViewVisibilityGone(topRated)
            setUpViewVisibilityGone(topRatedRecyclerView)
            setUpViewVisibilityGone(mostPopularRecyclerView)
            setUpViewVisibilityGone(popular)
            setUpViewVisibilityVisible(searchMoviesEditText)
            setUpViewVisibilityVisible(searchMoviesOff)
            setUpViewVisibilityVisible(searchedMoviesRecyclerView)
            setUpViewVisibilityVisible(searchedMoviesResults)

        }
    }

    private fun setUpSearchOffViewsVisibility() {
        binding.apply {
            setUpViewVisibilityVisible(searchMovies)
            setUpViewVisibilityVisible(upcoming)
            setUpViewVisibilityVisible(upcomingRecyclerView)
            setUpViewVisibilityVisible(topRated)
            setUpViewVisibilityVisible(topRatedRecyclerView)
            setUpViewVisibilityVisible(mostPopularRecyclerView)
            setUpViewVisibilityVisible(popular)
            setUpViewVisibilityGone(searchMoviesEditText)
            setUpViewVisibilityGone(searchMoviesOff)
            setUpViewVisibilityGone(searchedMoviesRecyclerView)
            setUpViewVisibilityGone(searchedMoviesResults)
        }
    }

    private fun setUpViewVisibilityGone(view: View) {
        view.visibility = View.GONE
    }

    private fun setUpViewVisibilityVisible(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun setUpObservers() {
        mainFragmentViewModel.apply {
            binding.apply {
                topRatedMoviesResultsLiveData.observe(viewLifecycleOwner) {
                    setUpMoviesHorizontalRecyclerViewAdapter(it, topRatedRecyclerView)
                }
                popularMoviesResultsLiveData.observe(viewLifecycleOwner) {
                    setUpMoviesHorizontalRecyclerViewAdapter(it, mostPopularRecyclerView)
                }
                upcomingMoviesResultsLiveData.observe(viewLifecycleOwner) {
                    setUpMoviesHorizontalRecyclerViewAdapter(it, upcomingRecyclerView)
                }
                searchedMoviesLiveData.observe(viewLifecycleOwner) {
                    setUpSearchedMoviesRecyclerViewAdapter(it)
                }
            }
        }
    }

    private fun setUpSearchedMoviesRecyclerViewAdapter(list: ArrayList<MovieDetailsResults>) {
        val adapter = MoviesRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
        adapter.list.apply {
            clear()
            addAll(list)
            binding.apply {
                searchedMoviesRecyclerView.layoutManager =
                    GridLayoutManager(requireContext(), 3)
//                    LinearLayoutManager(requireContext(), GridLayoutManager(3), false)
                searchedMoviesRecyclerView.adapter = adapter
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

    private fun setUpMoviesHorizontalRecyclerViewAdapter(
        list: ArrayList<MovieDetailsResults>,
        recyclerView: RecyclerView
    ) {
        val adapter = MoviesRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
        adapter.list.apply {
            clear()
            addAll(list)
            binding.apply {
                recyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                recyclerView.adapter = adapter
            }
        }
    }

//    private fun setUpMostPopularRecyclerViewAdapter(
//        list: ArrayList<MovieDetailsResults>,
//        recyclerView: RecyclerView
//    ) {
//        val adapter = MoviesRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
//        adapter.list.apply {
//            clear()
//            addAll(list)
//            binding.apply {
//                recyclerView.layoutManager =
//                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//                recyclerView.adapter = adapter
//            }
//        }
//    }
//
//    private fun setUpTopRatedRecyclerViewAdapter(list: ArrayList<MovieDetailsResults>) {
//        val adapter = MoviesRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
//        adapter.list.apply {
//            clear()
//            addAll(list)
//
//            binding.apply {
//                topRatedRecyclerView.layoutManager =
//                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//                topRatedRecyclerView.adapter = adapter
//            }
//        }
//    }
//
//    private fun setUpUpcomingRecyclerViewAdapter(list: ArrayList<MovieDetailsResults>) {
//        val adapter = MoviesRecyclerViewAdapter(requireView(), requireContext(), arrayListOf())
//        adapter.list.apply {
//            clear()
//            addAll(list)
//
//            binding.apply {
//                upcomingRecyclerView.layoutManager =
//                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//                upcomingRecyclerView.adapter = adapter
//            }
//        }
//    }

}
