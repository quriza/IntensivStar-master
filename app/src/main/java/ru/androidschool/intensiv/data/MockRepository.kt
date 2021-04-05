package ru.androidschool.intensiv.data

object MockRepository {

    fun getMovies(): List<Movie> {

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Назад в будущее $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movie)
        }

        return moviesList
    }

    fun getTVShows(): List<Movie> {

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Один дома - $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movie)
        }

        return moviesList
    }
}
