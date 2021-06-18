package com.example.rickandmorty.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.model.Episode

@Dao
interface EpisodeDao {

    @Query("SELECT*FROM EPISODES")
    fun getEpisodes() : List<Episode>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(episode : Episode)

    @Query("SELECT * FROM EPISODES ORDER BY id DESC LIMIT 1")
    fun getLastItem() : Episode?


}