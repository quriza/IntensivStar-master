package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.CreditsResponse
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.ui.load

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


        val movieId = requireArguments().getInt(FeedFragment.KEY_ID)
        var getMovieDetails = if (requireArguments().getString(FeedFragment.KEY_TYPE) == "TV_SHOW") {

                MovieApiClient.apiClient.getTVShowDetails(
                    movieId.toString(),
                    BuildConfig.API_KEY,
                    "ru"
                )
        } else {

                MovieApiClient.apiClient.getMovieDetails(
                    movieId.toString(),
                    BuildConfig.API_KEY,
                    "ru"
                )
        }

        getMovieDetails.enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, e: Throwable) {
                Log.e("getMovieDetails", e.toString())
            }

            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                val movie = response.body()
                if (movie !== null) {

                    title.text = movie?.title ?: ""
                    movie_details_rating.rating = movie.rating

                    movie_genre.text =
                        movie?.genres?.map({ it -> it.name })?.joinToString(", ") ?: ""
                    movie_year.text = movie?.release_date ?: ""
                    movie_produced_by.text =
                        movie?.productionCompanies?.map({ it -> it.name })?.joinToString(", ") ?: ""
                    movie_description.text = movie?.overview
                    movie_image.load(movie?.posterPath)

                }


            }
        })

        val getMovieCredits = if (requireArguments().getString(FeedFragment.KEY_TYPE) == "TV_SHOW") {
            MovieApiClient.apiClient.getTVCredits(movieId.toString(), BuildConfig.API_KEY, "ru")
        } else {
            MovieApiClient.apiClient.getMovieCredits(movieId.toString(), BuildConfig.API_KEY, "ru")
        }

        getMovieCredits.enqueue(object : Callback<CreditsResponse> {
            override fun onFailure(call: Call<CreditsResponse>, e: Throwable) {
                Log.e("getMovieCredits", e.toString())
            }

            override fun onResponse(
                call: Call<CreditsResponse>,
                response: Response<CreditsResponse>
            ) {
                val actors = response.body()?.actors
                if (actors === null) {
                    return;
                }
                val actorList =
                    actors.map {
                        ActorItem(it)
                    }.toList()
                actors_recycler_view.adapter = adapter.apply {
                    addAll(actorList)
                }


            }
        })


// getMovieDetails
/*  val movie = MockRepository.getTVShows().find { it.title == movieTitle }

actors_recycler_view.layoutManager =
   LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
actors_recycler_view.adapter = adapter.apply { addAll(listOf()) }

if (movie !== null) {
   title.text = movie?.title ?: ""
   movie_details_rating.rating = movie.rating
/*   movie_genre.text = movie?.genre ?: ""
   movie_year.text = movie?.year ?: ""
   movie_produced_by.text = movie?.producedBy ?: ""
   movie_description.text = movie?.description
   movie_image.load(movie?.movieImage)

   val actorList =
       movie?.actors.map {
           ActorItem(it)
       }.toList()
   actors_recycler_view.adapter = adapter.apply { addAll(actorList) }*/
} else {
   title.text = "не найден видосик"
   // Михаил а как принято отрабатывать такую ситуацию?
}*/
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
