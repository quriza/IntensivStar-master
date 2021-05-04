package ru.androidschool.intensiv.db

import androidx.room.*

@Entity
data class MovieDB(
    @PrimaryKey
    val movieId: Int,
    val title: String,
    val posterPath:String?,
    val popularity: Double?
)

@Entity
data class MList(
    @PrimaryKey
    val listKey: String, // строковый ключ ('liked','popular',...)
    val listName: String
)

@Entity(primaryKeys = ["listKey", "movieId"])
data class MovieMListCrossRef(
    val listKey: String,
    val movieId: Int
)

data class MlistWithMovies(
    @Embedded val mlist: MList,
    @Relation(
        parentColumn = "listKey",
        entityColumn = "movieId",
        associateBy = Junction(MovieMListCrossRef::class)
    )
    val movies: List<MovieDB>
)