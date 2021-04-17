package ru.androidschool.intensiv.data

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.network.MovieApiClient

object MockRepository {

    fun getMovies(): List<Movie> {


        val moviesList = mutableListOf<Movie>()
        return moviesList
    }


    fun getTVShows(): List<Movie> {
        val moviesList = mutableListOf<Movie>()
        return moviesList


    }
}
