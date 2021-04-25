package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class Actor(
    @SerializedName("name")
    val name: String
) {

    @SerializedName("profile_path")
    var profilePath: String? = null
        get() = BuildConfig.IMAGE_PATH+"$field"
}
