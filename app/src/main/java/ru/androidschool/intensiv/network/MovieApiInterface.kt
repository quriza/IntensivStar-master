package ru.androidschool.intensiv.network

import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.data.CreditsResponse
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MovieResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Single<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Single<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Single<MovieResponse>

    @GET("movie/{movieId}")
    fun getMovieDetails(
        @Path("movieId") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String

    ): Single<Movie>

    @GET("tv/{tvId}")
    fun getTVShowDetails(
        @Path("tvId") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String

    ): Single<Movie>

    @GET("movie/{movieId}/credits")
    fun getMovieCredits(
        @Path("movieId") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String

    ): Single<CreditsResponse>

    @GET("tv/popular")
    fun getTVPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Single<MovieResponse>

    @GET("tv/{tvId}/credits")
    fun getTVCredits(
        @Path("tvId") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String

    ): Single<CreditsResponse>
}
