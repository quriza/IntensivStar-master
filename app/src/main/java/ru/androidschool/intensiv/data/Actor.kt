package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class Actor(
    @SerializedName("name")
    val name: String
) {

    @SerializedName("profile_path")
    var profilePath: String? = null
        get() = "https://image.tmdb.org/t/p/w500$field"
}
