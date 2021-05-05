package ru.androidschool.intensiv.db

import androidx.room.*


@Dao
interface MovieDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movie: MovieDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movies: List<MovieDB>)


    @Query("DELETE FROM MovieDB")
    fun deleteAll()

    @Query("DELETE FROM movieMListCrossRef where movieId = :movieId and listKey = :listKey")
    fun deleteMovieFromList(listKey: String,movieId: Int)

    @Transaction
    @Query("SELECT * FROM MList Where listKey = :listKey")
    fun getOneMListWithMovie(listKey: String): MlistWithMovies

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(MList: MList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movieMListCrossRef: MovieMListCrossRef)

    @Query("SELECT * FROM movieMListCrossRef Where listKey = :listKey and movieId = :movieId")
    fun checkIfMovieInList(listKey: String,movieId: Int): MovieMListCrossRef
}

