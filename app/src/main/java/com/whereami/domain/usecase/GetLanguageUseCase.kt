package com.whereami.domain.usecase

import com.whereami.domain.model.Language
import com.whereami.domain.repository.SettingsRepository
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Language = settingsRepository.getLanguage()
}
