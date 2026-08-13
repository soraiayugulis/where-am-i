package com.whereami.domain.usecase

import com.whereami.domain.model.Language
import com.whereami.domain.repository.SettingsRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(language: Language) {
        settingsRepository.setLanguage(language)
    }
}
