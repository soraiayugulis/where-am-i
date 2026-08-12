package com.whereami.domain.usecase

import com.whereami.core.time.TimeProvider
import com.whereami.domain.repository.MatchRepository
import javax.inject.Inject

private const val WEEK_IN_MILLIS = 168L * 60L * 60L * 1000L

class GetWeeklyScoreUseCase @Inject constructor(
    private val matchRepository: MatchRepository,
    private val timeProvider: TimeProvider
) {
    suspend operator fun invoke(): Int {
        val cutoff = timeProvider.nowMs() - WEEK_IN_MILLIS
        return matchRepository.getAll()
            .filter { it.datePlayed >= cutoff }
            .sumOf { it.score }
    }
}
