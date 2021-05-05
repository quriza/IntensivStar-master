package ru.androidschool.intensiv.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_watchlist.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.db.MlistWithMovies
import ru.androidschool.intensiv.db.MovieDatabase
import ru.androidschool.intensiv.ui.profile.SharedViewModel
import ru.androidschool.intensiv.ui.profile.TabData
import timber.log.Timber

private const val TAB_NUM = "tab_num"

class WatchlistFragment : Fragment() {

    private var tabNum: Int = 0
    private val model: SharedViewModel by activityViewModels()
    val compositeDisposable = CompositeDisposable()

    val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabNum = it.getInt(TAB_NUM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movies_recycler_view.layoutManager = GridLayoutManager(context, 4)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        val listKeys = resources.getStringArray(R.array.tab_keys)
        compositeDisposable.add(
            Observable.fromCallable({
                val db = MovieDatabase.get(this.requireContext())
                db.movieDao().getOneMListWithMovie(listKeys[this.tabNum])
            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showLikedList(it)
                }, {
                    model.updateTabData(TabData(0, this.tabNum))
                    Timber.e(it.message)
                })
        )
    }

    fun showLikedList(mlist: MlistWithMovies) {
        val moviesList = mlist.movies.map { item ->
            val movie = Movie(id = item.movieId, title = item.title, voteAverage = item.popularity)
            movie.posterPath = item.posterPath
            MoviePreviewItem(
                movie
            ) { movie -> }
        }.toList()
        model.updateTabData(TabData(moviesList.count(), this.tabNum))
        movies_recycler_view.adapter = adapter.apply { addAll(moviesList) }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    companion object {

        @JvmStatic
        fun newInstance(tabNum: Int) =
            WatchlistFragment().apply {
                arguments = Bundle().apply {
                    putInt(TAB_NUM, tabNum)
                }
            }
    }
}
