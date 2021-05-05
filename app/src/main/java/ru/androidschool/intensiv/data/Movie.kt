package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class Movie(
    @SerializedName("adult")
    val isAdult: Boolean = false,
    @SerializedName("overview")
    val overview: String? = null,

    //   val genreIds: List<Int>,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("original_title")
    val originalTitle: String? = null,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName(value = "title", alternate = ["name"])
    var title: String? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("popularity")
    val popularity: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Int? = 0,
    @SerializedName("release_date")
    val release_date: String? = null,
    @SerializedName("video")
    val video: Boolean? = null,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("production_companies")
    var productionCompanies: List<ProductionCompany>? = listOf(),
    @SerializedName("genres")
    var genres: List<Genre>? = listOf()

) {
    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = BuildConfig.IMAGE_PATH + "$field"
        set(value) {
            field = value
        }

    val shortPosterPath: String?
        get() = this.posterPath?.replace(BuildConfig.IMAGE_PATH, "")

    val rating: Float
        get() = (voteAverage ?: 0.0).div(2).toFloat()
}
