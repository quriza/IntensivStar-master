package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MockRepository

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MovieDetailsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieTitle = requireArguments().getString("title")
        val movie = MockRepository.getTVShows().find { it.title == movieTitle }

        actors_recycler_view.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)

        actors_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        if (movie !== null) {
            title.text = movie?.title ?: ""
            movie_details_rating.rating = movie.rating
            movie_genre.text = movie?.genre ?: ""
            movie_year.text = movie?.year ?: ""
            movie_produced_by.text = movie?.producedBy ?: ""
            movie_description.text = movie?.description
            if (movie?.movieImage !== null) {
                Picasso.get()
                    .load(movie?.movieImage)
                    .into(movie_image)
            } else {
                movie_image.visibility = View.GONE
            }
            val actorList =
                movie?.actors.map {
                    ActorItem(it)

                }.toList()
            actors_recycler_view.adapter = adapter.apply { addAll(actorList) }
        } else {
            title.text = "не найден видосик"
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
