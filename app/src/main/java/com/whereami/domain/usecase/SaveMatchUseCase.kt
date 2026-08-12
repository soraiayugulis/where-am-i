package com.whereami.domain.usecase

import com.whereami.domain.model.MatchResult
import com.whereami.domain.repository.MatchRepository
import javax.inject.Inject

class SaveMatchUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(match: MatchResult) = matchRepository.save(match)
}
