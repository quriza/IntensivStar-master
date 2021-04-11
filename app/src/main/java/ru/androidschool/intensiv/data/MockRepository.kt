package ru.androidschool.intensiv.data

object MockRepository {

    fun getMovies(): List<Movie> {

        var actorsList = mutableListOf<Actor>()

        actorsList.add(Actor("Joe Cocker", ""))
        actorsList.add(Actor("Stainberg Kamber", ""))
        actorsList.add(Actor("Yet another actor", ""))


        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Назад в будущее $x",
                voteAverage = 10.0 - x,
                genre = "Комедия",
                movieImage = "https://www.kinopoisk.ru/images/film_big/1143242.jpg",
                actors = actorsList,
                producedBy = "MiraMax",
                year = (2000 + x).toString(),
                description = "текст про булки которые надо съесть в больших количествах, текст про булки которые надо съесть в больших количествах, текст про булки которые надо съесть в больших количествах,"


                )
            moviesList.add(movie)
        }

        return moviesList
    }

    fun getTVShows(): List<Movie> {
        var actorsList = mutableListOf<Actor>()

        actorsList.add(Actor("Joe Cocker", ""))
        actorsList.add(Actor("Stainberg Kamber", ""))
        actorsList.add(Actor("Yet another actor", ""))

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Один дома - $x",
                movieImage = "https://www.kinopoisk.ru/images/film_big/1143242.jpg",
                voteAverage = 10.0 - x,
                genre = "Комедия",
                actors = actorsList,
                producedBy = "MiraMax",
                description = "текст про булки которые надо съесть в больших количествах, текст про булки которые надо съесть в больших количествах, текст про булки которые надо съесть в больших количествах,",
                year = (2000 + x).toString()
            )
            moviesList.add(movie)
        }

        return moviesList
    }
}
