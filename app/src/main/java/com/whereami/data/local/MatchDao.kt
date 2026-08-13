package com.whereami.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MatchDao {
    @Insert
    suspend fun insert(match: MatchEntity): Long

    @Query("SELECT * FROM matches ORDER BY datePlayed DESC")
    suspend fun getAll(): List<MatchEntity>

    @Query("SELECT * FROM matches WHERE id = :id")
    suspend fun getById(id: Long): MatchEntity?

    @Query("DELETE FROM matches")
    suspend fun deleteAll()
}
