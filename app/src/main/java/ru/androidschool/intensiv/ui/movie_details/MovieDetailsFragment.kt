package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.CreditsResponse
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.db.MList
import ru.androidschool.intensiv.db.MovieDB
import ru.androidschool.intensiv.db.MovieDatabase
import ru.androidschool.intensiv.db.MovieMListCrossRef
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.network.applySchedulers
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.ui.load
import timber.log.Timber


class MovieDetailsFragment : Fragment() {


    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    val compositeDisposable = CompositeDisposable()
    private var movie: Movie? = null

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
        like_button.setOnClickListener({
            onLikeClicked()
        })
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


    fun onLikeClicked() {


        Completable.fromAction({
            val db = MovieDatabase.get(this.requireContext())
            val dao = db.movieDao()

            val movieDB =
                MovieDB(movie!!.id!!, movie!!.title!!, movie!!.posterPath, movie!!.popularity!!)
            dao.save(movieDB)

            val listKey = "liked"
            val mList = MList(listKey, "Любимые фильмы")
            dao.save(mList)

            val movieMListCrossRef = MovieMListCrossRef(listKey, movie!!.id!!)
            dao.save(movieMListCrossRef)
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e("liked")
                    //здесь можно например поменять цвет у кнопки
            })


    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun reportError(err: Throwable) {
        Timber.e("Error occured" + err.message ?: "")
    }

    private fun setMovie(movie: Movie) {
        this.movie = movie
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

                }
            }
    }
}
