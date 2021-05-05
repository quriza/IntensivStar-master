package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.CreditsResponse
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.network.applySchedulers
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.ui.load
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MovieDetailsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    val compositeDisposable = CompositeDisposable()

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

        if (requireArguments().getString(FeedFragment.KEY_TYPE) == FeedFragment.KEY_TYPE_TV_SHOW) {

            compositeDisposable.add(
                MovieApiClient.apiClient.getTVShowDetails(
                    movieId.toString()
                )
                    .applySchedulers()
                    .subscribe(
                        {
                            setMovie(it)
                        },
                        {
                            reportError(it)
                        }
                    ))
            compositeDisposable.add(
                MovieApiClient.apiClient.getTVCredits(movieId.toString())
                    .applySchedulers()
                    .subscribe(
                        {
                            setCredits(it)
                        },
                        {
                            reportError(it)
                        }
                    ))
        } else {
            compositeDisposable.add(
                MovieApiClient.apiClient.getMovieDetails(
                    movieId.toString()

                )
                    .applySchedulers()
                    .subscribe(
                        {
                            setMovie(it)
                        },
                        {
                            reportError(it)
                        }
                    ))

            compositeDisposable.add(
                MovieApiClient.apiClient.getMovieCredits(
                    movieId.toString()
                ).applySchedulers()
                    .subscribe(
                        {
                            setCredits(it)
                        },
                        {
                            reportError(it)
                        }
                    ))
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun reportError(err: Throwable) {
        Timber.e("Error occured" + err.message ?: "")
    }

    private fun setMovie(movie: Movie) {
        title.text = movie?.title ?: ""
        movie_details_rating.rating = movie.rating

        movie_genre.text =
            movie?.genres?.map({ it -> it.name })?.joinToString(", ") ?: ""
        if ((movie?.release_date?.length ?: 0) >= 4) {
            movie_year.text = movie?.release_date?.substring(0, 4) ?: ""
        } else {
            movie_year.text = ""
        }
        movie_produced_by.text =
            movie?.productionCompanies?.map({ it -> it.name })?.joinToString(", ") ?: ""
        movie_description.text = movie?.overview
        movie_image.load(movie?.posterPath)
    }

    private fun setCredits(creditsResponse: CreditsResponse) {
        val actors = creditsResponse?.actors
        if (actors == null) {
            return
        }
        val actorList =
            actors.map {
                ActorItem(it)
            }.toList()

        actors_recycler_view.adapter = adapter.apply {
            addAll(actorList)
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
