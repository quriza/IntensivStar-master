package ru.androidschool.intensiv.db

import androidx.room.*

@Dao
interface MovieDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movie: MovieDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movies: List<MovieDB>)


    @Query("DELETE FROM moviedb")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM MList Where listKey = :listKey")
    fun getOneMListWithMovie(listKey: String): List<MlistWithMovies>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(MList: MList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movieMListCrossRef: MovieMListCrossRef)
}

