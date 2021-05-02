package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.network.applySchedulers
import ru.androidschool.intensiv.ui.feed.FeedFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TvShowsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавляем recyclerView
        shows_recycler_view.layoutManager = LinearLayoutManager(context)
        shows_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        compositeDisposable.add(
            MovieApiClient.apiClient.getTVPopular()
                .applySchedulers()
                .doOnSubscribe {
                    tvShowsProgressBar.visibility = View.VISIBLE;
                }
                .doFinally {
                    tvShowsProgressBar.visibility = View.GONE
                }
                .subscribe(
                    {
                        addShowsToList(it?.results ?: listOf())
                    },
                    {

                        TODO()
                    }
                ))
    }

    private fun addShowsToList(movies: List<Movie>) {
        val showList =
            movies.map {
                TvShowItem(it) { movie ->
                    openShowDetails(
                        movie
                    )
                }
            }.toList()
        shows_recycler_view.adapter = adapter.apply { addAll(showList) }
    }

    private fun openShowDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putInt(FeedFragment.KEY_ID, movie.id ?: 0)
        bundle.putString(FeedFragment.KEY_TYPE, FeedFragment.KEY_TYPE_TV_SHOW)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TvShowsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
