package com.whereami.domain.repository

import com.whereami.domain.model.Language

interface SettingsRepository {
    suspend fun getLanguage(): Language
    suspend fun setLanguage(language: Language)
}
