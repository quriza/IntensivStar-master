package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class Movie(
    @SerializedName("adult")
    val isAdult: Boolean,
    @SerializedName("overview")
    val overview: String?,

    //   val genreIds: List<Int>,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName(value = "title", alternate = ["name"])
    var title: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("release_date")
    val release_date: String?,
    @SerializedName("video")
    val video: Boolean?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("production_companies")
    var productionCompanies: List<ProductionCompany>?,
    @SerializedName("genres")
    var genres: List<Genre>?

) {
    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = BuildConfig.IMAGE_PATH + "$field"

    val rating: Float
        get() = (voteAverage ?: 0.0).div(2).toFloat()
}
