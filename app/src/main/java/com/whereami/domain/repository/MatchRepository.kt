package com.whereami.domain.repository

import com.whereami.domain.model.MatchResult

interface MatchRepository {
    suspend fun save(match: MatchResult)
    suspend fun getAll(): List<MatchResult>
    suspend fun clearAll()
}
