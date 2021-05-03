package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MovieResponse
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.afterTextChanged
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment() {
    val compositeDisposable = CompositeDisposable()

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавляем recyclerView
        movies_recycler_view.layoutManager = LinearLayoutManager(context)
        movies_recycler_view.adapter = adapter.apply { clear() }

        // здесь делаем подписку через observable
        compositeDisposable.add(
            this.createSearchTextChangedObservable()

                .map { s -> s.trim() }
                .filter { s -> (s.length >= MIN_LENGTH) }
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    openSearch(it.toString())
                }, {
                    Timber.e(it)
                })
        )

        compositeDisposable.add(
            Observable.zip(
                MovieApiClient.apiClient.getNowPlayingMovies(),
                MovieApiClient.apiClient.getUpcomingMovies(),
                BiFunction<MovieResponse, MovieResponse, List<MovieResponse>> { nowPlaying, upcomingMovies ->
                    listOf(nowPlaying, upcomingMovies)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    progressBar.visibility = View.VISIBLE
                }
                .doFinally {
                    progressBar.visibility = View.GONE
                }
                .subscribe(
                    {
                        AddMoviesToFeed(R.string.now_playing, it?.get(0)?.results ?: listOf())
                        AddMoviesToFeed(R.string.upcoming, it?.get(1)?.results ?: listOf())
                    },
                    {
                        reportError(it)
                    }
                ))
    }

    private fun createSearchTextChangedObservable(): Observable<String> {

        return Observable.create { emitter ->
            search_toolbar.search_edit_text.afterTextChanged {
                emitter.onNext(search_toolbar.search_edit_text.text.toString())
            }
        }
    }

    private fun reportError(err: Throwable) {
        Timber.e("Error occured " + (err.message ?: ""))
    }

    private fun AddMoviesToFeed(titleRes: Int, movies: List<Movie>) {
        val moviesList = listOf(
            MainCardContainer(
                titleRes,
                movies.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(
                            movie
                        )
                    }
                }.toList()
            )
        )

        movies_recycler_view.adapter = adapter.apply { addAll(moviesList) }
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putInt(KEY_ID, movie.id ?: 0)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_SEARCH = "search"
        const val KEY_ID = "id"
        const val KEY_TYPE = "type"
        const val KEY_TYPE_TV_SHOW = "TV_SHOW"
        const val KEY_TYPE_MOVIE = "MOVIE"
    }
}
