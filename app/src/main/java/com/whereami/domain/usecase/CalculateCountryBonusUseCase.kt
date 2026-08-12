package com.whereami.domain.usecase

import com.whereami.domain.model.Country
import javax.inject.Inject

class CalculateCountryBonusUseCase @Inject constructor() {
    operator fun invoke(target: Country?, guess: Country?): Int {
        return if (target != null && guess != null && target.code == guess.code) 1000 else 0
    }
}
