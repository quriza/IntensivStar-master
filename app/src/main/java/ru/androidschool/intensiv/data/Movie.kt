package ru.androidschool.intensiv.data

class Movie(
    var title: String? = "",
    var voteAverage: Double = 0.0,
    var genre: String? ="",
    var actors: MutableList<Actor>,
    var producedBy: String? ="",
    var year: String? ="",
    var movieImage: String? = "",
    var description: String? =""

) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
