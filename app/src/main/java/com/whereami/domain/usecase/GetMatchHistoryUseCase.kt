package com.whereami.domain.usecase

import com.whereami.domain.model.MatchResult
import com.whereami.domain.repository.MatchRepository
import javax.inject.Inject

class GetMatchHistoryUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(): List<MatchResult> = matchRepository.getAll()
}
