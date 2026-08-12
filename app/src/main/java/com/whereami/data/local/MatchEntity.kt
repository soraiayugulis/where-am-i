package com.whereami.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val datePlayed: Long,
    val targetLatLng: String,
    val guessLatLng: String?,
    val timeTakenMs: Long,
    val score: Int,
    val status: String
)
