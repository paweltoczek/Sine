package com.amadev.rando

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
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

        choiceFragmentViewModel =
            ViewModelProviders.of(this).get(ChoiceFragmentViewModel::class.java)
        choiceFragmentViewModel.refresh()


        draw_btn.setOnClickListener {

            if (spinner1.visibility == View.VISIBLE) {
                Toast.makeText(requireContext(), "Wait a second", Toast.LENGTH_SHORT).show()
                Toast.LENGTH_SHORT

            } else {
                food_tv_randomchoice.text = ""
                movie_tv_randomchoice.text = ""
                spinner1.visibility = View.VISIBLE
                spinner2.visibility = View.VISIBLE

                Handler().postDelayed({
                    spinner1.visibility = View.GONE
                    spinner2.visibility = View.GONE
                    drawFood()
                    drawMovieGenre()
                }, 1500)
            }
        }
    }

    fun drawFood() {
        val foodList = listOf<String>(
            "Burgers",
            "Pizza",
            "Fish & chips",
            "Kebab",
            "Chinese food",
            "Taco",
            "Sushi",
            "Indian Food",
            "Salads",
            "Steak",
            "Baguette",
            "Fries",
            "Hot Dogs",
            "Sea food"
        )

        var rnd = (0..foodList.size - 1).shuffled().first()

        food_tv_randomchoice.text = foodList[rnd]

    }

    fun drawMovieGenre() {
        val movieGenreList = listOf<String>(
            "Action",
            "Animation",
            "Comedy",
            "Romantic Comedy",
            "Criminal",
            "Drama",
            "Horror",
            "War movie",
            "Biographic",
            "Action",
            "Advenure",
            "Sciene Fiction",
            "Thriller",
            "Western",
            "Historical",
            "Romance",
            "Fantasy"
        )

        var rnd = (0..movieGenreList.size - 1).shuffled().first()

        movie_tv_randomchoice.text = movieGenreList[rnd]
    }

}