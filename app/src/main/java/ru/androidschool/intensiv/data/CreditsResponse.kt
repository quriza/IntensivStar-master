package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("id")
    var movieId: Int,
    @SerializedName("cast")
    var actors: List<Actor>

)
