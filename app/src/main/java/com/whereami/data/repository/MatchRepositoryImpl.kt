package com.whereami.data.repository

import com.whereami.data.local.MatchDao
import com.whereami.data.local.MatchEntity
import com.whereami.domain.model.Location
import com.whereami.domain.model.MatchResult
import com.whereami.domain.model.Status
import com.whereami.domain.repository.MatchRepository
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val matchDao: MatchDao
) : MatchRepository {
    override suspend fun save(match: MatchResult) {
        matchDao.insert(match.toEntity())
    }

    override suspend fun getAll(): List<MatchResult> {
        return matchDao.getAll().map { it.toDomain() }
    }

    private fun MatchResult.toEntity(): MatchEntity = MatchEntity(
        id = id,
        datePlayed = datePlayed,
        targetLatLng = target.toLatLngString(),
        guessLatLng = guess?.toLatLngString(),
        timeTakenMs = timeTakenMs,
        score = score,
        status = status.name
    )

    private fun MatchEntity.toDomain(): MatchResult = MatchResult(
        id = id,
        datePlayed = datePlayed,
        target = targetLatLng.toLocation(),
        guess = guessLatLng?.toLocation(),
        timeTakenMs = timeTakenMs,
        score = score,
        status = Status.valueOf(status)
    )

    private fun String.toLocation(): Location {
        val parts = split(",")
        return Location(parts[0].toDouble(), parts[1].toDouble())
    }
}
