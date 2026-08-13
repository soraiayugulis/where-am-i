package com.whereami.domain.usecase

import com.whereami.domain.repository.MatchRepository
import javax.inject.Inject

class ClearAllMatchesUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke() = matchRepository.clearAll()
}
