package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?
)
